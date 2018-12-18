package ee.aoc.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Day16 {
  
  
  private static final Pattern NUMBERS_PATTERN = Pattern.compile("(\\d+),? (\\d+),? (\\d+),? (\\d+)");
  
  public static void main(String[] args) {
    doWork(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day16/input-part1.txt"));
  }
  
  private static void doWork(List<String> input) {
    int part2Index = 0;
    List<InstructionOperation> inputList = new ArrayList<>();
    for (int i = 0; i < input.size(); i += 4) {
      
      if (input.get(i + 1).equals("")) {
        part2Index = i + 2;
        break;
      }
      InstructionOperation instructionOperation = new InstructionOperation(intArrayFromStringInput(input.get(i)), intArrayFromStringInput(input.get(i + 1)),
          intArrayFromStringInput(input.get(i + 2)));
      inputList.add(instructionOperation);
    }


    Map<Integer, Set<String>> opCodeMapping = new HashMap<>();
    
    
    for (InstructionOperation instructionOperation : inputList) {
      
      Set<String> matchingOpCodesList = new HashSet<>();
      for (String opCode : INSTRUCTIONS) {
        Instruction instruction = new Instruction(Arrays.copyOf(instructionOperation.beforeRegister, 4));
        int[] instructionInput = instructionOperation.instructionInput;
        instruction.applyInstruction(opCode, instructionInput[1], instructionInput[2], instructionInput[3]);
        int[] outputRegister = instruction.getRegisters();
        if (Arrays.equals(outputRegister, instructionOperation.afterRegister)) {
          matchingOpCodesList.add(opCode);
        }
      }

      int opcodeNumber = instructionOperation.instructionInput[0];
      Set<String> potentialInstructions = opCodeMapping.get(opcodeNumber);
      if (potentialInstructions != null) {
        potentialInstructions.retainAll(matchingOpCodesList);
      } else {
        opCodeMapping.put(opcodeNumber, matchingOpCodesList);
      }

    }

    while (!opCodeMapping.values().stream().allMatch(x -> x.size() == 1)) {
      for (Map.Entry<Integer, Set<String>> opCodeEntry : opCodeMapping.entrySet()) {
        Set<String> entryValue = opCodeEntry.getValue();
        if (entryValue.size() > 1) {
          for (Map.Entry<Integer, Set<String>> opCodeEntryInner : opCodeMapping.entrySet()) {
            Set<String> innerEntryValue = opCodeEntryInner.getValue();
            if (innerEntryValue.size() == 1 && !opCodeEntryInner.getKey().equals(opCodeEntry.getKey())) {
              entryValue.removeAll(innerEntryValue);
            }
          }
        }
      }
    }

    List<int[]> instructionList = input.subList(part2Index, input.size()).stream()
        .map(Day16::intArrayFromStringInput).collect(Collectors.toList());
    
    int[] initialRegister = new int[] {0,0,0,0};
    for (int[] ints : instructionList) {
      Instruction instruction = new Instruction(initialRegister);
      instruction.applyInstruction(opCodeMapping.get(ints[0]).iterator().next(), ints[1], ints[2], ints[3]);
      initialRegister = instruction.getRegisters();
    }

    System.out.println(initialRegister[0]);
    

  }
  
  private static int[] intArrayFromStringInput(String input) {
    Matcher matcher = NUMBERS_PATTERN.matcher(input);
    if (matcher.find()) {
      return new int[] {Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)), 
          Integer.valueOf(matcher.group(3)), Integer.valueOf(matcher.group(4))};
    } else {
      throw new RuntimeException("Garbage input!");
    }
  }
  
  static class InstructionOperation {
    int[] beforeRegister;
    int[] instructionInput;
    int[] afterRegister;

    public InstructionOperation(int[] beforeRegister, int[] instructionInput, int[] afterRegister) {
      this.beforeRegister = beforeRegister;
      this.instructionInput = instructionInput;
      this.afterRegister = afterRegister;
    }
  }

  private static final Set<String> INSTRUCTIONS = Set.of("addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori", "setr",
      "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr");
  
  
  static class Instruction {

    private final int[] registers;

    public Instruction(int[] registers) {
      this.registers = registers;
    }

    public void applyInstruction(String opCode, int a, int b, int c) {
      switch (opCode) {
        case "addr": registers[c] = registers[a] + registers[b]; break;
        case "addi": registers[c] = registers[a] + b; break;
        case "mulr": registers[c] = registers[a] * registers[b]; break;
        case "muli": registers[c] = registers[a] * b; break;
        case "banr": registers[c] = registers[a] & registers[b]; break;
        case "bani": registers[c] = registers[a] & b; break;
        case "borr": registers[c] = registers[a] | registers[b]; break;
        case "bori": registers[c] = registers[a] | b; break;
        case "setr": registers[c] = registers[a]; break;
        case "seti": registers[c] = a; break;
        case "gtir": registers[c] = a > registers[b] ? 1 : 0; break;
        case "gtri": registers[c] = registers[a] > b ? 1 : 0; break;
        case "gtrr": registers[c] = registers[a] > registers[b] ? 1 : 0; break;
        case "eqir": registers[c] = a == registers[b] ? 1 : 0; break;
        case "eqri": registers[c] = registers[a] == b ? 1 : 0; break;
        case "eqrr": registers[c] = registers[a] == registers[b] ? 1 : 0; break;
        default: throw new RuntimeException("Unknown opcode!");
      }
    }

    public int[] getRegisters() {
      return registers;
    }
  }
}
