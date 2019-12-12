package ee.aoc.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import org.apache.commons.lang3.tuple.Pair;

import ee.aoc.InputFileReaderUtil;

public class Main {
    private static final Map<Integer, Long> EXTRA_MEMORY = new HashMap<>();
    private static int relativePosition = 0;

    @SneakyThrows
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day11.txt");
        List<Long> program = Arrays.stream(linesFromClassPathResourceFile.get(0).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Pair<Integer, Integer>> paintedPanels = Collections.synchronizedList(new ArrayList<>());
        List<Pair<Integer, Integer>> whitePanels = Collections.synchronizedList(new ArrayList<>());
        BlockingQueue<Integer> input = new ArrayBlockingQueue<>(10000);

        BlockingQueue<Integer> output = new ArrayBlockingQueue<>(10000);
        Brain brain = new Brain(program, input, output, paintedPanels);
        new Thread(brain, "Brain runnable").start();

        Direction currentDirection = Direction.UP;
        Pair<Integer, Integer> currentPos = Pair.of(0, 0);
//        whitePanels.add(Pair.of(0, 0));
        while (true) {
            input.put(whitePanels.contains(currentPos) ? 1 : 0);


            if (brain.getFinished().get()) {
                break;
            }

            Integer paintColor = output.take();
            Integer turnDirection = output.take();

            paintedPanels.add(currentPos);
            if (paintColor == 1) whitePanels.add(currentPos);
            currentDirection = turnDirection == 0 ? currentDirection.turnLeft() : currentDirection.turnRight();
            currentPos = move(currentPos, currentDirection.getVector());


        }



        System.out.println(new HashSet<>(paintedPanels).size());

        for (int i = -10; i < 40; i++) {
            for (int j = -10; j < 40; j++) {
                if (whitePanels.contains(Pair.of(i, j)))
                    System.out.println('■');
                else
                    System.out.print(' ');
            }
            System.out.println();
        }
    }

    private static Pair<Integer, Integer> move(Pair<Integer, Integer> startPos, Pair<Integer, Integer> vector) {
        return Pair.of(startPos.getLeft() + vector.getLeft(), startPos.getRight() + vector.getRight());
    }

    private enum Direction {
        UP(Pair.of(0, 1)),
        RIGHT(Pair.of(1, 0)),
        DOWN(Pair.of(0, -1)),
        LEFT(Pair.of(-1, 0));

        private final Pair<Integer, Integer> vector;
        Direction(Pair<Integer, Integer> of) {
            vector = of;
        }

        Direction turnLeft() {
            return values()[Math.floorMod((ordinal() - 1), 4)];
        }

        Direction turnRight() {
            return values()[Math.floorMod((ordinal() + 1), 4)];
        }

        public Pair<Integer, Integer> getVector() {
            return vector;
        }
    }

    @AllArgsConstructor
    @Getter
    private static class Brain implements Runnable {

        private List<Long> program;
        private BlockingQueue<Integer> input;
        private BlockingQueue<Integer> output;
        private List<Pair<Integer, Integer>> paintedPanels;

        private final AtomicBoolean finished = new AtomicBoolean(false);


