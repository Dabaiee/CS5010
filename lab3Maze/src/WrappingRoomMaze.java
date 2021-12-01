public class WrappingRoomMaze extends AbstractNonPerfectMaze {

    public WrappingRoomMaze(Player player, Location goalLoc, Location playerLoc, int totalRow, int totalCol, int wallNum) {
        super(player, goalLoc, playerLoc, totalRow, totalCol, wallNum);
    }

    @Override
    protected int collapseWalls() {
        return totalRow * totalCol + 1 - wallNum;
    }

    @Override
    protected boolean isWrapping() {
        return true;
    }
}
