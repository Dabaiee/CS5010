import java.util.List;

public interface Maze {
  // "The maze can produce the player's location and gold count"
  Location getPlayerLoc();
  int getGoldCount();

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
  void move(Direction direction);

  // more methods if necessary ...
}
