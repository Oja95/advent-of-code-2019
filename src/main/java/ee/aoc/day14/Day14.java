package ee.aoc.day14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {
  public static void main(String[] args) {
    getLastTenRecipes(704321);
  }

  private static void getLastTenRecipes(int lastRecipe) {
    List<Integer> state = new ArrayList<>(List.of(3, 7));
    List<Integer> lastDigits = digits(lastRecipe);
    
    int firstElfIndex = 0;
    int secondElfIndex = 1;
    
    while (true) {
      Integer firstElfRecipeScore = state.get(firstElfIndex);
      Integer secondElfRecipeScore = state.get(secondElfIndex);
      var newRecipes = digits(firstElfRecipeScore + secondElfRecipeScore);
      state.addAll(newRecipes);

      firstElfIndex = (firstElfIndex + 1 + state.get(firstElfIndex)) % state.size();
      secondElfIndex = (secondElfIndex + 1 + state.get(secondElfIndex)) % state.size();

      // two elves can at most create 2 new recipes on each iteration, so check the end for the pattern or one off the end
      if (state.size() <= lastDigits.size()) continue;
      List<Integer> firstPattern = state.subList(state.size() - lastDigits.size(), state.size());
      List<Integer> secondPattern = state.subList(state.size() - 1 - lastDigits.size(), state.size() - 1);
      if (lastDigits.equals(firstPattern)) {
        System.out.println("Part 2: " + (state.size() - lastDigits.size()));
        break;
      } else if (lastDigits.equals(secondPattern)) {
        System.out.println("Part 2: " + (state.size() - 1 - lastDigits.size()));
        break;
      }
    }
    
    // Part 1
    System.out.println("Part 1: " + state.subList(lastRecipe, lastRecipe + 10).stream().map(String::valueOf).collect(Collectors.joining("")));
  }
  
  private static List<Integer> digits(int num) {
    if (num == 0) return List.of(0);
    
    var res = new ArrayList<Integer>();
    while (num != 0) {
      int lastDigit = num % 10;
      res.add(lastDigit);
      num /= 10;
    }
    
    Collections.reverse(res);
    return res;
  }

}
