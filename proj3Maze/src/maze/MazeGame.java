package maze;

import java.util.List;
import java.util.Scanner;

public class MazeGame {
    /**
     * args
     * @param row
     * @param col
     * @param startPoint "x,y"
     * @param wumpusLocation "x,y"
     * @param numBats
     * @param numPits
     * @param numArrow
     * @param numWalls
     *
     * */
    public static void main(String[] args) {
        //check valid input
        int row = Integer.parseInt(args[0]);
        int col = Integer.parseInt(args[1]);
        if (row <= 0 || col <= 0){
            System.out.println("Please input valid maze row and column");
            return;
        }
        MazeType mazeType = MazeType.WRAPPING_ROOM_MAZE;
        int[] startPoint = parseLocationInput(args[2]);
        int[] wumpusLocation = parseLocationInput(args[3]);
        int numBats = Integer.parseInt(args[4]);
        int numPits = Integer.parseInt(args[5]);
        int numArrow = Integer.parseInt(args[6]);
        if (startPoint[0] <0 || startPoint[0] >= row || startPoint[1] < 0 || startPoint[1] >= col){
            System.out.println("Invalid start maze.Location Input. ");
            return;
        }

        int remainingWalls = 0;
        if (mazeType != MazeType.PERFECT_MAZE){
            remainingWalls = Integer.parseInt(args[7]);
        }
        if (remainingWalls > row * col - row - col + 1){
            System.out.println("The remaining walls number is invalid");
            return;
        }

        //Initialize
        Location startLoc = new Location(startPoint[0], startPoint[1]);
        Location goalLoc = new Location(wumpusLocation[0],wumpusLocation[1]);
        Player player = new Players();
        player.setLocation(startLoc);
        Maze maze;
        switch (mazeType){
            case PERFECT_MAZE:
                maze = new PerfectMaze(player,goalLoc,startLoc,row,col, numBats, numPits, numArrow);
                break;
            case ROOM_MAZE:
                maze = new RoomMaze(player,goalLoc,startLoc,row,col,remainingWalls, numBats, numPits, numArrow);
                break;
            case WRAPPING_ROOM_MAZE:
                maze = new WrappingRoomMaze(player,goalLoc,startLoc,row,col,remainingWalls, numBats, numPits, numArrow);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mazeType);
        }
        System.out.println(String.format(
                "Let's start the maze game with %d x %d size with starting point [%d, %d], bats number %d, and pits number %d",
                row, col, startPoint[0], startPoint[1], numBats, numPits));
        //print maze
        Controller controller = new Controller(maze, player);

    }

    private static int[] parseLocationInput(String locationString) {
        String[] coordinates = locationString.split(",");
        int[] location = new int[2];
        location[0] = Integer.parseInt(coordinates[0].trim());
        location[1] = Integer.parseInt(coordinates[1].trim());
        return location;
    }

    private static boolean reachGoalLocation(Player player, int[] goalLocation) {
        return player.getCurLocation().getLocations()[0] == goalLocation[0] && player.getCurLocation().getLocations()[1] == goalLocation[1];
    }
}
