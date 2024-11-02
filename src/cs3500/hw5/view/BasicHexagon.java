package cs3500.hw5.view;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Represents a standard Hexagon.
 * Class implementation adapted from:
 * https://github.com/javagl/Hexagon/blob/master/src/main/java/de/javagl/hexagon/Hexagons.java
 */

public class BasicHexagon extends Path2D.Double implements Hexagon {

  /**
   * A constant for the angle between two corners of a {@link Hexagon},
   * in radians.
   */
  private static final double ANGLE_STEP_RAD = 2 * Math.PI / 6;
  private final double radius;
  private final double angleStepOffset;

  /**
   * Default constructor for a BasicHexagon.
   *
   * @param radius the radius of the hexagon.
   * @param width  the width of the hexagon.
   * @param height the height of the hexagon.
   */
  public BasicHexagon(double radius, double width, double height) {
    this.radius = radius;
    this.angleStepOffset = 0.5;
  }

  /**
   * Returns the x-coordinate of the corner with the given index.
   *
   * @param index The index
   * @return The x-coordinate of the corner
   */
  @Override
  public double getCornerX(int index) {
    double angleRad = BasicHexagon.ANGLE_STEP_RAD * (index + angleStepOffset);
    double x = Math.cos(angleRad);
    return x * radius;
  }

  /**
   * Returns the y-coordinate of the corner with the given index.
   *
   * @param index The index
   * @return The y-coordinate of the corner
   */
  @Override
  public double getCornerY(int index) {
    double angleRad = BasicHexagon.ANGLE_STEP_RAD * (index + angleStepOffset);
    double y = Math.sin(angleRad);
    return y * radius;
  }

  /**
   * Creates a new Hexagon.
   *
   * @param hexagon the hexagon that is to be drawn.
   * @param center  the center of the hexagon.
   * @return the drawn hexagon, as a shape.
   */
  public Shape creteHexagon(Hexagon hexagon, Point2D center) {
    for (int vertex = 0; vertex < 6; vertex++) {
      double xCoord = hexagon.getCornerX(vertex) + center.getX();
      double yCoord = hexagon.getCornerY(vertex) + center.getY();

      if (vertex == 0) {
        this.moveTo(xCoord, yCoord);
      } else {
        this.lineTo(xCoord, yCoord);
      }
    }
    this.closePath();
    return this;
  }
}
