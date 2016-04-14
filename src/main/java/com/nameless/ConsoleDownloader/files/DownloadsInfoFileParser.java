package com.nameless.ConsoleDownloader.files;

import com.nameless.ConsoleDownloader.downloader.DownloadInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DownloadsInfoFileParser {

    private final static Charset ENCODING = StandardCharsets.UTF_8;

    public static List<DownloadInfo> parse(final String fileName) throws IOException, IllegalArgumentException {
        List<DownloadInfo> downloadsInfo = new ArrayList<>();

        Path path = Paths.get(fileName);
        Scanner scanner = new Scanner(path, ENCODING.name());
        int lineNumber = 0;

        while (scanner.hasNextLine()) {
            try {
                String[] params = scanner.nextLine().split(" ");

                lineNumber += 1;

                String url = params[0];
                String downloadFileName = params[1];
                DownloadInfo info = findInfoByUrl(url, downloadsInfo);

                if (info != null) {
                    info.addFile(downloadFileName);
                } else {
                    DownloadInfo newInfo = new DownloadInfo(url, downloadFileName);
                    downloadsInfo.add(newInfo);
                }
            } catch (MalformedURLException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException(String.format("Invalid download info at line %d", lineNumber), e);
            }
        }
        return downloadsInfo;
    }

    private static DownloadInfo findInfoByUrl(final String url, final List<DownloadInfo> downloadsInfo) {
        for(DownloadInfo file : downloadsInfo) {
            if(file.getUrl().toString().equals(url)) {
                return file;
            }
        }
        return null;
    }


}