        @SneakyThrows
        @Override
        public void run() {
            int programCounter = 0;

            while (true) {
                long instruction = program.get(programCounter);
                long opCode = instruction % 100;
                long flags = instruction / 100; // flags

                long result;
                if (opCode == 1) {
                    result = applyLongBiFunction(program, programCounter, flags, Long::sum);
                    memoryWrite(program, getReference(flags, 3, memoryRead(program, programCounter + 3).intValue()), result);
                    programCounter += 4;
                } else if (opCode == 2) {
                    result = applyLongBiFunction(program, programCounter, flags, (i, j) -> i * j);
                    memoryWrite(program, getReference(flags, 3, memoryRead(program, programCounter + 3).intValue()), result);
                    programCounter += 4;
                } else if (opCode == 3) {
                    memoryWrite(program, getReference(flags, 1, memoryRead(program, programCounter + 1).intValue()), input.take());
                    programCounter += 2;
                } else if (opCode == 4) {
                    Long parameter = memoryRead(program, programCounter + 1);
                    output.put(posFlag(1, flags) == 1 ? parameter.intValue() : memoryRead(program, getReference(flags, 1, parameter.intValue())).intValue());
                    programCounter += 2;
                } else if (opCode == 5) {
                    Long firstParam = memoryRead(program, programCounter + 1);
                    long firstOperand = getOperand(program, flags, firstParam, 1);
                    if (firstOperand != 0) {
                        Long secondParam = memoryRead(program, programCounter + 2);
                        long secondOperand = getOperand(program, flags, secondParam, 2);
                        programCounter = (int)secondOperand;
                    } else {
                        programCounter += 3;
                    }
                } else if (opCode == 6) {
                    Long firstParam = memoryRead(program, programCounter + 1);
                    long firstOperand = getOperand(program, flags, firstParam, 1);
                    if (firstOperand == 0) {
                        Long secondParam = memoryRead(program, programCounter + 2);
                        long secondOperand = getOperand(program, flags, secondParam, 2);
                        programCounter = (int)secondOperand;
                    } else {
                        programCounter += 3;
                    }
                } else if (opCode == 7) {
                    result = applyLongBiFunction(program, programCounter, flags, (i, j) -> (long)i.compareTo(j));
                    if (result == -1L) {

                        memoryWrite(program, getReference(flags, 3, memoryRead(program, programCounter + 3).intValue()), 1L);
                    } else {
                        memoryWrite(program, getReference(flags, 3, memoryRead(program, programCounter + 3).intValue()), 0L);
                    }
                    programCounter += 4;
                } else if (opCode == 8) {
                    result = applyLongBiFunction(program, programCounter, flags, (i, j) -> (long)i.compareTo(j));
                    if (result == 0L) {
                        memoryWrite(program, getReference(flags, 3, memoryRead(program, programCounter + 3).intValue()), 1L);
                    } else {
                        memoryWrite(program, getReference(flags, 3, memoryRead(program, programCounter + 3).intValue()), 0L);
                    }
                    programCounter += 4;
                } else if (opCode == 9) {
                    Long firstParam = memoryRead(program, programCounter + 1);
                    //                long firstOperand = posFlag(1, flags) == 1 ? firstParam : memoryRead(program, firstParam.intValue());
                    long firstOperand = getOperand(program, flags, firstParam, 1);
                    relativePosition += firstOperand;
                    programCounter += 2;
                } else if (opCode == 99) {
                    finished.set(true);
                    break;
                } else {
                    throw new IllegalStateException("Unexpected program!");
                }
            }
        }

        private static long applyLongBiFunction(List<Long> program, int programCounter, long flags, BiFunction<Long, Long, Long> function) {
            Long firstParam = memoryRead(program, programCounter + 1);
            Long secondParam = memoryRead(program, programCounter + 2);
            Long firstOperand = getOperand(program, flags, firstParam, 1);
            Long secondOperand = getOperand(program, flags, secondParam, 2);
            return function.apply(firstOperand, secondOperand);
        }

        private static Long getOperand(List<Long> program, long flags, Long firstParam, int i) {
            if (posFlag(i, flags) == 1)
                return firstParam;
            if (posFlag(i, flags) == 0)
                return memoryRead(program, firstParam.intValue());
            return memoryRead(program, firstParam.intValue() + relativePosition);
        }

        private static int getReference(long flags, long pos, int value) {
            long flag = posFlag(pos, flags);
            if (flag == 2) {
                return value + relativePosition;
            } else if (flag == 0) {
                return value;
            }
            throw new IllegalStateException("Not allowed!");
        }

        private static long posFlag(long pos, long flags) {
            long count = 1;
            while (count < pos) {
                flags /= 10;
                count++;
            }
            return flags % 10;
        }

        private static void memoryWrite(List<Long> program, int index, long value) {
            if (index >= program.size() || index < 0) {
                EXTRA_MEMORY.put(index, value);
            } else {
                program.set(index, value);
            }
        }

        private static Long memoryRead(List<Long> program, int index) {
            if (index >= program.size() || index < 0) {
                Long aLong = EXTRA_MEMORY.get(index);
                if (aLong == null) {
                    return 0L;
                }
                return aLong;
            } else {
                return program.get(index);
            }
        }
    }
}
