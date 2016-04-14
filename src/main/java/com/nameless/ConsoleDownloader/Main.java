package com.nameless.ConsoleDownloader;

import com.nameless.ConsoleDownloader.Utils.*;
import com.nameless.ConsoleDownloader.arguments.AppArguments;
import com.nameless.ConsoleDownloader.downloader.*;
import com.nameless.ConsoleDownloader.files.DownloadsInfoFileParser;

import java.nio.file.NoSuchFileException;
import java.util.List;

class Main {

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        try {
            AppArguments arg = new AppArguments(args);
            List<DownloadInfo> downloadsInfo = DownloadsInfoFileParser.parse(arg.getUrlsFile());
            Downloader downloader = new Downloader(downloadsInfo, arg.getOutputFolder(), arg.getNumberOfThreads(), arg.getSpeedLimit());

            downloader.startDownload();

            final double workTime = (System.currentTimeMillis() - startTime) / 1000.0;

            System.out.printf("Downloaded: %d bytes\n", downloader.getDownloadedBytes());
            System.out.printf("Average speed: %s/s\n", Utils.humanReadableByteCount(downloader.getDownloadSpeed()));
            System.out.printf("Work time: %.2f ms\n", workTime);
        } catch (NoSuchFileException e) {
            System.out.printf("Can't find file %s\n", e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
