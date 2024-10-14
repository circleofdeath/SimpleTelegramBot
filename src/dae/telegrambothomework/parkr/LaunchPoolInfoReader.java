package dae.telegrambothomework.parkr;

import dae.telegrambothomework.dto.LaunchPoolInfo;
import io.vavr.control.Either;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchPoolInfoReader {
    public static final char[] HEADER = "LaunchPoolInfo".toCharArray();

    public static Either<String, List<LaunchPoolInfo>> readArray(String string) {
        return readArray(string.replace("\n", "").replace("\t", "").replace("\r", "").toCharArray());
    }

    public static Either<String, List<LaunchPoolInfo>> readArray(char[] buffer) {
        int[] i = new int[1];
        List<LaunchPoolInfo> result = new ArrayList<>();

        while(buffer[i[0]] != '[') i[0]++;
        i[0]++;
        while(buffer[i[0]] != ']') {
            while(buffer[i[0]] == ' ') i[0]++;
            for(int j = 0; j < HEADER.length; j++) {
                if(buffer[i[0] + j] != HEADER[j]) {
                    return Either.left("Expected LaunchPoolInfo header, but got " + new String(buffer, i[0], HEADER.length));
                }
            }

            result.add(read(i, buffer).getOrElseThrow((message) -> {
                throw new RuntimeException(message);
            }));
            do i[0]++;
            while (buffer[i[0]] == ' ');
            if(buffer[i[0]] == ',') {
                do {
                    i[0]++;
                } while (buffer[i[0]] == ' ');
            }
        }

        return Either.right(result);
    }

    public static Either<String, LaunchPoolInfo> read(int[] i, char[] buffer) {
        for(int j = 0; j < HEADER.length; j++) {
            if(buffer[i[0] + j] != HEADER[j]) {
                return Either.left("Expected LaunchPoolInfo header, but got " + new String(buffer, i[0], HEADER.length));
            }
        }
        LaunchPoolInfo info = new LaunchPoolInfo();
        i[0] += HEADER.length;
        while (buffer[i[0]] == ' ') i[0]++;
        i[0]++;
        try {
            while(true) {
                int j = 0;
                while(buffer[i[0]] == ' ') i[0]++;
                while(buffer[i[0] + j] != '=') {
                    j++;
                }
                String header = new String(buffer, i[0], j).trim();
                Object result = null;
                i[0] += j + 1;
                while(buffer[i[0]] == ' ') i[0]++;
                j = 0;
                if(buffer[i[0]] == '\'') {
                    i[0]++;
                    while(buffer[i[0] + j] != '\'') {
                        j++;
                    }

                    result = new String(buffer, i[0], j).trim();
                    i[0] += j + 1;
                } else if(buffer[i[0]] == '{') {
                    Map<String, String> tmp = new HashMap<>();
                    i[0]++;

                    do {
                        int l = 0;
                        while(buffer[i[0]] == ' ') i[0]++;
                        while (buffer[i[0] + l] != ':') {
                            l++;
                        }
                        String name = new String(buffer, i[0], l).trim();
                        i[0] += l + 2;
                        while(buffer[i[0]] == ' ') i[0]++;
                        int u = 0;
                        while (buffer[i[0] + u] != ',' && buffer[i[0] + u] != '}') {
                            u++;
                        }
                        String value = new String(buffer, i[0], u).trim();
                        tmp.put(name, value);
                        i[0] += u + 1;
                    } while (buffer[i[0] - 1] != '}');

                    result = tmp;
                }
                try {
                    Field f = info.getClass().getDeclaredField(header);
                    f.setAccessible(true);
                    f.set(info, result);
                } catch(Throwable ignored) {
                    return Either.left("Invalid buffer reading: " + header + ", value: " + result);
                }
                while(buffer[i[0]] == ' ') i[0]++;
                if(buffer[i[0]] == ',') {
                    i[0]++;
                } else if(buffer[i[0]] == '}') {
                    break;
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            return Either.left("Buffer overflow detected: " + e.getMessage());
        } catch(Throwable e) {
            return Either.left("Undefined behaviour detected: " + e.getMessage());
        }
        return Either.right(info);
    }
}