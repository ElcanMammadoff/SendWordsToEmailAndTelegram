package com.example.SendWordsToEmail.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class TelegramMessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String chatId;
    private String text;

    private Date dateCreated;

    private int status; //status 1- user mesaj gonderir. status 2- biz gonderdiyimiz mesaj

    private int isSentence;//1 dirse cumleleri saxlayir .2 dirse sehv cumleleri saxlayir. 3 durse  yazdigi esas  soz sozler arasinda yoxdur.



}
