package cs3500.hw5.model;

/**
 * Represents an interface for players participating in a Reversi game.
 * Defines methods for player actions such as passing a turn or making a move.
 */
public interface Player extends PlayerActions {
  /**
   * Renders the player as a String as one of the following symbols.
   * (X, O)
   *
   * @return the specific player, as a String.
   */
  public String toString();
}
