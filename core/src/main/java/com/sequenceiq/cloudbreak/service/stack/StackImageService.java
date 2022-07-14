package com.sequenceiq.cloudbreak.service.stack;

import static com.sequenceiq.cloudbreak.cloud.model.Platform.platform;

import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider;
import com.sequenceiq.cloudbreak.auth.crn.Crn;
import com.sequenceiq.cloudbreak.auth.security.internal.InternalCrnModifier;
import com.sequenceiq.cloudbreak.cloud.model.Image;
import com.sequenceiq.cloudbreak.common.exception.CloudbreakServiceException;
import com.sequenceiq.cloudbreak.common.exception.NotFoundException;
import com.sequenceiq.cloudbreak.common.json.Json;
import com.sequenceiq.cloudbreak.common.type.ComponentType;
import com.sequenceiq.cloudbreak.core.CloudbreakImageCatalogException;
import com.sequenceiq.cloudbreak.core.CloudbreakImageNotFoundException;
import com.sequenceiq.cloudbreak.domain.ImageCatalog;
import com.sequenceiq.cloudbreak.domain.stack.Component;
import com.sequenceiq.cloudbreak.domain.stack.Stack;
import com.sequenceiq.cloudbreak.dto.StackDtoDelegate;
import com.sequenceiq.cloudbreak.service.ComponentConfigProviderService;
import com.sequenceiq.cloudbreak.service.image.ImageCatalogService;
import com.sequenceiq.cloudbreak.service.image.ImageService;
import com.sequenceiq.cloudbreak.service.image.PlatformStringTransformer;
import com.sequenceiq.cloudbreak.service.image.StatedImage;
import com.sequenceiq.cloudbreak.service.image.catalog.model.ImageCatalogPlatform;
import com.sequenceiq.cloudbreak.view.StackView;

