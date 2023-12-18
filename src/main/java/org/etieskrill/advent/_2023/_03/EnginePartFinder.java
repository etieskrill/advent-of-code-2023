package org.etieskrill.advent._2023._03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EnginePartFinder {

    public static void main(String[] args) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Path.of("src/main/resources/org/etieskrill/advent/_2023/03/abbreviated-puzzle-input-03.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }

        int sum = 0, matches = 0;

        for (int i = 0; i < lines.size(); i++) {
            char[] lineChars = lines.get(i).toCharArray();

            int currentIndex = nextNumber(lineChars, 0);
            while (currentIndex != -1) {
                int numDigits = numDigits(lineChars, currentIndex);

                if (hasSymbol(lines, i, currentIndex, numDigits)) {
                    int part = Integer.parseInt(lines.get(i).substring(currentIndex, currentIndex + numDigits));
                    sum += part;
                    matches++;
                    System.out.println("match at " + i + " " + currentIndex + "+" + numDigits + ": " + part);
                }

                currentIndex += numDigits;
                currentIndex = nextNumber(lineChars, currentIndex);
            }
        }

        System.out.println("The number of matching decimals is: " + matches);
        System.out.println("The total sum of the numbers is: " + sum);
    }

    static int nextNumber(char[] line, int offset) {
        for (int i = offset; i < line.length; i++) {
            if (Character.isDigit(line[i]))
                return i;
        }
        return -1;
    }

    static int numDigits(char[] line, int offset) {
        for (int i = offset; i < line.length; i++) {
            if (line[i] == '.' || !Character.isDigit(line[i]))
                return i - offset;
        }
        return -1;
    }

    static boolean hasSymbol(List<String> lines, int line, int row, int digits) {
        if (line > 0 && lines.get(line - 1)
                .substring(Math.max(row - 1, 0), Math.min(row + digits + 1, lines.get(line - 1).length()))
                .chars().anyMatch(c -> c != '.' && !Character.isDigit(c))
        ) return true;
        if (line < lines.size() - 1 && lines.get(line + 1)
                .substring(Math.max(row - 1, 0), Math.min(row + digits + 1, lines.get(line + 1).length()))
                .chars().anyMatch(c -> c != '.' && !Character.isDigit(c))
        ) return true;
        if (row > 0 && lines.get(line)
                .substring(row - 1, row)
                .chars().anyMatch(c -> c != '.' && !Character.isDigit(c))
        ) return true;
        return row < lines.get(line).length() - 1 && lines.get(line)
                .substring(row + 1, row + 2)
                .chars().anyMatch(c -> c != '.' && !Character.isDigit(c));
    }

}
