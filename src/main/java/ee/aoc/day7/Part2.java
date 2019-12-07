package ee.aoc.day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.collections4.iterators.PermutationIterator;

import ee.aoc.InputFileReaderUtil;

public class Part2 {
    public static void main(String[] args) throws InterruptedException {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day7.txt");
        List<Integer> program = Arrays.stream(linesFromClassPathResourceFile.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        partTwo(program);
    }

    private static void partTwo(List<Integer> program) throws InterruptedException {
        PermutationIterator<Integer> permutations = new PermutationIterator<>(List.of(5, 6, 7, 8, 9));
        int maxThrust = 0;
        while (permutations.hasNext()) {
            int permutationOutput = calculateThrustForPermutation(program, permutations.next());
            if (permutationOutput > maxThrust) {
                maxThrust = permutationOutput;
            }
        }
        System.out.println(maxThrust);
    }

    private static int calculateThrustForPermutation(List<Integer> program, List<Integer> phaseSequence) throws InterruptedException {
        List<Amplifier> amplifiers = new ArrayList<>();
        BlockingQueue<Integer> previousOutput = new ArrayBlockingQueue<>(42);

        for (Integer integer : phaseSequence) {
            BlockingQueue<Integer> input = previousOutput;
            input.add(integer);
            BlockingQueue<Integer> output = new ArrayBlockingQueue<>(42);
            Amplifier amplifier = new Amplifier(input, output, new ArrayList<>(program));
            previousOutput = output;
            amplifiers.add(amplifier);
        }

        amplifiers.get(0).getInput().add(0);
        amplifiers.get(amplifiers.size() - 1).setOutput(amplifiers.get(0).getInput());
        List<Thread> amplifierThreads = amplifiers.stream()
                .map(Thread::new)
                .collect(Collectors.toList());
        amplifierThreads.forEach(Thread::start);
        for (Thread ampfilier : amplifierThreads) {
            ampfilier.join();
        }
        return amplifiers.get(amplifiers.size() - 1).getFinalOutput();
    }

    private static class Amplifier implements Runnable {
        private BlockingQueue<Integer> input;
        private BlockingQueue<Integer> output;
        private final List<Integer> program;
        private int finalOutput;

        public Amplifier(BlockingQueue<Integer> input, BlockingQueue<Integer> output, List<Integer> program) {
            this.input = input;
            this.output = output;
            this.program = program;
        }

        @Override
        public void run() {
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
                    try {
                        program.set(pos, input.take());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    programCounter += 2;
                } else if (opCode == 4) {
                    int pos = program.get(programCounter + 1);
                    Integer outputValue = program.get(pos);
                    finalOutput = outputValue;
                    output.add(outputValue);
                    programCounter += 2;
                } else if (opCode == 5) {
                    int firstParam = program.get(programCounter + 1);
                    int firstOperand = posFlag(1, flags) ? firstParam : program.get(firstParam);
                    if (firstOperand != 0) {
                        int secondParam = program.get(programCounter + 2);
                        programCounter = posFlag(2, flags) ? secondParam : program.get(secondParam);
                    } else {
                        programCounter += 3;
                    }
                } else if (opCode == 6) {
                    int firstParam = program.get(programCounter + 1);
                    int firstOperand = posFlag(1, flags) ? firstParam : program.get(firstParam);
                    if (firstOperand == 0) {
                        int secondParam = program.get(programCounter + 2);
                        programCounter = posFlag(2, flags) ? secondParam : program.get(secondParam);
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
                    throw new IllegalStateException("Unexpected opcode!");
                }
            }
        }

        private int applyIntegerBiFunction(List<Integer> program, int programCounter, int flags, BiFunction<Integer, Integer, Integer> function) {
            int firstParam = program.get(programCounter + 1);
            int secondParam = program.get(programCounter + 2);
            int firstOperand = posFlag(1, flags) ? firstParam : program.get(firstParam);
            int secondOperand = posFlag(2, flags) ? secondParam : program.get(secondParam);
            return function.apply(firstOperand, secondOperand);
        }

        private boolean posFlag(int pos, int flags) {
            int count = 1;
            while (count < pos) {
                flags /= 10;
                count++;
            }
            return flags % 10 == 1;
        }

        public BlockingQueue<Integer> getInput() {
            return input;
        }

        public void setInput(BlockingQueue<Integer> input) {
            this.input = input;
        }

        public BlockingQueue<Integer> getOutput() {
            return output;
        }

        public void setOutput(BlockingQueue<Integer> output) {
            this.output = output;
        }

        public int getFinalOutput() {
            return finalOutput;
        }
    }
}
