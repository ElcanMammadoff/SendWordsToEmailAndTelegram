package com.example.SendWordsToEmail.service;

import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import com.example.SendWordsToEmail.model.entity.TelegramMessageLog;
import com.example.SendWordsToEmail.model.entity.Word;
import com.example.SendWordsToEmail.repository.LastTextRepository;
import com.example.SendWordsToEmail.repository.TelegramChatIdRepository;
import com.example.SendWordsToEmail.repository.TelegramMessageLogRepository;
import com.example.SendWordsToEmail.repository.WordRepository;
import com.example.SendWordsToEmail.service.inter.LastTextServiceInter;
import com.example.SendWordsToEmail.service.inter.TelegramMessageLogServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramChatIdRepository telegramChatIdRepository;
    private final TelegramMessageLogServiceInter telegramMessageLogServiceInter;
    private final LastTextRepository lastTextRepository;
    private final WordRepository wordRepository;
    private final LastTextServiceInter lastTextServiceInter;
    private final TelegramMessageLogRepository telegramMessageLogRepository;

    @Override
    public String getBotUsername() {
    }

    @Override
    public String getBotToken() {

    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            String chatId = message.getChatId().toString();

            List<Integer> types = wordRepository.findDistinctTypes();

            if ("/start".equals(text)) {
                TelegramChatId telegramChatId = telegramChatIdRepository.findByChatId(chatId);
                if (telegramChatId != null) {
                    System.out.println(telegramChatId.getChatId());
                    Boolean finish = lastTextServiceInter.checkIfAllOfMaterialsFinishedTelegram(types, telegramChatId, lastTextRepository, telegramChatIdRepository);
                    if (finish) {
                        sendMessage(chatId, "Siz artıq bütün proqramı öyrənib bitirmisiniz.");
                    } else {

                        if (telegramChatId.getStatus() == 0) {
                            telegramChatId.setStatus(1);
                            telegramChatIdRepository.save(telegramChatId);
                            sendMessage(chatId, "Sizi yenidən xoş gördük, Bu gündən etibarən qaldığımız yerdən davam edəcəyik.");
                        } else {
                            sendMessage(chatId, "Siz hal-hazırda proqramdan istifadə edirsiniz.");
                        }
                    }
                } else {
                    TelegramChatId telegramChatId1 = new TelegramChatId();
                    telegramChatId1.setChatId(chatId);
                    telegramChatId1.setCreated(new Date());
                    telegramChatId1.setStatus(1);
                    telegramChatIdRepository.save(telegramChatId1);
                    sendMessage(chatId, "Xoş gəldiniz!\n\nBu gündən etibarən sizə hər gün 5 söz və 1 idiom göndərəcəyik.\n\nDigər istifadəçilərin bu sözlərə və idiomlara uyğun yazdığı cümlələri görmək üçün aşağıdakı nümunəyə uyğun şəkildə sözü cümlədə işləyədərək bizə göndərə bilərsiniz.\n\nNümunə:\n" +
                            "Car-I have a car");
                    sendMessageToTelegram(chatId);
                }
            } else if ("/stop".equals(text)) {
                sendMessage(chatId, "Siz proqramı dayandırdınız, bu gündən etibarən sizə materiallar göndərilməyəcək.");
                TelegramChatId telegramChatId = telegramChatIdRepository.findByChatId(chatId);
                telegramChatId.setStatus(0);
                telegramChatIdRepository.save(telegramChatId);
            } else {
                foo(chatId, text);
            }
        }
    }


    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        telegramMessageLogServiceInter.saveLog(chatId, message, 2, 0);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("403")) {
                System.out.println("user with chat id: " + chatId + " botu bloklayib");
                TelegramChatId telegramChatId = telegramChatIdRepository.findByChatId(chatId);
                telegramChatId.setStatus(0);
                telegramChatIdRepository.save(telegramChatId);
            } else {
                System.out.println("sendMessage methodunda bir xeta cixdi.");
            }
        }
    }

    public void sendMessageToTelegram(String chatId) {
        List<Integer> types = wordRepository.findDistinctTypes();

        for (int type : types) {
            List<Word> words = wordRepository.findAllByType(type);
            StringBuilder text = new StringBuilder();
            if (type == 0) {
                for (int i = 0; i < 5; i++) {
                    Word word = words.get(i);
                    text.append(word.getText()).append("---").append(word.getDefination()).append("\n\n");
                }
                sendMessage(chatId, text.toString());
                telegramMessageLogServiceInter.saveLog(chatId, text.toString(), 2, 0);

            } else if (type == 1) {
                for (int i = 0; i < 1; i++) {
                    Word word = words.get(i);
                    text.append(word.getText()).append("---").append(word.getDefination()).append("\n\n");
                }
                sendMessage(chatId, text.toString());
                telegramMessageLogServiceInter.saveLog(chatId, text.toString(), 2, 0);
            }
        }

    }

    public void foo(String chatId, String sentence) {

        try {

            if (sentence.contains("-")) {

                String sentenceWithouthyphen = sentence.trim().replaceAll("-+", "-").trim().replaceAll("\\s+", " ");
                int dashIndex = sentenceWithouthyphen.indexOf('-');
                int length = sentence.length();
                String wordOfSentence = sentence.substring(0, dashIndex).trim();
                String word = wordOfSentence;  //burda - var idi
                String extraSentence = sentenceWithouthyphen.substring(dashIndex + 1, length).trim();
                String readySentence = word + "-" + extraSentence;


                Optional<List<Word>> words = wordRepository.findAllByTextStartingWith(wordOfSentence);
                int countOfWordsInSentence = extraSentence.split(" ").length;


                if (!words.isPresent()) {
                    telegramMessageLogServiceInter.saveLog(chatId, readySentence, 1, 3); // Cümlədə uyğun söz yoxdursa
                } else if (countOfWordsInSentence < 3) {
                    telegramMessageLogServiceInter.saveLog(chatId, readySentence, 1, 2);
                    sendMessage(chatId, "Qurduğunuz cümlə ən azı üç sözdən ibarət olmalıdır.");
                } else {
                    // Yalnız düzgün cümlə və uyğun söz varsa
                    telegramMessageLogServiceInter.saveLog(chatId, readySentence, 1, 1);
                    Optional<List<TelegramMessageLog>> sentenceList = telegramMessageLogRepository.findAllByTextStartingWithAndIsSentence(word, 1);
                    if (sentenceList.isPresent() && !sentenceList.get().isEmpty()) {
                        StringBuilder message = new StringBuilder();
                        for (TelegramMessageLog sentence1 : sentenceList.get()) {
                            message.append(sentence1.getText()).append("\n\n");
                        }
                        sendMessage(chatId, message.toString());
                    }
                }
            } else {
                telegramMessageLogServiceInter.saveLog(chatId, sentence, 1, 2); // Cümlə strukturuna uyğun deyilsə
                sendMessage(chatId, "Qurduğunuz cümlə strukturu aşağıdakı nümunədə göstərildiyi kimi olmalıdır. \n" +
                        "\n" +
                        "\n" +
                        "Car-I have a car.");
            }
        } catch (IndexOutOfBoundsException e) {
            sendMessage(chatId, "Qurduğunuz cümlə strukturu aşağıdakı nümunədə göstərildiyi kimi olmalıdır. \n" +
                    "\n" +
                    "\n" +
                    "Car-I have a car.");
        }
    }
}

