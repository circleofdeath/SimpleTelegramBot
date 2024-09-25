package dae.telegrambothomework;

import dae.telegrambothomework.credits.CreditsMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class MsgInput {
    public abstract void process(String input, SendMessage message);

    public static final MsgInput DEFAULT = new MsgInput() {
        @Override
        public void process(String input, SendMessage message) {
            switch(input) {
                case "Credit" -> {
                    message.setText("Let's begin, please enter mark");
                    Bot.msgInput = CreditsMain.CAR_CREDIT;
                }
                case "CreditMarks" -> {
                    String[] keys = CreditsMain.data.keySet().toArray(String[]::new);
                    StringBuilder builder = new StringBuilder("Available marks:\n").append(keys[0]);
                    for(int i = 1; i < keys.length; i++) {
                        builder.append(", ").append(keys[i]);
                    }
                    message.setText(builder.toString());
                }
                case "Launchpool" -> {
                    message.setText("Let's begin, please enter prize ($)");
                    Bot.msgInput = Launchpad.LAUNCHPAD;
                }
                default -> message.setText("""
                        Hello, how can i help you?
                        Available commands:
                        Credit - calculate credits for mark of car
                        CreditMarks - lists of available marks
                        Launchpool - do launchpool thing
                        """);
            }
        }
    };
}