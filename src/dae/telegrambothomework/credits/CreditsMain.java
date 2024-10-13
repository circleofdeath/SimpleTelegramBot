package dae.telegrambothomework.credits;

import dae.telegrambothomework.Bot;
import dae.telegrambothomework.MsgInput;
import dae.telegrambothomework.dto.Bank;
import dae.telegrambothomework.dto.CreditInput;
import dae.telegrambothomework.dto.CreditParser;
import dae.telegrambothomework.dto.Registerer;

import io.vavr.control.Either;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CreditsMain {
    private static final String ERROR_NUMBER_FORMAT = "Sorry, need provide a number";
    public static final Map<String, CreditParser> data;
    public static final Map<String, Bank> bankData;

    static {
        data = readMap(CreditParser.class, "credits").getOrElseThrow(e -> new RuntimeException(e));
        bankData = readMap(Bank.class, "banks").getOrElseThrow(e -> new RuntimeException(e));
    }

    public static<T extends Registerer> Either<Exception, T> read(Class<T> type, Path path) {
        try {
            var content = new String(Files.readAllBytes(path));
            var json = Bot.objectReader.readValue(content, type);
            json.setFileContent(content);
            return Either.right(json);
        } catch(Exception e) {
            return Either.left(e);
        }
    }

    public static<T extends Registerer> Either<Exception, Map<String, T>> readMap(Class<T> type, String directory) {
        if(type == null || directory == null) return Either.left(new NullPointerException());
        var resourceDir = CreditsMain.class.getClassLoader().getResource(directory);
        if(resourceDir == null) return Either.left(new IllegalStateException());
        Map<String, T> output = new HashMap<>();

        try(var list = Files.list(Path.of(resourceDir.toURI()))) {
            list.forEach(path -> {
                read(type, path).peek(json -> {
                    output.put(json.getRegister(), json);
                }).orElseRun(error -> {
                    System.out.println("[WARNING] error loading .json, skipped: " + error.getMessage());
                });
            });
        } catch (IOException | URISyntaxException e) {
            return Either.left(e);
        }

        return Either.right(output);
    }

    public static final MsgInput CAR_CREDIT = new MsgInput() {
        public CreditInput cred_input;
        public int step = 0;

        @Override
        public void process(String input, SendMessage message) {
            switch(step) {
                case 0: {
                    if(data.containsKey(input)) {
                        message.setText("Great! Next, write bank name");
                        cred_input.setParser(data.get(input));
                        step++;
                    } else {
                        String[] keys = data.keySet().toArray(String[]::new);
                        StringBuilder builder = new StringBuilder("Provided mark not found, available marks:\n").append(keys[0]);
                        for(int i = 1; i < keys.length; i++) {
                            builder.append(", ").append(keys[i]);
                        }
                        message.setText(builder.toString());
                    }
                    break;
                }

                case 1: {
                    if(bankData.containsKey(input)) {
                        message.setText("Great! Next, write total cost ($)");
                        cred_input.setBank(bankData.get(input));
                        step++;
                    } else {
                        String[] keys = bankData.keySet().toArray(String[]::new);
                        StringBuilder builder = new StringBuilder("Provided bank not found, available banks:\n").append(keys[0]);
                        for(int i = 1; i < keys.length; i++) {
                            builder.append(", ").append(keys[i]);
                        }
                        message.setText(builder.toString());
                    }
                    break;
                }

                case 2: {
                    Bot.parse(input).peek((n) -> {
                        message.setText("Great! Next, write initial payment");
                        cred_input.setTotalCost(n);
                        step++;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }

                case 3: {
                    Bot.parse(input).peek((n) -> {
                        message.setText("Great! Next, write monthly payment");
                        cred_input.setInitialPayment(n);
                        step++;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }

                case 4: {
                    Bot.parse(input).peek((n) -> {
                        cred_input.setMonthlyPayment(n);
                        cred_input.setMessage(message);
                        cred_input.getParser().selfExecute(cred_input);
                        step = 0;
                        Bot.msgInput = DEFAULT;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }
            }
        }
    };
}