package maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
  private int row;
  private int col;
  private final int[] locations;
  private final Map<Direction, Location> edges = new HashMap<>();
  private boolean bats;
  private boolean pit;
  private boolean isCave;

  public Location(int row, int col) {
    this.row = row;
    this.col = col;
    locations = new int[]{row,col};
    isCave = true;
    bats = false;
    pit = false;
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
   * Getter& setter if current location is a cave
   * @return boolean
   */
  public void setCave(boolean isCave){
    this.isCave = isCave;
  }
  public boolean getCave(){
    return this.isCave;
  }


  /**
   * Get available directions for the current location
   * @return directions
   */

  public List<Direction> getDirection(){
    return new ArrayList<>(edges.keySet());
  }

  public void setBats(){
    bats = true;
  }
  public boolean getBats(){
    return bats;
  }
  public void setPit(){
    pit = true;
  }
  public boolean getPit(){
    return pit;
  }
}
