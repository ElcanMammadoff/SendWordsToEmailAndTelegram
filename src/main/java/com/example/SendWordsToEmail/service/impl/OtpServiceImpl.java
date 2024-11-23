package com.example.SendWordsToEmail.service.impl;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.Otp;
import com.example.SendWordsToEmail.repository.EmailRepository;
import com.example.SendWordsToEmail.repository.OtpRepository;
import com.example.SendWordsToEmail.service.inter.EmailServiceInter;
import com.example.SendWordsToEmail.service.inter.OtpServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpServiceInter {

    private final OtpRepository otpRepository;
    private final EmailRepository emailRepository;
    private final EmailServiceInter emailServiceInter;

    private final EmailSender emailSender;


    @Override
    public void saveOtp(String otpCode,String email) {

        Otp otp=new Otp();

       otp.setOtpCode(otpCode);
       otp.setEmailId(emailRepository.findByEmail(email));
       otp.setCreatedAt(LocalDateTime.now());
       otp.setIsActive(1);

         otpRepository.save(otp);

    }
    @Override
    public Otp findOtpByCode(String otpCode) {
        return otpRepository.findByOtpCode(otpCode);
    }

    @Override
    public void deactivatePreviousOtpCodes(Email email) {
        List<Otp> otpCodes = otpRepository.findByEmailIdAndIsActive(email, 1);
        for (Otp otp : otpCodes) {
            otp.setIsActive(0);
            otpRepository.save(otp);
        }
    }

    @Override
    public String createAndSendOtpCode(String email, Map<Long, String> otpCodeCache) {
        String otpCode = emailServiceInter.createOtpCode();
        String verificationUrl = "http://localhost:8083/verifyOTP/" + otpCode;
        String subject = "Email Confirmation ";
        String emailMessage = "Click the link to confirm email\n\n" + verificationUrl;

        try {
            emailSender.sendEmail(email, subject, emailMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Long id = emailRepository.findByEmail(email).getId();
        otpCodeCache.put(id, otpCode);
        saveOtp(otpCode, email);

        return "Verification email was sent! Please check your email";
    }

    @Override
    public Long getUserIdByOtpCode(String otpCode, Map<Long, String> otpCodeCache) {
        for (Map.Entry<Long, String> entry : otpCodeCache.entrySet()) {
            if (entry.getValue().equals(otpCode)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public boolean isOtpExpired(Otp otp) {
        return otp.getCreatedAt().plusMinutes(120).isBefore(LocalDateTime.now());
    }

}
