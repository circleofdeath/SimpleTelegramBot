package dae.telegrambothomework;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class MsgInput {
    public abstract void process(String input, SendMessage message);

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
                    if(Credits.data.containsKey(input)) {
                        message.setText("Great! Next, write total cost ($)");
                        mark = input;
                        step++;
                    } else {
                        String[] keys = Credits.data.keySet().toArray(String[]::new);
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
                        message.setText(Credits.calculate(mark, totalCost, initialPayment, monthlyPayment));
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

    public static final MsgInput DEFAULT = new MsgInput() {
        @Override
        public void process(String input, SendMessage message) {
            if(input.equals("Credit")) {
                message.setText("Let's begin, please enter mark");
                Bot.msgInput = CAR_CREDIT;
            } else if(input.equals("CreditMarks")) {
                String[] keys = Credits.data.keySet().toArray(String[]::new);
                StringBuilder builder = new StringBuilder("Available marks:\n").append(keys[0]);
                for(int i = 1; i < keys.length; i++) {
                    builder.append(", ").append(keys[i]);
                }
                message.setText(builder.toString());
            } else {
                message.setText("Hello, how can i help you?\nAvailable commands:\nCredit - calculate credits for mark of car\nCreditMarks - lists of available marks");
            }
        }
    };
}