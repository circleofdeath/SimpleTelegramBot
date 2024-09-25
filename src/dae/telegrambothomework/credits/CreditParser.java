package dae.telegrambothomework.credits;

import dae.telegrambothomework.credits.factory.ICalculationResult;

import lombok.Data;

@Data
@SuppressWarnings("unchecked")
public class CreditParser {
    private String register;
    private String parser;
    private String fileContent;

    public String selfExecute(double totalCost, double initialPayment, double monthlyPayment) {
        return get().calculate(fileContent, totalCost, initialPayment, monthlyPayment);
    }

    public<T extends ICalculationResult> T get() {
        try {
            return ((Class<T>) Class.forName(parser)).getConstructor().newInstance();
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}