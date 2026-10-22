package com.zakhar.videoloader.bot.buttons;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
@Component
public class KeyboardButtons {

    public InlineKeyboardMarkup chooseMediaType(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtons = List.of(
                InlineKeyboardButton.builder()
                        .text("Video")
                        .callbackData("video")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Audio")
                        .callbackData("audio")
                        .build()
        );

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));

        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup waitState(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtons = List.of(
                InlineKeyboardButton.builder()
                        .text("Очікуй тварюка")
                        .callbackData("wait")
                        .build()
        );

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));

        return inlineKeyboardMarkup;
    }

}
