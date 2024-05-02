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
 * Alternative to dockerConfigJson.
 */
@JsonPropertyOrder({
  CreatePrivateEnvironmentRequestDockerUserPass.JSON_PROPERTY_USERNAME,
  CreatePrivateEnvironmentRequestDockerUserPass.JSON_PROPERTY_PASSWORD,
  CreatePrivateEnvironmentRequestDockerUserPass.JSON_PROPERTY_EMAIL,
  CreatePrivateEnvironmentRequestDockerUserPass.JSON_PROPERTY_SERVER
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class CreatePrivateEnvironmentRequestDockerUserPass {
  public static final String JSON_PROPERTY_USERNAME = "username";
  private String username;

  public static final String JSON_PROPERTY_PASSWORD = "password";
  private String password;

  public static final String JSON_PROPERTY_EMAIL = "email";
  private String email;

  public static final String JSON_PROPERTY_SERVER = "server";
  private String server;

  public CreatePrivateEnvironmentRequestDockerUserPass() { 
  }

  public CreatePrivateEnvironmentRequestDockerUserPass username(String username) {
    this.username = username;
    return this;
  }

   /**
   * Docker username.
   * @return username
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_USERNAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getUsername() {
    return username;
  }


  @JsonProperty(JSON_PROPERTY_USERNAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUsername(String username) {
    this.username = username;
  }


  public CreatePrivateEnvironmentRequestDockerUserPass password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Docker password.
   * @return password
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getPassword() {
    return password;
  }


  @JsonProperty(JSON_PROPERTY_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPassword(String password) {
    this.password = password;
  }


  public CreatePrivateEnvironmentRequestDockerUserPass email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Docker email.
   * @return email
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EMAIL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEmail() {
    return email;
  }


  @JsonProperty(JSON_PROPERTY_EMAIL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEmail(String email) {
    this.email = email;
  }


  public CreatePrivateEnvironmentRequestDockerUserPass server(String server) {
    this.server = server;
    return this;
  }

   /**
   * Docker Registry FQDN.
   * @return server
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SERVER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getServer() {
    return server;
  }


  @JsonProperty(JSON_PROPERTY_SERVER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setServer(String server) {
    this.server = server;
  }


  /**
   * Return true if this CreatePrivateEnvironmentRequest_dockerUserPass object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePrivateEnvironmentRequestDockerUserPass createPrivateEnvironmentRequestDockerUserPass = (CreatePrivateEnvironmentRequestDockerUserPass) o;
    return Objects.equals(this.username, createPrivateEnvironmentRequestDockerUserPass.username) &&
        Objects.equals(this.password, createPrivateEnvironmentRequestDockerUserPass.password) &&
        Objects.equals(this.email, createPrivateEnvironmentRequestDockerUserPass.email) &&
        Objects.equals(this.server, createPrivateEnvironmentRequestDockerUserPass.server);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email, server);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePrivateEnvironmentRequestDockerUserPass {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    server: ").append(toIndentedString(server)).append("\n");
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

    // add `username` to the URL query string
    if (getUsername() != null) {
      joiner.add(String.format("%susername%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getUsername()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `password` to the URL query string
    if (getPassword() != null) {
      joiner.add(String.format("%spassword%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getPassword()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `email` to the URL query string
    if (getEmail() != null) {
      joiner.add(String.format("%semail%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEmail()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `server` to the URL query string
    if (getServer() != null) {
      joiner.add(String.format("%sserver%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getServer()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

