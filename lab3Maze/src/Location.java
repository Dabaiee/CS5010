import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
  private int row;
  private int col;
  private final int[] locations;
  private final Map<Direction, Location> edges = new HashMap<>();
  private int gold;
  private boolean thief;

  public Location(int row, int col) {
    this.row = row;
    this.col = col;
    locations = new int[]{row,col};
    thief = false;
    gold = 0;
  }

  /**
   * Get location
   * @return
   */

  public int[] getLocations(){
    return this.locations;
  }

  /**
   * Add edge with another location
   * @param direction  direction
   * @param anotherLocation  another cell
   */
  public void addEdge(Direction direction, Location anotherLocation){
    edges.put(direction,anotherLocation);
  }

  /**
   * Get available directions for the current location
   * @return directions
   */

  public List<Direction> getDirection(){
    return new ArrayList<>(edges.keySet());
  }

  public void setGold(){
    gold = 1;
  }
  public int getGold(){
    return gold;
  }
  public void setThief(){
    thief = true;
  }
  public boolean getThief(){
    return thief;
  }
}
