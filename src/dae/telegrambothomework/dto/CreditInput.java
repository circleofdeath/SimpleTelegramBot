package dae.telegrambothomework.dto;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.math.BigDecimal;

@Data
public class CreditInput {
    private SendMessage message;
    private CreditParser parser;
    private Bank bank;
    private BigDecimal totalCost;
    private BigDecimal initialPayment;
    private BigDecimal monthlyPayment;
}