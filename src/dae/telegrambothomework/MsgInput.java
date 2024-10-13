package dae.telegrambothomework;

import dae.telegrambothomework.credits.CreditsMain;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class MsgInput {
    public abstract void process(String input, SendMessage message);

    public String help() {
        return "No description provided";
    }

    public static MsgInput fastCommand(String description, BiConsumer<String, SendMessage> action) {
        return new MsgInput() {
            @Override
            public void process(String input, SendMessage message) {
                action.accept(input, message);
            }

            @Override
            public String help() {
                return description;
            }
        };
    }

    public static final Map<String, MsgInput> commands = Map.of(
            "Credit", fastCommand("calculate credits for mark of car", (input, message) -> {
                message.setText("Let's begin, please enter mark");
                Bot.msgInput = CreditsMain.CAR_CREDIT;
            }),

            "CreditMarks", fastCommand("lists of available marks", (input, message) -> {
                String[] keys = CreditsMain.data.keySet().toArray(String[]::new);
                StringBuilder builder = new StringBuilder("Available marks:\n").append(keys[0]);
                for(int i = 1; i < keys.length; i++) {
                    builder.append(", ").append(keys[i]);
                }
                message.setText(builder.toString());
            }),

            "Banks", fastCommand("lists of available banks", (input, message) -> {
                String[] keys = CreditsMain.bankData.keySet().toArray(String[]::new);
                StringBuilder builder = new StringBuilder("Available banks:\n").append(keys[0]);
                for(int i = 1; i < keys.length; i++) {
                    builder.append(", ").append(keys[i]);
                }
                message.setText(builder.toString());
            }),

            "Launchpool", fastCommand("do launchpool thing", (input, message) -> {
                message.setText("Let's begin, please enter prize ($)");
                Bot.msgInput = Launchpad.LAUNCHPAD;
            }),

            "Game", fastCommand("do gaming", (input, message) -> {
                message.setText("Let's begin! Enter number from 0 to 100");
                Bot.msgInput = Game.GAME;
            })
    );

    public static final MsgInput DEFAULT = new MsgInput() {
        @Override
        public void process(String input, SendMessage message) {
            boolean found = false;
            for(Map.Entry<String, MsgInput> entry : commands.entrySet()) {
                if(Objects.equals(entry.getKey(), input)) {
                    found = true;
                    entry.getValue().process(input, message);
                    break;
                }
            }

            if(!found) {
                StringBuilder builder = new StringBuilder("Hello, how can i help you?\nAvailable commands:");
                for(Map.Entry<String, MsgInput> entry : commands.entrySet()) {
                    builder.append("\n").append(entry.getKey()).append(" - ").append(entry.getValue().help());
                }
                message.setText(builder.toString());
            }
        }
    };
}