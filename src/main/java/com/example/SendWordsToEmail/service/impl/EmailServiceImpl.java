package com.example.SendWordsToEmail.service.impl;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.LastText;
import com.example.SendWordsToEmail.model.entity.Otp;
import com.example.SendWordsToEmail.model.entity.Word;
import com.example.SendWordsToEmail.repository.EmailRepository;
import com.example.SendWordsToEmail.repository.LastTextRepository;
import com.example.SendWordsToEmail.repository.OtpRepository;
import com.example.SendWordsToEmail.repository.WordRepository;
import com.example.SendWordsToEmail.service.inter.EmailServiceInter;
import com.example.SendWordsToEmail.service.inter.LastTextServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailServiceInter {


    private final EmailRepository emailRepository;
    private final OtpRepository otpRepository;

    private final WordRepository wordRepository;
    private final LastTextRepository lastTextRepository;
    private final LastTextServiceInter lastTextServiceInter;
    private final EmailSender emailSender;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void saveUser(String email) {
        Email email1 = new Email();
        email1.setEmail(email);
        email1.setCreated(new Date());
        email1.setStatus(1);
        emailRepository.save(email1);
    }

    @Override
    public Boolean checkIfEmailIsExists(String email) {

        List<Email> list = emailRepository.findAll();

        for (Email user : list) {
            if (user.getEmail().equals(email)) {
                return true;

            }
        }
        return false;
    }

    @Override
    public String createOtpCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(999999));
    }

    @Override
    public boolean checkIfEmailAlreadySubscribed(Email email) {
        return checkIfEmailIsExists(email.getEmail()) && email.getConfirmStatus() == 1;
    }

    @Override
    public void confirmEmail(Otp otp, Map<Long, String> otpCodeCache) {
        Email email = otp.getEmailId();
        email.setConfirmStatus(1);
        email.setStatus(1);
        emailRepository.save(email);
        otp.setIsActive(0);
        otpRepository.save(otp);
        otpCodeCache.remove(otp.getEmailId().getId());
    }

    @Override
    public void sendWordsToUserByEmail(Email email) {
        List<Integer> types = wordRepository.findDistinctTypes();
        for (Integer type : types) {
            sendWordsForTypeToUser(email, type,types);
        }
    }

    @Override
    public void sendWordsForTypeToUser(Email email, Integer type,List<Integer> types) {
        StringBuilder textMessage = new StringBuilder();
        int finalSerialNumber = wordRepository.findAllByType(type).size();
        LastText lastText = lastTextServiceInter.getOrCreateLastTextForUser(email, type);

        if (lastText == null) {
            return;
        }

        int serialNumber = lastText.getLastSerialNumber();
        if (serialNumber + 5 > finalSerialNumber) {
            notifyUserThatContentIsFinished(email,type);
            lastText.setStatus(2);
            lastTextRepository.save(lastText);
            emailRepository.save(email);
            boolean check = checkIfAllMaterialIsFinshedAndChangeStatusTo2(email);
            if (check) {
                try {
                    emailSender.sendEmail(email.getEmail(), "Qutardi", "siz butun materiali oyrenib qutardiniz");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return;
        }

        for (int i = serialNumber + 1; i <= serialNumber + 5; i++) {
            Word word = wordRepository.findBySerialNumberAndType(i, type);
            textMessage.append(word.getText()).append("---").append(word.getDefination()).append("\n\n");
        }

        sendEmailToUser(email, type, textMessage.toString());
        lastText.setLastSerialNumber(serialNumber + 5);
        lastTextRepository.saveAndFlush(lastText);
        entityManager.detach(lastText);
    }

    @Override
    public void notifyUserThatContentIsFinished(Email email,int type) {
        try {
            if (type == 0) {
                emailSender.sendEmail(email.getEmail(), "Qutardi", "siz sozleri oyrenib qutardiniz");
            } else if (type == 1) {
                emailSender.sendEmail(email.getEmail(), "Qutardi", "siz idiomlari oyrenib qutardiniz");
            } else {
                emailSender.sendEmail(email.getEmail(), "Qutardi", "siz materiali oyrenib qutardiniz");
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailToUser(Email email, Integer type, String textMessage) {
        String dayOfWeek = LocalDateTime.now().getDayOfWeek().toString();
        String subject = getSubjectForEmail(type, dayOfWeek);

        try {
            emailSender.sendEmail(email.getEmail(), subject, textMessage);
//            emailSender.sendEmail(email.getEmail(), subject, textMessage + "\n\n" + "http://localhost:8083/unsubscribe/" + email.getEmail()); //emailin icinde unsubscribe linki gondermek istesen bu linki ac
            System.out.println("gonderildi");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSubjectForEmail(Integer type, String dayOfWeek) {
        String subject = "";
        if (type == 0) {
            subject = "Bugünkü Sözlər - " + dayOfWeek + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (type == 1) {
            subject = "Bugünkü idiomlar - " + dayOfWeek + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return subject;
    }

    @Override
    public boolean checkIfAllMaterialIsFinshedAndChangeStatusTo2(Email email){

        List<Integer> types = wordRepository.findDistinctTypes();
        Boolean check=true;
        for(int type:types){
            if(lastTextRepository.findByEmailAndTypeAndStatus(email,type,1)==null){
                check=true;
            }else{
                check=false;
                break;
            }
        }
        if(check){
           email.setStatus(2);
           emailRepository.save(email);
           return true;
        }else{
            return false;
        }
    }












}
