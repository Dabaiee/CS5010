import java.util.List;
import java.util.Scanner;

public class MazeGame {
    public static void main(String[] args) {
        //check valid input
        int row = Integer.parseInt(args[0]);
        int col = Integer.parseInt(args[1]);
        if (row <= 0 || col <= 0){
            System.out.println("Please input valid maze row and column");
            return;
        }
        MazeType mazeType = MazeType.valueOf(args[2]);
        int[] startPoint = parseLocationInput(args[3]);
        int[] goalPoint = parseLocationInput(args[4]);
        if (startPoint[0] <0 || startPoint[0] >= row || startPoint[1] < 0 || startPoint[1] >= col){
            System.out.println("Invalid start Location Input. ");
            return;
        }
        if (goalPoint[0] <0 || goalPoint[0] >= row || goalPoint[1] < 0 || goalPoint[1] >= col){
            System.out.println("Invalid Goal Location Input. ");
            return;
        }

        int remainingWalls = 0;
        if (mazeType != MazeType.PERFECT_MAZE){
            remainingWalls = Integer.parseInt(args[5]);
        }
        if (remainingWalls > row * col - row - col + 1){
            System.out.println("The remaining walls number is invalid");
            return;
        }

        //Initialize
        Location startLoc = new Location(startPoint[0], startPoint[1]);
        Location goalLoc = new Location(goalPoint[0],goalPoint[1]);
        Player player = new Players();
        player.setLocation(startLoc);
        Maze maze;
        switch (mazeType){
            case PERFECT_MAZE:
                maze = new PerfectMaze(player,goalLoc,startLoc,row,col);
                break;
            case ROOM_MAZE:
                maze = new RoomMaze(player,goalLoc,startLoc,row,col,remainingWalls);
                break;
            case WRAPPING_ROOM_MAZE:
                maze = new WrappingRoomMaze(player,goalLoc,startLoc,row,col,remainingWalls);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mazeType);
        }

        //print maze
        System.out.println(String.format(
                "Let's start the maze game with %d x %d %s with starting point [%d, %d]," +
                        " goal location [%d, %d].",
                row, col, mazeType, startPoint[0], startPoint[1], goalPoint[0], goalPoint[1]));
        Scanner scanner = new Scanner(System.in);

        while (!reachGoalLocation(player, goalPoint)) {
            Location currentCell = player.getCurLocation();

            System.out.println(String.format("player's position: %s %s",player.getCurLocation().getLocations()[0], player.getCurLocation().getLocations()[1]));
            List<Direction> availableDirections = currentCell.getDirection();
            System.out.print("Next available directions are [");
            for (int i = 0; i < availableDirections.size(); i++) {
                if (i != 0) {
                    System.out.print(" ");
                }
                System.out.print(String.format("(%d)%s", i, availableDirections.get(i)));
            }
            System.out.println("].");
            System.out.print("Please select next direction by index:");
            int selectedDirectionIndex = scanner.nextInt();
            while (selectedDirectionIndex < 0 || selectedDirectionIndex >= availableDirections.size()) {
                System.out.print(String.format("Invalid index %d, please select next direction by index:",
                        selectedDirectionIndex));
                selectedDirectionIndex = scanner.nextInt();
            }
            maze.move(availableDirections.get(selectedDirectionIndex));
        }
        System.out.println(String.format("Congratulations! %s", player));
        System.out.println(String.format("Win Gold Coins: %s", player.getCoins()));
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
