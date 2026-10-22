package com.zakhar.videoloader.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
@Component
public class InstagramDownloader {

    private final FFmpeg fFmpeg;

    public String download(String url, String outputPath, boolean onlyAudio) throws IOException, InterruptedException{

        String[] words = url.split("/");
        String mediaID = words[words.length-1];

        String command = String.format(
                "python -m instaloader --dirname-pattern=%s -- -%s",
                outputPath,
                mediaID
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

            File[] files = new File(outputPath).listFiles();
            String videoPath = null;


            for(File file: files){
                if(file.getName().endsWith(".mp4")) {
                    videoPath = file.getPath();
                } else {
                    file.delete();
                }
            }
            if(onlyAudio){
                String audioPath = fFmpeg.convertToMp3(videoPath);
                new File(videoPath).delete();
                return audioPath;
            }



            System.out.println("Download complete");

            return videoPath;
        } else {
            throw new RuntimeException("Download filed with code: " + exitCode);
        }

    }
}
