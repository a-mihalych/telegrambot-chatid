package ru.mihalych.telegrambotchatid.bot.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mihalych.telegrambotchatid.bot.components.BotCommands;
import ru.mihalych.telegrambotchatid.bot.config.BotConfig;

@Service
@Slf4j
public class TelegramBotImpl extends TelegramLongPollingBot implements BotCommands {

    private final BotConfig botConfig;

    public TelegramBotImpl(BotConfig botConfig) {
        this.botConfig = botConfig;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Косяк в 'class TelegramBotImpl', конструктор: {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private int getChatId() {
        return Integer.parseInt(botConfig.getChatId());
    }

    private String getBotName() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if ((update.hasMessage()) && (update.getMessage().hasText())) {
            long chatId = update.getMessage().getChatId();
            String txtCommand = update.getMessage().getText();
            String group = update.getMessage().getChat().getTitle();
            switch (txtCommand) {
                case START:
                case "/start@MihalychTestBot":
                    sendMessage(chatId, "Бот готов к работе!", group != null);
                    break;
                case HELP:
                case "/help@MihalychTestBot":
                    sendMessage(chatId,
                                String.format("Команды для взаимодействия с ботом:\n\n%s - старт бота\n" +
                                              "%s - помощь\n%s - получить chatId этой группы", START, HELP, CHAT),
                          group != null);
                    break;
                case CHAT:
                case "/chat@MihalychTestBot":
                    sendMessage(chatId, "chatId получен!", group != null);
                    if (group != null) {
                        sendMessage(getChatId(), String.format("Группа: %s, chatId: %d", group, chatId), false);
                    } else {
                        String name1 = update.getMessage().getFrom().getFirstName();
                        String name2 = update.getMessage().getFrom().getLastName();
                        String name;
                        if (StringUtils.isEmpty(name1)) {
                            if (StringUtils.isEmpty(name2)) {
                                name = "";
                            } else {
                                name = name2;
                            }
                        } else {
                            name = name1;
                            if (!StringUtils.isEmpty(name2)) {
                                name += (" " + name2);
                            }
                        }
                        sendMessage(getChatId(), String.format("Имя: %s, chatId: %d", name, chatId), false);
                    }
                    break;
                default:
                    sendMessage(chatId,
                                String.format("Бот не может обработать сообщение: '%s'", txtCommand),
                          group != null);
            }
        }
    }

    private void sendMessage(long chatId, String txt, boolean group) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (group) {
            sendMessage.setText(txt);
        } else {
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setText(String.format("<code>%s</code>\n%s", getBotName(), txt));
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Косяк в 'class TelegramBotImpl', метод 'sendMessage': {}", e.getMessage());
        }
    }
}
