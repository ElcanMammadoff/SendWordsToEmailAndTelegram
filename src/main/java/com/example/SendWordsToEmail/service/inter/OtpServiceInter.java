package com.example.SendWordsToEmail.service.inter;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.Otp;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public interface OtpServiceInter {

    void saveOtp(String otpCode,String email);

    Otp findOtpByCode(String otpCode);

     void deactivatePreviousOtpCodes(Email email);


    String createAndSendOtpCode(String email, Map<Long, String> otpCodeCache);

     Long getUserIdByOtpCode(String otpCode,Map<Long, String> otpCodeCache);
     boolean isOtpExpired(Otp otp);
}
