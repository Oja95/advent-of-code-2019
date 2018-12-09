package ee.aoc.day9;

import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {
  public static void main(String[] args) {
    System.out.println(getWinningElfScore("459 players; last marble is worth 71790 points"));
    System.out.println(getWinningElfScore("459 players; last marble is worth 7179000 points"));
  }

  static int getWinningElfScore(String input) {
    Pattern compile = Pattern.compile("(\\d+).* (\\d+)");
    Matcher matcher = compile.matcher(input);
    if (!matcher.find()) {
      throw new RuntimeException("Garbage input!");
    }

    int playerAmount = Integer.valueOf(matcher.group(1));
    int lastMarbleScore = Integer.valueOf(matcher.group(2));

    int currentPlayer = -1; // first statement in loop will continue with correct player indexing
    ShiftingArrayList<Integer> placedMarbles = new ShiftingArrayList<>();
    placedMarbles.add(0);

    var elfScores = new HashMap<Integer, Integer>();
    int lastPlacedMarbleValue = 0;
    int lastPlacedMarbleIndex = 0;

    while (lastPlacedMarbleValue < lastMarbleScore) {
      currentPlayer = (currentPlayer + 1) % playerAmount;
      int numToPlace = ++lastPlacedMarbleValue;

      if (numToPlace % 23 == 0) {
        elfScores.put(currentPlayer, elfScores.getOrDefault(currentPlayer, 0) + numToPlace);

        lastPlacedMarbleIndex -= 7;
        if (lastPlacedMarbleIndex < 0) {
          lastPlacedMarbleIndex = placedMarbles.size() + lastPlacedMarbleIndex;
        }

        Integer remove = placedMarbles.remove(lastPlacedMarbleIndex);
        elfScores.put(currentPlayer, elfScores.getOrDefault(currentPlayer, 0) + remove);
      } else {
        if ((lastPlacedMarbleIndex + 2) == placedMarbles.size() || placedMarbles.size() == 1) {
          placedMarbles.add(numToPlace);
          lastPlacedMarbleIndex = placedMarbles.size() - 1;
        } else {
          int toBePlacedIndex = (lastPlacedMarbleIndex + 2) % placedMarbles.size();
          placedMarbles = new ShiftingArrayList<>(placedMarbles.addAndShift(numToPlace, toBePlacedIndex));
          lastPlacedMarbleIndex = toBePlacedIndex;
        }
      }
    }

    return Collections.max(elfScores.values());
  }
}
