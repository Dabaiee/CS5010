import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Abstract Maze implementation
 */
public abstract class AbstractMaze implements Maze{
  public static final double GOLD_PROBABILITY = 0.2f;
  public static final double THIEF_PROBABILITY = 0.1f;

  protected final Player player;
  protected Location goalLoc;
  protected Location playerLoc;

  protected final int totalRow;
  protected final int totalCol;
  protected final int wallNum;

  protected static final Direction[] DIRECTIONS = new Direction[] {Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};
  protected static final Map<Direction, Direction> OPPOSITE_DIRECTIONS = new HashMap<Direction, Direction>() {{
    put(Direction.SOUTH, Direction.NORTH);
    put(Direction.NORTH, Direction.SOUTH);
    put(Direction.EAST, Direction.WEST);
    put(Direction.WEST, Direction.EAST);
  }};

  protected Location[][] graph;
  protected static final Random RANDOM = new Random();
  /**
   * Constructs the Abstract Maze
   * @param player     player
   * @param goalLoc    goal location of the maze
   * @param playerLoc  player's location
   * @param totalRow   total row of maze
   * @param totalCol   total column of the maze
   * @param wallNum    remaining walls of the maze
   */
  public AbstractMaze(Player player, Location goalLoc, Location playerLoc, int totalRow, int totalCol, int wallNum) throws IllegalArgumentException{
    if (totalRow <=0 || totalCol <=0) {
      throw new IllegalArgumentException("Ops, invalid maze size!");
    }

    if (wallNum > totalCol * totalRow - totalCol - totalRow + 1) {
      throw new IllegalArgumentException("Invalid remaining wall number");
    }

    this.player = player;
    this.goalLoc = goalLoc;
    this.playerLoc = playerLoc;
    this.totalRow = totalRow;
    this.totalCol = totalCol;
    this.wallNum = wallNum;

    start();

  }

  /**
   * start the game.
   */
  private void start(){
    graph = new Location[totalRow][totalCol];
    for (int i = 0; i < totalRow; i++){
      for (int j = 0; j < totalCol; j++){
        graph[i][j] = new Location(i,j);
      }
    }

    generateGoldThief();
    generateMaze();

    //initialize player
    updatePlayer(graph[playerLoc.getLocations()[0]][playerLoc.getLocations()[1]]);
  }

  /**
   * Generate a perfect maze
   */
  protected void generateMaze(){
    int mazeSize = totalRow * totalCol;
    //create union find index list
    int[] cells = new int[mazeSize];
    for (int i = 0; i < mazeSize; i++){
      cells[i] = i;
    }
    int tearDownWalls = 0;
    while (tearDownWalls < mazeSize - 1){
      int cell = RANDOM.nextInt(mazeSize);
      int neighbour = pickNeighbour(cell);
      int p = find(cells,cell);
      int q = find(cells,neighbour);
      if (p!= q) {
        union(cell,neighbour);
        tearDownWalls++;
        cells[p] = q;
      }
    }
  }

  /**
   * generate Gold Thief
   * Generate 20% of the locations have gold coins and 10% of locations have a thief Randomly.
   */
  protected void generateGoldThief(){
    int totalCells = totalRow * totalCol;
    int goldCells = (int) Math.ceil(GOLD_PROBABILITY * totalCells);
    int thiefCells = (int) Math.ceil(THIEF_PROBABILITY * totalCells);
    Set<Integer> created = new HashSet<>();
    while(goldCells > 0){
      int index = RANDOM.nextInt(totalCells);
      if (created.contains(index)){
        continue;
      }
      int[] coordinates = parseCellIndexToLoc(index);
      graph[coordinates[0]][coordinates[1]].setGold();
      created.add(index);
      System.out.println(String.format("gold in %s%s", coordinates[0],coordinates[1]));
      goldCells--;
    }
    while(thiefCells > 0){
      int thiefIndex = RANDOM.nextInt(totalCells);
      if (created.contains(thiefIndex)){
        continue;
      }
      int[] coordinates = parseCellIndexToLoc(thiefIndex);
      graph[coordinates[0]][coordinates[1]].setThief();
      System.out.println(String.format("thief in %s%s", coordinates[0],coordinates[1]));
      created.add(thiefIndex);
      thiefCells--;
    }
  }

  protected int[] parseCellIndexToLoc(int cellIndex){
    return new int[] {cellIndex / totalCol, cellIndex % totalCol};
  }

  /**
   * Randomly pick a neighbour
   * @param index
   * @return
   */

