package com.sequenceiq.redbeams.api.endpoint.v4.database.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sequenceiq.cloudbreak.auth.crn.CrnResourceDescriptor;
import com.sequenceiq.cloudbreak.validation.ValidCrn;
import com.sequenceiq.redbeams.doc.ModelDescriptions;
import com.sequenceiq.redbeams.doc.ModelDescriptions.Database;
import com.sequenceiq.redbeams.doc.ModelDescriptions.DatabaseServer;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A request for creating a database on a database server.
 */
@Schema(description = ModelDescriptions.CREATE_DATABASE_REQUEST)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDatabaseV4Request implements Serializable {

    @ValidCrn(resource = CrnResourceDescriptor.DATABASE_SERVER)
    @NotNull
    @Schema(description = DatabaseServer.CRN)
    private String existingDatabaseServerCrn;

    @NotNull
    @Schema(description = Database.NAME)
    private String databaseName;

    @NotNull
    @Schema(description = Database.TYPE)
    private String type;

    @Size(max = 1000000)
    @Schema(description = Database.DESCRIPTION)
    private String databaseDescription;

    /**
     * Gets the crn of the existing database on which to create the schema.
     *
     * @return the existing database name
     */
    public String getExistingDatabaseServerCrn() {
        return existingDatabaseServerCrn;
    }

    /**
     * Sets the crn of the existing database on which to create the schema.
     *
     * @param existingDatabaseServerCrn the existing database name
     */
    public void setExistingDatabaseServerCrn(String existingDatabaseServerCrn) {
        this.existingDatabaseServerCrn = existingDatabaseServerCrn;
    }

    /**
     * Gets the database name to create.
     *
     * @return the database name
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the database name to create.
     *
     * @param databaseName the database name
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Gets the database type.
     *
     * @return the database type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the database type.
     *
     * @param type the database type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the database description.
     *
     * @return the database description
     */
    public String getDatabaseDescription() {
        return databaseDescription;
    }

    /**
     * Sets the database description.
     *
     * @param databaseDescription the database description
     */
    public void setDatabaseDescription(String databaseDescription) {
        this.databaseDescription = databaseDescription;
    }
}
