package maze;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private int numBats;
    private int numPits;
    private int numArrow;
    private Maze maze_test;
    @Before
    public void setup(){
        player = new Players();
        goalLoc = new Location(2,2);
        playerLoc = new Location(0,0);
        rows = 4;
        cols = 4;
        walls = 0;
        numBats = 2;
        numPits = 2;
        numArrow = 4;
        maze = new PerfectMaze(player, goalLoc, playerLoc,rows,cols, numBats, numPits, numArrow);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMazeRowCol() {

        maze_test = new WrappingRoomMaze(player,goalLoc,playerLoc, -1,-1,3, numBats, numPits, numArrow);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWalls() {
        maze_test = new WrappingRoomMaze(player,goalLoc,playerLoc, rows,cols,15, numBats, numPits, numArrow);
    }

    @Test
    public void testBatPitNum(){
        maze_test = new WrappingRoomMaze(player,goalLoc,playerLoc, rows,cols,6, numBats, numPits, numArrow);
        assertEquals(numBats, maze_test.getNumBats());
        assertEquals(numPits, maze_test.getNumPits());
    }

    @Test
    public void testMove(){
        maze_test = new WrappingRoomMaze(player,goalLoc,playerLoc, rows,cols,6, numBats, numPits, numArrow);
        List<Direction> directions= maze_test.getPossibleMoves();
        for (Direction d: directions){
            if (directions.size()> 0){
                maze_test.move(directions.get(0));
                assertNotEquals(playerLoc, maze_test.getPlayerLoc());
            }
        }
    }

    @Test
    public void testShoot(){
        maze_test = new WrappingRoomMaze(player,goalLoc,playerLoc, rows,cols,6, numBats, numPits, numArrow);
        List<Direction> directions= maze_test.getPossibleMoves();
        for (Direction d: directions){
            if (directions.size()> 0){
                maze_test.shoot(directions.get(0), 1);
                assertNotEquals(numArrow, maze_test.getNumArrow());
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testWin(){
        maze_test = new WrappingRoomMaze(player,goalLoc,playerLoc, rows,cols,6, numBats, numPits, numArrow);
        maze_test.setPlayer(player , new Location(2, 1));
        maze_test.shoot(Direction.EAST, 1);
    }

}
