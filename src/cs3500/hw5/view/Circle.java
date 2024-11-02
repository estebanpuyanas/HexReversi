package cs3500.hw5.view;

import cs3500.hw5.model.Coord;
import java.awt.Shape;

/**
 * Interface of the player circle.
 */
public interface Circle {

  /**
   * Creates a new circle.
   *
   * @param circle the circle to be drawn.
   * @param center the center of this circle.
   * @return a new circle.
   */
  Shape createCircle(PlayerCircle circle, Coord center);
}
