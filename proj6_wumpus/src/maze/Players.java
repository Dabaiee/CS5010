package maze;

import java.util.Random;

public class Players implements Player{
    private int arrows;
    private Location curLoc;
    private boolean isLive = true;

    @Override
    public void setArrow(int numArrow) {
        this.arrows = numArrow;
    }

    @Override
    public int getArrow() {
        return this.arrows;
    }

    @Override
    public int useArrow() {
        this.arrows-= 1;
        return this.arrows;
    }

    @Override
    public Location getCurLocation() {
        return this.curLoc;
    }

    @Override
    public void setLocation(Location location) {
        this.curLoc = location;
    }

    @Override
    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    @Override
    public boolean getLive() {
        return isLive;
    }

}
