package com.example.SendWordsToEmail.service.inter;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.LastText;
import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import com.example.SendWordsToEmail.repository.EmailRepository;
import com.example.SendWordsToEmail.repository.LastTextRepository;
import com.example.SendWordsToEmail.repository.TelegramChatIdRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LastTextServiceInter {

      Boolean checkIfAllOfMaterialsFinishedTelegram(List<Integer> types, TelegramChatId telegramChatId, LastTextRepository lastTextRepository, TelegramChatIdRepository telegramChatIdRepository);
      Boolean checkIfAllOfMaterialsFinishedEmail(List<Integer> types, Email email, LastTextRepository lastTextRepository, EmailRepository emailRepository);

      LastText getOrCreateLastTextForUser(Email email, Integer type);

       LastText getOrCreateLastText(TelegramChatId telegramChatId, Integer type);

     void updateLastTextWord(LastText lastText, int serialNumber);
     void updateLastTextIdiom(LastText lastText, int serialNumber);
}
