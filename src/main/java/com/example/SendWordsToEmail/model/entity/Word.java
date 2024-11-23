package com.example.SendWordsToEmail.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

   private String text;

   private int type;

   private int status;

   private int serialNumber;

   private String defination;

}