@Service
public class StackImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackImageService.class);

    @Inject
    private ImageService imageService;

    @Inject
    private ImageCatalogService imageCatalogService;

    @Inject
    private ComponentConfigProviderService componentConfigProviderService;

    @Inject
    private InternalCrnModifier internalCrnModifier;

    @Inject
    private PlatformStringTransformer platformStringTransformer;

    @Inject
    private StackDtoService stackDtoService;

    public void storeNewImageComponent(StackDtoDelegate stack, StatedImage targetImage) {
        try {
            replaceStackImageComponent(stack, targetImage);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Failed to create json", e);
            throw new CloudbreakServiceException("Failed to create json", e);
        }
    }

    public Image getImageModelFromStatedImage(StackView stack, Image currentImage, StatedImage targetImage) {
        try {
            ImageCatalogPlatform platformString = platformStringTransformer.getPlatformStringForImageCatalog(
                    stack.getCloudPlatform(),
                    stack.getPlatformVariant());
            String cloudPlatform = platform(stack.getCloudPlatform()).value().toLowerCase();
            String newImageName = imageService.determineImageName(cloudPlatform, platformString, stack.getRegion(), targetImage.getImage());
            return new Image(newImageName, currentImage.getUserdata(), targetImage.getImage().getOs(), targetImage.getImage().getOsType(),
                    targetImage.getImageCatalogUrl(), targetImage.getImageCatalogName(), targetImage.getImage().getUuid(),
                    targetImage.getImage().getPackageVersions());
        } catch (CloudbreakImageNotFoundException e) {
            LOGGER.info("Could not find image", e);
            throw new CloudbreakServiceException("Could not find image", e);
        }
    }

    public void removeImageByComponentName(Long stackId, String imageComponentName) {
        Optional<Component> imageComponent = findImageComponentByName(stackId, imageComponentName);
        if (imageComponent.isPresent()) {
            LOGGER.debug("Deleting image component {} from stack {}", imageComponentName, stackId);
            componentConfigProviderService.deleteComponents(Collections.singleton(imageComponent.get()));
        } else {
            LOGGER.warn("There is no image component found for stack {} with name {}", stackId, imageComponentName);
        }
    }

    public Image getCurrentImage(Long stackId) throws CloudbreakImageNotFoundException {
            return componentConfigProviderService.getImage(stackId);
    }

    public void changeImageCatalog(Stack stack, String imageCatalog) {
        try {
            Image currentImage = componentConfigProviderService.getImage(stack.getId());
            ImageCatalog sourceImageCatalog = imageCatalogService.getImageCatalogByName(stack.getWorkspace().getId(), currentImage.getImageCatalogName());
            if (imageCatalogService.isCustomImageCatalog(sourceImageCatalog)) {
                throw new CloudbreakServiceException(
                        String.format("Current image catalog '%s' is a non-JSON based one. Image catalog change is not supported.",
                                sourceImageCatalog.getName()));
            }

            ImageCatalog targetImageCatalog = imageCatalogService.getImageCatalogByName(stack.getWorkspace().getId(), imageCatalog);
            if (imageCatalogService.isCustomImageCatalog(targetImageCatalog)) {
                throw new CloudbreakServiceException(String.format("Migrating from a JSON based catalog '%s' to a non-JSON based one '%s' is not supported.",
                        sourceImageCatalog.getName(), targetImageCatalog.getName()));
            }

            StatedImage targetImage = imageCatalogService.getImage(
                    stack.getWorkspace().getId(),
                    targetImageCatalog.getImageCatalogUrl(),
                    targetImageCatalog.getName(),
                    currentImage.getImageId()
            );
            replaceStackImageComponent(stack, targetImage);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to change the image catalog.", e);
            throw new CloudbreakServiceException("Failed to change the image catalog.");
        } catch (CloudbreakImageNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new NotFoundException(e.getMessage());
        } catch (CloudbreakImageCatalogException e) {
            LOGGER.error("Failed to replace stack image component", e);
            throw new CloudbreakServiceException(e.getMessage());
        }
    }

    private void replaceStackImageComponent(StackDtoDelegate stack, StatedImage targetImage) {
        try {
            Image newImage = getImageModelFromStatedImage(stack.getStack(), componentConfigProviderService.getImage(stack.getId()), targetImage);
            Stack stackReference = stackDtoService.getStackReferenceById(stack.getId());
            Component imageComponent = new Component(ComponentType.IMAGE, ComponentType.IMAGE.name(), new Json(newImage), stackReference);
            componentConfigProviderService.replaceImageComponentWithNew(imageComponent);
        } catch (CloudbreakImageNotFoundException e) {
            LOGGER.info("Could not find image", e);
            throw new CloudbreakServiceException("Could not find image", e);
        }
    }

    public Optional<Component> findImageComponentByName(Long stackId, String componentName) {
        Component targetImageComponent = componentConfigProviderService.getComponent(stackId, ComponentType.IMAGE, componentName);
        LOGGER.debug("The following target image found for stack {}, {}", stackId, targetImageComponent);
        return Optional.ofNullable(targetImageComponent);
    }

    public Optional<StatedImage> getStatedImageInternal(Stack stack, com.sequenceiq.cloudbreak.cloud.model.Image image, ImageCatalog imageCatalog) {
        return ThreadBasedUserCrnProvider.doAs(getInternalUserCrn(stack),
                () -> {
                    try {
                        return Optional.ofNullable(imageCatalogService.getImageByCatalogName(
                                stack.getWorkspaceId(), image.getImageId(), imageCatalog.getName()));
                    } catch (Exception e) {
                        LOGGER.warn("Error during obtaining image catalog", e);
                        return Optional.empty();
                    }
                });
    }

    public Optional<StatedImage> getStatedImageInternal(Stack stack) throws CloudbreakImageNotFoundException {
        com.sequenceiq.cloudbreak.cloud.model.Image image = getCurrentImage(stack.getId());
        ImageCatalog imageCatalog = getImageCatalogFromStackAndImage(stack, image);
        return getStatedImageInternal(stack, image, imageCatalog);
    }

    public ImageCatalog getImageCatalogFromStackAndImage(Stack stack, com.sequenceiq.cloudbreak.cloud.model.Image image) {
        return ThreadBasedUserCrnProvider.doAs(getInternalUserCrn(stack),
                () -> imageCatalogService.getImageCatalogByName(stack.getWorkspaceId(), image.getImageCatalogName()));
    }

    private String getInternalUserCrn(Stack stack) {
        return internalCrnModifier.getInternalCrnWithAccountId(Crn.fromString(stack.getResourceCrn()).getAccountId());
    }
}
