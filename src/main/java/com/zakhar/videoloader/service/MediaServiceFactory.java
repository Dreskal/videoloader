package com.zakhar.videoloader.service;

public interface MediaServiceFactory {
    MediaService getDownloader(String url);

}
