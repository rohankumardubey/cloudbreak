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
 * Object for validating services.
 */
@JsonPropertyOrder({
  EnvValidation.JSON_PROPERTY_SEVERITY,
  EnvValidation.JSON_PROPERTY_MESSAGE
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class EnvValidation {
  /**
   * The level of validation severity.
   */
  public enum SeverityEnum {
    INFO("INFO"),
    
    WARNING("WARNING"),
    
    ERROR("ERROR");

    private String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SEVERITY = "severity";
  private SeverityEnum severity;

  public static final String JSON_PROPERTY_MESSAGE = "message";
  private String message;

  public EnvValidation() { 
  }

  public EnvValidation severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

   /**
   * The level of validation severity.
   * @return severity
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEVERITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public SeverityEnum getSeverity() {
    return severity;
  }


  @JsonProperty(JSON_PROPERTY_SEVERITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }


  public EnvValidation message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Validation message.
   * @return message
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MESSAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMessage() {
    return message;
  }


  @JsonProperty(JSON_PROPERTY_MESSAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMessage(String message) {
    this.message = message;
  }


  /**
   * Return true if this EnvValidation object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnvValidation envValidation = (EnvValidation) o;
    return Objects.equals(this.severity, envValidation.severity) &&
        Objects.equals(this.message, envValidation.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(severity, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnvValidation {\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

    // add `severity` to the URL query string
    if (getSeverity() != null) {
      joiner.add(String.format("%sseverity%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSeverity()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `message` to the URL query string
    if (getMessage() != null) {
      joiner.add(String.format("%smessage%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getMessage()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

