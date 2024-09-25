package dae.telegrambothomework.credits.factory;

import dae.telegrambothomework.credits.CreditTable;
import dae.telegrambothomework.credits.CreditsMain;

public class DefaultCalculationResult implements ICalculationResult {
    @Override
    public String calculate(String fileContent, double totalCost, double initialPayment, double monthlyPayment) {
       CreditTable table = CreditsMain.gson.fromJson(fileContent, CreditTable.class);

        StringBuilder builder = new StringBuilder()
                .append("Result for ").append(table.name());

        int lowDiffPrev = table.lowDiffPrev(totalCost, initialPayment);
        int lowDiffNext = table.lowDiffNext(totalCost, initialPayment, monthlyPayment);

        if(CreditTable.initialPayCounter(totalCost, table.initialPayment()[lowDiffPrev]) != initialPayment) {
            builder
                    .append("\nInitial payment ")
                    .append(initialPayment)
                    .append(" is not available. Closest available ")
                    .append(CreditTable.initialPayCounter(totalCost, table.initialPayment()[lowDiffPrev]));
        }

        builder.append("\nInitial\t\tMonthly payment\npayment");
        for(int i : table.monthList()) builder.append("\t\t").append(i);
        builder.append("\n").append(initialPayment).append("\t");

        for(int i = 0; i < table.percentTable()[lowDiffPrev].length; i++) {
            builder.append("\t").append(Math.ceil(CreditTable.monthlyPayCounter(
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
                .append(Math.ceil(CreditTable.monthlyPayCounter(
                        totalCost,
                        table.initialPayment()[lowDiffPrev],
                        table.monthList()[lowDiffNext],
                        table.percentTable()[lowDiffPrev][lowDiffNext]
                )))
                .append("\nTotal cost of the credit is: ")
                .append(Math.ceil(table.totalCostCounter(totalCost, lowDiffPrev, lowDiffNext)))
                .toString();
    }
}