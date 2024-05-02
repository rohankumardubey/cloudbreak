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
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Request object to set environment configuration settings.
 */
@JsonPropertyOrder({
  SetEnvironmentSettingRequest.JSON_PROPERTY_SETTINGS,
  SetEnvironmentSettingRequest.JSON_PROPERTY_ENVIRONMENT_NAME
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class SetEnvironmentSettingRequest {
  public static final String JSON_PROPERTY_SETTINGS = "settings";
  private Map<String, String> settings = new HashMap<>();

  public static final String JSON_PROPERTY_ENVIRONMENT_NAME = "environmentName";
  private String environmentName;

  public SetEnvironmentSettingRequest() { 
  }

  public SetEnvironmentSettingRequest settings(Map<String, String> settings) {
    this.settings = settings;
    return this;
  }

  public SetEnvironmentSettingRequest putSettingsItem(String key, String settingsItem) {
    if (this.settings == null) {
      this.settings = new HashMap<>();
    }
    this.settings.put(key, settingsItem);
    return this;
  }

   /**
   * Dictionary of settings to set.
   * @return settings
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SETTINGS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Map<String, String> getSettings() {
    return settings;
  }


  @JsonProperty(JSON_PROPERTY_SETTINGS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }


  public SetEnvironmentSettingRequest environmentName(String environmentName) {
    this.environmentName = environmentName;
    return this;
  }

   /**
   * The name or CRN of the environment. Empty to set system wide settings.
   * @return environmentName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEnvironmentName() {
    return environmentName;
  }


  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName;
  }


  /**
   * Return true if this SetEnvironmentSettingRequest object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetEnvironmentSettingRequest setEnvironmentSettingRequest = (SetEnvironmentSettingRequest) o;
    return Objects.equals(this.settings, setEnvironmentSettingRequest.settings) &&
        Objects.equals(this.environmentName, setEnvironmentSettingRequest.environmentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(settings, environmentName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetEnvironmentSettingRequest {\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    environmentName: ").append(toIndentedString(environmentName)).append("\n");
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

    // add `settings` to the URL query string
    if (getSettings() != null) {
      for (String _key : getSettings().keySet()) {
        joiner.add(String.format("%ssettings%s%s=%s", prefix, suffix,
            "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, _key, containerSuffix),
            getSettings().get(_key), URLEncoder.encode(String.valueOf(getSettings().get(_key)), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
      }
    }

    // add `environmentName` to the URL query string
    if (getEnvironmentName() != null) {
      joiner.add(String.format("%senvironmentName%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEnvironmentName()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

