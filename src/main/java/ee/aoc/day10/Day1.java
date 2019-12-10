package ee.aoc.day10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import ee.aoc.InputFileReaderUtil;


public class Day1 {
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day10.txt");
        partOne(linesFromClassPathResourceFile);
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        List<Pair<Integer, Integer>> assSteroids = new ArrayList<>();
        for (int i = 0; i < linesFromClassPathResourceFile.size(); i++) {
            char[] line = linesFromClassPathResourceFile.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                if (line[j] == '#') {
                    assSteroids.add(Pair.of(i, j));
                }
            }
        }

        int observedMax = 0;
        for (Pair<Integer, Integer> observable : assSteroids) {
            List<Pair<Integer, Integer>> pairs = new ArrayList<>(assSteroids);
            pairs.sort(Comparator.comparing(x -> manhattanDistance(observable, x)));
            pairs.remove(observable);

            Set<Pair<Integer, Integer>> minDeltas = new HashSet<>();
            for (Pair<Integer, Integer> target : assSteroids) {
                if (observable.equals(target)) continue;

                int xDelta = Math.abs(target.getLeft() - observable.getLeft());
                int yDelta = Math.abs(target.getRight() - observable.getRight());
                int gcd = gcd(xDelta, yDelta);
                Pair<Integer, Integer> minDelta = Pair.of(xDelta / gcd, yDelta / gcd);
                minDeltas.add(minDelta);
            }

            if (minDeltas.size() > observedMax) {
                observedMax = minDeltas.size();
            }
        }
        System.out.println(observedMax);
    }

    private static int gcd(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }

    private static int manhattanDistance(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        return Math.abs(start.getLeft() - end.getLeft()) + Math.abs(start.getLeft() - end.getLeft());
    }
}
