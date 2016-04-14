package com.nameless.ConsoleDownloader.Utils;

import java.io.File;

public class Utils {

    public static String createPathFromCurrentDir(final String path) {
        String currentDir = System.getProperty("user.dir");
        File fullPath = new File(currentDir, path);

        fullPath.mkdirs();

        return fullPath.toString();
    }

    public static String humanReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = ("KMGTPE").charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
