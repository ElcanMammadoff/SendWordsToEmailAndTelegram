package com.example.SendWordsToEmail.service.inter;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.Otp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EmailServiceInter {

    void saveUser(String email);

    Boolean checkIfEmailIsExists(String email);

    String createOtpCode();

    boolean checkIfEmailAlreadySubscribed(Email email);

    void confirmEmail(Otp otp, Map<Long, String> otpCodeCache);

    void sendWordsToUserByEmail(Email email);

    void sendWordsForTypeToUser(Email email, Integer type, List<Integer> types);

    void notifyUserThatContentIsFinished(Email email,int type);


     void sendEmailToUser(Email email, Integer type, String textMessage);

     String getSubjectForEmail(Integer type, String dayOfWeek);

     boolean checkIfAllMaterialIsFinshedAndChangeStatusTo2(Email email);
}


