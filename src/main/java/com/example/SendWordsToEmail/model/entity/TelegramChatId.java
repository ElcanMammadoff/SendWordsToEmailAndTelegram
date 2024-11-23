package com.example.SendWordsToEmail.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class TelegramChatId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String chatId;

    private int status;

    private Date created=new Date();


}
