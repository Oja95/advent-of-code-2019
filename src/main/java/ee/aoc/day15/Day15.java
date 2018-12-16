package ee.aoc.day15;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

@SuppressWarnings("Duplicates")
public class Day15 {

  public static void main(String[] args) {
    getCollisionCoords(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day15/test-input.txt"));
  }

  private static void getCollisionCoords(List<String> linesFromClassPathResourceFile) {
    MatrixElement[][] initialMatrix = new MatrixElement[9][9];
    for (int i = 0; i < linesFromClassPathResourceFile.size(); i++) {
      char[] chars = linesFromClassPathResourceFile.get(i).toCharArray();
      for (int j = 0; j < chars.length; j++) {
        char element = chars[j];
        if (element == '#') {
          initialMatrix[j][i] = new Wall();
        } else if (element == '.') {
          initialMatrix[j][i] = new Cavern();
        } else if (element == 'E') {
          initialMatrix[j][i] = new Elf();
        } else if (element == 'G') {
          initialMatrix[j][i] = new Goblin();
        } else throw new RuntimeException("Garbage input!");
      }
    }


    MatrixElement[][] matrix;
    int cycles;
    int newElfAtkpower = 3;
    while (true) {
      matrix = cloneMatrix(initialMatrix, newElfAtkpower++);

      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          if (matrix[j][i] instanceof Elf) {
            ((Elf) matrix[j][i]).attackPower++;
          }
        }
      }
      
      int initialElfAmount = getElfAmount(matrix);
      FightSim fightSim = new FightSim(matrix).invoke();
      matrix = fightSim.getMatrix();
      cycles = fightSim.getCycles();

      int hpSum = 0;
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          if (matrix[j][i] instanceof Mob) {
            hpSum += ((Mob) matrix[j][i]).health;
          }
        }
      }
      
      if (getElfAmount(matrix) == initialElfAmount) {
        System.out.println(newElfAtkpower);
        System.out.println(cycles);
        System.out.println(hpSum);
        System.out.println(cycles * hpSum);
        break;
      }
    }
  }


  private static MatrixElement[][] cloneMatrix(MatrixElement[][] cloneableMatrix, int elfAtkPower) {
    MatrixElement[][] newMatrix = new MatrixElement[cloneableMatrix.length][cloneableMatrix[0].length];
    for (int i = 0; i < cloneableMatrix.length; i++) {
      for (int j = 0; j < cloneableMatrix[0].length; j++) {
        newMatrix[j][i] = cloneableMatrix[j][i];

        if (newMatrix[j][i] instanceof Mob) {
          ((Mob) newMatrix[j][i]).health = 200;
        }
        if (newMatrix[j][i] instanceof Elf) {
          ((Elf) newMatrix[j][i]).attackPower = elfAtkPower;
        }
      }
    }
    
    return newMatrix;
  }
  
  private static Coords canMobAttack(Mob mob, MatrixElement[][] matrix, Coords mobCoords) {
    MatrixElement upElement = matrix[mobCoords.x][mobCoords.y - 1];
    MatrixElement downElement = matrix[mobCoords.x][mobCoords.y + 1];
    MatrixElement leftElement = matrix[mobCoords.x - 1][mobCoords.y];
    MatrixElement rightElement = matrix[mobCoords.x + 1][mobCoords.y];
    Map<MatrixElement, Coords> mobToCoord = Map.of(upElement, Coords.of(mobCoords.x, mobCoords.y - 1), downElement, Coords.of(mobCoords.x, mobCoords.y + 1),
        leftElement, Coords.of(mobCoords.x - 1, mobCoords.y), rightElement, Coords.of(mobCoords.x + 1, mobCoords.y));

    Map<MatrixElement, Coords> targetsMap = mobToCoord.entrySet().stream()
        .filter(x -> x.getKey() instanceof Mob)
        .filter(x -> ((Mob) x.getKey()).element == mob.getOpponentElement())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    
    if (targetsMap.isEmpty()) return null;
    
    int lowestHp = Integer.MAX_VALUE;
    Coords target = null;

    for (Map.Entry<MatrixElement, Coords> matrixElementCoordsEntry : targetsMap.entrySet()) {
      if (((Mob) matrixElementCoordsEntry.getKey()).health < lowestHp) {
        lowestHp = ((Mob) matrixElementCoordsEntry.getKey()).health;
        target = matrixElementCoordsEntry.getValue();      
      }
    }
    
    return target;
  }
  
  private static int getElfAmount(MatrixElement[][] matrix) {
    int count = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[j][i] instanceof Elf) count++;
      }
    }
    return count;
  }
  
  private static Coords getStepTarget(MatrixElement[][] matrix, Coords originCoords) {
    
    Coords targetCoords = getEarliestOccurringCoordinate(
        findClosestReachableCoordsForAttackingEnemyTarget(matrix, (Mob) matrix[originCoords.x][originCoords.y], originCoords)
    );

    List<Coords> potentialSteps = new ArrayList<>();

    MatrixElement topPos = matrix[originCoords.x][originCoords.y - 1];
    if (topPos.canMoveInto()) {
      potentialSteps.add(Coords.of(originCoords.x, originCoords.y -1));
    }

    MatrixElement upPos = matrix[originCoords.x][originCoords.y + 1];
    if (upPos.canMoveInto()) {
      potentialSteps.add(Coords.of(originCoords.x, originCoords.y + 1));
    }

    MatrixElement leftPos = matrix[originCoords.x - 1][originCoords.y];
    if (leftPos.canMoveInto()) {
      potentialSteps.add(Coords.of(originCoords.x - 1, originCoords.y));
    }

    MatrixElement rightPos = matrix[originCoords.x + 1][originCoords.y];
    if (rightPos.canMoveInto()) {
      potentialSteps.add(Coords.of(originCoords.x + 1, originCoords.y));
    }

    Map<Integer, List<Coords>> distanceMap = new HashMap<>();
    for (Coords potentialStep : potentialSteps) {
      int coordinateDistance = shortestDistanceFromCoordtoCoord(matrix, potentialStep, targetCoords);
      if (coordinateDistance == Integer.MAX_VALUE) continue; // no path
      List<Coords> coords = distanceMap.computeIfAbsent(coordinateDistance, x -> new ArrayList<>());
      coords.add(potentialStep);
    }
    return getEarliestOccurringCoordinate(distanceMap.get(distanceMap.keySet().stream().mapToInt(x -> x).min().getAsInt()));
  }

  private static boolean movesDownOrRight(Coords origin, Coords target) {
    return target.y - origin.y > 0 || target.x - origin.x > 0; 
  }
  
  private static Coords getEarliestOccurringCoordinate(List<Coords> nearestEnemyPositions) {
    int lowestX = Integer.MAX_VALUE;
    int lowestY = Integer.MAX_VALUE;
    Coords bestCoort = null;
    for (Coords nearestEnemyPosition : nearestEnemyPositions) {
      if (nearestEnemyPosition.y < lowestY) {
        bestCoort = nearestEnemyPosition;
        lowestX = nearestEnemyPosition.x;
        lowestY = nearestEnemyPosition.y;
        continue;
      }
      
      if (nearestEnemyPosition.y == lowestY) {
        if (nearestEnemyPosition.x < lowestX) {
          bestCoort = nearestEnemyPosition;
          lowestX = nearestEnemyPosition.x;
          lowestY = nearestEnemyPosition.y;
        }
      }
    }
    
//    if (bestCoort == null) {
//      throw new RuntimeException("No target!");
//    }
    return bestCoort;
  }
  
  // breadth first search for finding distances of closest attacking points
  private static List<Coords> findClosestReachableCoordsForAttackingEnemyTarget(MatrixElement[][] matrix, Mob mob, Coords mobPosition) {
    // distance -> enemyCoords
    Map<Integer, List<Coords>> enemyDistances = new HashMap<>();
    char enemy = mob.getOpponentElement();
    
    boolean[][] visited = new boolean[matrix.length][matrix[0].length];
    Queue<QueueItem> queue = new ArrayDeque<>();
    queue.add(new QueueItem(mobPosition, 0));
    visited[mobPosition.x][mobPosition.y] = true;
    while (!queue.isEmpty()) {
      QueueItem observable = queue.poll();
      Coords observableCoords = observable.coords;
      
      MatrixElement upElement = matrix[observableCoords.x][observableCoords.y - 1];
      MatrixElement downElement = matrix[observableCoords.x][observableCoords.y + 1];
      MatrixElement leftElement = matrix[observableCoords.x - 1][observableCoords.y];
      MatrixElement rightElement = matrix[observableCoords.x + 1][observableCoords.y];
      
      if (containsEnemy(List.of(upElement, downElement, leftElement, rightElement), enemy)) {
        enemyDistances.computeIfAbsent(observable.distance, integer -> new ArrayList<>()).add(observableCoords);
      }

      // up
      if (!visited[observableCoords.x][observableCoords.y- 1] && upElement.canMoveInto()) {
        visited[observableCoords.x][observableCoords.y- 1] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x, observableCoords.y - 1), observable.distance + 1));
      }

      // down
      if (!visited[observableCoords.x][observableCoords.y + 1] && downElement.canMoveInto()) {
        visited[observableCoords.x][observableCoords.y + 1] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x, observableCoords.y + 1), observable.distance + 1));
      }

      // left
      if (!visited[observableCoords.x - 1][observableCoords.y] && leftElement.canMoveInto()) {
        visited[observableCoords.x - 1][observableCoords.y] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x - 1, observableCoords.y), observable.distance + 1));
      }

      // right
      if (!visited[observableCoords.x + 1][observableCoords.y] && rightElement.canMoveInto()) {
        visited[observableCoords.x + 1][observableCoords.y] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x + 1, observableCoords.y), observable.distance + 1));
      }
      
    }
    
    if (enemyDistances.keySet().isEmpty()) return Collections.emptyList();
    int asInt = enemyDistances.keySet().stream().mapToInt(x -> x).min().getAsInt();
    return enemyDistances.get(asInt);
  }
  
  // another bfs 
  private static int shortestDistanceFromCoordtoCoord(MatrixElement[][] matrix, Coords origin, Coords target) {
    boolean[][] visited = new boolean[matrix.length][matrix[0].length];
    Queue<QueueItem> queue = new ArrayDeque<>();
    queue.add(new QueueItem(origin, 0));
    visited[origin.x][origin.y] = true;
    int lowestDistance = Integer.MAX_VALUE;
    while (!queue.isEmpty()) {
      QueueItem observable = queue.poll();
      Coords observableCoords = observable.coords;
      
      if (observableCoords.equals(target) && lowestDistance > observable.distance) {
          lowestDistance = observable.distance;
      }
      
      // up
      MatrixElement up = matrix[observableCoords.x][observableCoords.y - 1];
      if (!visited[observableCoords.x][observableCoords.y - 1] && up.canMoveInto()) {
        visited[observableCoords.x][observableCoords.y - 1] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x, observableCoords.y - 1), observable.distance + 1));
      }

      // down
      MatrixElement down = matrix[observableCoords.x][observableCoords.y + 1];
      if (!visited[observableCoords.x][observableCoords.y + 1] && down.canMoveInto()) {
        visited[observableCoords.x][observableCoords.y + 1] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x, observableCoords.y + 1), observable.distance + 1));
      }

      // left
      MatrixElement left = matrix[observableCoords.x - 1][observableCoords.y];
      if (!visited[observableCoords.x - 1][observableCoords.y] && left.canMoveInto()) {
        visited[observableCoords.x - 1][observableCoords.y] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x - 1, observableCoords.y), observable.distance + 1));
      }

      // right
      MatrixElement right = matrix[observableCoords.x + 1][observableCoords.y];
      if (!visited[observableCoords.x + 1][observableCoords.y] && right.canMoveInto()) {
        visited[observableCoords.x + 1][observableCoords.y] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x + 1, observableCoords.y), observable.distance + 1));
      }
    }
    
    return lowestDistance;
  }

  // and another other wtf
  private static boolean existsClearPathToTarget(MatrixElement[][] matrix, Coords origin, Coords target) {
    if (target == null) return false;
    boolean[][] visited = new boolean[matrix.length][matrix[0].length];
    Queue<QueueItem> queue = new ArrayDeque<>();
    queue.add(new QueueItem(origin, 0));
    visited[origin.x][origin.y] = true;
    while (!queue.isEmpty()) {
      QueueItem observable = queue.poll();
      Coords observableCoords = observable.coords;

      List<Coords> adjacentCoords = List.of(Coords.of(observableCoords.x, observableCoords.y - 1), Coords.of(observableCoords.x, observableCoords.y + 1),
          Coords.of(observableCoords.x - 1, observableCoords.y), Coords.of(observableCoords.x + 1, observableCoords.y));
      if (adjacentCoords.stream().anyMatch(x -> x.equals(target))) return true;

      // up
      if (!visited[observableCoords.x][observableCoords.y - 1] && matrix[observableCoords.x][observableCoords.y - 1].canMoveInto()) {
        visited[observableCoords.x][observableCoords.y - 1] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x, observableCoords.y - 1), observable.distance + 1));
      }

      // down
      if (!visited[observableCoords.x][observableCoords.y + 1] && matrix[observableCoords.x][observableCoords.y + 1].canMoveInto()) {
        visited[observableCoords.x][observableCoords.y + 1] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x, observableCoords.y + 1), observable.distance + 1));
      }

      // left
      if (!visited[observableCoords.x - 1][observableCoords.y] && matrix[observableCoords.x - 1][observableCoords.y].canMoveInto()) {
        visited[observableCoords.x - 1][observableCoords.y] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x - 1, observableCoords.y), observable.distance + 1));
      }

      // right
      if (!visited[observableCoords.x + 1][observableCoords.y] && matrix[observableCoords.x + 1][observableCoords.y].canMoveInto()) {
        visited[observableCoords.x + 1][observableCoords.y] = true;
        queue.add(QueueItem.of(Coords.of(observableCoords.x + 1, observableCoords.y), observable.distance + 1));
      }
    }
    
    return false;
  }
  
  private static boolean containsEnemy(List<MatrixElement> elements, char enemy) {
    return elements.stream().anyMatch(x -> x.element == enemy);
  }


  static class QueueItem {
    private Coords coords;
    private int distance;

    public QueueItem(Coords coords, int distance) {
      this.coords = coords;
      this.distance = distance;
    }
    
    static QueueItem of(Coords coords, int distance) {
      return new QueueItem(coords, distance);
    }
  }

  static class Coords {
    int x;
    int y;

    Coords(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    static Coords of(int x, int y) {
      return new Coords(x, y);
    }

    @Override
    public String toString() {
      return x + "," + y + ";";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Coords coords = (Coords) o;
      return x == coords.x &&
          y == coords.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }
  
  // Classes representing elements in matrix, duh
  static abstract class MatrixElement {
    char element;
    abstract boolean canMoveInto(); // Can path calculation use its position? 

    @Override
    public String toString() {
      return String.valueOf(element);
    }
  }
  
  static class Wall extends MatrixElement {
    @Override
    protected boolean canMoveInto() {
      return false;
    }

    Wall() {
      this.element = '#';
    }
  }
  static class Cavern extends MatrixElement {
    @Override
    protected boolean canMoveInto() {
      return true;
    }

    Cavern() {
      this.element = '.';
    }
  }

  static abstract class Mob extends MatrixElement {
    protected int health = 200;
    protected boolean hasMovedThisTurn = false;
    protected int attackPower = 3;
    abstract char getOpponentElement();

    @Override
    protected boolean canMoveInto() {
      return false;
    }
  }
  static class Goblin extends Mob {
    Goblin() {
      this.element = 'G';
    }

    @Override
    char getOpponentElement() {
      return 'E';
    }
  }
  static class Elf extends Mob {
    Elf() {
      this.element = 'E';
    }

    @Override
    char getOpponentElement() {
      return 'G';
    }
  }

  private static class FightSim {
    private MatrixElement[][] matrix;
    private int cycles;

    public FightSim(MatrixElement[][] matrix) {
      this.matrix = matrix;
      this.cycles = 0;
    }

    public MatrixElement[][] getMatrix() {
      return matrix;
    }

    public int getCycles() {
      return cycles;
    }

    public FightSim invoke() {
      while (true){
        for (int i = 0; i < 9; i++) {
          for (int j = 0; j < 9; j++) {
            if (matrix[j][i] instanceof Mob) {
              Mob mob = (Mob) matrix[j][i];
              if (mob.hasMovedThisTurn) {
                mob.hasMovedThisTurn = false;
                continue;
              }
              Coords mobCoords = Coords.of(j, i);
              Coords targetMobCoords = canMobAttack(mob, matrix, mobCoords);
              if (targetMobCoords != null) {
                Mob targetMob = (Mob) matrix[targetMobCoords.x][targetMobCoords.y];
                targetMob.health -= mob.attackPower;
                if (targetMob.health <= 0) {
                  matrix[targetMobCoords.x][targetMobCoords.y] = new Cavern();
                }
              } else {
                if (existsClearPathToTarget(matrix, mobCoords,
                    getEarliestOccurringCoordinate(findClosestReachableCoordsForAttackingEnemyTarget(matrix, mob, mobCoords)))) {
                  Coords stepTarget = getStepTarget(matrix, Coords.of(j, i));
                  matrix[stepTarget.x][stepTarget.y] = mob;
                  matrix[j][i] = new Cavern();
                  if (movesDownOrRight(mobCoords, stepTarget)) {
                    ((Mob) matrix[stepTarget.x][stepTarget.y]).hasMovedThisTurn = true;
                  }
  
                  Coords newTargetMobCoords = canMobAttack(mob, matrix, mobCoords);
                  if (newTargetMobCoords != null) {
                    Mob targetMob = (Mob) matrix[newTargetMobCoords.x][newTargetMobCoords.y];
                    targetMob.health -= mob.attackPower;
                    if (targetMob.health <= 0) {
                      matrix[newTargetMobCoords.x][newTargetMobCoords.y] = new Cavern();
                    }
                  }
                }
              }
            }
          }
        }
        
        boolean foundGoblin = false;
        boolean foundElf = false;
        
        for (int i = 0; i < 9; i++) {
          for (int j = 0; j < 9; j++) {
            if (matrix[j][i] instanceof Goblin) {
              foundGoblin = true;
            }
            
            if (matrix[j][i] instanceof Elf) {
              foundElf = true;
            }
          }
        }
        
        if (!foundGoblin || !foundElf) {
          break;
        }
  
        cycles++;
      }
      return this;
    }
  }
}
