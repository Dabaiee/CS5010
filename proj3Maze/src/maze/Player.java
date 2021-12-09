package maze;

// Note that maze.Player is optional, it's okay
// to move the functionalities to maze!
public interface Player {
  static final double BATS_PICK_RATE = 0.5f;

  /**
   * Get current coins.
   *
   * @return current coins.
   */
  void setArrow(int numArrow);
  int getArrow();
  int useArrow();
  Location getCurLocation();
  void setLocation(Location location);

  void setLive(boolean isLive);
  boolean getLive();
}
