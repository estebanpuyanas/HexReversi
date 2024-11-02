package cs3500.hw5.model;

/**
 * Enum to represent player colors.
 */
public enum TileColor {
  BLACK, WHITE;

  /**
   * Show the player colors in string format where black is an "X" and white is an "0."
   * @return String format of player piece
   */
  public String toString() {
    if (this == BLACK) {
      return "X";
    } else if (this == WHITE) {
      return "O";
    } else {
      throw new IllegalArgumentException("Invalid color");
    }
  }
}
