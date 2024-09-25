package dae.telegrambothomework;

import java.util.HashMap;
import java.util.Map;

public class Credits {
    public static Map<String, CreditTable> data = new HashMap<>();

    static {
        data.put("Mazda", new CreditTable(
                new double[][] {
                        {6.5, 21.9},
                        {4.9, 21.9},
                        {2.9, 21.9},
                        {0.01, 21.9},
                        {0.01, 14, 9}
                },
                new double[] {0.2, 0.3, 0.4, 0.5, 0.6},
                new int[] {24, 36},
                new double[] {0, 0}
        ));

        data.put("Toyota",new CreditTable(
                new double[][] {
                        {3.49, 6.99, 8.99, 11.99, 11.99},
                        {2.49, 5.99, 8.99, 11.99, 11.99},
                        {1.49, 4.99, 7.49, 9.99, 9.99},
                        {0.01, 3.99, 5.49, 9.99, 9.99},
                        {0.01, 0.01, 4.99, 7.99, 7.99}
                },
                new double[] {0.3, 0.4, 0.5, 0.6, 0.7},
                new int[] {12, 24, 36, 48, 60},
                new double[]{2.99, 2.99, 2.99, 0, 0}
        ));
    }

    public record CreditTable(double[][] percentTable, double[] initialPayment, int[] monthList, double[] initialCommission) {
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
    }

    public static String calculate(String mark, double totalCost, double initialPayment, double monthlyPayment) {
        if(!data.containsKey(mark)) return "Mark not found!";
        CreditTable table = data.get(mark);

        StringBuilder builder = new StringBuilder()
                .append("Result for ").append(mark);

        int lowDiffPrev = table.lowDiffPrev(totalCost, initialPayment);
        int lowDiffNext = table.lowDiffNext(totalCost, initialPayment, monthlyPayment);

        if(initialPayCounter(totalCost, table.initialPayment()[lowDiffPrev]) != initialPayment) {
            builder
                    .append("\nInitial payment ")
                    .append(initialPayment)
                    .append(" is not available. Closest available ")
                    .append(initialPayCounter(totalCost, table.initialPayment()[lowDiffPrev]));
        }

        builder.append("\nInitial\t\tMonthly payment\npayment");
        for(int i : table.monthList()) builder.append("\t\t").append(i);
        builder.append("\n").append(initialPayment).append("\t");

        for(int i = 0; i < table.percentTable()[lowDiffPrev].length; i++) {
            builder.append("\t").append(Math.ceil(monthlyPayCounter(
                    totalCost,
                    table.initialPayment()[lowDiffPrev],
                    table.monthList()[i],
                    table.percentTable()[lowDiffPrev][i]
            )));
        }

        return builder
                .append("\nThe closest monthly payment to ")
                .append(monthlyPayment)
                .append(" you can do is: ")
                .append(Math.ceil(monthlyPayCounter(
                        totalCost,
                        table.initialPayment()[lowDiffPrev],
                        table.monthList()[lowDiffNext],
                        table.percentTable()[lowDiffPrev][lowDiffNext]
                )))
                .append("\nTotal cost of the credit is: ")
                .append(Math.ceil(table.totalCostCounter(totalCost, lowDiffPrev, lowDiffNext)))
                .toString();
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