package dae.telegrambothomework.dto;

import dae.telegrambothomework.credits.factory.ICalculationResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unchecked")
public class CreditParser extends Registerer {
    private String parser;

    public void selfExecute(CreditInput input) {
        input.setFileContent(getFileContent());
        get().calculate(input);
    }

    public<T extends ICalculationResult> T get() {
        try {
            return ((Class<T>) Class.forName(parser)).getConstructor().newInstance();
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}