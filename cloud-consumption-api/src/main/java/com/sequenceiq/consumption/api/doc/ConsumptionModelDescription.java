package com.sequenceiq.consumption.api.doc;

public class ConsumptionModelDescription {
    public static final String ENVIRONMENT_CRN = "CRN of the environment which the consumption data is for";
    public static final String STORAGE_LOCATION = "Cloud storage location which consumption is being tracked";
    public static final String MONITORED_RESOURCE_TYPE = "Type of the resource which consumption is being tracked";
    public static final String MONITORED_RESOURCE_CRN = "CRN of the resource which consumption is being tracked";
    public static final String MONITORED_RESOURCE_NAME = "Name of the resource which consumption is being tracked";

    private ConsumptionModelDescription() {
    }
}
