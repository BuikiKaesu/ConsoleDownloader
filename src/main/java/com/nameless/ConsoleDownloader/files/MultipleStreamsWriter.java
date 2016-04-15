package com.nameless.ConsoleDownloader.files;

import com.nameless.ConsoleDownloader.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MultipleStreamsWriter {
    final private String outFolder;
    final private List<FileOutputStream> outputStreams = new ArrayList<>();


    public MultipleStreamsWriter(final String outFolder, final List<String> fileNames) throws IOException {
        this.outFolder = Utils.createPathFromCurrentDir(outFolder);
        initializeWriters(fileNames);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        for (FileOutputStream os : outputStreams) {
            os.write(b, off, len);
        }
    }

    public void close() throws IOException {
        for (FileOutputStream os : outputStreams) {
            os.close();
        }
    }

    private void initializeWriters(final List<String> outFiles) throws IOException {
        for (String fileName : outFiles) {
            File file = new File(outFolder, fileName);
            file.createNewFile();
            outputStreams.add(new FileOutputStream(file.toString()));
        }
    }
}
