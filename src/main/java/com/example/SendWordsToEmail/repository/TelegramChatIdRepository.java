package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramChatIdRepository extends JpaRepository<TelegramChatId,Long> {

    List<TelegramChatId> findAllByStatus(int Status);

    TelegramChatId  findByChatId(String chatId);

}



