package dae.telegrambothomework;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class MsgInput {
    public abstract void process(String input, SendMessage message);

    public static final MsgInput LAUNCHPAD = new MsgInput() {
        public int step = 0;

        public String prize;
        public String totalSum;
        public String fund;
        public String percent;
        public String days;

        @Override
        public void process(String input, SendMessage message) {
            switch(step) {
                case 0: {
                    try {
                        Double.parseDouble(input);
                        message.setText("Great! Next, write total sum of prizes ($)");
                        prize = input;
                        step++;
                    } catch(NumberFormatException ignored) {
                        message.setText("Wrong input, try again");
                    }
                    break;
                }

                case 1: {
                    try {
                        Double.parseDouble(input);
                        totalSum = input;
                        message.setText("Great! Next, write fund ($)");
                        step++;
                    } catch(NumberFormatException ignored) {
                        message.setText("Wrong input, try again");
                    }
                    break;
                }

                case 2: {
                    try {
                        Double.parseDouble(input);
                        fund = input;
                        message.setText("Great! Next, write APR (%)");
                        step++;
                    } catch(NumberFormatException ignored) {
                        message.setText("Wrong input, try again");
                    }
                    break;
                }

                case 3: {
                    try {
                        Double.parseDouble(input);
                        percent = input;
                        message.setText("Great! Next, write days");
                        step++;
                    } catch(NumberFormatException ignored) {
                        message.setText("Wrong input, try again");
                    }
                    break;
                }

                case 4: {
                    try {
                        Double.parseDouble(input);
                        days = input;
                        message.setText(Launchpad.launchpoolCalculate(prize, totalSum, fund, percent, days));
                        Bot.msgInput = MsgInput.DEFAULT;
                        step = 0;
                    } catch(NumberFormatException ignored) {
                        message.setText("Wrong input, try again");
                    }
                    break;
                }
            }
        }
    };

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
            } else if(input.equals("Launchpool")) {
                message.setText("Let's begin, please enter prize ($)");
                Bot.msgInput = LAUNCHPAD;
            } else {
                message.setText("""
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