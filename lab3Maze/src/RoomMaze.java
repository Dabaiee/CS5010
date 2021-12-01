import java.util.List;

public class RoomMaze extends AbstractNonPerfectMaze {
    public RoomMaze(Player player, Location goalLoc, Location playerLoc, int totalRow, int totalCol, int wallNum) {
        super(player, goalLoc, playerLoc, totalRow, totalCol, wallNum);
    }

    @Override
    protected int collapseWalls() {
        return totalRow * totalCol - totalRow - totalCol + 1 - wallNum;
    }

    @Override
    protected boolean isWrapping() {
        return false;
    }
}
