public class Players implements Player{
    private int playerGold;
    private Location curLoc;
    static final double COIN_LOSS_RATIO = 0.1f;

    @Override
    public int getCoins() {
        return playerGold;
    }

    @Override
    public void pickupCoins(int coins) {
        playerGold = playerGold + coins;
    }

    @Override
    public void loseCoins() {
        playerGold = (int) (playerGold * (1-COIN_LOSS_RATIO));
    }

    @Override
    public Location getCurLocation() {
        return curLoc;
    }

    @Override
    public void setLocation(Location location) {
        curLoc = location;
    }


}
