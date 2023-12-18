package org.etieskrill.advent._2023._02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ColouredCubes {

    private final List<Game> games;

    private record Game(
            int id,
            Pull[] pulls
    ) {
        public record Pull(
                int numRedCubes,
                int numGreenCubes,
                int numBlueCubes
        ) {
            public static Pull parse(String s) {
                int red = 0, green = 0, blue = 0;
                for (String pull : s.split(", ")) {
                    int num = -1;
                    int partCounter = 0;
                    for (String part : pull.split(" ")) {
                        if (part == null || part.isBlank()) continue;
                        if (partCounter == 0) num = Integer.parseInt(part);
                        if (partCounter == 1) switch (part) {
                            case "red" -> red += num;
                            case "green" -> green += num;
                            case "blue" -> blue += num;
                        }
                        partCounter++;
                    }
                }

                return new Pull(red, green, blue);
            }
        }

        public static Game parse(String s) {
            String[] split = s.split(":");
            int id = Integer.parseInt(split[0].split(" ")[1]);

            String[] splitPulls = split[1].split("; ");
            Pull[] pulls = Arrays.stream(splitPulls).map(Pull::parse).toArray(Pull[]::new);

            return new Game(id, pulls);
        }

        @Override
        public String toString() {
            return "Game{" +
                    "id=" + id +
                    ", pulls=" + Arrays.toString(pulls) +
                    '}';
        }
    }

    ColouredCubes() {
        this.games = new ArrayList<>(100);
    }

    public static void main(String[] args) {
        ColouredCubes cubes = new ColouredCubes();

        try (Stream<String> lines = Files.lines(Path.of("src/main/resources/games-record.txt"))) {
            cubes.games.addAll(lines.map(Game::parse).toList());
        } catch (IOException e) {
            System.err.println("File could not be found");
        } catch (RuntimeException e) {
            throw new RuntimeException("Malformed input file", e);
        }

        List<Game> games = cubes.games;

        int red = 12, green = 13, blue = 14;
        List<Game> validGames = games.stream()
                .filter(game -> Arrays.stream(game.pulls).noneMatch(pull ->
                        pull.numRedCubes > red || pull.numGreenCubes > green || pull.numBlueCubes > blue)
                ).toList();

        System.out.println("Number of valid games: " + validGames.size());
        System.out.println("Sum of the indices: " + validGames.stream().mapToInt(Game::id).sum());

        int[] minimalSetPower = games.stream()
                .mapToInt(game -> {
                    int minRed = Arrays.stream(game.pulls).mapToInt(Game.Pull::numRedCubes).max().orElseThrow();
                    int minGreen = Arrays.stream(game.pulls).mapToInt(Game.Pull::numGreenCubes).max().orElseThrow();
                    int minBlue = Arrays.stream(game.pulls).mapToInt(Game.Pull::numBlueCubes).max().orElseThrow();
                    return minRed * minGreen * minBlue;
                }).toArray();

        System.out.println("The total set power is: " + Arrays.stream(minimalSetPower).sum());
    }

}
