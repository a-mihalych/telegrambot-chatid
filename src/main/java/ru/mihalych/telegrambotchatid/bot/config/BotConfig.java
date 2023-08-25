package ru.mihalych.telegrambotchatid.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@Setter
@PropertySource("classpath:config.properties")
public class BotConfig {

    @Value("${bot.token}")
    String token;
    @Value("${bot.name}")
    String botName;
    @Value("${bot.username}")
    String botUsername;
    @Value("${bot.chatId}")
    String chatId;
}
