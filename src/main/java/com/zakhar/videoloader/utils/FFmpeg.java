package com.zakhar.videoloader.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class FFmpeg {

    public String convertToMp3(String filePath) throws IOException, InterruptedException {

        String newFilePath = filePath.replace(".mp4", ".mp3");

        String command = String.format(
                "ffmpeg -i %s %s",
                filePath,
                newFilePath
        );

        Process process = Runtime.getRuntime().exec(command);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {

            System.out.println("Download complete");

            return newFilePath;
        } else {
            throw new RuntimeException("Download filed with code: " + exitCode);
        }
    }

}
