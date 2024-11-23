package com.example.SendWordsToEmail.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;

    private int status;

    private int confirmStatus;

    private Date created=new Date();


}
