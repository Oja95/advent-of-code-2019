package ee.aoc.day7;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

import ee.aoc.InputFileReaderUtil;

class Day7Test {

  public static final List<String> LINES_FROM_CLASS_PATH_RESOURCE_FILE = InputFileReaderUtil.getLinesFromClassPathResourceFile("/day7/test-input.txt");

  @Test
  void testTopologicalOrdering() {
    assertEquals("CABDFE", Part1.getTopologicalOrdering(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day7/test-input.txt")));
  }

  @Test
  void testConcurrentTaskExecution() {
    assertEquals(15, Part2.getConurrentExecutionTime(LINES_FROM_CLASS_PATH_RESOURCE_FILE, 0, 2));
  }
}