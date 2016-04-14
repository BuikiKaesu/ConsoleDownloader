package com.nameless.ConsoleDownloader.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Downloader {

    final private static int TERMINATION_WAIT_TIME = 10;

    private final long speedLimit;
    private final String outFolder;
    private final List<DownloadInfo> downloadsInfo;
    private final ExecutorService executor;
    private long downloadedBytes = 0;
    private long downloadTime = 0;

    public Downloader(final List<DownloadInfo> downloadsInfo, final String outFolder, final int numberOfThreads, final long speedLimit) {
        this.outFolder = outFolder;
        this.downloadsInfo = downloadsInfo;
        this.speedLimit = speedLimit;
        this.executor = initializeService(numberOfThreads);
    }

    public void startDownload() {
        final long startTime = System.currentTimeMillis();

        TokenBucket bucket = new TokenBucket(speedLimit * 10, speedLimit);
        List<Callable<Long>> tasks = new ArrayList<>();
        List<Future<Long>> results;

        try {
            if(speedLimit > 0)
                bucket.start();

            for (DownloadInfo info : downloadsInfo)
                tasks.add(new Download(info, outFolder, bucket));

            results = executor.invokeAll(tasks);

            waitForTermination();

            final long finishTime = System.currentTimeMillis();
            this.downloadTime = finishTime - startTime;

            for (Future<Long> result : results)
                this.downloadedBytes += result.get();

        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw new RuntimeException(cause);
        } finally {
            if(speedLimit > 0)
                bucket.stop();
        }
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public long getDownloadSpeed() {
        int timeInSec = (int) downloadTime / 1000;

        return downloadedBytes / timeInSec;
    }

    private ExecutorService initializeService(final int numberOfThreads) {
        if (numberOfThreads <= downloadsInfo.size())
            return Executors.newFixedThreadPool(numberOfThreads);
        else
            return Executors.newFixedThreadPool(downloadsInfo.size());
    }

    private void waitForTermination() {
        executor.shutdown();
        try {
            boolean terminated;
            do {
                terminated = executor.awaitTermination(TERMINATION_WAIT_TIME, TimeUnit.MINUTES);
            } while (!terminated);
        } catch (InterruptedException e) {}
    }
}
