package com.example.SendWordsToEmail.repository;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {

     Otp findByOtpCode(String otpCode);

     List<Otp> findByEmailIdAndIsActive(Email emailId,int isActive);




}
