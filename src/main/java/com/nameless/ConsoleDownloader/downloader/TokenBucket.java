package com.nameless.ConsoleDownloader.downloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TokenBucket {

    private volatile long availableTokens = 0;
    private final long capacity;
    private final long tokensRate;
    private boolean isRunning;
    private final ExecutorService refillService = Executors.newSingleThreadExecutor();

    public TokenBucket(final long capacity, final long speed) {
        this.capacity = capacity;
        this.tokensRate = speed / 10;
    }

    public void start() {
        BucketRefillThread refillThread = new BucketRefillThread(this);
        refillService.submit(refillThread);
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;

        refillService.shutdownNow();

        try {
            boolean terminated;

            do {
                terminated = refillService.awaitTermination(10, TimeUnit.MINUTES);
            } while (!terminated);
        } catch (InterruptedException e) {}
    }

    private void addTokens() {
        if (availableTokens + tokensRate > capacity)
            availableTokens = capacity;
        else
            availableTokens += tokensRate;
    }

    public synchronized boolean takeTokens(int tokens) {
        if (availableTokens >= tokens) {
            availableTokens -= tokens;
            return true;
        }
        return false;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    private class BucketRefillThread implements Runnable {

        private final TokenBucket bucket;

        public BucketRefillThread(final TokenBucket bucket) {
            this.bucket = bucket;
        }

        @Override
        public void run() {
            while (bucket.isRunning()) {
                try {
                    if (bucket.availableTokens < bucket.capacity) {
                        bucket.addTokens();
                        Thread.currentThread().sleep(100);
                    }
                } catch (InterruptedException e) {}
            }
        }

    }
}
