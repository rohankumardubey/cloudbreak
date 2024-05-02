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
import com.cloudera.cdp.environments2.model.RepairOperationDetails;
import com.cloudera.cdp.environments2.model.RepairStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Response object for Repair Operation.
 */
@JsonPropertyOrder({
  GetRepairFreeipaStatusResponse.JSON_PROPERTY_STATUS,
  GetRepairFreeipaStatusResponse.JSON_PROPERTY_SUCCESSFUL_OPERATION_DETAILS,
  GetRepairFreeipaStatusResponse.JSON_PROPERTY_FAILURE_OPERATION_DETAILS,
  GetRepairFreeipaStatusResponse.JSON_PROPERTY_ERROR,
  GetRepairFreeipaStatusResponse.JSON_PROPERTY_START_DATE,
  GetRepairFreeipaStatusResponse.JSON_PROPERTY_END_DATE
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-05-02T16:36:31.330723+02:00[Europe/Budapest]", comments = "Generator version: 7.5.0")
public class GetRepairFreeipaStatusResponse {
  public static final String JSON_PROPERTY_STATUS = "status";
  private RepairStatus status;

  public static final String JSON_PROPERTY_SUCCESSFUL_OPERATION_DETAILS = "successfulOperationDetails";
  private List<RepairOperationDetails> successfulOperationDetails = new ArrayList<>();

  public static final String JSON_PROPERTY_FAILURE_OPERATION_DETAILS = "failureOperationDetails";
  private List<RepairOperationDetails> failureOperationDetails = new ArrayList<>();

  public static final String JSON_PROPERTY_ERROR = "error";
  private String error;

  public static final String JSON_PROPERTY_START_DATE = "startDate";
  private OffsetDateTime startDate;

  public static final String JSON_PROPERTY_END_DATE = "endDate";
  private OffsetDateTime endDate;

  public GetRepairFreeipaStatusResponse() { 
  }

  public GetRepairFreeipaStatusResponse status(RepairStatus status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public RepairStatus getStatus() {
    return status;
  }


  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStatus(RepairStatus status) {
    this.status = status;
  }


  public GetRepairFreeipaStatusResponse successfulOperationDetails(List<RepairOperationDetails> successfulOperationDetails) {
    this.successfulOperationDetails = successfulOperationDetails;
    return this;
  }

  public GetRepairFreeipaStatusResponse addSuccessfulOperationDetailsItem(RepairOperationDetails successfulOperationDetailsItem) {
    if (this.successfulOperationDetails == null) {
      this.successfulOperationDetails = new ArrayList<>();
    }
    this.successfulOperationDetails.add(successfulOperationDetailsItem);
    return this;
  }

   /**
   * List of operation details for all successes. If the repair is only partially successful both successful and failure operation details will be populated.
   * @return successfulOperationDetails
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SUCCESSFUL_OPERATION_DETAILS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<RepairOperationDetails> getSuccessfulOperationDetails() {
    return successfulOperationDetails;
  }


  @JsonProperty(JSON_PROPERTY_SUCCESSFUL_OPERATION_DETAILS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSuccessfulOperationDetails(List<RepairOperationDetails> successfulOperationDetails) {
    this.successfulOperationDetails = successfulOperationDetails;
  }


  public GetRepairFreeipaStatusResponse failureOperationDetails(List<RepairOperationDetails> failureOperationDetails) {
    this.failureOperationDetails = failureOperationDetails;
    return this;
  }

  public GetRepairFreeipaStatusResponse addFailureOperationDetailsItem(RepairOperationDetails failureOperationDetailsItem) {
    if (this.failureOperationDetails == null) {
      this.failureOperationDetails = new ArrayList<>();
    }
    this.failureOperationDetails.add(failureOperationDetailsItem);
    return this;
  }

   /**
   * List of operation details for failures. If the repair is only partially successful both successful and failure operation details will be populated.
   * @return failureOperationDetails
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FAILURE_OPERATION_DETAILS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<RepairOperationDetails> getFailureOperationDetails() {
    return failureOperationDetails;
  }


  @JsonProperty(JSON_PROPERTY_FAILURE_OPERATION_DETAILS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFailureOperationDetails(List<RepairOperationDetails> failureOperationDetails) {
    this.failureOperationDetails = failureOperationDetails;
  }


  public GetRepairFreeipaStatusResponse error(String error) {
    this.error = error;
    return this;
  }

   /**
   * If there is any error associated. The error will be populated on any error and it may be populated when the operation failure details are empty. The error will typically contain the high level information such as the assocated repair failure phase.
   * @return error
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ERROR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getError() {
    return error;
  }


  @JsonProperty(JSON_PROPERTY_ERROR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setError(String error) {
    this.error = error;
  }


  public GetRepairFreeipaStatusResponse startDate(OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * Date when the operation started.
   * @return startDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_START_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getStartDate() {
    return startDate;
  }


  @JsonProperty(JSON_PROPERTY_START_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }


  public GetRepairFreeipaStatusResponse endDate(OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Date when the operation ended. Omitted if operation has not ended.
   * @return endDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_END_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getEndDate() {
    return endDate;
  }


  @JsonProperty(JSON_PROPERTY_END_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }


  /**
   * Return true if this GetRepairFreeipaStatusResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRepairFreeipaStatusResponse getRepairFreeipaStatusResponse = (GetRepairFreeipaStatusResponse) o;
    return Objects.equals(this.status, getRepairFreeipaStatusResponse.status) &&
        Objects.equals(this.successfulOperationDetails, getRepairFreeipaStatusResponse.successfulOperationDetails) &&
        Objects.equals(this.failureOperationDetails, getRepairFreeipaStatusResponse.failureOperationDetails) &&
        Objects.equals(this.error, getRepairFreeipaStatusResponse.error) &&
        Objects.equals(this.startDate, getRepairFreeipaStatusResponse.startDate) &&
        Objects.equals(this.endDate, getRepairFreeipaStatusResponse.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, successfulOperationDetails, failureOperationDetails, error, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRepairFreeipaStatusResponse {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    successfulOperationDetails: ").append(toIndentedString(successfulOperationDetails)).append("\n");
    sb.append("    failureOperationDetails: ").append(toIndentedString(failureOperationDetails)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

    // add `status` to the URL query string
    if (getStatus() != null) {
      joiner.add(String.format("%sstatus%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getStatus()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `successfulOperationDetails` to the URL query string
    if (getSuccessfulOperationDetails() != null) {
      for (int i = 0; i < getSuccessfulOperationDetails().size(); i++) {
        if (getSuccessfulOperationDetails().get(i) != null) {
          joiner.add(getSuccessfulOperationDetails().get(i).toUrlQueryString(String.format("%ssuccessfulOperationDetails%s%s", prefix, suffix,
          "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `failureOperationDetails` to the URL query string
    if (getFailureOperationDetails() != null) {
      for (int i = 0; i < getFailureOperationDetails().size(); i++) {
        if (getFailureOperationDetails().get(i) != null) {
          joiner.add(getFailureOperationDetails().get(i).toUrlQueryString(String.format("%sfailureOperationDetails%s%s", prefix, suffix,
          "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `error` to the URL query string
    if (getError() != null) {
      joiner.add(String.format("%serror%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getError()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `startDate` to the URL query string
    if (getStartDate() != null) {
      joiner.add(String.format("%sstartDate%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getStartDate()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `endDate` to the URL query string
    if (getEndDate() != null) {
      joiner.add(String.format("%sendDate%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEndDate()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

