package maze;

import java.util.List;
import java.util.Scanner;

public class Controller {
    public Controller (Maze maze , Player player){
        run(maze , player);
    }

    private static void run(Maze maze, Player player) {
        Scanner scanner = new Scanner(System.in);
        Location currentCell = maze.getPlayerLoc();
        boolean gameStill = true;
        System.out.println("player starts");

        while (gameStill) {
            maze.detect();
            System.out.println(String.format("player's position: %s %s",player.getCurLocation().getLocations()[0], player.getCurLocation().getLocations()[1]));
            System.out.println(String.format("player's arrows: %d", player.getArrow()));

            List<Direction> availableDirections = currentCell.getDirection();
            System.out.print("Next available directions are [");
            for (int i = 0; i < availableDirections.size(); i++) {
                if (i != 0) {
                    System.out.print(" ");
                }
                System.out.print(String.format("(%d)%s", i, availableDirections.get(i)));
            }
            System.out.println("].");
            System.out.print("Shoot or Move: (0)S (1)M ?");
            int selectedActionIndex = scanner.nextInt();
            while (selectedActionIndex != 1 && selectedActionIndex!= 0){
                selectedActionIndex = scanner.nextInt();
                System.out.print(String.format("Invalid input,Shoot or Move: (0)S (1)M ?"));
            }
            System.out.print("Please select next direction by index:");
            int selectedDirectionIndex = scanner.nextInt();
            while (selectedDirectionIndex < 0 || selectedDirectionIndex >= availableDirections.size()) {
                System.out.print(String.format("Invalid put, please select next direction by index:"));
                selectedDirectionIndex = scanner.nextInt();
            }
            //Move action
            if (selectedActionIndex == 1){
                gameStill = maze.move(availableDirections.get(selectedDirectionIndex));
                currentCell = maze.getPlayerLoc();
            } else if(selectedActionIndex == 0) {

            //Shoot action
                System.out.print("Please input shoot distance:");
                int shootDistance = scanner.nextInt();
                while (!(shootDistance > 0)){
                    System.out.print(String.format("Invalid input, please select next direction by index:"));
                    shootDistance = scanner.nextInt();
                }
                gameStill = maze.shoot(availableDirections.get(selectedDirectionIndex), shootDistance);
            }

        }
        if (!player.getLive()){
            System.out.println(String.format("Oops, player lose"));
        }

    }

}
