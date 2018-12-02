package ee.aoc;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class InputFileReaderUtil {

  public static List<String> getLinesFromClassPathResourceFile(String filename) {
    try {
      return Files.readAllLines(getPathForClassPathResource(filename));
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static Stream<String> getLinesStreamFromClassPathResourceFile(String filename) {
    try {
      return Files.lines(getPathForClassPathResource(filename), StandardCharsets.UTF_8);
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static Path getPathForClassPathResource(String filename) {
    try {
      return Paths.get(InputFileReaderUtil.class.getResource(filename).toURI());
    }
    catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
