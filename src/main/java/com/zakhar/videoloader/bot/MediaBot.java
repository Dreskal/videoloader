package com.zakhar.videoloader.bot;

import com.zakhar.videoloader.bot.buttons.KeyboardButtons;
import com.zakhar.videoloader.service.MediaService;
import com.zakhar.videoloader.service.MediaServiceFactory;
import com.zakhar.videoloader.service.impl.MediaServiceFactoryImpl;
import com.zakhar.videoloader.utils.YouTubeDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MediaBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Value("${media.video}")
    private String videoDir;

    @Value("${media.audio}")
    private String audioDir;

    private final MediaServiceFactoryImpl mediaServiceFactory;
    private final KeyboardButtons keyboardButtons;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {


        if(update.hasMessage()){
            Message message = update.getMessage();
            long chatID = message.getChatId();
            if(message.hasText()) {
                String text = message.getText();
                    if (text.startsWith("/")){
                        handleCommand(text,chatID);
                    }else{
                        sendChooseMediaType(text, chatID);
                    }
            }
        }

        if (update.hasCallbackQuery()) {

            CallbackQuery callback = update.getCallbackQuery();
            Message message = callback.getMessage();

            String text = message.getText();
            long chatID = message.getChatId();
            String data = callback.getData();


            MediaService mediaService = mediaServiceFactory.getDownloader(text);

            switch (data){
                case "video" -> {
                    changeButtons(message, keyboardButtons.waitState());
                    handleDownload(text, chatID, false);
                    changeButtons(message, keyboardButtons.chooseMediaType());

                }
                case "audio" -> {
                    changeButtons(message, keyboardButtons.waitState());
                    handleDownload(text, chatID, true);
                    changeButtons(message, keyboardButtons.chooseMediaType());
                }
            }
        }
    }

    private void handleDownload(String url, long chatId, boolean onlyAudio){

        MediaService mediaService = mediaServiceFactory.getDownloader(url);

        String output = (onlyAudio ? audioDir : videoDir) + "/";

        String outputPath;

        if(onlyAudio){
            outputPath = mediaService.downloadAudio(url, output);
            sendAudio(outputPath, chatId);
        }
        else {
            outputPath = mediaService.downloadVideo(url, output);
            sendVideo(outputPath, chatId);
        }

        File file = new File(outputPath);

        file.delete();

    }

    private void sendChooseMediaType(String text, long chatID){



        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatID)
                .text(text)
                .replyMarkup(keyboardButtons.chooseMediaType())
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeButtons(Message message, InlineKeyboardMarkup inlineKeyboardMarkup){

        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .messageId(message.getMessageId())
                .chatId(message.getChatId())
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCommand(String command, long chatID){
        switch (command){
            case  "/start" -> sendMessage("ЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄЄ АСАЛАМАЛЕКУМ", chatID);
            default -> sendMessage("ты воще сука ПРо ЧЕ?", chatID);
        }
    }

    private void sendMessage(String text, long chatID){
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatID)
                .text(text)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendVideo(String path, long chatID){

        SendVideo sendVideo = SendVideo.builder()
                .chatId(chatID)
                .video(new InputFile(new File(path)))
                .build();

        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendAudio(String path, long chatID){

        SendAudio sendAudio = SendAudio.builder()
                .chatId(chatID)
                .audio(new InputFile(new File(path)))
                .build();

        try {
            execute(sendAudio);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
