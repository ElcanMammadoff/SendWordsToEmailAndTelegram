package com.example.SendWordsToEmail.controller;

import com.example.SendWordsToEmail.model.entity.Word;
import com.example.SendWordsToEmail.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController {

    private final WordRepository wordRepository;

    @GetMapping("/giveserialNumber")
    public String giveSerialNumberToText(){
        List<Word> wordList=wordRepository.findAllByType(0);
        List<Word> idiomList=wordRepository.findAllByType(1);
        int wordSerialNumber=1;
        int idiomSerialNumber=1;
        for(Word word:wordList){
            word.setSerialNumber(wordSerialNumber);
            wordRepository.save(word);
            wordSerialNumber+=1;
        }
        for(Word idiom:idiomList){
            idiom.setSerialNumber(idiomSerialNumber);
            wordRepository.save(idiom);
            idiomSerialNumber+=1;
        }
        return "siraNomresi verdim";
    }

}
