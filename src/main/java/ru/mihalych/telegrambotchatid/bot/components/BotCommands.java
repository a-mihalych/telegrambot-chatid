package ru.mihalych.telegrambotchatid.bot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {

    String START = "/start";
    String HELP = "/help";
    String CHAT = "/chat";

    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand(START, "старт бота"),
            new BotCommand(HELP, "помощь"),
            new BotCommand(CHAT, "получить chatId")
    );
}
