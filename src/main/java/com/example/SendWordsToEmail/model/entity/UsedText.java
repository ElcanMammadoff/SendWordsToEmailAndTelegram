package com.example.SendWordsToEmail.model.entity;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Getter
@Setter
public class UsedText {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL )
    @JoinColumn(name="email_id",referencedColumnName = "id")
    private Email emailId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="text_id",referencedColumnName = "id")
    private Word textId;
}
