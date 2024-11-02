package cs3500.hw5.view;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Objects;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;

/**
 * Implementation of a player circle. A player circle appears when a player has captured a
 * hexagon in a game of Reversi.
 */
public class PlayerCircle extends Path2D.Double implements Circle {
  private final double radius;
  private Color color;

  /**
   * Default constructor for a player circle.
   *
   * @param radius the radius of this circle.
   */
  public PlayerCircle(double radius) {
    this.radius = radius;
  }

  /**
   * Creates a new circle.
   *
   * @param circle the circle to be drawn.
   * @param center the center of this circle.
   * @return the new circle.
   */
  @Override
  public Shape createCircle(PlayerCircle circle, Coord center) {
    double xCoord = center.getRow();
    double yCoord = center.getCol();
    return new Ellipse2D.Double(xCoord - radius, yCoord - radius, radius * 2, radius * 2);
  }

  /**
   * Assigns the circle a color depending on which Hexagon it corresponds to.
   *
   * @param player the player to whom this circle correpsonds.
   * @return the relevant color of the circle.
   */
  protected Color hexagonColor(Player player) {
    if (Objects.equals(player.toString(), "X")) {
      color = Color.BLACK;
    } else if (Objects.equals(player.toString(), "O")) {
      color = Color.WHITE;
    }
    return color;
  }
}