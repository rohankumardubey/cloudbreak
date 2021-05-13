package com.sequenceiq.cloudbreak.quartz.metric;

import java.time.Duration;

import javax.inject.Inject;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.common.metrics.MetricService;

@Component
public class JobMetricsListener extends JobListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobMetricsListener.class);

    @Inject
    private MetricService metricService;

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        LOGGER.trace("Job will be executed with group: {}, name: {}, class: {}",
                jobKey.getGroup(), jobKey.getName(), context.getJobDetail().getJobClass().getName());
        metricService.incrementMetricCounter(QuartzMetricType.JOB_TRIGGERED,
                "group", jobKey.getGroup());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobKey jobKey = context.getJobDetail().getKey();
        LOGGER.trace("Job was executed with group: {}, name: {}, class: {} in {} ms",
                jobKey.getGroup(), jobKey.getName(), context.getJobDetail().getJobClass().getName(), context.getJobRunTime());
        QuartzMetricType metricType = jobException == null ? QuartzMetricType.JOB_FINISHED : QuartzMetricType.JOB_FAILED;
        Duration jobRunTime = Duration.ofMillis(context.getJobRunTime());
        metricService.recordTimerMetric(metricType, jobRunTime,
                "group", jobKey.getGroup());
    }
}
