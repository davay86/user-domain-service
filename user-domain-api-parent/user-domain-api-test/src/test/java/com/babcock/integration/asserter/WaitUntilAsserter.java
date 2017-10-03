package com.babcock.integration.asserter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public abstract class WaitUntilAsserter {

    private static final Logger LOG = LoggerFactory.getLogger(WaitUntilAsserter.class);

    private long maxWaitTime;
    private long accumulatedTime;

    public WaitUntilAsserter() {
        setMaxWaitTime(5000);
    }

    protected abstract boolean execute();

    protected abstract String getTaskName();

    protected abstract String getFailureMessage();

    public long getAccumulatedTime() {
        return accumulatedTime;
    }

    public long getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(long maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void performAssertion() throws InterruptedException {
        accumulatedTime = 0;

        LOG.debug("Initial Accumulated Time = {}",accumulatedTime);

        boolean success = execute();

        LOG.debug("Initial Assertion Result = {}",success);

        while(!success && accumulatedTime < getMaxWaitTime()) {

            accumulatedTime += sleep();
            LOG.debug("Accumulated Time = {}",accumulatedTime);

            LOG.debug("Retrying Assertion");
            success = execute();

            LOG.debug("Assertion Result = {}",success);
        }

        if(!success) {
          throw new WaitUntilAssertionError(getTaskName()+" assertionFailed : "+getFailureMessage());
        }
    }

    private long sleep() throws InterruptedException {
        final long sleepPeriod = 500;
        final long sleepStart = System.currentTimeMillis();

        LOG.debug("Sleeping for = {}",sleepPeriod);
        TimeUnit.MILLISECONDS.sleep(sleepPeriod);

        final long sleepEnd = System.currentTimeMillis();
        return sleepEnd - sleepStart;
    }
}
