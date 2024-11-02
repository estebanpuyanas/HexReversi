package cs3500.hw5.model;

/**
 * Represents a state of a position in a Reversi game board.
 * Each state can be occupied by a player's disc (X or O) or remain empty (-).
 */
public enum Hexagon {

  X("X"),
  O("O"),
  EMPTY("-");


  private final String description;
  Hexagon(String description) {
    this.description = description;
  }

  /**
   * Renders the Hexagon as a String as one of the following symbols.
   * (X, O, -)
   *
   * @return the Hexagon in its string representation, according to its current state.
   */
  public String toString() {
    return description;
  }

}
