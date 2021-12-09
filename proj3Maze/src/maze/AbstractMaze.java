package maze;

import java.util.*;

/**
 * Abstract maze.Maze implementation
 */
public abstract class AbstractMaze implements Maze{
  protected final Player player;
  private final int numBats;
  private final int numPits;
  private final int numArrow;
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

  protected Set<Integer> caves = new HashSet<>();
  protected Location[][] graph;
  protected static final Random RANDOM = new Random();
  /**
   * Constructs the Abstract maze.Maze
   * @param player     player
   * @param goalLoc    goal location of the maze
   * @param playerLoc  player's location
   * @param totalRow   total row of maze
   * @param totalCol   total column of the maze
   * @param wallNum    remaining walls of the maze
   */
  public AbstractMaze(Player player, Location goalLoc, Location playerLoc, int totalRow, int totalCol, int wallNum, int numBats, int numPits, int numArrow) throws IllegalArgumentException{
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
    this.numBats = numBats;
    this.numPits = numPits;
    this.numArrow = numArrow;

    start();

  }
  @Override
  public int getNumBats(){
    return this.numBats;
  }

  @Override
  public int getNumPits(){
    return this.numPits;
  }

  @Override
  public int getNumArrow(){
    return this.numArrow;
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
    player.setArrow(numArrow);
    System.out.println(String.format("Wumpus in %s %s", goalLoc.getLocations()[0],goalLoc.getLocations()[1]));
    generateMaze();
    for (int i = 0; i < totalRow; i++){
      for (int j = 0; j < totalCol; j++){
        if (graph[i][j].getDirection().size()== 2){
          graph[i][j].setCave(false);
        } else{
          graph[i][j].setCave(true);
          this.caves.add(parseLocToCellIndex(graph[i][j].getLocations()));
        }
      }
    }
    generateEvent();
    printMaze();
    getWinnable();
    //initialize player
    updatePlayer(graph[playerLoc.getLocations()[0]][playerLoc.getLocations()[1]]);
  }

  protected boolean getWinnable(){
    return true;
  }

