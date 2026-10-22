package com.zakhar.videoloader.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class YouTubeDownloader {

    public String download(String url, String outputPath, boolean onlyAudio) throws IOException, InterruptedException{

        String filePath = outputPath + System.currentTimeMillis() + (onlyAudio ? ".mp3" : ".mp4");

        String command = String.format(
                "yt-dlp %s -o %s %s",
                onlyAudio ? "-x --audio-format mp3" : "-f mp4",
                filePath,
                url
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
            return filePath;
        } else {
            throw  new RuntimeException("Download filet with code: " + exitCode);
        }

    }
}
