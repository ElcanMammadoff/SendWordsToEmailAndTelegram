////package com.example.SendWordsToEmail.controller;
//
//import com.example.SendWordsToEmail.model.entity.Email;
//import com.example.SendWordsToEmail.model.entity.LastText;
//import com.example.SendWordsToEmail.model.entity.Otp;
//import com.example.SendWordsToEmail.model.entity.Word;
//import com.example.SendWordsToEmail.repository.EmailRepository;
//import com.example.SendWordsToEmail.repository.LastTextRepository;
//import com.example.SendWordsToEmail.repository.OtpRepository;
//import com.example.SendWordsToEmail.repository.WordRepository;
//import com.example.SendWordsToEmail.service.impl.EmailSender;
//import com.example.SendWordsToEmail.service.inter.EmailServiceInter;
//import com.example.SendWordsToEmail.service.inter.LastTextServiceInter;
//import com.example.SendWordsToEmail.service.inter.OtpServiceInter;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import javax.mail.MessagingException;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.io.UnsupportedEncodingException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/")
//@RequiredArgsConstructor
//public class EmailController {
//
//    private final EmailServiceInter emailServiceInter;
//    private final EmailSender emailSender;
//    private final OtpServiceInter otpServiceInter;
//    private final EmailRepository emailRepository;
//    private final OtpRepository otpRepository;
//    private final WordRepository wordRepository;
//
//    private final LastTextRepository lastTextRepository;
//    private final LastTextServiceInter lastTextServiceInter;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//    private Map<Long, String> otpCodeCache = new HashMap<>();
//
//    @GetMapping("/subscribeWithoutVerification/{email}")
//    public String subscribeWithoutVerification(@PathVariable("email") String email) {
//
//        Email email1 = emailRepository.findByEmail(email);
//
//        Boolean check = emailServiceInter.checkIfEmailIsExists(email);
//        if (check && email1.getConfirmStatus() == 1) {
//            return "this mail has already been subscribed";
//        } else if (check && email1.getConfirmStatus() == 0) {
//            List<Otp> otpCode = otpRepository.findByEmailIdAndIsActive(email1, 1);
//            for (Otp otp1 : otpCode) {
//                otp1.setIsActive(0);
//                otpRepository.save(otp1);
//            }
//            return createAndSendOtpCode(email);
//        } else {
//            emailServiceInter.saveUser(email);
//
//            return createAndSendOtpCode(email);
//        }
//    }
//
//
//    @Operation(summary = "this method creates a OtpCode and sends to given email")
//    private String createAndSendOtpCode(String email) {
//        String otpCode = emailServiceInter.createOtpCode();
//
//        String verificationUrl = "http://localhost:8083/verifyOTP/" + otpCode;
//
//        String subject = "Email Confirmation ";
//        String emailMessage = "Click the link to confirm email\n\n" + verificationUrl;
//
//        try {
//            emailSender.sendEmail(email, subject, emailMessage);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        Long id = emailRepository.findByEmail(email).getId();
//        otpCodeCache.put(id, otpCode);
//
//        otpServiceInter.saveOtp(otpCode, email);
//
//        return "Verification email was sent! Please check your email";
//    }
//
//    @Operation(summary = "this method verifies otpCode and changes confirmStatus of email to 1")
//    @GetMapping("/verifyOTP/{otpCode}")
////    @Transactional
//    public String verifyOTP(@PathVariable("otpCode") String otpCode) {
//        Long userId = null;
//
//        for (Map.Entry<Long, String> entry : otpCodeCache.entrySet()) {
//            if (entry.getValue().equals(otpCode)) {
//                userId = entry.getKey();
//                break;
//            }
//        }
//
//        if (userId == null) {
//            return "Wrong OTP Code";
//        }
//
//        Otp otp = otpServiceInter.findOtpByCode(otpCode);
//        if (otp == null) {
//            return "This OTP Code is not present in the database";
//        }
//
//        // Check if OTP has expired (for example, expired after 10 minutes)
//        if (otp.getCreatedAt().plusMinutes(120).isBefore(LocalDateTime.now())) {
//            otp.setIsActive(0);
//            otpRepository.save(otp);
//            return "OTP has expired. Please request a new one.";
//        }
//
//        if (otp.getOtpCode().equals(otpCode)) {
//            Email email = otp.getEmailId();
//            email.setConfirmStatus(1);
//            email.setStatus(1);
//            emailRepository.save(email);
//            otp.setIsActive(0);
//            otpRepository.save(otp);
//
//
//            otpCodeCache.remove(userId);
//
//
//            return "Your Email was successfully confirmed";
//        } else {
//            return "Wrong OTP Code";
//        }
//    }
//
//    @GetMapping("/sendWordToEmail")
//    @Operation(summary = "Bu metod, istifadəçilərə sözləri və ya ifadələri e-poçt vasitəsilə göndərir")
//    @Scheduled(fixedRate = 30 * 1000) // Hər 5 deqiqeden bir işə düşəcək
//    @Async // Asinxron olaraq işləyəcək
//    @Transactional // Verilənlər bazası əməliyyatları tərtib olunacaq
//    public void sendWordToEmail() {
//        List<Email> emailList = emailRepository.findByStatusAndConfirmStatus(1, 1);
//
//        for (Email email : emailList) {
//
//            List<Integer> types = wordRepository.findDistinctTypes();
//            for (Integer type : types) {
//                String textMessage = "";
//                int finalSerialNumber = wordRepository.findAllByType(type).size();
//                LastText lastText = lastTextRepository.findByEmailAndType(email, type);
//
//                if (lastText == null) {
//                    lastText = new LastText();
//                    lastText.setEmail(email);
//                    lastText.setType(type);
//                    lastText.setLastSerialNumber(0);
//                    lastText.setStatus(1);
//                    lastTextRepository.save(lastText);
//                }
//                lastText = lastTextRepository.findByEmailAndTypeAndStatus(email, type, 1);
//
//                if (lastText == null) {
//                    continue;
//                }
//
//
//                int serialNumber = lastText.getLastSerialNumber(); //sozler ucun en son gonderdiyi sozun sira nomresini goturur
//                if (serialNumber + 5 > finalSerialNumber) {
//                    System.out.println("bazada  qutardi");
//                    try {
//                        emailSender.sendEmail(email.getEmail(), "Qutardi", "siz  materiali oyrenib qutardiniz.");
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    lastText.setStatus(2);
//                    lastTextRepository.save(lastText);
//                    emailRepository.save(email);
//                    continue;
//                }//eger 5den az soz qalibsa serial numberi 0 eleyecek
//                for (int i = serialNumber + 1; i <= serialNumber + 5; i++) {
//                    Word word = wordRepository.findBySerialNumberAndType(i, type);
//                    textMessage += word.getText() + "---" + word.getDefination() + "\n\n";
//                }
//
//                String dayOfWeek = LocalDateTime.now().getDayOfWeek().toString();
//
//                String subject = "";
//                if (type == 0) {
//                    subject = "Bugünkü Sözlər - " + dayOfWeek + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                } else if (type == 1) {
//                    subject = "Bugünkü idiomlar - " + dayOfWeek + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                } //burada subjecti duzgun yazmaq ucun bir nece case verirem.soz gonderecekse yazsin soz gonderirem.idiom gonderirse yazsin idiom gonderirem ve.s
//
//                try {
//                    emailSender.sendEmail(email.getEmail(), subject, textMessage + "\n\n" + "http://localhost:8083/unsubscribe/" + email.getEmail());
////                    emailSender.sendEmail(email.getEmail(), subject, textMessage+"\n\n"+"http://localhost:8083/unsubscribe/"+email.getEmail());  //mesajin icinde unsubscribe link gondermek istesen bu mesaji ac
//                    System.out.println("gonderildi");
//                } catch (MessagingException | UnsupportedEncodingException e) {
//                    e.printStackTrace(); // Əgər e-poçt göndərilmirsə, səhv məlumatını çap edirik
//                }
//
//                lastText.setLastSerialNumber(serialNumber + 5);
//                lastTextRepository.saveAndFlush(lastText);
//                entityManager.detach(lastText);
//
//
//            }
//
//            lastTextServiceInter.checkIfAllOfMaterialsFinishedEmail(types, email, lastTextRepository, emailRepository);
//
//        }
//    }
//
//    @GetMapping("/unsubscribe/{email}")
//    public void unsubscribe(@PathVariable("email") String email) {
//
//        Email email1 = emailRepository.findByEmail(email);
//        email1.setStatus(0);
//        email1.setConfirmStatus(0);
//        emailRepository.save(email1);
//        try {
//            emailSender.sendEmail(email1.getEmail(), "Unsubscribe", "Siz unsubscribe olundunuz");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
//
//
//




