package com.sequenceiq.cloudbreak.api.endpoint.v4.imagecatalog.responses;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@JsonIgnoreProperties(ignoreUnknown = true)
@NotNull
public class ImageBasicInfoV4Response {

    @JsonProperty("uuid")
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "ImageBasicInfoV4Response{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
