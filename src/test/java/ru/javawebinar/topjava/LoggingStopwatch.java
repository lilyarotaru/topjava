package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoggingStopwatch extends Stopwatch {

    private static final Logger log = LoggerFactory.getLogger(LoggingStopwatch.class);
    private static final Map<String, Long> map = new HashMap<>();
    private final String message = "Test %s took %d ms";
    private final boolean staticRule;

    public LoggingStopwatch(boolean staticRule) {
        this.staticRule = staticRule;
    }

    private void finishedTest() {
        StringBuilder s = new StringBuilder("Statistics of tests:\n");
        map.forEach((key, value) -> s.append(key).append(" - ").append(value).append(" ms\n"));
        log.info(s.toString().trim());
    }

    @Override
    protected void finished(long nanos, Description description) {
        if (staticRule) {
            finishedTest();
        } else {
            log.info(String.format(message, description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos)));
            map.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        }
    }
}
