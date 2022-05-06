package com.sequenceiq.consumption.flow.consumption.storage.event;

import static com.sequenceiq.consumption.flow.consumption.storage.event.StorageConsumptionCollectionStateSelectors.STORAGE_CONSUMPTION_COLLECTION_FAILED_EVENT;

public class StorageConsumptionCollectionFailureEvent extends StorageConsumptionCollectionEvent {

    private final Exception exception;

    public StorageConsumptionCollectionFailureEvent(Long resourceId, Exception exception, String resourceCrn) {
        super(STORAGE_CONSUMPTION_COLLECTION_FAILED_EVENT.name(), resourceId, resourceCrn, null);
        this.exception = exception;
    }

    @Override
    public String selector() {
        return STORAGE_CONSUMPTION_COLLECTION_FAILED_EVENT.name();
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "StorageConsumptionCollectionHandlerEvent{" +
                "exception=" + exception +
                "} " + super.toString();
    }
}
