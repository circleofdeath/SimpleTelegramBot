package dae.telegrambothomework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.vavr.control.Either;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.math.BigDecimal;

// RUN WITH JAVA 16+
@SuppressWarnings("ALL") // send message is deprecated
public class Bot extends TelegramLongPollingBot {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    public static final ObjectReader objectReader = objectMapper.reader();
    public static MsgInput msgInput = MsgInput.DEFAULT;

    public static Either<Exception, BigDecimal> parse(String string) {
        if(string == null) return Either.left(new NullPointerException());
        try {
            return Either.right(new BigDecimal(string));
        } catch(NumberFormatException exception) {
            return Either.left(exception);
        }
    }

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
            msgInput.process(messageText, message);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "abcpofdjdspofjd";
    }

    @Override
    public String getBotToken() {
        return "7892407730:AAEMrCebaasElEFuSHQ5aTinaB65sBFcO8c";
    }
}