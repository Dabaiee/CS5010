package maze;

import java.util.List;

public abstract class AbstractNonPerfectMaze extends AbstractMaze{
    private static final int MAX_NEIGHBORS = 4;
    /**
     * Constructs the Abstract maze.Maze
     *
     * @param player    player
     * @param goalLoc   goal location of the maze
     * @param playerLoc player's location
     * @param totalRow  total row of maze
     * @param totalCol  total column of the maze
     * @param wallNum   remaining walls of the maze
     */
    public AbstractNonPerfectMaze(Player player, Location goalLoc, Location playerLoc, int totalRow, int totalCol, int wallNum, int numBats, int numPits, int numArrow) throws IllegalArgumentException {
        super(player, goalLoc, playerLoc, totalRow, totalCol, wallNum, numBats, numPits, numArrow);
    }
    @Override
    public void generateMaze() {
        super.generateMaze();
        int totalCellSize = totalRow * totalCol;
        int needCollapseWalls = collapseWalls();
        int breakDownWalls = 0;
        while (breakDownWalls < needCollapseWalls) {
            int randomCellIndex = RANDOM.nextInt(totalCellSize);
            int[] coordinates = parseCellIndexToLoc(randomCellIndex);
            Location cell = graph[coordinates[0]][coordinates[1]];
            List<Direction> currentDirections = cell.getDirection();
            if (currentDirections.size() < MAX_NEIGHBORS) {
                int index = RANDOM.nextInt(MAX_NEIGHBORS);
                if (currentDirections.isEmpty()
                        || !currentDirections.contains(DIRECTIONS[index])) {
                    // If the index direction is not connected, which means it's a wall.
                    Direction direction = DIRECTIONS[index];
                    Location neighborCell = neighborLoc(cell, direction, isWrapping());
                    if (neighborCell == null) {
                        continue;
                    }
                    cell.addEdge(direction, neighborCell);
                    neighborCell.addEdge(OPPOSITE_DIRECTIONS.get(direction), cell);
                    breakDownWalls++;
                }
            }
        }
    }
    /**
     * Gets the number cells which need to be collapsed.
     *
     * @return the collapse number.
     */
    protected abstract int collapseWalls();

    /**
     * Returns true if the maze is wrapping maze.
     *
     * @return true if the maze is wrapping maze, otherwise false.
     */
    protected abstract boolean isWrapping();
}
