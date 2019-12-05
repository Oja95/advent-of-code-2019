package ee.aoc.day3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import ee.aoc.InputFileReaderUtil;

public class Main {
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day3.txt");
        partOne(linesFromClassPathResourceFile);
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        String[] firstWire = linesFromClassPathResourceFile.get(0).split(",");
        String[] secondWire = linesFromClassPathResourceFile.get(1).split(",");

        List<Pair<Integer, Integer>> firstWireCoords = mapWire(firstWire);
        List<Pair<Integer, Integer>> secondWireCoords = mapWire(secondWire);

        Set<Pair<Integer, Integer>> intersections = new HashSet<>(firstWireCoords);
        intersections.retainAll(new HashSet<>(secondWireCoords));
        //first solution
//        firstWireCoords.retainAll(secondWireCoords);
//        List<Integer> collect = intersections.stream()
//                .map(pair -> pair.getLeft() + pair.getRight())
//                .sorted()
//                .collect(Collectors.toList());
//        System.out.println(collect);
//

        List<Integer> collect = intersections.stream()
                .map(intersection -> firstWireCoords.indexOf(intersection) + secondWireCoords.indexOf(intersection))
                .sorted()
                .collect(Collectors.toList());
        System.out.println(collect);

    }

    private static List<Pair<Integer, Integer>> mapWire(String[] firstWire) {
        List<Pair<Integer, Integer>> firstWireCoords = new ArrayList<>(List.of(Pair.of(0,0)));
        Pair<Integer, Integer> startPos = Pair.of(0,0);
        for (String input : firstWire) {
            List<Pair<Integer, Integer>> movedCoords = movedCoords(startPos, input);
            firstWireCoords.addAll(movedCoords);
            startPos = movedCoords.get(movedCoords.size() - 1);
        }
        return firstWireCoords;
    }

    private static List<Pair<Integer, Integer>> movedCoords(Pair<Integer, Integer> startPos, String move) {
        Pair<Integer, Integer> moveVector = getMoveVector(move.charAt(0));
        int steps = Integer.parseInt(move.substring(1));
        List<Pair<Integer, Integer>> movedCoords = new ArrayList<>();
        while (steps > 0) {
            Pair<Integer, Integer> newPos = move(startPos, moveVector);
            movedCoords.add(newPos);
            startPos = newPos;
            steps--;
        }
        return movedCoords;
    }

    private static Pair<Integer, Integer> getMoveVector(char direction) {
        switch (direction) {
            case 'U':
                return Pair.of(0, 1);
            case 'D':
                return Pair.of(0, -1);
            case 'R':
                return Pair.of(1, 0);
            case 'L':
                return Pair.of(-1, 0);
            default:
                throw new IllegalArgumentException("Unknown direction");
        }
    }

    private static Pair<Integer, Integer> move(Pair<Integer, Integer> startPos, Pair<Integer, Integer> vector) {
        return Pair.of(startPos.getLeft() + vector.getLeft(), startPos.getRight() + vector.getRight());
    }
}
