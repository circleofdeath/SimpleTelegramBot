package dae.telegrambothomework;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Random;

public class Game {
    public static final MsgInput GAME = new MsgInput() {
        public long number = -1;

        @Override
        public void process(String input, SendMessage message) {
            if(number == -1) number = new Random().nextInt(100);
            int num;
            try {
                num = Integer.parseInt(input);
            } catch(NumberFormatException ignored) {
                message.setText("Sorry, need provide a number");
                return;
            }
            if(number == num) {
                message.setText("You win!");
                number = -1;
                Bot.msgInput = MsgInput.DEFAULT;
            } else if(num > number) {
                message.setText("Your number is too big");
            } else if(num < number) {
                message.setText("Your number is too small");
            }
        }
    };
}