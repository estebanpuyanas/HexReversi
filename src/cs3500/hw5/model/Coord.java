package cs3500.hw5.model;


import java.util.Objects;

/**
 * Represents a coordinate with a row and column. This class need not rely on any particular
 * implementation.
 */
public class Coord {

  public int row;
  public int col;

  /**
   * Constructor for a coordinate system.
   *
   * @param row represents a row.
   * @param col represents a column.
   */
  public Coord(int row, int col) {
    Objects.requireNonNull(row);
    Objects.requireNonNull(col);

    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row &&
        col == coord.col;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}