  private int pickNeighbour(int index) {
    int[] curLoc = {index / totalCol, index % totalCol};
    int[] neighbors = new int[] {0, 1, 0, -1, 0};
    boolean validNeighbor = false;
    int[] neighbor = new int[2];
    while (!validNeighbor) {
      int neighborIndex = RANDOM.nextInt(neighbors.length - 1);
      neighbor = new int[] {curLoc[0] + neighbors[neighborIndex],
              curLoc[1] + neighbors[neighborIndex + 1]};
      if (neighbor[0] >= 0 && neighbor[0] < totalRow && neighbor[1] >= 0 && neighbor[1] < totalCol){
        validNeighbor = true;
      }else{
        validNeighbor = false;
      }
    }
    return totalCol * neighbor[0] + neighbor[1];
  }

  /**
   * Union Find Algorithms
   * @param cells
   * @param i
   * @return
   */


  private int find(int[] cells, int i) {
    while (i != cells[i]) {
      cells[i] = cells[cells[i]];   //compression
      i = cells[i];
    }
    return i;
  }

  private void union(int index1, int index2) {
    int[] loc1 = {index1 / totalCol, index1 % totalCol};
    int[] loc2 = {index2 / totalCol, index2 % totalCol};
    Location cell1 = graph[loc1[0]][loc1[1]];
    Location cell2 = graph[loc2[0]][loc2[1]];

    if (loc1[0] == loc2[0]) {
      cell1.addEdge(loc1[1] - loc2[1] == 1 ? Direction.WEST : Direction.EAST, cell2);
      cell2.addEdge(loc1[1] - loc2[1] == 1 ? Direction.EAST : Direction.WEST, cell1);
    } else {
      cell1.addEdge(loc1[0] - loc2[0] == 1 ? Direction.NORTH : Direction.SOUTH, cell2);
      cell2.addEdge(loc1[0] - loc2[0] == 1 ? Direction.SOUTH : Direction.NORTH, cell1);
    }
  }

  /**
   * Find the current location's neighbour including wrapping condition.
   * @param location current location
   * @param direction  next direction
   * @param wrapping
   * @return   neighbour location
   */

  protected Location neighborLoc(Location location, Direction direction, boolean wrapping) {
    int[] coordinates = location.getLocations();
    int neighborRow = coordinates[0];
    int neighborCol = coordinates[1];
    switch (direction) {
      case NORTH:
        neighborRow--;
        break;
      case SOUTH:
        neighborRow++;
        break;
      case EAST:
        neighborCol++;
        break;
      case WEST:
        neighborCol--;
        break;
      default:
    }
    if (wrapping) {
      if (neighborRow < 0) {
        neighborRow = totalRow - 1;
      } else if (neighborRow >= totalRow) {
        neighborRow = 0;
      } else if (neighborCol < 0) {
        neighborCol = totalCol - 1;
      } else if (neighborCol >= totalCol) {
        neighborCol = 0;
      }
    } else {
      if (neighborRow < 0 || neighborRow >= totalRow || neighborCol < 0 || neighborCol >= totalCol) {
        return null;
      }
    }
    return graph[neighborRow][neighborCol];

  }

  /**
   * update player location
   * @return
   */
  private void updatePlayer(Location location) {
    if (location == null){
      return;
    }
    if (location.getGold() != 0){
      player.pickupCoins(location.getGold());
    }
    if (location.getThief()){
      player.loseCoins();
    }
    player.setLocation(location);
  }



  @Override
  public Location getPlayerLoc() {
    return player.getCurLocation();
  }

  @Override
  public int getGoldCount() {
    return player.getCoins();
  }

  @Override
  public void setPlayer(Player player, Location loc) {
    player.setLocation(loc);
  }

  @Override
  public void setGoal(Location goal) {
    goalLoc = goal;
  }

  @Override
  public List<Direction> getPossibleMoves() {
    return null;
  }

  @Override
  public void move(Direction direction) {
    Location player_cur_location = player.getCurLocation();
    if (player_cur_location.getLocations() == goalLoc.getLocations()){
      throw new IllegalArgumentException(("Reach the Goal"));
    }
    List<Direction> directions = player_cur_location.getDirection();
    System.out.println("directions");
    System.out.println(directions);
    if (directions.contains(direction)){
      Location nextLoc = neighborLoc(player_cur_location,direction,true);
      updatePlayer(nextLoc);
    }else{
      throw new IllegalArgumentException("Invalid direction");
    }
  }

}
