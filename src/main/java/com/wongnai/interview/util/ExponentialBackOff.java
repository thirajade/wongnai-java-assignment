package com.wongnai.interview.util;

import java.util.Random;

public class ExponentialBackOff {
    public static final int DEFAULT_RETRIES = 3;
    public static final long DEFAULT_WAIT_TIME_IN_MILLI = 1000;
    public static final long DEFAULT_MAXIMUM_WAIT_TIME_IN_MILLI = 30000;

    private int numberOfRetries;
    private int numberOfTriesLeft;
    private long defaultTimeToWait;
    private long timeToWait;
    private long maxTimeToWait;
    private Random random = new Random();

    public ExponentialBackOff() {
        this(DEFAULT_RETRIES, DEFAULT_WAIT_TIME_IN_MILLI, DEFAULT_MAXIMUM_WAIT_TIME_IN_MILLI);
    }

    public ExponentialBackOff(int numberOfRetries, long defaultTimeToWait, long maxTimeToWait) {
        this.numberOfRetries = numberOfRetries;
        this.numberOfTriesLeft = numberOfRetries;
        this.defaultTimeToWait = defaultTimeToWait;
        this.timeToWait = defaultTimeToWait;
        this.maxTimeToWait = maxTimeToWait;
    }

    public boolean shouldRetry() {
        return numberOfTriesLeft > 0;
    }

    public void errorOccuredWithException() throws Exception {
        numberOfTriesLeft--;
        System.out.println("numberOfTriesLeft=" + numberOfTriesLeft + ", " + System.currentTimeMillis());
        if (!shouldRetry()) {
            throw new Exception("Retry Failed: Total of attempts: " + numberOfRetries + ". Total waited time: "
                    + timeToWait + "ms.");
        }
        waitUntilNextTry();
        if (timeToWait < maxTimeToWait) {
            timeToWait *= 2;
            // we add a random time (google recommendation)
            timeToWait += random.nextInt(500);
        }
    }

    public void errorOccured() {
        numberOfTriesLeft--;
        if (!shouldRetry()) {
            System.out.println("Retry Failed: Total of attempts: " + numberOfRetries + ". Total waited time: "
                    + timeToWait + "ms.");
        }
        waitUntilNextTry();
        if (timeToWait < maxTimeToWait) {
            timeToWait *= 2;
            // we add a random time (google recommendation)
            timeToWait += random.nextInt(500);
        }
    }

    private void waitUntilNextTry() {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            System.out.println("Error waiting until next try for the backoff strategy. Error: " + e.getMessage());
        }
    }

    public long getTimeToWait() {
        return this.timeToWait;
    }

    public void doNotRetry() {
        numberOfTriesLeft = 0;
    }

    public void reset() {
        this.numberOfTriesLeft = numberOfRetries;
        this.timeToWait = defaultTimeToWait;
    }
}
