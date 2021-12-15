package maze;

import java.util.List;

public interface Maze {
  // "The maze can produce the player's location and gold count"
  Location getPlayerLoc();

  /**
   * Set player at the start location.
   *
   * @param player player.
   * @param loc start location.
   */
  void setPlayer(Player player, Location loc);

  /**
   * Set the goal location.
   *
   * @param goal goal location.
   */
  void setGoal(Location goal);

  /**
   * Get possible directions could move to.
   *
   * @return possible directions.
   */
  List<Direction> getPossibleMoves();

  /**
   * move to the direction.
   *
   * @param direction the direction to move.
   */
  boolean move(Direction direction);

  /**
   * shoot to the direction with distance.
   *
   * @param direction the direction to move.
   * @param  distance the arrow travels.
   */
  boolean shoot(Direction direction , int distance);

  /**
   * test if there is a pit or wumpus nearby
   * */
  void detect();

  /**
   * Getters
   * */
  int getNumBats();
  int getNumPits();
  int getNumArrow();

  // more methods if necessary ...
}
