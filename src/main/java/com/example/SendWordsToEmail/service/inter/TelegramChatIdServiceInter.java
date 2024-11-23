package com.example.SendWordsToEmail.service.inter;

import com.example.SendWordsToEmail.model.entity.LastText;
import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import org.springframework.stereotype.Service;


@Service
public interface TelegramChatIdServiceInter {

     void sendWordsToUser(TelegramChatId telegramChatId);

     void processWordsForType(TelegramChatId telegramChatId, Integer type);

     void notifyUserAboutCompletion(TelegramChatId telegramChatId,int type);

      void setLastTextAsCompleted(LastText lastText, TelegramChatId telegramChatId);

      String buildTextMessageForNextWords(int serialNumber, Integer type);
    String buildTextMessageForNextWordsIdiom(int serialNumber, Integer type);

      void sendMessageToTelegram(TelegramChatId telegramChatId, String textMessage);
}
