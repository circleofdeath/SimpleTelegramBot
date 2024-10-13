package dae.telegrambothomework.credits.factory;

import dae.telegrambothomework.dto.CreditInput;

public class Car2SeatsCalculationResult implements ICalculationResult {
    @Override
    public void calculate(CreditInput input) {
        input.getMessage().setText("""
                Never gonna give you up
                Never gonna let you down
                Never gonna run around and desert you
                Never gonna make you cry
                Never gonna say goodbye
                Never gonna tell a lie and hurt you""");
    }
}