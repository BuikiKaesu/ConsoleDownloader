package com.nameless.ConsoleDownloader.downloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadInfo {
    final private URL url;

    private final List<String> fileNames;

    public DownloadInfo(final String url, final String outFile) throws MalformedURLException {
        this.url = new URL(url);
        this.fileNames = new ArrayList<>();
        this.fileNames.add(outFile);
    }

    public void addFile(String fileName) {
        this.fileNames.add(fileName);
    }

    public URL getUrl() {
        return this.url;
    }

    public List<String> getFileNames() {
        return fileNames;
    }
}
