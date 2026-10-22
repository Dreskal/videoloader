package com.zakhar.videoloader.service.impl;

import com.zakhar.videoloader.exceptions.YouTubeDownloadException;
import com.zakhar.videoloader.service.MediaService;
import com.zakhar.videoloader.utils.InstagramDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class MediaInstagramImpl implements MediaService {

    private final InstagramDownloader instagramDownloader;

    @Override
    public String downloadVideo(String url, String output) {

        try {
            return instagramDownloader.download(url, output, false);
        } catch (IOException | InterruptedException e) {
            throw new YouTubeDownloadException("Download fail " + e);
        }
    }

    @Override
    public String downloadAudio(String url, String output) {
        try {
            return instagramDownloader.download(url, output, true);
        } catch (IOException | InterruptedException e) {
            throw new YouTubeDownloadException("Download fail " + e);
        }
    }
}
