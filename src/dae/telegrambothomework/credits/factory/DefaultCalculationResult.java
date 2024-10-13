package dae.telegrambothomework.credits.factory;

import dae.telegrambothomework.dto.CreditInput;
import lombok.Data;
import lombok.experimental.Accessors;

public class DefaultCalculationResult implements ICalculationResult {
    @Override
    public void calculate(CreditInput input) {
        input.getMessage().setText("TODO");
    }

    @Data
    @Accessors(fluent = true)
    public static class CreditTable {
        private double[][] percentTable;
        private double[] initialPayment;
        private int[] monthList;
        private double[] initialCommission;
    }
}