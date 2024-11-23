package com.example.SendWordsToEmail.service.impl;

import com.example.SendWordsToEmail.model.entity.TelegramMessageLog;
import com.example.SendWordsToEmail.repository.TelegramMessageLogRepository;
import com.example.SendWordsToEmail.service.inter.TelegramMessageLogServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TelegramMessageLogServiceImpl implements TelegramMessageLogServiceInter {

private final TelegramMessageLogRepository telegramMessageLogRepository;
    @Override
    public void saveLog(String chatId,String text,int status,int isSentence) {
        TelegramMessageLog telegramMessageLog=new TelegramMessageLog();
        telegramMessageLog.setChatId(chatId);
        telegramMessageLog.setText(text);
        telegramMessageLog.setStatus(status);
        telegramMessageLog.setIsSentence(isSentence);
        telegramMessageLog.setDateCreated(new Date());
        telegramMessageLogRepository.save(telegramMessageLog);
    }
}
