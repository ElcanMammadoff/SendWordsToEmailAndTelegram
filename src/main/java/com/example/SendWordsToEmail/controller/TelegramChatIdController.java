//package com.example.SendWordsToEmail.controller;
//
//import com.example.SendWordsToEmail.model.entity.LastText;
//import com.example.SendWordsToEmail.model.entity.TelegramChatId;
//import com.example.SendWordsToEmail.model.entity.Word;
//import com.example.SendWordsToEmail.repository.LastTextRepository;
//import com.example.SendWordsToEmail.repository.TelegramChatIdRepository;
//import com.example.SendWordsToEmail.repository.WordRepository;
//import com.example.SendWordsToEmail.service.TelegramBot;
//import com.example.SendWordsToEmail.service.inter.LastTextServiceInter;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class TelegramChatIdController {
//
//   private final TelegramChatIdRepository telegramChatIdRepository;
//   private final WordRepository wordRepository;
//
//   private final LastTextRepository lastTextRepository;
//   private final TelegramBot telegramBot;
//   private final LastTextServiceInter lastTextServiceInter;
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Operation(summary = "Bu metod, istifadəçilərə sözləri və ya ifadələri e-poçt vasitəsilə göndərir")
//    @Scheduled(fixedRate = 30 * 1000) // Hər 5 deqiqeden bir işə düşəcək
//    @Async // Asinxron olaraq işləyəcək
//    @Transactional // Verilənlər bazası əməliyyatları tərtib olunacaq
//    public void sendWordsToChatId(){
//
//        List<TelegramChatId> telegramChatIdList = telegramChatIdRepository.findAllByStatus(1);
//
//        for (TelegramChatId telegramChatId : telegramChatIdList) {
//            List<Integer> types = wordRepository.findDistinctTypes();
//            for (Integer type : types) {
//                String textMessage = "";
//                int finalSerialNumber = wordRepository.findAllByType(type).size();
//                LastText lastText= lastTextRepository.findByTelegramChatIdAndType(telegramChatId, type);
//
//                if (lastText== null) {
//                    lastText=new LastText();
//                    lastText.setTelegramChatId(telegramChatId);
//                    lastText.setType(type);
//                    lastText.setLastSerialNumber(0);
//                    lastText.setStatus(1);
//                    lastTextRepository.save(lastText);
//                }
//
//
//                    lastText = lastTextRepository.findByTelegramChatIdAndTypeAndStatus(telegramChatId, type, 1);
//
//                 if(lastText==null){
//                     continue;
//                 }
//                int serialNumber = lastText.getLastSerialNumber(); //sozler ucun en son gonderdiyi sozun sira nomresini goturu//
//
//                if (serialNumber + 5 > finalSerialNumber) {
//                    System.out.println("bazada  qutardi");
//                    try {
//                        telegramBot.sendMessage(telegramChatId.getChatId(),"siz  materiali oyrenib qutardiniz.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    lastText.setStatus(2);
//                    lastTextRepository.save(lastText);
//                    telegramChatIdRepository.save(telegramChatId);
//                    continue;
//                }
//
//
//                for (int i = serialNumber + 1; i <= serialNumber + 5; i++) {
//                    Word word = wordRepository.findBySerialNumberAndType(i, type);
//                    textMessage += word.getText() + "---"+word.getDefination()+"\n\n";
//                }
////
//                try {
//                    telegramBot.sendMessage(telegramChatId.getChatId(),textMessage);
//                    System.out.println("gonderildi");
//                } catch (Exception e) {
//                    e.printStackTrace(); // Əgər e-poçt göndərilmirsə, səhv məlumatını çap edirik
//                }
//
////            LastText lastText=lastTextRepository.findByEmailAndType(email,0);
//                lastText.setLastSerialNumber(serialNumber + 5);
//                lastTextRepository.saveAndFlush(lastText);
//                entityManager.detach(lastText);
//
//
//                }
//           lastTextServiceInter.checkIfAllOfMaterialsFinishedTelegram(types, telegramChatId, lastTextRepository, telegramChatIdRepository);
//        }
//
//    }
//
//    }
//
//


package com.example.SendWordsToEmail.controller;

import com.example.SendWordsToEmail.model.entity.TelegramChatId;
import com.example.SendWordsToEmail.repository.TelegramChatIdRepository;
import com.example.SendWordsToEmail.service.inter.TelegramChatIdServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class TelegramChatIdController {

    private final TelegramChatIdRepository telegramChatIdRepository;
    private final TelegramChatIdServiceInter telegramChatIdServiceInter;

//    @Operation(summary = "Bu metod, istifadəçilərə sözləri və ya ifadələri Telegram vasitəsilə göndərir")
////    @Scheduled(fixedRate = 24*60*60* 1000) //proqram run olduqda sonra  Hər 1 gunden  bir işə düşəcək
//    @Scheduled(cron = "0 0 16 * * ?") //her gun saat 20:00 da ise dusecek
//    @Async // Asinxron olaraq işləyəcək
//    @Transactional // Verilənlər bazası əməliyyatları tərtib olunacaq
//    public void sendWordsToChatId() {
//        List<TelegramChatId> telegramChatIdList = telegramChatIdRepository.findAllByStatus(1);
//
//        for (TelegramChatId telegramChatId : telegramChatIdList) {
//            telegramChatIdServiceInter.sendWordsToUser(telegramChatId);
//        }
//    }


//    @Operation(summary = "Bu metod, istifadəçilərə sözləri və ya ifadələri Telegram vasitəsilə göndərir")
//    @Scheduled(fixedRate = 60*60* 1000) //proqram run olduqda sonra  Hər 1 gunden  bir işə düşəcək
////    @Scheduled(cron = "0 0 16 * * ?") //her gun saat 20:00 da ise dusecek
//    @Async // Asinxron olaraq işləyəcək
//    @Transactional // Verilənlər bazası əməliyyatları tərtib olunacaq
//    public void sendWordsToChatId() {
//        List<TelegramChatId> telegramChatIdList = telegramChatIdRepository.findAllByStatus(1);
//
//        for (TelegramChatId telegramChatId : telegramChatIdList) {
//            telegramChatIdServiceInter.sendWordsToUser(telegramChatId);
//        }
//    }

    @Scheduled(cron = "0 15 20 * * ?", zone = "Asia/Baku") // 20:15 AZT (Asia/Baku time zone)
    @Async
    @Transactional
    public void sendWordsToChatId() {
        List<TelegramChatId> telegramChatIdList = telegramChatIdRepository.findAllByStatus(1);

        for (TelegramChatId telegramChatId : telegramChatIdList) {
            telegramChatIdServiceInter.sendWordsToUser(telegramChatId);
        }
    }

}