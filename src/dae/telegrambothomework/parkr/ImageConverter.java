package dae.telegrambothomework.parkr;

import dae.telegrambothomework.Bot;
import io.vavr.control.Try;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter {
    public static void write(Path path, Object object) {
        String data = Try.of(() -> Bot.objectWriter.writeValueAsString(object)).get();
        System.out.println(data.length());
        BufferedImage img = toImage(data.toCharArray());

        try {
            ImageIO.write(img, "png", path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage toImage(char[] chars) {
        int sideLength = 1 + (int) Math.ceil(Math.sqrt(Math.ceil(chars.length / 2f) + 2));
        BufferedImage image = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, sideLength);
        image.setRGB(1, 0, chars.length);

        for(int i = 2; i < chars.length + 2; i += 2) {
            short data1 = (short) (i - 2 < chars.length ? chars[i - 2] : 0);
            short data2 = (short) (i - 1 < chars.length ? chars[i - 1] : 0);
            int red   = (data1 >> 8) & 0xFF;
            int green = data1 & 0xFF;
            int blue  = (data2 >> 8) & 0xFF;
            int alpha = data2 & 0xFF;

            int x = (i / 2 + 1) % sideLength;
            int y = (i / 2 + 1) / sideLength;

            image.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
        }

        return image;
    }
}