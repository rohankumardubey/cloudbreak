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
import com.cloudera.cdp.environments2.model.LastAutomatedSyncDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Response object for getting automated sync environment status.
 */
@JsonPropertyOrder({
  GetAutomatedSyncEnvironmentStatusResponse.JSON_PROPERTY_ENVIRONMENT_CRN,
  GetAutomatedSyncEnvironmentStatusResponse.JSON_PROPERTY_SYNC_PENDING_STATE,
  GetAutomatedSyncEnvironmentStatusResponse.JSON_PROPERTY_LAST_SYNC_STATUS
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class GetAutomatedSyncEnvironmentStatusResponse {
  public static final String JSON_PROPERTY_ENVIRONMENT_CRN = "environmentCrn";
  private String environmentCrn;

  /**
   * The state to indicate whether the environment is synced or has a sync pending.
   */
  public enum SyncPendingStateEnum {
    UNKNOWN("UNKNOWN"),
    
    SYNC_PENDING("SYNC_PENDING"),
    
    SYNCED("SYNCED"),
    
    SYNC_HALTED("SYNC_HALTED"),
    
    QUARANTINED("QUARANTINED");

    private String value;

    SyncPendingStateEnum(String value) {
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
    public static SyncPendingStateEnum fromValue(String value) {
      for (SyncPendingStateEnum b : SyncPendingStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SYNC_PENDING_STATE = "syncPendingState";
  private SyncPendingStateEnum syncPendingState;

  public static final String JSON_PROPERTY_LAST_SYNC_STATUS = "lastSyncStatus";
  private LastAutomatedSyncDetails lastSyncStatus;

  public GetAutomatedSyncEnvironmentStatusResponse() { 
  }

  public GetAutomatedSyncEnvironmentStatusResponse environmentCrn(String environmentCrn) {
    this.environmentCrn = environmentCrn;
    return this;
  }

   /**
   * The CRN of the environment.
   * @return environmentCrn
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_CRN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEnvironmentCrn() {
    return environmentCrn;
  }


  @JsonProperty(JSON_PROPERTY_ENVIRONMENT_CRN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEnvironmentCrn(String environmentCrn) {
    this.environmentCrn = environmentCrn;
  }


  public GetAutomatedSyncEnvironmentStatusResponse syncPendingState(SyncPendingStateEnum syncPendingState) {
    this.syncPendingState = syncPendingState;
    return this;
  }

   /**
   * The state to indicate whether the environment is synced or has a sync pending.
   * @return syncPendingState
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SYNC_PENDING_STATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public SyncPendingStateEnum getSyncPendingState() {
    return syncPendingState;
  }


  @JsonProperty(JSON_PROPERTY_SYNC_PENDING_STATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSyncPendingState(SyncPendingStateEnum syncPendingState) {
    this.syncPendingState = syncPendingState;
  }


  public GetAutomatedSyncEnvironmentStatusResponse lastSyncStatus(LastAutomatedSyncDetails lastSyncStatus) {
    this.lastSyncStatus = lastSyncStatus;
    return this;
  }

   /**
   * Get lastSyncStatus
   * @return lastSyncStatus
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LAST_SYNC_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LastAutomatedSyncDetails getLastSyncStatus() {
    return lastSyncStatus;
  }


  @JsonProperty(JSON_PROPERTY_LAST_SYNC_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLastSyncStatus(LastAutomatedSyncDetails lastSyncStatus) {
    this.lastSyncStatus = lastSyncStatus;
  }


  /**
   * Return true if this GetAutomatedSyncEnvironmentStatusResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAutomatedSyncEnvironmentStatusResponse getAutomatedSyncEnvironmentStatusResponse = (GetAutomatedSyncEnvironmentStatusResponse) o;
    return Objects.equals(this.environmentCrn, getAutomatedSyncEnvironmentStatusResponse.environmentCrn) &&
        Objects.equals(this.syncPendingState, getAutomatedSyncEnvironmentStatusResponse.syncPendingState) &&
        Objects.equals(this.lastSyncStatus, getAutomatedSyncEnvironmentStatusResponse.lastSyncStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environmentCrn, syncPendingState, lastSyncStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAutomatedSyncEnvironmentStatusResponse {\n");
    sb.append("    environmentCrn: ").append(toIndentedString(environmentCrn)).append("\n");
    sb.append("    syncPendingState: ").append(toIndentedString(syncPendingState)).append("\n");
    sb.append("    lastSyncStatus: ").append(toIndentedString(lastSyncStatus)).append("\n");
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

    // add `syncPendingState` to the URL query string
    if (getSyncPendingState() != null) {
      joiner.add(String.format("%ssyncPendingState%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSyncPendingState()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `lastSyncStatus` to the URL query string
    if (getLastSyncStatus() != null) {
      joiner.add(getLastSyncStatus().toUrlQueryString(prefix + "lastSyncStatus" + suffix));
    }

    return joiner.toString();
  }
}

