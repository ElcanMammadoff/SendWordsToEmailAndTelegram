package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email,String> {

    Email findByEmail(String email);

    List<Email> findByStatusAndConfirmStatus(int status,int confirmStatus);



}
