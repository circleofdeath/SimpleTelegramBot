package dae.telegrambothomework.credits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dae.telegrambothomework.Bot;
import dae.telegrambothomework.MsgInput;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CreditsMain {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final Map<String, CreditParser> data = new HashMap<>();

    public static String calculate(String mark, double totalCost, double initialPayment, double monthlyPayment) {
        return data.get(mark).selfExecute(totalCost, initialPayment, monthlyPayment);
    }

    static {
        var resourceDir = CreditsMain.class.getClassLoader().getResource("credits");
        if(resourceDir == null) {
            throw new IllegalStateException("Can't find resource dir");
        }
        try(var list = java.nio.file.Files.list(java.nio.file.Path.of(resourceDir.toURI()))) {
            list.forEach(path -> {
                try(var is = java.nio.file.Files.newInputStream(path)) {
                    var content = new String(is.readAllBytes());
                    var json = gson.fromJson(content, CreditParser.class);
                    json.setFileContent(content);
                    data.put(json.getRegister(), json);
                } catch (IOException e) {
                    throw new IllegalStateException("Can't read file", e);
                }
            });
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Can't list files", e);
        }
    }

    public static final MsgInput CAR_CREDIT = new MsgInput() {
        public int step = 0;

        public String mark;
        public double totalCost;
        public double initialPayment;
        public double monthlyPayment;

        @Override
        public void process(String input, SendMessage message) {
            switch(step) {
                case 0: {
                    if(CreditsMain.data.containsKey(input)) {
                        message.setText("Great! Next, write total cost ($)");
                        mark = input;
                        step++;
                    } else {
                        String[] keys = CreditsMain.data.keySet().toArray(String[]::new);
                        StringBuilder builder = new StringBuilder("Provided mark not found, available marks:\n").append(keys[0]);
                        for(int i = 1; i < keys.length; i++) {
                            builder.append(", ").append(keys[i]);
                        }
                        message.setText(builder.toString());
                    }
                    break;
                }

                case 1: {
                    try {
                        totalCost = Double.parseDouble(input);
                        message.setText("Great! Next, write initial payment");
                        step++;
                    } catch(NumberFormatException ignored) {
                        message.setText("Sorry, need provide a number");
                    }
                    break;
                }

                case 2: {
                    try {
                        initialPayment = Double.parseDouble(input);
                        message.setText("Great! Next, write monthly payment");
                        step++;
                    } catch(NumberFormatException ignored) {
                        message.setText("Sorry, need provide a number");
                    }
                    break;
                }

                case 3: {
                    try {
                        monthlyPayment = Double.parseDouble(input);
                        message.setText(CreditsMain.calculate(mark, totalCost, initialPayment, monthlyPayment));
                        step = 0;
                        Bot.msgInput = DEFAULT;
                    } catch(NumberFormatException ignored) {
                        message.setText("Sorry, need provide a number");
                    }
                    break;
                }
            }
        }
    };
}