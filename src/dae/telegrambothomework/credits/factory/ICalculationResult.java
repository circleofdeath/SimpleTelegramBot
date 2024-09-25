package dae.telegrambothomework.credits.factory;

public interface ICalculationResult {
    String calculate(String fileContent, double totalCost, double initialPayment, double monthlyPayment);
}