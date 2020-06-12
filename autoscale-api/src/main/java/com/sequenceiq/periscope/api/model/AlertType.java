package com.sequenceiq.periscope.api.model;

public enum AlertType {
    METRIC, TIME, PROMETHEUS, LOAD;

    @Override
    public String toString() {
        switch (this) {
            case TIME: return "SCHEDULE";
            default: return name();
        }
    }
}
