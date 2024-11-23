package com.example.SendWordsToEmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SendWordsToEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendWordsToEmailApplication.class, args);
	}

	@Bean
	TelegramBotsApi telegramBotsApi() throws TelegramApiException {
		return new TelegramBotsApi(DefaultBotSession.class);
	}

}
