package dae.telegrambothomework.credits.factory;

public class Car2SeatsCalculationResult implements ICalculationResult {
    @Override
    public String calculate(String fileContent, double totalCost, double initialPayment, double monthlyPayment) {
        return """
                Never gonna give you up
                Never gonna let you down
                Never gonna run around and desert you
                Never gonna make you cry
                Never gonna say goodbye
                Never gonna tell a lie and hurt you""";
    }
}