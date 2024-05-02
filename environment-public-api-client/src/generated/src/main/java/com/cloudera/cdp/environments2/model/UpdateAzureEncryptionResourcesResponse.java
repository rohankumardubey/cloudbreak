/*
 * Cloudera Environments Service
 * Cloudera Environments Service is a web service that manages cloud provider access.
 *
 * The version of the OpenAPI document: __API_VERSION__
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.cloudera.cdp.environments2.model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import com.cloudera.cdp.environments2.model.AzureResourceEncryptionParameters;
import com.cloudera.cdp.environments2.model.Environment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Response object for a update Azure encryption resources request.
 */
@JsonPropertyOrder({
  UpdateAzureEncryptionResourcesResponse.JSON_PROPERTY_ENVIRONMENT,
  UpdateAzureEncryptionResourcesResponse.JSON_PROPERTY_RESOURCE_ENCRYPTION_PARAMETERS
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class UpdateAzureEncryptionResourcesResponse {
  public static final String JSON_PROPERTY_ENVIRONMENT = "environment";
  private Environment environment;

  public static final String JSON_PROPERTY_RESOURCE_ENCRYPTION_PARAMETERS = "resourceEncryptionParameters";
  private AzureResourceEncryptionParameters resourceEncryptionParameters;

  public UpdateAzureEncryptionResourcesResponse() { 
  }

  public UpdateAzureEncryptionResourcesResponse environment(Environment environment) {
    this.environment = environment;
    return this;
  }

   /**
   * Get environment
   * @return environment
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ENVIRONMENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Environment getEnvironment() {
    return environment;
  }


  @JsonProperty(JSON_PROPERTY_ENVIRONMENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }


  public UpdateAzureEncryptionResourcesResponse resourceEncryptionParameters(AzureResourceEncryptionParameters resourceEncryptionParameters) {
    this.resourceEncryptionParameters = resourceEncryptionParameters;
    return this;
  }

   /**
   * Get resourceEncryptionParameters
   * @return resourceEncryptionParameters
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_ENCRYPTION_PARAMETERS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AzureResourceEncryptionParameters getResourceEncryptionParameters() {
    return resourceEncryptionParameters;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_ENCRYPTION_PARAMETERS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceEncryptionParameters(AzureResourceEncryptionParameters resourceEncryptionParameters) {
    this.resourceEncryptionParameters = resourceEncryptionParameters;
  }


  /**
   * Return true if this UpdateAzureEncryptionResourcesResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateAzureEncryptionResourcesResponse updateAzureEncryptionResourcesResponse = (UpdateAzureEncryptionResourcesResponse) o;
    return Objects.equals(this.environment, updateAzureEncryptionResourcesResponse.environment) &&
        Objects.equals(this.resourceEncryptionParameters, updateAzureEncryptionResourcesResponse.resourceEncryptionParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environment, resourceEncryptionParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateAzureEncryptionResourcesResponse {\n");
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    resourceEncryptionParameters: ").append(toIndentedString(resourceEncryptionParameters)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Convert the instance into URL query string.
   *
   * @return URL query string
   */
  public String toUrlQueryString() {
    return toUrlQueryString(null);
  }

  /**
   * Convert the instance into URL query string.
   *
   * @param prefix prefix of the query string
   * @return URL query string
   */
  public String toUrlQueryString(String prefix) {
    String suffix = "";
    String containerSuffix = "";
    String containerPrefix = "";
    if (prefix == null) {
      // style=form, explode=true, e.g. /pet?name=cat&type=manx
      prefix = "";
    } else {
      // deepObject style e.g. /pet?id[name]=cat&id[type]=manx
      prefix = prefix + "[";
      suffix = "]";
      containerSuffix = "]";
      containerPrefix = "[";
    }

    StringJoiner joiner = new StringJoiner("&");

    // add `environment` to the URL query string
    if (getEnvironment() != null) {
      joiner.add(getEnvironment().toUrlQueryString(prefix + "environment" + suffix));
    }

    // add `resourceEncryptionParameters` to the URL query string
    if (getResourceEncryptionParameters() != null) {
      joiner.add(getResourceEncryptionParameters().toUrlQueryString(prefix + "resourceEncryptionParameters" + suffix));
    }

    return joiner.toString();
  }
}