  protected void printMaze(){
    for (int i= 0; i< totalRow; i++){
      String temp = "";
      for (int j=0; j< totalCol; j++){
        String cur;
        int numEdge = graph[i][j].getDirection().size();
        if (i== goalLoc.getLocations()[0] && j== goalLoc.getLocations()[1]){
          temp+= "W";
        }else if(numEdge == 2){
          temp+= "-";
        }else if(graph[i][j].getBats()){
          if (graph[i][j].getPit()){
            temp+= "X";
          }else{
            temp+= "B";
          }
        }else if (graph[i][j].getPit()){
          temp+= "P";
        }else {
          temp+= String.valueOf(numEdge);
        }
      }
      System.out.println(temp);
    }
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
   * generate bats pits
   * Generate 20% of the locations have gold coins and 10% of locations have a thief Randomly.
   */
  protected void generateEvent(){
    int totalCells = totalRow * totalCol;
    int batsCells = numBats;
    int pitCells = numPits;
    Set<Integer> created = new HashSet<>();

    //avoid generate at wampus cell or start cell
    int goalIndex = parseLocToCellIndex(goalLoc.getLocations());
    int startIndex = parseLocToCellIndex(playerLoc.getLocations());
    created.add(goalIndex);
    created.add(startIndex);

    //generate bats
    while(batsCells > 0){
      int index = pickCave();
      if (created.contains(index)){
        continue;
      }
      int[] coordinates = parseCellIndexToLoc(index);
      graph[coordinates[0]][coordinates[1]].setBats();
      created.add(index);
      System.out.println(String.format("bat in %s %s", coordinates[0],coordinates[1]));
      batsCells--;
    }

    //generate pits
    created = new HashSet<>();
    while(pitCells > 0){
      int pitIndex = pickCave();
      if (created.contains(pitIndex)){
        continue;
      }
      int[] coordinates = parseCellIndexToLoc(pitIndex);
      graph[coordinates[0]][coordinates[1]].setPit();
      System.out.println(String.format("pit in %s %s", coordinates[0],coordinates[1]));
      created.add(pitIndex);
      pitCells--;
    }
  }

  protected int[] parseCellIndexToLoc(int cellIndex){
    return new int[] {cellIndex / totalCol, cellIndex % totalCol};
  }

  protected int parseLocToCellIndex(int[] loc){
    return loc[0]* totalCol+ loc[1];
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
    //meet superBats, lookup new location
    if (location.getBats()){
      location = attack(location);
    }
    //meet pit game over
    if (location.getPit()){
      System.out.println("You dropped into a pit");
      player.setLive(false);
      return;
    }
    //meet wumpus game over
    if (location.getLocations()[0] == goalLoc.getLocations()[0] && location.getLocations()[1] == goalLoc.getLocations()[1]){
      System.out.println("You've been defeated by Wumpus");
      player.setLive(false);
      return;
    }
//    test
//    System.out.println("dirct"+ location.getDirection().toString());
    player.setLocation(location);
  }

  private Location attack(Location location) {
    boolean ifAttack = false;
    if (RANDOM.nextBoolean()){
      ifAttack = true;
    }else {
      ifAttack = false;
    }
    if (ifAttack){
      int nxtIndex = pickCave();
      if (nxtIndex== -1){
        throw new IllegalStateException("no available cave");
      }
      int[] nxtXY = parseCellIndexToLoc(nxtIndex);
      System.out.println(String.format("You've been drop by SuperBat to %d %d",nxtXY[0], nxtXY[1] ));
      return graph[nxtXY[0]][nxtXY[1]];
    }else {
      System.out.println("The superBat ignored you");
      return location;
    }
  }

  private int pickCave() {

    int index = RANDOM.nextInt(this.caves.size());
    int i= 0;
    for (int obj : caves){
      if (i== index){
        return obj;
      }
      i++;
    }
    return -1;
  }


  @Override
  public Location getPlayerLoc() {
    return player.getCurLocation();
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
    return playerLoc.getDirection();
  }

  @Override
  public boolean move(Direction direction) {
    System.out.println("dirct is"+ direction.toString());
    Location player_cur_location = this.getPlayerLoc();
    List<Direction> directions = player_cur_location.getDirection();
    if (directions.contains(direction)){
      Location nextLoc = neighborLoc(player_cur_location,direction,true);
      while (!nextLoc.getCave()){
        for (Direction nextDirection: nextLoc.getDirection()){
          if(!(nextDirection.equals(OPPOSITE_DIRECTIONS.get(direction)) )){
            nextLoc = neighborLoc(nextLoc, nextDirection, true);
          }
        }
      }
      updatePlayer(nextLoc);
    }else{
      throw new IllegalArgumentException("Invalid direction");
    }
    return player.getLive();
  }

  @Override
  public void detect() {
    Location player_cur_location = player.getCurLocation();
    for (Direction nextDirection: Direction.values()){
      Location nextLoc =  neighborLoc(player_cur_location, nextDirection, true);
      if (nextLoc.getBats()){
        System.out.println("You feel a draft");
      }
      if (nextLoc.getLocations()[0] == goalLoc.getLocations()[0] && nextLoc.getLocations()[1] == goalLoc.getLocations()[1]){
        System.out.println("You smell a Wumpus!");
      }
    }
  }

  @Override
  public boolean shoot(Direction direction, int distance) {
    Location arrow_cur_location = player.getCurLocation();
    //test
//    System.out.println("arrow start"+ arrow_cur_location.getLocations().toString());
    while (distance> 0){
      Location nextLoc = neighborLoc(arrow_cur_location, direction, true);
      if (nextLoc.getCave()){
        //hit the wumpus
        if (nextLoc.getLocations()[0] == goalLoc.getLocations()[0]
                && nextLoc.getLocations()[1] == goalLoc.getLocations()[1] && distance== 1){
          throw new IllegalStateException(("Player wins, Killed the Wumpus"));
        }
        else{
          //hit the wall
          if (!nextLoc.getDirection().contains(direction)){
            break;
          }
          distance -= 1;
        }
      } else {// shoot into a tunnel
          for (Direction nextDirection: nextLoc.getDirection()){
            if(!(nextDirection.equals(OPPOSITE_DIRECTIONS.get(direction)) )){
              direction = nextDirection;
            }
          }
      }
      arrow_cur_location = nextLoc;
    }
    //use 1 arrow
    int arrow = player.useArrow();
    //EXIT: use all arrow
    if (arrow<= 0){
      System.out.println("You used all the arrows");
      player.setLive(false);
    }
    System.out.println(String.format("Your arrow hit %d %d",
            arrow_cur_location.getLocations()[0], arrow_cur_location.getLocations()[1]));
    return player.getLive();
  }

}
