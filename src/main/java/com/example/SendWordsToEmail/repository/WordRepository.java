package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.TelegramMessageLog;
import com.example.SendWordsToEmail.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word,Long> {

    List<Word> findAllByStatus(int type);

    List<Word> findAllByStatusAndType(int status,int type);

    List<Word> findAllByType(int type);

    Word findBySerialNumberAndType(int serialNumber,int type);

    @Query("SELECT DISTINCT w.type FROM Word w")
    List<Integer> findDistinctTypes();

    Optional<List<Word>> findAllByTextStartingWith(String word);

}
