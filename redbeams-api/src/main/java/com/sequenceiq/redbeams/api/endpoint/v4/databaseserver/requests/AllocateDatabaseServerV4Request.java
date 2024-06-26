package com.sequenceiq.redbeams.api.endpoint.v4.databaseserver.requests;

import static com.sequenceiq.cloudbreak.validation.ValidCrn.Effect.DENY;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sequenceiq.cloudbreak.auth.crn.CrnResourceDescriptor;
import com.sequenceiq.cloudbreak.common.mappable.Mappable;
import com.sequenceiq.cloudbreak.common.mappable.ProviderParametersBase;
import com.sequenceiq.cloudbreak.validation.ValidCrn;
import com.sequenceiq.redbeams.api.endpoint.v4.stacks.DatabaseServerV4StackRequest;
import com.sequenceiq.redbeams.api.endpoint.v4.stacks.NetworkV4StackRequest;
import com.sequenceiq.redbeams.api.endpoint.v4.stacks.aws.AwsDBStackV4Parameters;
import com.sequenceiq.redbeams.api.endpoint.v4.stacks.azure.AzureDBStackV4Parameters;
import com.sequenceiq.redbeams.api.endpoint.v4.stacks.gcp.GcpDBStackV4Parameters;
import com.sequenceiq.redbeams.doc.ModelDescriptions;
import com.sequenceiq.redbeams.doc.ModelDescriptions.DBStack;
import com.sequenceiq.redbeams.doc.ModelDescriptions.DatabaseServer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = ModelDescriptions.ALLOCATE_DATABASE_SERVER_REQUEST)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllocateDatabaseServerV4Request extends ProviderParametersBase {

    public static final int RDS_NAME_MAX_LENGTH = 40;

    @Size(max = RDS_NAME_MAX_LENGTH, min = 5, message = "The length of the name must be between 5 to " + RDS_NAME_MAX_LENGTH + " inclusive")
    @Pattern(regexp = "(^[a-z][-a-z0-9]*[a-z0-9]$)",
            message = "The name can only contain lowercase alphanumeric characters and hyphens and must start with an alphanumeric character")
    @Schema(description = DBStack.STACK_NAME)
    private String name;

    @NotNull
    @ValidCrn(resource = CrnResourceDescriptor.ENVIRONMENT)
    @Schema(description = DatabaseServer.ENVIRONMENT_CRN, required = true)
    private String environmentCrn;

    @NotNull
    @ValidCrn(resource = { CrnResourceDescriptor.ENVIRONMENT }, effect = DENY)
    @Schema(description = DatabaseServer.CLUSTER_CRN, required = true)
    private String clusterCrn;

    @Valid
    @Schema(description = DBStack.NETWORK)
    private NetworkV4StackRequest network;

    @NotNull
    @Valid
    @Schema(description = DBStack.DATABASE_SERVER, required = true)
    private DatabaseServerV4StackRequest databaseServer;

    @Schema(description = DBStack.AWS_PARAMETERS)
    private AwsDBStackV4Parameters aws;

    @Schema(description = DBStack.AZURE_PARAMETERS)
    private AzureDBStackV4Parameters azure;

    @Schema(description = DBStack.AZURE_PARAMETERS)
    private GcpDBStackV4Parameters gcp;

    @Schema(description = DatabaseServer.SSL_CONFIG)
    private SslConfigV4Request sslConfig;

    @Schema(description = DatabaseServer.TAGS)
    private Map<String, String> tags = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironmentCrn() {
        return environmentCrn;
    }

    public void setEnvironmentCrn(String environmentCrn) {
        this.environmentCrn = environmentCrn;
    }

    public NetworkV4StackRequest getNetwork() {
        return network;
    }

    public void setNetwork(NetworkV4StackRequest network) {
        this.network = network;
    }

    public DatabaseServerV4StackRequest getDatabaseServer() {
        return databaseServer;
    }

    public void setDatabaseServer(DatabaseServerV4StackRequest databaseServer) {
        this.databaseServer = databaseServer;
    }

    public String getClusterCrn() {
        return clusterCrn;
    }

    public void setClusterCrn(String clusterCrn) {
        this.clusterCrn = clusterCrn;
    }

    public SslConfigV4Request getSslConfig() {
        return sslConfig;
    }

    public void setSslConfig(SslConfigV4Request sslConfig) {
        this.sslConfig = sslConfig;
    }

    @Override
    public AwsDBStackV4Parameters createAws() {
        if (aws == null) {
            aws = new AwsDBStackV4Parameters();
        }
        return aws;
    }

    public void setAws(AwsDBStackV4Parameters aws) {
        this.aws = aws;
    }

    @Override
    public Mappable createGcp() {
        if (gcp == null) {
            gcp = new GcpDBStackV4Parameters();
        }
        return gcp;
    }

    public GcpDBStackV4Parameters getGcp() {
        return gcp;
    }

    public void setGcp(GcpDBStackV4Parameters gcp) {
        this.gcp = gcp;
    }

    @Override
    public Mappable createAzure() {
        if (azure == null) {
            azure = new AzureDBStackV4Parameters();
        }
        return azure;
    }

    public void setAzure(AzureDBStackV4Parameters azure) {
        this.azure = azure;
    }

    @Override
    public Mappable createYarn() {
        return null;
    }

    @Override
    public Mappable createMock() {
        if (aws == null) {
            aws = new AwsDBStackV4Parameters();
        }
        return aws;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public AwsDBStackV4Parameters getAws() {
        return aws;
    }

    @Override
    public String toString() {
        return "AllocateDatabaseServerV4Request{" +
                "name='" + name + '\'' +
                ", environmentCrn='" + environmentCrn + '\'' +
                ", clusterCrn='" + clusterCrn + '\'' +
                ", network=" + network +
                ", databaseServer=" + databaseServer +
                ", aws=" + aws +
                ", azure=" + azure +
                ", gcp=" + gcp +
                ", sslConfig=" + sslConfig +
                ", tags=" + tags +
                '}';
    }
}
