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
 * Request object for get keytab request.
 */
@JsonPropertyOrder({
  GetKeytabRequest.JSON_PROPERTY_ENVIRONMENT_NAME,
  GetKeytabRequest.JSON_PROPERTY_ACTOR_CRN
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class GetKeytabRequest {
  public static final String JSON_PROPERTY_ENVIRONMENT_NAME = "environmentName";
  private String environmentName;

  public static final String JSON_PROPERTY_ACTOR_CRN = "actorCrn";
  private String actorCrn;

  public GetKeytabRequest() { 
  }

  public GetKeytabRequest environmentName(String environmentName) {
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


  public GetKeytabRequest actorCrn(String actorCrn) {
    this.actorCrn = actorCrn;
    return this;
  }

   /**
   * The CRN of the user or machine user to retrieve the keytab for. If it is not included, it defaults to the user making the request.
   * @return actorCrn
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACTOR_CRN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getActorCrn() {
    return actorCrn;
  }


  @JsonProperty(JSON_PROPERTY_ACTOR_CRN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setActorCrn(String actorCrn) {
    this.actorCrn = actorCrn;
  }


  /**
   * Return true if this GetKeytabRequest object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetKeytabRequest getKeytabRequest = (GetKeytabRequest) o;
    return Objects.equals(this.environmentName, getKeytabRequest.environmentName) &&
        Objects.equals(this.actorCrn, getKeytabRequest.actorCrn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environmentName, actorCrn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetKeytabRequest {\n");
    sb.append("    environmentName: ").append(toIndentedString(environmentName)).append("\n");
    sb.append("    actorCrn: ").append(toIndentedString(actorCrn)).append("\n");
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

    // add `actorCrn` to the URL query string
    if (getActorCrn() != null) {
      joiner.add(String.format("%sactorCrn%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getActorCrn()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

