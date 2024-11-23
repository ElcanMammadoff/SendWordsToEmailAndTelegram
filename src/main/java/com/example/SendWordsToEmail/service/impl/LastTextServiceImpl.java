package com.example.SendWordsToEmail.service.impl;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.LastText;
import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import com.example.SendWordsToEmail.repository.EmailRepository;
import com.example.SendWordsToEmail.repository.LastTextRepository;
import com.example.SendWordsToEmail.repository.TelegramChatIdRepository;
import com.example.SendWordsToEmail.service.inter.LastTextServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LastTextServiceImpl implements LastTextServiceInter {

    private final LastTextRepository lastTextRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Boolean checkIfAllOfMaterialsFinishedTelegram(List<Integer> types, TelegramChatId telegramChatId, LastTextRepository lastTextRepository, TelegramChatIdRepository telegramChatIdRepository) {
        boolean typeChecker = true;
        for (int type2 : types) {
            LastText lastText = lastTextRepository.findByTelegramChatIdAndType(telegramChatId, type2);
            if (lastText == null) {
                typeChecker = false;
            }else if (lastTextRepository.findByTelegramChatIdAndType(telegramChatId, type2).getStatus() != 2) {
                typeChecker = false;
                break;
            }
        }if (typeChecker) {
            telegramChatId.setStatus(2);
            telegramChatIdRepository.save(telegramChatId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkIfAllOfMaterialsFinishedEmail(List<Integer> types, Email email, LastTextRepository lastTextRepository, EmailRepository emailRepository) {
        boolean typeChecker = true;
        for (int type2 : types) {
            if (lastTextRepository.findByEmailAndType(email, type2).getStatus() != 2) {
                typeChecker = false;
                break;
            }
        }
        if (typeChecker) {
            email.setStatus(2);
            emailRepository.save(email);
            return true;
        }
        return false;
    }

    @Override
    public LastText getOrCreateLastTextForUser(Email email, Integer type) {
        LastText lastText = lastTextRepository.findByEmailAndType(email, type);
        if (lastText == null) {
            lastText = new LastText();
            lastText.setEmail(email);
            lastText.setType(type);
            lastText.setLastSerialNumber(0);
            lastText.setStatus(1);
            lastTextRepository.save(lastText);
        }
        return lastTextRepository.findByEmailAndTypeAndStatus(email, type, 1);
    }

    @Override
    public LastText getOrCreateLastText(TelegramChatId telegramChatId, Integer type) {
        LastText lastText = lastTextRepository.findByTelegramChatIdAndType(telegramChatId, type);

        if (lastText == null) {
            lastText = new LastText();
            lastText.setTelegramChatId(telegramChatId);
            lastText.setType(type);
            lastText.setLastSerialNumber(5);
            lastText.setStatus(1);
            lastTextRepository.save(lastText);
        }

        return lastTextRepository.findByTelegramChatIdAndTypeAndStatus(telegramChatId, type, 1);
    }

    @Override
    public void updateLastText(LastText lastText, int serialNumber) {
        lastText.setLastSerialNumber(serialNumber + 5);
        lastTextRepository.saveAndFlush(lastText);
        entityManager.detach(lastText);
    }

}
