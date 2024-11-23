package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.TelegramMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramMessageLogRepository extends JpaRepository<TelegramMessageLog,Long> {

    List<TelegramMessageLog> findAllByStatus(int status);

    Optional<List<TelegramMessageLog>> findAllByTextStartingWithAndIsSentence(String word, int isSentence);

}
