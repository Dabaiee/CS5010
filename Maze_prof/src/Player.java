// Note that Player is optional, it's okay
// to move the functionalities to maze!
public interface Player {
  static final double COIN_LOSS_RATIO = 0.1f;

  /**
   * Get current coins.
   *
   * @return current coins.
   */
  int getCoins();
  void pickupCoins(int coins);
  void loseCoins();
}
