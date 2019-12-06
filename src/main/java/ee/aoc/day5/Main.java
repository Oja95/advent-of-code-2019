package ee.aoc.day5;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Main {
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day5.txt");
        partOne(linesFromClassPathResourceFile);
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        List<Integer> program = Arrays.stream(linesFromClassPathResourceFile.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int input = 5;
        int programCounter = 0;

        while (true) {
            int instruction = program.get(programCounter);
            int opCode = instruction % 100;
            int flags = instruction / 100; // flags

            int result;
            if (opCode == 1) {
                result = applyIntegerBiFunction(program, programCounter, flags, (i, j) -> i + j);
                program.set(program.get(programCounter + 3), result);
                programCounter += 4;
            } else if (opCode == 2) {
                result = applyIntegerBiFunction(program, programCounter, flags, (i, j) -> i * j);
                program.set(program.get(programCounter + 3), result);
                programCounter += 4;
            } else if (opCode == 3) {
                int pos = program.get(programCounter + 1);
                program.set(pos, input);
                programCounter += 2;
            } else if (opCode == 4) {
                int pos = program.get(programCounter + 1);
                System.out.println(program.get(pos));
                programCounter += 2;
            } else if (opCode == 5) {
                int firstParam = program.get(programCounter + 1);
                int firstOperand = posFlag(1, flags) ? firstParam : program.get(firstParam);
                if (firstOperand != 0) {
                    int secondParam = program.get(programCounter + 2);
                    int secondOperand = posFlag(2, flags) ? secondParam : program.get(secondParam);
                    programCounter = secondOperand;
                } else {
                    programCounter += 3;
                }
            } else if (opCode == 6) {
                int firstParam = program.get(programCounter + 1);
                int firstOperand = posFlag(1, flags) ? firstParam : program.get(firstParam);
                if (firstOperand == 0) {
                    int secondParam = program.get(programCounter + 2);
                    int secondOperand = posFlag(2, flags) ? secondParam : program.get(secondParam);
                    programCounter = secondOperand;
                } else {
                    programCounter += 3;
                }
            } else if (opCode == 7) {
                result = applyIntegerBiFunction(program, programCounter, flags, Integer::compare);
                if (result == -1) {
                    program.set(program.get(programCounter + 3), 1);
                } else {
                    program.set(program.get(programCounter + 3), 0);
                }
                programCounter += 4;
            } else if (opCode == 8) {
                result = applyIntegerBiFunction(program, programCounter, flags, Integer::compare);
                if (result == 0) {
                    program.set(program.get(programCounter + 3), 1);
                } else {
                    program.set(program.get(programCounter + 3), 0);
                }
                programCounter += 4;
            } else if (opCode == 99) {
                break;
            } else {
                throw new IllegalStateException("Unexpected program!");
            }
        }
    }

    private static int applyIntegerBiFunction(List<Integer> program, int programCounter, int flags, BiFunction<Integer, Integer, Integer> function) {
        int firstParam = program.get(programCounter + 1);
        int secondParam = program.get(programCounter + 2);
        int firstOperand = posFlag(1, flags) ? firstParam : program.get(firstParam);
        int secondOperand = posFlag(2, flags) ? secondParam : program.get(secondParam);
        return function.apply(firstOperand, secondOperand);
    }

    private static boolean posFlag(int pos, int flags) {
        int count = 1;
        while (count < pos) {
            flags /= 10;
            count++;
        }
        return flags % 10 == 1;
    }

}
