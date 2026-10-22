package com.zakhar.videoloader.service;


public interface MediaService {
    String downloadVideo(String url, String outputName);
    String downloadAudio(String url, String output);
}
