import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class testMaze {
    private Maze maze;
    private Player player;
    private Location goalLoc;
    private Location playerLoc;
    private int rows;
    private int cols;
    private int walls;
    private Maze maze_test;
    @Before
    public void setup(){
        player = new Players();
        goalLoc = new Location(2,2);
        playerLoc = new Location(0,0);
        rows = 4;
        cols = 4;
        walls = 0;
        maze = new PerfectMaze(player, goalLoc, playerLoc,rows,cols);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMazeRowCol() {
        maze_test = new PerfectMaze(player,goalLoc,playerLoc, -1,-1);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWalls() {
        maze_test = new RoomMaze(player,goalLoc,playerLoc, rows,cols,15);
    }
}
