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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * The request object for FreeIPA downscale. Either targetAvailabilityType or instances
 */
@JsonPropertyOrder({
  DownscaleFreeipaRequest.JSON_PROPERTY_ENVIRONMENT_NAME,
  DownscaleFreeipaRequest.JSON_PROPERTY_TARGET_AVAILABILITY_TYPE,
  DownscaleFreeipaRequest.JSON_PROPERTY_INSTANCES
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class DownscaleFreeipaRequest {
  public static final String JSON_PROPERTY_ENVIRONMENT_NAME = "environmentName";
  private String environmentName;

  /**
   * The target FreeIPA availability type.
   */
  public enum TargetAvailabilityTypeEnum {
    HA("HA"),
    
    TWO_NODE_BASED("TWO_NODE_BASED");

    private String value;

    TargetAvailabilityTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TargetAvailabilityTypeEnum fromValue(String value) {
      for (TargetAvailabilityTypeEnum b : TargetAvailabilityTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_TARGET_AVAILABILITY_TYPE = "targetAvailabilityType";
  private TargetAvailabilityTypeEnum targetAvailabilityType;

  public static final String JSON_PROPERTY_INSTANCES = "instances";
  private List<String> instances = new ArrayList<>();

  public DownscaleFreeipaRequest() { 
  }

  public DownscaleFreeipaRequest environmentName(String environmentName) {
    this.environmentName = environmentName;
    return this;
  }

   /**
   * The name or CRN of the environment.
   * @return environmentName
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getEnvironmentName() {
    return environmentName;
  }


  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName;
  }


  public DownscaleFreeipaRequest targetAvailabilityType(TargetAvailabilityTypeEnum targetAvailabilityType) {
    this.targetAvailabilityType = targetAvailabilityType;
    return this;
  }

   /**
   * The target FreeIPA availability type.
   * @return targetAvailabilityType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TARGET_AVAILABILITY_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public TargetAvailabilityTypeEnum getTargetAvailabilityType() {
    return targetAvailabilityType;
  }


  @JsonProperty(JSON_PROPERTY_TARGET_AVAILABILITY_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTargetAvailabilityType(TargetAvailabilityTypeEnum targetAvailabilityType) {
    this.targetAvailabilityType = targetAvailabilityType;
  }


  public DownscaleFreeipaRequest instances(List<String> instances) {
    this.instances = instances;
    return this;
  }

  public DownscaleFreeipaRequest addInstancesItem(String instancesItem) {
    if (this.instances == null) {
      this.instances = new ArrayList<>();
    }
    this.instances.add(instancesItem);
    return this;
  }

   /**
   * The instance Ids to downscale.
   * @return instances
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INSTANCES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<String> getInstances() {
    return instances;
  }


  @JsonProperty(JSON_PROPERTY_INSTANCES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInstances(List<String> instances) {
    this.instances = instances;
  }


  /**
   * Return true if this DownscaleFreeipaRequest object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DownscaleFreeipaRequest downscaleFreeipaRequest = (DownscaleFreeipaRequest) o;
    return Objects.equals(this.environmentName, downscaleFreeipaRequest.environmentName) &&
        Objects.equals(this.targetAvailabilityType, downscaleFreeipaRequest.targetAvailabilityType) &&
        Objects.equals(this.instances, downscaleFreeipaRequest.instances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environmentName, targetAvailabilityType, instances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DownscaleFreeipaRequest {\n");
    sb.append("    environmentName: ").append(toIndentedString(environmentName)).append("\n");
    sb.append("    targetAvailabilityType: ").append(toIndentedString(targetAvailabilityType)).append("\n");
    sb.append("    instances: ").append(toIndentedString(instances)).append("\n");
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

    // add `environmentName` to the URL query string
    if (getEnvironmentName() != null) {
      joiner.add(String.format("%senvironmentName%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEnvironmentName()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `targetAvailabilityType` to the URL query string
    if (getTargetAvailabilityType() != null) {
      joiner.add(String.format("%stargetAvailabilityType%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getTargetAvailabilityType()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `instances` to the URL query string
    if (getInstances() != null) {
      for (int i = 0; i < getInstances().size(); i++) {
        joiner.add(String.format("%sinstances%s%s=%s", prefix, suffix,
            "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix),
            URLEncoder.encode(String.valueOf(getInstances().get(i)), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
      }
    }

    return joiner.toString();
  }
}

