package ru.javawebinar.topjava.service;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class LoggingStopwatch extends Stopwatch {

    protected final String message = "Test %s %s, took %d ms";
    protected static final Logger log = LoggerFactory.getLogger(LoggingStopwatch.class);
    protected static final Map<String, Long> map = new HashMap<>();

    @PreDestroy
    public static void afterClass() throws Exception {
        StringBuilder s = new StringBuilder("Statistics of tests:\n");
        map.forEach((key, value) -> s.append(key).append(" - ").append(value).append(" ms\n"));
        log.info(s.toString().trim());
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        log.info(String.format(message, description.getMethodName(), "failed", TimeUnit.NANOSECONDS.toMillis(nanos)));
        map.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        log.info(String.format(message, description.getMethodName(), "skipped", TimeUnit.NANOSECONDS.toMillis(nanos)));
        map.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }

    @Override
    protected void finished(long nanos, Description description) {
        log.info(String.format(message, description.getMethodName(), "finished", TimeUnit.NANOSECONDS.toMillis(nanos)));
        map.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }
}
