package dae.telegrambothomework;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SuppressWarnings("ALL") // send message is deprecated
public class Bot extends TelegramLongPollingBot {
    public static final String BOT_TOKEN = "[{ insert token }]";
    public static final String BOT_NAME = "[{ insert name }]";

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(chatId);

            if(messageText.startsWith("credit ")) {
                String[] args = messageText.substring(7).split(" ");

                try {
                    message.setText(Calculations.calculate(args[0],
                            Double.parseDouble(args[1]),
                            Double.parseDouble(args[2]),
                            Double.parseDouble(args[3])
                    ));
                } catch(NumberFormatException ignored) {
                    message.setText("Insert numbers");
                } catch(ArrayIndexOutOfBoundsException ignored) {
                    message.setText("Say more");
                }
            } else {
                message.setText("What?");
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}