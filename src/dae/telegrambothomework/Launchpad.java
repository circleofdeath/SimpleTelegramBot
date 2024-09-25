package dae.telegrambothomework;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

public class Launchpad {
    private static String APRCounter(String percent, String days) {
        BigDecimal p = new BigDecimal(percent);
        BigDecimal y = new BigDecimal(days);
        return (p.multiply(y).add(new BigDecimal("1"))).toString();
    }

    private static String launchpoolCounter(String prize, String totalSum, String fund, String APR){
        BigDecimal prz = new BigDecimal(prize);
        BigDecimal sum = new BigDecimal(totalSum);
        BigDecimal fun = new BigDecimal(fund);
        BigDecimal apr = new BigDecimal(APR);
        return fun.divide(sum).multiply(prz).multiply(apr).toString();
    }

    public static String launchpoolCalculate(String prize, String totalSum, String fund, String percent, String days) {
        return launchpoolCounter(prize, totalSum, fund, APRCounter(percent, days));
    }

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
}