package com.zakhar.videoloader.service.impl;

import com.zakhar.videoloader.exceptions.YouTubeDownloadException;
import com.zakhar.videoloader.service.MediaService;
import com.zakhar.videoloader.utils.YouTubeDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class MediaYouTubeImpl implements MediaService {

    private final YouTubeDownloader youTubeDownloader;



    @Override
    public String downloadVideo(String url, String output) {

        try {
            return youTubeDownloader.download(url, output, false);
        } catch (IOException | InterruptedException e) {
            throw new YouTubeDownloadException("Download fail " + e);
        }

    }

    @Override
    public String downloadAudio(String url, String output) {

        try {
            return youTubeDownloader.download(url, output, true);
        } catch (IOException | InterruptedException e) {
            throw new YouTubeDownloadException("Download fail " + e);
        }

    }
}
