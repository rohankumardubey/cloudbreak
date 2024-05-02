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
 * Additional SSH key authentication configuration for accessing cluster node.
 */
@JsonPropertyOrder({
  AuthenticationRequest.JSON_PROPERTY_PUBLIC_KEY,
  AuthenticationRequest.JSON_PROPERTY_PUBLIC_KEY_ID
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class AuthenticationRequest {
  public static final String JSON_PROPERTY_PUBLIC_KEY = "publicKey";
  private String publicKey;

  public static final String JSON_PROPERTY_PUBLIC_KEY_ID = "publicKeyId";
  private String publicKeyId;

  public AuthenticationRequest() { 
  }

  public AuthenticationRequest publicKey(String publicKey) {
    this.publicKey = publicKey;
    return this;
  }

   /**
   * Public SSH key string. Mutually exclusive with publicKeyId.
   * @return publicKey
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPublicKey() {
    return publicKey;
  }


  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }


  public AuthenticationRequest publicKeyId(String publicKeyId) {
    this.publicKeyId = publicKeyId;
    return this;
  }

   /**
   * Public SSH key ID already registered in the cloud provider. Mutually exclusive with publicKey.
   * @return publicKeyId
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPublicKeyId() {
    return publicKeyId;
  }


  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPublicKeyId(String publicKeyId) {
    this.publicKeyId = publicKeyId;
  }


  /**
   * Return true if this AuthenticationRequest object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthenticationRequest authenticationRequest = (AuthenticationRequest) o;
    return Objects.equals(this.publicKey, authenticationRequest.publicKey) &&
        Objects.equals(this.publicKeyId, authenticationRequest.publicKeyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publicKey, publicKeyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthenticationRequest {\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
    sb.append("    publicKeyId: ").append(toIndentedString(publicKeyId)).append("\n");
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

    // add `publicKey` to the URL query string
    if (getPublicKey() != null) {
      joiner.add(String.format("%spublicKey%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getPublicKey()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `publicKeyId` to the URL query string
    if (getPublicKeyId() != null) {
      joiner.add(String.format("%spublicKeyId%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getPublicKeyId()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

