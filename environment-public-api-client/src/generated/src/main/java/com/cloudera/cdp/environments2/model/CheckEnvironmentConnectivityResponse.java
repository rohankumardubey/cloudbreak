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
import com.cloudera.cdp.environments2.model.CdpCluster;
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
 * Response object to check connectivity to private cloud environment.
 */
@JsonPropertyOrder({
  CheckEnvironmentConnectivityResponse.JSON_PROPERTY_CLUSTERS
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class CheckEnvironmentConnectivityResponse {
  public static final String JSON_PROPERTY_CLUSTERS = "clusters";
  private List<CdpCluster> clusters = new ArrayList<>();

  public CheckEnvironmentConnectivityResponse() { 
  }

  public CheckEnvironmentConnectivityResponse clusters(List<CdpCluster> clusters) {
    this.clusters = clusters;
    return this;
  }

  public CheckEnvironmentConnectivityResponse addClustersItem(CdpCluster clustersItem) {
    if (this.clusters == null) {
      this.clusters = new ArrayList<>();
    }
    this.clusters.add(clustersItem);
    return this;
  }

   /**
   * List of discovered clusters
   * @return clusters
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CLUSTERS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<CdpCluster> getClusters() {
    return clusters;
  }


  @JsonProperty(JSON_PROPERTY_CLUSTERS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setClusters(List<CdpCluster> clusters) {
    this.clusters = clusters;
  }


  /**
   * Return true if this CheckEnvironmentConnectivityResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckEnvironmentConnectivityResponse checkEnvironmentConnectivityResponse = (CheckEnvironmentConnectivityResponse) o;
    return Objects.equals(this.clusters, checkEnvironmentConnectivityResponse.clusters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clusters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckEnvironmentConnectivityResponse {\n");
    sb.append("    clusters: ").append(toIndentedString(clusters)).append("\n");
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

    // add `clusters` to the URL query string
    if (getClusters() != null) {
      for (int i = 0; i < getClusters().size(); i++) {
        if (getClusters().get(i) != null) {
          joiner.add(getClusters().get(i).toUrlQueryString(String.format("%sclusters%s%s", prefix, suffix,
          "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    return joiner.toString();
  }
}

