package dae.telegrambothomework.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LaunchPoolInfo {
    private String exchange;
    private String launchPool;
    private Map<String, String> pools;
    private String period;
    private String status;
}