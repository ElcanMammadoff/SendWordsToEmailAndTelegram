package com.example.SendWordsToEmail.service.inter;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.Word;
import org.springframework.stereotype.Service;

@Service
public interface UsedTextServiceInter {

    void saveUsedWord(Email email, Word text);

}
