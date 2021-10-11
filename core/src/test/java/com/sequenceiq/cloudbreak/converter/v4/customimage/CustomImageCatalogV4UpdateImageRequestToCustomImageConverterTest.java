package com.sequenceiq.cloudbreak.converter.v4.customimage;

import com.sequenceiq.cloudbreak.api.endpoint.v4.customimage.request.CustomImageCatalogV4UpdateImageRequest;
import com.sequenceiq.cloudbreak.api.endpoint.v4.customimage.request.CustomImageCatalogV4VmImageRequest;
import com.sequenceiq.cloudbreak.domain.CustomImage;
import com.sequenceiq.cloudbreak.domain.VmImage;
import com.sequenceiq.common.api.type.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomImageCatalogV4UpdateImageRequestToCustomImageConverterTest {

    private static final String SOURCE_IMAGE_ID = "source image id";

    private static final String BASE_PARCEL_URL = "base parcel url";

    private static final String VALID_IMAGE_TYPE = "RUNTIME";

    private static final String INVALID_IMAGE_TYPE = "invalid image type";

    private static final String REGION = "region";

    private static final String IMAGE_REFERENCE = "image reference";

    private CustomImageCatalogV4UpdateImageRequestToCustomImageConverter victim;

    @BeforeEach
    public void initTest() {
        victim = new CustomImageCatalogV4UpdateImageRequestToCustomImageConverter();
    }

    @Test
    public void shouldConvert() {
        CustomImageCatalogV4UpdateImageRequest source = new CustomImageCatalogV4UpdateImageRequest();
        source.setSourceImageId(SOURCE_IMAGE_ID);
        source.setBaseParcelUrl(BASE_PARCEL_URL);
        source.setImageType(VALID_IMAGE_TYPE);
        source.setVmImages(Collections.singleton(getVmImageRequest(REGION, IMAGE_REFERENCE)));

        CustomImage result = victim.convert(source);
        assertEquals(SOURCE_IMAGE_ID, result.getCustomizedImageId());
        assertEquals(BASE_PARCEL_URL, result.getBaseParcelUrl());
        assertEquals(ImageType.RUNTIME, result.getImageType());
        assertEquals(1, result.getVmImage().size());

        VmImage vmImage = result.getVmImage().stream().findFirst().get();
        assertEquals(REGION, vmImage.getRegion());
        assertEquals(IMAGE_REFERENCE, vmImage.getImageReference());
    }

    @Test
    public void shouldAllowNulls() {
        CustomImageCatalogV4UpdateImageRequest source = new CustomImageCatalogV4UpdateImageRequest();
        source.setSourceImageId(null);
        source.setBaseParcelUrl(null);
        source.setImageType(null);
        source.setVmImages(null);

        CustomImage result = victim.convert(source);
        assertNull(result.getCustomizedImageId());
        assertNull(result.getBaseParcelUrl());
        assertNull(result.getImageType());
        assertNull(result.getVmImage());
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidImageType() {
        CustomImageCatalogV4UpdateImageRequest source = new CustomImageCatalogV4UpdateImageRequest();
        source.setImageType(INVALID_IMAGE_TYPE);

        assertThrows(IllegalArgumentException.class, () -> victim.convert(source));
    }

    private CustomImageCatalogV4VmImageRequest getVmImageRequest(String region, String imageReference) {
        CustomImageCatalogV4VmImageRequest request = new CustomImageCatalogV4VmImageRequest();
        request.setRegion(region);
        request.setImageReference(imageReference);

        return request;
    }
}