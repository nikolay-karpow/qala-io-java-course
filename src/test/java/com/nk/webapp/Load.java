package com.nk.webapp;

import com.google.common.io.ByteStreams;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Load {

    @Test
    public void load() throws Exception {
        int nThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        URL url = new URL("http://localhost:8080/sjc/hello");
        for (int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100000; j++) {
                    System.out.println(Thread.currentThread().getName());
                    readUrl(url);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }

    private void readUrl(URL url) {
        try (InputStream inputStream = url.openStream()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ByteStreams.copy(inputStream, byteArrayOutputStream);
            System.out.println(Thread.currentThread().getName() + new Date() + new String(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            System.err.println("error");
            e.printStackTrace();
        }
    }
}
