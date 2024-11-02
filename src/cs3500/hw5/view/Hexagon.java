package cs3500.hw5.view;

/**
 * Interface adapted from:
 * https://github.com/javagl/Hexagon/blob/master/src/main/java/de/javagl/hexagon/Hexagon.java
 * Represents a hexagon on a GUI visualization of Reversi.
 */
public interface Hexagon {

  /**
   * Returns the x-coordinate of the corner with the given index.
   *
   * @param index The index
   * @return The x-coordinate of the corner
   */
  double getCornerX(int index);

  /**
   * Returns the y-coordinate of the corner with the given index.
   *
   * @param index The index
   * @return The y-coordinate of the corner
   */
  double getCornerY(int index);
}
