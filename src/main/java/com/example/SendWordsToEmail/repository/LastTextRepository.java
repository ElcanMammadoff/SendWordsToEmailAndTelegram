package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.LastText;
import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LastTextRepository extends JpaRepository<LastText,Long> {


     LastText findByEmailAndType(Email email,int type);

     LastText findByTelegramChatIdAndTypeAndStatus(TelegramChatId telegramChatId,int type,int status);
     LastText findByEmailAndTypeAndStatus(Email email,int type,int status);
     LastText findByTelegramChatIdAndType(TelegramChatId telegramChatId,int type);

     List<LastText> findAllByTelegramChatId(TelegramChatId chatId);


}
