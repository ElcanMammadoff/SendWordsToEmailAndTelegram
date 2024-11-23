package com.example.SendWordsToEmail.service.impl;

import com.example.SendWordsToEmail.model.entity.LastText;
import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import com.example.SendWordsToEmail.model.entity.Word;
import com.example.SendWordsToEmail.repository.LastTextRepository;
import com.example.SendWordsToEmail.repository.TelegramChatIdRepository;
import com.example.SendWordsToEmail.repository.WordRepository;
import com.example.SendWordsToEmail.service.TelegramBot;
import com.example.SendWordsToEmail.service.inter.LastTextServiceInter;
import com.example.SendWordsToEmail.service.inter.TelegramChatIdServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramChatIdServiceImpl implements TelegramChatIdServiceInter {

    private final WordRepository wordRepository;
    private final LastTextServiceInter lastTextServiceInter;
    private final TelegramBot telegramBot;
    private final TelegramChatIdRepository telegramChatIdRepository;
    private final  LastTextRepository lastTextRepository;

    @Override
    public void sendWordsToUser(TelegramChatId telegramChatId) {
        List<Integer> types = wordRepository.findDistinctTypes();
        for (Integer type : types) {
            processWordsForType(telegramChatId, type);
        }
    }

    @Override
    public void processWordsForType(TelegramChatId telegramChatId, Integer type) {
        int finalSerialNumber = wordRepository.findAllByType(type).size();
        LastText lastText = lastTextServiceInter.getOrCreateLastText(telegramChatId, type);
            if (type == 0) {
                if (lastText == null) {
                    return; // No last text entry found, skip this type.
                }
                int serialNumber = lastText.getLastSerialNumber(); // En son gönderilen kelimenin sırasını alır.

                if (serialNumber + 5 > finalSerialNumber) {
                    notifyUserAboutCompletion(telegramChatId, type);
                    setLastTextAsCompleted(lastText, telegramChatId);
                    return; // No more words to send.
                }

                // Send next 5 words
                String textMessage = buildTextMessageForNextWords(serialNumber, type);
                sendMessageToTelegram(telegramChatId, textMessage);
                lastTextServiceInter.updateLastText(lastText, serialNumber);
            } else if (type == 1) {
                if (lastText == null) {
                    return; // No last text entry found, skip this type.
                }

                int serialNumber = lastText.getLastSerialNumber(); // En son gönderilen kelimenin sırasını alır.

                if (serialNumber + 1 > finalSerialNumber) {
                    notifyUserAboutCompletion(telegramChatId, type);
                    setLastTextAsCompleted(lastText, telegramChatId);
                    return; // No more words to send.
                }

                // Send next 1 Idiom
                String textMessage = buildTextMessageForNextWordsIdiom(serialNumber, type);
                sendMessageToTelegram(telegramChatId, textMessage);
                lastTextServiceInter.updateLastText(lastText, serialNumber);
            }
        }

    @Override
    public void notifyUserAboutCompletion(TelegramChatId telegramChatId,int type) {
        try {
            if(type==0){
                telegramBot.sendMessage(telegramChatId.getChatId(), "Siz  sozleri oyrenib qutardiniz.");
            }else if(type==1){
                telegramBot.sendMessage(telegramChatId.getChatId(), "Siz  idiomlari oyrenib qutardiniz.");
            }else {
                telegramBot.sendMessage(telegramChatId.getChatId(), "Siz artıq bütün proqramı öyrənib bitirmisiniz.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLastTextAsCompleted(LastText lastText, TelegramChatId telegramChatId) {
        lastText.setStatus(2);
        lastTextRepository.save(lastText);
        telegramChatIdRepository.save(telegramChatId);
    }

    @Override
    public String buildTextMessageForNextWords(int serialNumber, Integer type) {
        StringBuilder textMessage = new StringBuilder();

        for (int i = serialNumber + 1; i <= serialNumber + 5; i++) {
            Word word = wordRepository.findBySerialNumberAndType(i, type);
            textMessage.append(word.getText()).append("---").append(word.getDefination()).append("\n\n");
        }

        return textMessage.toString();
    }

    @Override
    public String buildTextMessageForNextWordsIdiom(int serialNumber, Integer type) {
        StringBuilder textMessage = new StringBuilder();

        for (int i = serialNumber + 1; i <= serialNumber +1; i++) {
            Word word = wordRepository.findBySerialNumberAndType(i, type);
            textMessage.append(word.getText()).append("---").append(word.getDefination()).append("\n\n");
        }

        return textMessage.toString();
    }



    @Override
    public void sendMessageToTelegram(TelegramChatId telegramChatId, String textMessage) {
        try {
            telegramBot.sendMessage(telegramChatId.getChatId(), textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}