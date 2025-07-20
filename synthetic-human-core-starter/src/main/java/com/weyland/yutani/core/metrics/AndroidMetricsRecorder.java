package com.weyland.yutani.core.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;


public class AndroidMetricsRecorder {

    private static final Logger log = LoggerFactory.getLogger(AndroidMetricsRecorder.class);

    private static final String METRIC_TASKS_COMPLETED = "android.tasks.completed.total";
    private static final String METRIC_QUEUE_SIZE = "android.queue.size";
    private static final String TAG_AUTHOR = "author";

    private final MeterRegistry meterRegistry;
    private ThreadPoolExecutor executorToMonitor;

    public AndroidMetricsRecorder(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void monitorExecutor(ThreadPoolExecutor executor) {
        this.executorToMonitor = executor;
    }

    @PostConstruct
    private void registerMetrics() {
        if (executorToMonitor != null) {
            Gauge.builder(METRIC_QUEUE_SIZE, executorToMonitor, executor -> executor.getQueue().size())
                    .description("The current number of commands in the COMMON priority queue.")
                    .register(meterRegistry);
            log.info("Registered gauge metric '{}'", METRIC_QUEUE_SIZE);
        } else {
            log.warn("ThreadPoolExecutor was not provided to AndroidMetricsRecorder. Queue size metric will not be available.");
        }
    }

    public void incrementCompletedTasksCounter(String author) {
        Counter.builder(METRIC_TASKS_COMPLETED)
                .description("Total number of completed commands by author.")
                .tag(TAG_AUTHOR, author)
                .register(meterRegistry)
                .increment();
    }
}
