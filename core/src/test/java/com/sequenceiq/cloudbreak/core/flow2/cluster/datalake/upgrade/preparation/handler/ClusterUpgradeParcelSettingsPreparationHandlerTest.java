package com.sequenceiq.cloudbreak.core.flow2.cluster.datalake.upgrade.preparation.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.sequenceiq.cloudbreak.core.flow2.cluster.datalake.upgrade.preparation.ClusterUpgradePreparationStateSelectors.FAILED_CLUSTER_UPGRADE_PREPARATION_EVENT;
import static com.sequenceiq.cloudbreak.core.flow2.cluster.datalake.upgrade.preparation.ClusterUpgradePreparationStateSelectors.START_CLUSTER_UPGRADE_PARCEL_DOWNLOAD_EVENT;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.cloudbreak.cloud.model.ClouderaManagerProduct;
import com.sequenceiq.cloudbreak.cloud.model.catalog.Image;
import com.sequenceiq.cloudbreak.cluster.api.ClusterApi;
import com.sequenceiq.cloudbreak.common.event.Selectable;
import com.sequenceiq.cloudbreak.core.CloudbreakImageCatalogException;
import com.sequenceiq.cloudbreak.core.CloudbreakImageNotFoundException;
import com.sequenceiq.cloudbreak.core.flow2.cluster.datalake.upgrade.preparation.event.ClusterUpgradeParcelSettingsPreparationEvent;
import com.sequenceiq.cloudbreak.domain.stack.Stack;
import com.sequenceiq.cloudbreak.service.CloudbreakException;
import com.sequenceiq.cloudbreak.service.cluster.ClusterApiConnectors;
import com.sequenceiq.cloudbreak.service.image.ImageCatalogService;
import com.sequenceiq.cloudbreak.service.image.ImageChangeDto;
import com.sequenceiq.cloudbreak.service.image.StatedImage;
import com.sequenceiq.cloudbreak.service.parcel.ParcelService;
import com.sequenceiq.cloudbreak.service.stack.StackService;
import com.sequenceiq.cloudbreak.workspace.model.Workspace;
import com.sequenceiq.flow.reactor.api.handler.HandlerEvent;

import reactor.bus.Event;

@ExtendWith(MockitoExtension.class)
class ClusterUpgradeParcelSettingsPreparationHandlerTest {

    private static final long STACK_ID = 1L;

    private static final long WORKSPACE_ID = 2L;

    private static final String IMAGE_ID = "image-id";

    private static final String IMAGE_CATALOG_NAME = "image-catalog-name";

    private static final String IMAGE_CATALOG_URL = "image-catalog-url";

    @InjectMocks
    private ClusterUpgradeParcelSettingsPreparationHandler underTest;

    @Mock
    private StackService stackService;

    @Mock
    private ClusterApiConnectors clusterApiConnectors;

    @Mock
    private ParcelService parcelService;

    @Mock
    private ImageCatalogService imageCatalogService;

    @Mock
    private ClusterApi clusterApi;

    @Mock
    private Image image;

    @Test
    void testDoAcceptShouldCallClusterApiToUpdateParcelSettings() throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException,
            CloudbreakException {
        ImageChangeDto imageChangeDto = new ImageChangeDto(STACK_ID, IMAGE_ID, IMAGE_CATALOG_NAME, IMAGE_CATALOG_URL);
        Stack stack = createStack();
        Set<ClouderaManagerProduct> requiredProducts = Collections.singleton(new ClouderaManagerProduct());
        when(stackService.getByIdWithListsInTransaction(STACK_ID)).thenReturn(stack);
        when(imageCatalogService.getImage(WORKSPACE_ID, IMAGE_CATALOG_URL, IMAGE_CATALOG_NAME, IMAGE_ID)).thenReturn(
                StatedImage.statedImage(image, IMAGE_CATALOG_URL, IMAGE_CATALOG_NAME));
        when(parcelService.getRequiredProductsFromImage(stack, image)).thenReturn(requiredProducts);
        when(clusterApiConnectors.getConnector(stack)).thenReturn(clusterApi);

        Selectable nextFlowStepSelector = underTest.doAccept(createEvent(imageChangeDto));

        assertEquals(START_CLUSTER_UPGRADE_PARCEL_DOWNLOAD_EVENT.name(), nextFlowStepSelector.selector());
        verify(stackService).getByIdWithListsInTransaction(STACK_ID);
        verify(imageCatalogService).getImage(WORKSPACE_ID, IMAGE_CATALOG_URL, IMAGE_CATALOG_NAME, IMAGE_ID);
        verify(parcelService).getRequiredProductsFromImage(stack, image);
        verify(clusterApiConnectors).getConnector(stack);
        verify(clusterApi).updateParcelSettings(requiredProducts);
    }

    @Test
    void testDoAcceptShouldReturnPreparationFailureEventWhenClusterApiReturnsWithAnException()
            throws CloudbreakImageNotFoundException, CloudbreakImageCatalogException,
            CloudbreakException {
        ImageChangeDto imageChangeDto = new ImageChangeDto(STACK_ID, IMAGE_ID, IMAGE_CATALOG_NAME, IMAGE_CATALOG_URL);
        Stack stack = createStack();
        Set<ClouderaManagerProduct> requiredProducts = Collections.singleton(new ClouderaManagerProduct());
        when(stackService.getByIdWithListsInTransaction(STACK_ID)).thenReturn(stack);
        when(imageCatalogService.getImage(WORKSPACE_ID, IMAGE_CATALOG_URL, IMAGE_CATALOG_NAME, IMAGE_ID)).thenReturn(
                StatedImage.statedImage(image, IMAGE_CATALOG_URL, IMAGE_CATALOG_NAME));
        when(parcelService.getRequiredProductsFromImage(stack, image)).thenReturn(requiredProducts);
        when(clusterApiConnectors.getConnector(stack)).thenReturn(clusterApi);
        doThrow(new CloudbreakException("Failed to update parcel settings")).when(clusterApi).updateParcelSettings(requiredProducts);

        Selectable nextFlowStepSelector = underTest.doAccept(createEvent(imageChangeDto));

        assertEquals(FAILED_CLUSTER_UPGRADE_PREPARATION_EVENT.name(), nextFlowStepSelector.selector());
        verify(stackService).getByIdWithListsInTransaction(STACK_ID);
        verify(imageCatalogService).getImage(WORKSPACE_ID, IMAGE_CATALOG_URL, IMAGE_CATALOG_NAME, IMAGE_ID);
        verify(parcelService).getRequiredProductsFromImage(stack, image);
        verify(clusterApiConnectors).getConnector(stack);
        verify(clusterApi).updateParcelSettings(requiredProducts);
    }

    private HandlerEvent<ClusterUpgradeParcelSettingsPreparationEvent> createEvent(ImageChangeDto imageChangeDto) {
        return new HandlerEvent<>(new Event<>(new ClusterUpgradeParcelSettingsPreparationEvent(STACK_ID, imageChangeDto)));
    }

    private Stack createStack() {
        Stack stack = new Stack();
        stack.setId(STACK_ID);
        Workspace workspace = new Workspace();
        workspace.setId(WORKSPACE_ID);
        stack.setWorkspace(workspace);
        return stack;
    }

}