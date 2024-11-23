package com.example.SendWordsToEmail.model.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String otpCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_id", referencedColumnName = "id")
    private Email emailId;

    private LocalDateTime createdAt;

    private int isActive;

}
