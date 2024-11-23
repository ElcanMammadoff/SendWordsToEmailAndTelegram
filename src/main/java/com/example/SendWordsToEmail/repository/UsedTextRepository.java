package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.UsedText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedTextRepository extends JpaRepository<UsedText,Long> {



}
