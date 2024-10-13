package dae.telegrambothomework;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

public class Launchpad {
    private static final String ERROR_NUMBER_FORMAT = "Wrong input, try to input a number";
    private static final BigDecimal ONE = new BigDecimal("1");

    public static String launchpoolCalculate(BigDecimal prize, BigDecimal totalSum, BigDecimal fund, BigDecimal percent, BigDecimal days) {
        return fund.divide(totalSum).multiply(prize).multiply(percent.multiply(days).add(ONE)).toString();
    }

    public static final MsgInput LAUNCHPAD = new MsgInput() {
        public int step = 0;

        public BigDecimal prize;
        public BigDecimal totalSum;
        public BigDecimal fund;
        public BigDecimal percent;

        @Override
        public void process(String input, SendMessage message) {
            switch(step) {
                case 0: {
                    Bot.parse(input).peek((n) -> {
                        message.setText("Great! Next, write total sum of prizes ($)");
                        prize = n;
                        step++;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }

                case 1: {
                    Bot.parse(input).peek((n) -> {
                        message.setText("Great! Next, write fund ($)");
                        totalSum = n;
                        step++;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }

                case 2: {
                    Bot.parse(input).peek((n) -> {
                        message.setText("Great! Next, write APR (%)");
                        fund = n;
                        step++;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }

                case 3: {
                    Bot.parse(input).peek((n) -> {
                        message.setText("Great! Next, write APR (%)");
                        percent = n;
                        step++;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }

                case 4: {
                    Bot.parse(input).peek((days) -> {
                        message.setText(Launchpad.launchpoolCalculate(prize, totalSum, fund, percent, days));
                        Bot.msgInput = MsgInput.DEFAULT;
                        step = 0;
                    }).orElseRun((ignored) -> {
                        message.setText(ERROR_NUMBER_FORMAT);
                    });
                    break;
                }
            }
        }
    };
}