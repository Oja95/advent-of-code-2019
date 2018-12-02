package ee.aoc.day2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import ee.aoc.InputFileReaderUtil;

public class Part1 {

  public static void main(String[] args) {
    System.out.println(getChecksum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day2/input.txt")));
  }

  static int getChecksum(List<String> idList) {
    var twiceCount = 0;
    var thriceCount = 0;
    var characterOccurrencesList = idList.stream().map(Part1::getCharacterOccurrencesSet).collect(Collectors.toUnmodifiableList());

    var twiceOccurred = false;
    var thriceOccurred = false;

    for (Multiset<Character> characterOccurrences : characterOccurrencesList) {
      Set<Character> characters = characterOccurrences.elementSet();
      for (Character character : characters) {
        int characterCount = characterOccurrences.count(character);
        if (characterCount == 2 && !twiceOccurred) {
          twiceCount++;
          twiceOccurred = true;
        }

        if (characterCount == 3 && !thriceOccurred) {
          thriceCount++;
          thriceOccurred = true;
        }
      }

      twiceOccurred = false;
      thriceOccurred = false;
    }

    return twiceCount * thriceCount;
  }

  private static Multiset<Character> getCharacterOccurrencesSet(String inputString) {
    HashMultiset<Character> characterOccurrences = HashMultiset.create();
    inputString.chars().forEach(character -> characterOccurrences.add((char) character));
    return characterOccurrences;
  }
}
