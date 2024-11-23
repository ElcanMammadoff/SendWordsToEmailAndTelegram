package com.example.SendWordsToEmail.service.inter;

import org.springframework.stereotype.Service;

@Service
public interface TelegramMessageLogServiceInter {

    void saveLog(String chatId,String text,int status,int isSentence);

}
