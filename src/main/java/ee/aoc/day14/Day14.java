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
    int firstElfIndex = 0;
    int secondElfIndex = 1;
    
    while (state.size() - 10 < lastRecipe) {
      Integer firstElfRecipeScore = state.get(firstElfIndex);
      Integer secondElfRecipeScore = state.get(secondElfIndex);
      var newRecipes = digits(firstElfRecipeScore + secondElfRecipeScore);
      state.addAll(newRecipes);
      
      int firstElfMoves = 1 + state.get(firstElfIndex);
      int secondElfMoves = 1 + state.get(secondElfIndex);
      
      firstElfIndex = (firstElfIndex + firstElfMoves) % state.size();
      secondElfIndex = (secondElfIndex + secondElfMoves) % state.size();
    }
    
    System.out.println(state.subList(lastRecipe, lastRecipe + 10).stream().map(String::valueOf).collect(Collectors.joining("")));
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
