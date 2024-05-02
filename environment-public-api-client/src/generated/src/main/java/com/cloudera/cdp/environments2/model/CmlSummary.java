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
import com.cloudera.cdp.environments2.model.CmlWorkspace;
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
 * The CML summary.
 */
@JsonPropertyOrder({
  CmlSummary.JSON_PROPERTY_CML_WORKSPACES
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class CmlSummary {
  public static final String JSON_PROPERTY_CML_WORKSPACES = "cmlWorkspaces";
  private List<CmlWorkspace> cmlWorkspaces = new ArrayList<>();

  public CmlSummary() { 
  }

  public CmlSummary cmlWorkspaces(List<CmlWorkspace> cmlWorkspaces) {
    this.cmlWorkspaces = cmlWorkspaces;
    return this;
  }

  public CmlSummary addCmlWorkspacesItem(CmlWorkspace cmlWorkspacesItem) {
    if (this.cmlWorkspaces == null) {
      this.cmlWorkspaces = new ArrayList<>();
    }
    this.cmlWorkspaces.add(cmlWorkspacesItem);
    return this;
  }

   /**
   * List of CML workspaces based on the environment.
   * @return cmlWorkspaces
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CML_WORKSPACES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<CmlWorkspace> getCmlWorkspaces() {
    return cmlWorkspaces;
  }


  @JsonProperty(JSON_PROPERTY_CML_WORKSPACES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCmlWorkspaces(List<CmlWorkspace> cmlWorkspaces) {
    this.cmlWorkspaces = cmlWorkspaces;
  }


  /**
   * Return true if this CmlSummary object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CmlSummary cmlSummary = (CmlSummary) o;
    return Objects.equals(this.cmlWorkspaces, cmlSummary.cmlWorkspaces);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cmlWorkspaces);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CmlSummary {\n");
    sb.append("    cmlWorkspaces: ").append(toIndentedString(cmlWorkspaces)).append("\n");
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

    // add `cmlWorkspaces` to the URL query string
    if (getCmlWorkspaces() != null) {
      for (int i = 0; i < getCmlWorkspaces().size(); i++) {
        if (getCmlWorkspaces().get(i) != null) {
          joiner.add(getCmlWorkspaces().get(i).toUrlQueryString(String.format("%scmlWorkspaces%s%s", prefix, suffix,
          "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    return joiner.toString();
  }
}