package com.example.SendWordsToEmail.controller;

import com.example.SendWordsToEmail.model.entity.Email;
import com.example.SendWordsToEmail.model.entity.Otp;
import com.example.SendWordsToEmail.repository.EmailRepository;
import com.example.SendWordsToEmail.repository.OtpRepository;
import com.example.SendWordsToEmail.service.impl.EmailSender;
import com.example.SendWordsToEmail.service.inter.EmailServiceInter;
import com.example.SendWordsToEmail.service.inter.OtpServiceInter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class EmailController {

    private final EmailServiceInter emailServiceInter;
    private final EmailSender emailSender;
    private final OtpServiceInter otpServiceInter;
    private final EmailRepository emailRepository;
    private final OtpRepository otpRepository;

    private Map<Long, String> otpCodeCache = new HashMap<>();

    @GetMapping("/subscribeWithoutVerification/{email}")
    public String subscribeWithoutVerification(@PathVariable("email") String email) {
        Email email1 = emailRepository.findByEmail(email);

        Boolean check = emailServiceInter.checkIfEmailIsExists(email);

        if (check && email1.getConfirmStatus() == 1) {
            return "this mail has already been subscribed";
        } else if (check && email1.getConfirmStatus() == 0) {
            otpServiceInter.deactivatePreviousOtpCodes(email1);
            return otpServiceInter.createAndSendOtpCode(email, otpCodeCache);
        } else {
            emailServiceInter.saveUser(email);

            return otpServiceInter.createAndSendOtpCode(email,otpCodeCache);
        }
    }

    @Operation(summary = "this method verifies otpCode and changes confirmStatus of email to 1")
    @GetMapping("/verifyOTP/{otpCode}")
    public String verifyOTP(@PathVariable("otpCode") String otpCode) {
        Long userId = otpServiceInter.getUserIdByOtpCode(otpCode,otpCodeCache);

        if (userId == null) {
            return "Wrong OTP Code";
        }

        Otp otp = otpServiceInter.findOtpByCode(otpCode);
        if (otp == null) {
            return "This OTP Code is not present in the database";
        }

        if (otpServiceInter.isOtpExpired(otp)) {
            otp.setIsActive(0);
            otpRepository.save(otp);
            return "OTP has expired. Please request a new one.";
        }

        if (otp.getOtpCode().equals(otpCode)) {
            emailServiceInter.confirmEmail(otp,otpCodeCache);
            return "Your Email was successfully confirmed";
        } else {
            return "Wrong OTP Code";
        }
    }

    @GetMapping("/sendWordToEmail")
    @Operation(summary = "Bu metod, istifadəçilərə sözləri və ya ifadələri e-poçt vasitəsilə göndərir")
    @Scheduled(fixedRate = 30 * 1000)
    @Async
    @Transactional
    public void sendWordToEmail() {
        List<Email> emailList = emailRepository.findByStatusAndConfirmStatus(1, 1);

        for (Email email : emailList) {
            emailServiceInter.sendWordsToUserByEmail(email);
        }
    }

    @GetMapping("/unsubscribe/{email}")
    public void unsubscribe(@PathVariable("email") String email) {
        Email emailToUnsubscribe = emailRepository.findByEmail(email);
        emailToUnsubscribe.setStatus(0);
        emailToUnsubscribe.setConfirmStatus(0);
        emailRepository.save(emailToUnsubscribe);
        sendUnsubscribeConfirmation(emailToUnsubscribe);
    }

    private void sendUnsubscribeConfirmation(Email email) {
        try {
            emailSender.sendEmail(email.getEmail(), "Unsubscribe", "Siz unsubscribe olundunuz");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
