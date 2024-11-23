package com.example.SendWordsToEmail.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class LastText {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "email_id",referencedColumnName = "id")
    private Email email;

    @ManyToOne
    @JoinColumn(name="telegramChatId_id",referencedColumnName = "id")
    private TelegramChatId telegramChatId;

    private int lastSerialNumber;

    private int type;
    private int status;




}
