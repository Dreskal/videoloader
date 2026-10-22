package com.zakhar.videoloader.service.impl;

import com.zakhar.videoloader.service.MediaService;
import com.zakhar.videoloader.service.MediaServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MediaServiceFactoryImpl implements MediaServiceFactory {

    private final MediaYouTubeImpl mediaYouTube;
    private final MediaInstagramImpl mediaInstagram;

    @Override
    public MediaService getDownloader(String url) {
        if(url.startsWith("https://www.youtube.com")){
            return mediaYouTube;
        } else if (url.startsWith("https://www.instagram.com/")) {
            return mediaInstagram;
        }

        return null;
    }
}
