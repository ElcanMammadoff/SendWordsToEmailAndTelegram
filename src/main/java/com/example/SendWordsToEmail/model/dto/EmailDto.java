package com.example.SendWordsToEmail.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {

    private long id;
    private String email;

    private int confirmStatus;

}
