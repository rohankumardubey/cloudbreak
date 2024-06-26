package com.sequenceiq.cloudbreak.converter.v4.userprofiles;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.api.endpoint.v4.userprofile.requests.DurationV4Request;

@Component
public class DurationV4RequestToDurationConverter {

    public Duration convert(DurationV4Request source) {
        return Duration
                .ofMinutes(getValue(source.getMinutes()))
                .plusHours(getValue(source.getHours()))
                .plusDays(getValue(source.getDays()));
    }

    private long getValue(Integer value) {
        return value != null ? value.longValue() : 0L;
    }
}
