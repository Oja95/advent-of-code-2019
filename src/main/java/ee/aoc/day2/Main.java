package ee.aoc.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;

import ee.aoc.InputFileReaderUtil;

public class Main {
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day2.txt");
        partOne(linesFromClassPathResourceFile);
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        List<Integer> initialInput = Arrays.stream(linesFromClassPathResourceFile.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Pair<Integer, Integer>> possiblePairs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                possiblePairs.add(Pair.of(i, j));
            }
        }

        Iterator<Pair<Integer, Integer>> iterator = possiblePairs.iterator();
        List<Integer> input = new ArrayList<>(initialInput);
        int expected = 19690720;
        int programCounter;
        while (input.get(0) != expected) {
            input = new ArrayList<>(initialInput);
            Pair<Integer, Integer> next = iterator.next();
            input.set(1, next.getKey());
            input.set(2, next.getValue());
            programCounter = 0;

            while (true) {
                Integer opCode = input.get(programCounter);
                Integer firstOperandLocation = input.get(programCounter + 1);
                Integer secondOperandLocation = input.get(programCounter + 2);
                Integer firstOperand = input.get(firstOperandLocation);
                Integer secondOperand = input.get(secondOperandLocation);

                int result;
                if (opCode == 1) {
                    result = firstOperand + secondOperand;
                } else if (opCode == 2) {
                    result = firstOperand * secondOperand;
                } else if (opCode == 99) {
                    break;
                } else {
                    throw new IllegalStateException("Unexpected input!");
                }

                input.set(input.get(programCounter + 3), result);
                programCounter += 4;
            }
        }
        System.out.println(100 * input.get(1) + input.get(2));
    }
}
