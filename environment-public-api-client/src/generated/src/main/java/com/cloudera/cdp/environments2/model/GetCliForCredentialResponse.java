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
 * Response object for generating a create credential CLI command.
 */
@JsonPropertyOrder({
  GetCliForCredentialResponse.JSON_PROPERTY_COMMAND,
  GetCliForCredentialResponse.JSON_PROPERTY_ADDITIONAL_COMMANDS
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class GetCliForCredentialResponse {
  public static final String JSON_PROPERTY_COMMAND = "command";
  private String command;

  public static final String JSON_PROPERTY_ADDITIONAL_COMMANDS = "additionalCommands";
  private List<String> additionalCommands = new ArrayList<>();

  public GetCliForCredentialResponse() { 
  }

  public GetCliForCredentialResponse command(String command) {
    this.command = command;
    return this;
  }

   /**
   * cdp cli command string for creating the credential
   * @return command
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_COMMAND)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCommand() {
    return command;
  }


  @JsonProperty(JSON_PROPERTY_COMMAND)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCommand(String command) {
    this.command = command;
  }


  public GetCliForCredentialResponse additionalCommands(List<String> additionalCommands) {
    this.additionalCommands = additionalCommands;
    return this;
  }

  public GetCliForCredentialResponse addAdditionalCommandsItem(String additionalCommandsItem) {
    if (this.additionalCommands == null) {
      this.additionalCommands = new ArrayList<>();
    }
    this.additionalCommands.add(additionalCommandsItem);
    return this;
  }

   /**
   * additional cdp cli commands for creating the credential
   * @return additionalCommands
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ADDITIONAL_COMMANDS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<String> getAdditionalCommands() {
    return additionalCommands;
  }


  @JsonProperty(JSON_PROPERTY_ADDITIONAL_COMMANDS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAdditionalCommands(List<String> additionalCommands) {
    this.additionalCommands = additionalCommands;
  }


  /**
   * Return true if this GetCliForCredentialResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCliForCredentialResponse getCliForCredentialResponse = (GetCliForCredentialResponse) o;
    return Objects.equals(this.command, getCliForCredentialResponse.command) &&
        Objects.equals(this.additionalCommands, getCliForCredentialResponse.additionalCommands);
  }

  @Override
  public int hashCode() {
    return Objects.hash(command, additionalCommands);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCliForCredentialResponse {\n");
    sb.append("    command: ").append(toIndentedString(command)).append("\n");
    sb.append("    additionalCommands: ").append(toIndentedString(additionalCommands)).append("\n");
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

    // add `command` to the URL query string
    if (getCommand() != null) {
      joiner.add(String.format("%scommand%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCommand()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `additionalCommands` to the URL query string
    if (getAdditionalCommands() != null) {
      for (int i = 0; i < getAdditionalCommands().size(); i++) {
        joiner.add(String.format("%sadditionalCommands%s%s=%s", prefix, suffix,
            "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix),
            URLEncoder.encode(String.valueOf(getAdditionalCommands().get(i)), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
      }
    }

    return joiner.toString();
  }
}

