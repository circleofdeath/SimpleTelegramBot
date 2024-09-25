package dae.telegrambothomework;

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
}