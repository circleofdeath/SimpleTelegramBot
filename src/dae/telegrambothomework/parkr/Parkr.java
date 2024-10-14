package dae.telegrambothomework.parkr;

import dae.telegrambothomework.Bot;
import dae.telegrambothomework.dto.Data;
import dae.telegrambothomework.dto.LaunchPoolInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static dae.telegrambothomework.parkr.LaunchPoolMain.DATA;

public class Parkr {
    public static void main(String[] args) {
        Data dto = new Data();
        dto.setLaunchpools(LaunchPoolInfoReader
                .readArray(DATA)
                .getOrElseThrow((x) -> new RuntimeException(x))
                .toArray(LaunchPoolInfo[]::new)
        );

        Path runningDir = Path.of(".").toAbsolutePath().normalize();
        Path targetDir = runningDir.resolve("resources/");
        Path src = runningDir.resolve("buil/");

        try(var x = Files.list(src.resolve("credits"))) {
            dto.setCredits(x.map(p -> {
                try {
                    return Files.readString(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toArray(String[]::new));
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }

        try(var x = Files.list(src.resolve("banks"))) {
            dto.setBanks(x.map(p -> {
                try {
                    return Files.readString(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toArray(String[]::new));
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }

        Path dst = targetDir.resolve("UNIVERSE.png");
        ImageConverter.write(dst, dto);
        System.out.println(Bot.readData(dst));
    }
}