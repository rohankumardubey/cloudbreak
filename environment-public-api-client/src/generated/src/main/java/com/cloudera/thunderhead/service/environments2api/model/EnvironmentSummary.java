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


package com.cloudera.thunderhead.service.environments2api.model;

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
import java.time.OffsetDateTime;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * The environment summary.
 */
@JsonPropertyOrder({
  EnvironmentSummary.JSON_PROPERTY_ENVIRONMENT_NAME,
  EnvironmentSummary.JSON_PROPERTY_CRN,
  EnvironmentSummary.JSON_PROPERTY_STATUS,
  EnvironmentSummary.JSON_PROPERTY_REGION,
  EnvironmentSummary.JSON_PROPERTY_CLOUD_PLATFORM,
  EnvironmentSummary.JSON_PROPERTY_CREDENTIAL_NAME,
  EnvironmentSummary.JSON_PROPERTY_DESCRIPTION,
  EnvironmentSummary.JSON_PROPERTY_CREATED,
  EnvironmentSummary.JSON_PROPERTY_PROXY_CONFIG_NAME,
  EnvironmentSummary.JSON_PROPERTY_ENABLE_SECRET_ENCRYPTION,
  EnvironmentSummary.JSON_PROPERTY_CDP_RUNTIME_VERSION,
  EnvironmentSummary.JSON_PROPERTY_CLOUDERA_MANAGER_VERSION,
  EnvironmentSummary.JSON_PROPERTY_CDP_PVC_VERSION
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.5.0")
public class EnvironmentSummary {
  public static final String JSON_PROPERTY_ENVIRONMENT_NAME = "environmentName";
  private String environmentName;

  public static final String JSON_PROPERTY_CRN = "crn";
  private String crn;

  public static final String JSON_PROPERTY_STATUS = "status";
  private String status;

  public static final String JSON_PROPERTY_REGION = "region";
  private String region;

  public static final String JSON_PROPERTY_CLOUD_PLATFORM = "cloudPlatform";
  private String cloudPlatform;

  public static final String JSON_PROPERTY_CREDENTIAL_NAME = "credentialName";
  private String credentialName;

  public static final String JSON_PROPERTY_DESCRIPTION = "description";
  private String description;

  public static final String JSON_PROPERTY_CREATED = "created";
  private OffsetDateTime created;

  public static final String JSON_PROPERTY_PROXY_CONFIG_NAME = "proxyConfigName";
  private String proxyConfigName;

  public static final String JSON_PROPERTY_ENABLE_SECRET_ENCRYPTION = "enableSecretEncryption";
  private Boolean enableSecretEncryption;

  public static final String JSON_PROPERTY_CDP_RUNTIME_VERSION = "cdpRuntimeVersion";
  private String cdpRuntimeVersion;

  public static final String JSON_PROPERTY_CLOUDERA_MANAGER_VERSION = "clouderaManagerVersion";
  private String clouderaManagerVersion;

  public static final String JSON_PROPERTY_CDP_PVC_VERSION = "cdpPvcVersion";
  private String cdpPvcVersion;

  public EnvironmentSummary() { 
  }

  public EnvironmentSummary environmentName(String environmentName) {
    this.environmentName = environmentName;
    return this;
  }

   /**
   * Name of the environment.
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


  public EnvironmentSummary crn(String crn) {
    this.crn = crn;
    return this;
  }

   /**
   * CRN of the environment.
   * @return crn
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CRN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getCrn() {
    return crn;
  }


  @JsonProperty(JSON_PROPERTY_CRN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCrn(String crn) {
    this.crn = crn;
  }


  public EnvironmentSummary status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Status of the environment,
   * @return status
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getStatus() {
    return status;
  }


  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setStatus(String status) {
    this.status = status;
  }


  public EnvironmentSummary region(String region) {
    this.region = region;
    return this;
  }

   /**
   * Region of the environment.
   * @return region
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_REGION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getRegion() {
    return region;
  }


  @JsonProperty(JSON_PROPERTY_REGION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRegion(String region) {
    this.region = region;
  }


  public EnvironmentSummary cloudPlatform(String cloudPlatform) {
    this.cloudPlatform = cloudPlatform;
    return this;
  }

   /**
   * Cloud platform of the environment.
   * @return cloudPlatform
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CLOUD_PLATFORM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getCloudPlatform() {
    return cloudPlatform;
  }


  @JsonProperty(JSON_PROPERTY_CLOUD_PLATFORM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCloudPlatform(String cloudPlatform) {
    this.cloudPlatform = cloudPlatform;
  }


  public EnvironmentSummary credentialName(String credentialName) {
    this.credentialName = credentialName;
    return this;
  }

   /**
   * Name of the credential of the environment. Must contain only lowercase letters, numbers and hyphens.
   * @return credentialName
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CREDENTIAL_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getCredentialName() {
    return credentialName;
  }


  @JsonProperty(JSON_PROPERTY_CREDENTIAL_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCredentialName(String credentialName) {
    this.credentialName = credentialName;
  }


  public EnvironmentSummary description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of the environment.
   * @return description
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDescription() {
    return description;
  }


  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDescription(String description) {
    this.description = description;
  }


  public EnvironmentSummary created(OffsetDateTime created) {
    this.created = created;
    return this;
  }

   /**
   * Creation date
   * @return created
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CREATED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getCreated() {
    return created;
  }


  @JsonProperty(JSON_PROPERTY_CREATED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }


  public EnvironmentSummary proxyConfigName(String proxyConfigName) {
    this.proxyConfigName = proxyConfigName;
    return this;
  }

   /**
   * Name of the proxy config of the environment.
   * @return proxyConfigName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PROXY_CONFIG_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getProxyConfigName() {
    return proxyConfigName;
  }


  @JsonProperty(JSON_PROPERTY_PROXY_CONFIG_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProxyConfigName(String proxyConfigName) {
    this.proxyConfigName = proxyConfigName;
  }


  public EnvironmentSummary enableSecretEncryption(Boolean enableSecretEncryption) {
    this.enableSecretEncryption = enableSecretEncryption;
    return this;
  }

   /**
   * True if the secret encryption feature is enabled for the environment.
   * @return enableSecretEncryption
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENABLE_SECRET_ENCRYPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEnableSecretEncryption() {
    return enableSecretEncryption;
  }


  @JsonProperty(JSON_PROPERTY_ENABLE_SECRET_ENCRYPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEnableSecretEncryption(Boolean enableSecretEncryption) {
    this.enableSecretEncryption = enableSecretEncryption;
  }


  public EnvironmentSummary cdpRuntimeVersion(String cdpRuntimeVersion) {
    this.cdpRuntimeVersion = cdpRuntimeVersion;
    return this;
  }

   /**
   * The version of CDP runtime.
   * @return cdpRuntimeVersion
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CDP_RUNTIME_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCdpRuntimeVersion() {
    return cdpRuntimeVersion;
  }


  @JsonProperty(JSON_PROPERTY_CDP_RUNTIME_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCdpRuntimeVersion(String cdpRuntimeVersion) {
    this.cdpRuntimeVersion = cdpRuntimeVersion;
  }


  public EnvironmentSummary clouderaManagerVersion(String clouderaManagerVersion) {
    this.clouderaManagerVersion = clouderaManagerVersion;
    return this;
  }

   /**
   * The version of Cloudera Manager that the environment is registered with.
   * @return clouderaManagerVersion
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLOUDERA_MANAGER_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClouderaManagerVersion() {
    return clouderaManagerVersion;
  }


  @JsonProperty(JSON_PROPERTY_CLOUDERA_MANAGER_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClouderaManagerVersion(String clouderaManagerVersion) {
    this.clouderaManagerVersion = clouderaManagerVersion;
  }


  public EnvironmentSummary cdpPvcVersion(String cdpPvcVersion) {
    this.cdpPvcVersion = cdpPvcVersion;
    return this;
  }

   /**
   * The version of CDP PVC.
   * @return cdpPvcVersion
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CDP_PVC_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCdpPvcVersion() {
    return cdpPvcVersion;
  }


  @JsonProperty(JSON_PROPERTY_CDP_PVC_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCdpPvcVersion(String cdpPvcVersion) {
    this.cdpPvcVersion = cdpPvcVersion;
  }


  /**
   * Return true if this EnvironmentSummary object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnvironmentSummary environmentSummary = (EnvironmentSummary) o;
    return Objects.equals(this.environmentName, environmentSummary.environmentName) &&
        Objects.equals(this.crn, environmentSummary.crn) &&
        Objects.equals(this.status, environmentSummary.status) &&
        Objects.equals(this.region, environmentSummary.region) &&
        Objects.equals(this.cloudPlatform, environmentSummary.cloudPlatform) &&
        Objects.equals(this.credentialName, environmentSummary.credentialName) &&
        Objects.equals(this.description, environmentSummary.description) &&
        Objects.equals(this.created, environmentSummary.created) &&
        Objects.equals(this.proxyConfigName, environmentSummary.proxyConfigName) &&
        Objects.equals(this.enableSecretEncryption, environmentSummary.enableSecretEncryption) &&
        Objects.equals(this.cdpRuntimeVersion, environmentSummary.cdpRuntimeVersion) &&
        Objects.equals(this.clouderaManagerVersion, environmentSummary.clouderaManagerVersion) &&
        Objects.equals(this.cdpPvcVersion, environmentSummary.cdpPvcVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environmentName, crn, status, region, cloudPlatform, credentialName, description, created, proxyConfigName, enableSecretEncryption, cdpRuntimeVersion, clouderaManagerVersion, cdpPvcVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnvironmentSummary {\n");
    sb.append("    environmentName: ").append(toIndentedString(environmentName)).append("\n");
    sb.append("    crn: ").append(toIndentedString(crn)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    cloudPlatform: ").append(toIndentedString(cloudPlatform)).append("\n");
    sb.append("    credentialName: ").append(toIndentedString(credentialName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    proxyConfigName: ").append(toIndentedString(proxyConfigName)).append("\n");
    sb.append("    enableSecretEncryption: ").append(toIndentedString(enableSecretEncryption)).append("\n");
    sb.append("    cdpRuntimeVersion: ").append(toIndentedString(cdpRuntimeVersion)).append("\n");
    sb.append("    clouderaManagerVersion: ").append(toIndentedString(clouderaManagerVersion)).append("\n");
    sb.append("    cdpPvcVersion: ").append(toIndentedString(cdpPvcVersion)).append("\n");
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

    // add `crn` to the URL query string
    if (getCrn() != null) {
      joiner.add(String.format("%scrn%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCrn()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `status` to the URL query string
    if (getStatus() != null) {
      joiner.add(String.format("%sstatus%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getStatus()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `region` to the URL query string
    if (getRegion() != null) {
      joiner.add(String.format("%sregion%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getRegion()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `cloudPlatform` to the URL query string
    if (getCloudPlatform() != null) {
      joiner.add(String.format("%scloudPlatform%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCloudPlatform()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `credentialName` to the URL query string
    if (getCredentialName() != null) {
      joiner.add(String.format("%scredentialName%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCredentialName()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `description` to the URL query string
    if (getDescription() != null) {
      joiner.add(String.format("%sdescription%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getDescription()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `created` to the URL query string
    if (getCreated() != null) {
      joiner.add(String.format("%screated%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCreated()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `proxyConfigName` to the URL query string
    if (getProxyConfigName() != null) {
      joiner.add(String.format("%sproxyConfigName%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getProxyConfigName()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `enableSecretEncryption` to the URL query string
    if (getEnableSecretEncryption() != null) {
      joiner.add(String.format("%senableSecretEncryption%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEnableSecretEncryption()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `cdpRuntimeVersion` to the URL query string
    if (getCdpRuntimeVersion() != null) {
      joiner.add(String.format("%scdpRuntimeVersion%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCdpRuntimeVersion()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `clouderaManagerVersion` to the URL query string
    if (getClouderaManagerVersion() != null) {
      joiner.add(String.format("%sclouderaManagerVersion%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getClouderaManagerVersion()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    // add `cdpPvcVersion` to the URL query string
    if (getCdpPvcVersion() != null) {
      joiner.add(String.format("%scdpPvcVersion%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCdpPvcVersion()), StandardCharsets.UTF_8).replaceAll("\\+", "%20")));
    }

    return joiner.toString();
  }
}

