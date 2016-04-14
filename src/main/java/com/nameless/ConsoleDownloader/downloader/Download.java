package com.nameless.ConsoleDownloader.downloader;

import com.nameless.ConsoleDownloader.files.MultipleStreamsWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;

class Download implements Callable<Long> {

    private final URL url;
    private final List<String> fileNames;
    private final String outFolder;
    private Long bytesDownloaded = 0L;
    private final TokenBucket bucket;

    public Download(DownloadInfo downloadInfo, String outFolder, TokenBucket bucket) {
        this.url = downloadInfo.getUrl();
        this.fileNames = downloadInfo.getFileNames();
        this.outFolder = outFolder;
        this.bucket = bucket;
    }

    @Override
    public Long call() {
        Thread.currentThread().setName(url.toString());
        startDownload();

        return this.bytesDownloaded;
    }

    private void startDownload() {
        MultipleStreamsWriter writer = null;

        try {
            System.out.printf("Downloading %s\n", url);

            writer = new MultipleStreamsWriter(outFolder, fileNames);
            InputStream is = url.openStream();

            byte[] buffer = new byte[2048];
            int length;

            while ((length = is.read(buffer)) != -1) {
                tryTakeTokens(length);
                writer.write(buffer, 0, length);
                bytesDownloaded += length;
            }

            System.out.printf("%s downloaded\n", url);
        } catch(UnknownHostException | FileNotFoundException e) {
            System.out.printf("Can't reach %s\n", url);
        } catch (IOException e) {
            System.out.printf("Error while downloading %s ( %s )\n", url, e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {}
        }
    }

    private void tryTakeTokens(int tokens) {
        if (bucket.isRunning()) {
            while (!bucket.takeTokens(tokens)) {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {}
            }
        }
    }
}
