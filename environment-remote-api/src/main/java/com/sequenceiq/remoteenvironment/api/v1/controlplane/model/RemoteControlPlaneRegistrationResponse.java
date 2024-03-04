package com.sequenceiq.remoteenvironment.api.v1.controlplane.model;

import java.util.Objects;

import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoteControlPlaneRegistrationResponse {

    @NotEmpty
    @Schema
    private String crn;

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteControlPlaneRegistrationResponse that = (RemoteControlPlaneRegistrationResponse) o;
        return Objects.equals(crn, that.crn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crn);
    }

    @Override
    public String toString() {
        return "RemoteControlPlaneRegistrationResponse{" +
                "crn='" + crn + '\'' +
                '}';
    }
}
