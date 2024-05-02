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
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * The related environment where we update the corresponding FreeIPA to use AWS IMDSv2.
 */
@JsonPropertyOrder({
  UpdateFreeipaToAwsImdsV2Request.JSON_PROPERTY_ENVIRONMENT_CRN
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class UpdateFreeipaToAwsImdsV2Request {
  public static final String JSON_PROPERTY_ENVIRONMENT_CRN = "environmentCrn";
  private String environmentCrn;

  public UpdateFreeipaToAwsImdsV2Request() { 
  }

  public UpdateFreeipaToAwsImdsV2Request environmentCrn(String environmentCrn) {
    this.environmentCrn = environmentCrn;
    return this;
  }

   /**
   * The CRN of the environment.
   * @return environmentCrn
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_CRN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getEnvironmentCrn() {
    return environmentCrn;
  }


  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_CRN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEnvironmentCrn(String environmentCrn) {
    this.environmentCrn = environmentCrn;
  }


  /**
   * Return true if this UpdateFreeipaToAwsImdsV2Request object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateFreeipaToAwsImdsV2Request updateFreeipaToAwsImdsV2Request = (UpdateFreeipaToAwsImdsV2Request) o;
    return Objects.equals(this.environmentCrn, updateFreeipaToAwsImdsV2Request.environmentCrn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environmentCrn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateFreeipaToAwsImdsV2Request {\n");
    sb.append("    environmentCrn: ").append(toIndentedString(environmentCrn)).append("\n");
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

    // add `environmentCrn` to the URL query string
    if (getEnvironmentCrn() != null) {
      joiner.add(String.format("%senvironmentCrn%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEnvironmentCrn()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

