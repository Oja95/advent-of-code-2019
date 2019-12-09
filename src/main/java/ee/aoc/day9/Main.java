package ee.aoc.day9;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Main {
    private static final Map<Integer, Long> EXTRA_MEMORY = new HashMap<>();
    private static int relativePosition = 0;

    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day9.txt");
        partOne(linesFromClassPathResourceFile);
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        List<Long> program = Arrays.stream(linesFromClassPathResourceFile.get(0).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        long input = 2;
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
                memoryWrite(program, getReference(flags, 1, memoryRead(program, programCounter + 1).intValue()), input);
                programCounter += 2;
            } else if (opCode == 4) {
                System.out.println(memoryRead(program, getReference(flags, 1, memoryRead(program, programCounter + 1).intValue())));
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
