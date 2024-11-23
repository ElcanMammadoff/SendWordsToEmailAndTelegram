package com.example.SendWordsToEmail.service.impl;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.UsedText;
import com.example.SendWordsToEmail.model.entity.Word;
import com.example.SendWordsToEmail.repository.UsedTextRepository;
import com.example.SendWordsToEmail.service.inter.UsedTextServiceInter;
import org.springframework.stereotype.Service;

@Service
public class UsedTextServiceImpl implements UsedTextServiceInter {

    private final UsedTextRepository usedTextRepository;

    public UsedTextServiceImpl(UsedTextRepository usedTextRepository) {
        this.usedTextRepository = usedTextRepository;

    }

    @Override
    public void saveUsedWord(Email email, Word text) {
        UsedText usedText=new UsedText();
        usedText.setTextId(text);
        usedText.setEmailId(email);
        usedTextRepository.save(usedText);
    }
}
