package dae.telegrambothomework.credits;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class CreditTable {
    private double[][] percentTable;
    private double[] initialPayment;
    private int[] monthList;
    private double[] initialCommission;
    private String name;

    private double _tmp_124001(double totalCost, double tmp2, double[] tmp1, double monthlyPay, int x) {
        return Math.abs(monthlyPayCounter(totalCost, tmp2, monthList()[x], tmp1[x]) - monthlyPay);
    }

    public boolean lowDiffNextCounter(double totalCost, double monthlyPay, int lowDiffPrev, int lowDiffNext, int i) {
        double[] tmp1 = percentTable()[lowDiffPrev];
        double tmp2 = initialPayment()[lowDiffPrev];

        return _tmp_124001(totalCost, tmp2, tmp1, monthlyPay, lowDiffNext)
                > _tmp_124001(totalCost, tmp2, tmp1, monthlyPay, i);
    }

    public boolean lowDiffPrevCounter(double totalCost, double initialPay, int lowDiffPrev, int i) {
        return Math.abs(initialPay - initialPayCounter(totalCost, initialPayment()[lowDiffPrev])) >
                Math.abs(initialPay - initialPayCounter(totalCost, initialPayment()[i]));
    }

    public double totalCostCounter(double totalCost, int lowDiffPrev, int lowDiffNext) {
        double initialPay = initialPayCounter(totalCost, initialPayment()[lowDiffPrev]);
        double mothPay = monthlyPayCounter(totalCost, initialPayment()[lowDiffPrev],
                monthList()[lowDiffNext], percentTable()[lowDiffPrev][lowDiffNext]);

        return initialPay + Math.ceil(mothPay) * monthList()[lowDiffNext] + mothPay * initialCommission()[lowDiffNext] * 0.01;
    }

    public int lowDiffPrev(double totalCost, double initialPay) {
        int lowDiffPrev = 0;
        for (int i = 1; i < monthList().length; i++) {
            if (lowDiffPrevCounter(totalCost, initialPay, lowDiffPrev, i)) {
                lowDiffPrev = i;
            }
        }
        return lowDiffPrev;
    }

    public int lowDiffNext(double totalCost, double initialPay, double monthlyPay) {
        int lowDiffNext = 0;
        for (int i = 1; i < monthList().length; i++) {
            if (lowDiffNextCounter(totalCost, monthlyPay, lowDiffPrev(totalCost, initialPay), lowDiffNext, i)) {
                lowDiffNext = i;
            }
        }
        return lowDiffNext;
    }

    public static double initialPayCounter(double totalCost, double initialPayPercent) {
        return Math.ceil(totalCost * initialPayPercent);
    }

    public static double restPayCounter(double totalCost, double initialPayPercent) {
        return Math.ceil(totalCost - (totalCost * initialPayPercent));
    }

    public static double monthlyPayCounter(double totalCost, double initialPayPercent, int month, double percent) {
        return restPayCounter(totalCost, initialPayPercent) / month * (percent / 100) + restPayCounter(totalCost, initialPayPercent) / month;
    }
}