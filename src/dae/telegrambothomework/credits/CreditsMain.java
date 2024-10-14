package dae.telegrambothomework.credits;

import dae.telegrambothomework.Bot;
import dae.telegrambothomework.MsgInput;
import dae.telegrambothomework.dto.Bank;
import dae.telegrambothomework.dto.CreditInput;
import dae.telegrambothomework.dto.CreditParser;
import dae.telegrambothomework.dto.Registerer;

import io.vavr.control.Either;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

public class CreditsMain {
    private static final String ERROR_NUMBER_FORMAT = "Sorry, need provide a number";
    public static final Map<String, CreditParser> data;
    public static final Map<String, Bank> bankData;

    static {
        // said to not save in .json files than I save in .png files
        data = readMap(CreditParser.class, Bot.global.getCredits()).getOrElseThrow(e -> new RuntimeException(e));
        bankData = readMap(Bank.class, Bot.global.getBanks()).getOrElseThrow(e -> new RuntimeException(e));
    }

    public static<T extends Registerer> Either<Exception, T> read(Class<T> type, String content) {
        try {
            var json = Bot.objectReader.readValue(content, type);
            json.setFileContent(content);
            return Either.right(json);
        } catch(Exception e) {
            return Either.left(e);
        }
    }

    public static<T extends Registerer> Either<Exception, Map<String, T>> readMap(Class<T> type, String[] packages) {
        if(type == null || packages == null) return Either.left(new NullPointerException());
        Map<String, T> output = new HashMap<>();

        for(String string : packages) {
            read(type, string).peek(json -> {
                output.put(json.getRegister(), json);
            }).orElseRun(error -> {
                System.out.println("[WARNING] error loading .json, skipped: " + error.getMessage());
            });
        }

        return Either.right(output);
    }

    public static final MsgInput CAR_CREDIT = new MsgInput() {
        public final CreditInput cred_input = new CreditInput();
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