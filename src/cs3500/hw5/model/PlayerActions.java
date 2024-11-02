package cs3500.hw5.model;

/**
 * Represents actions that a player in a Reversi game can perform.
 */
public interface PlayerActions {

  /**
   * Allows the player to pass on their move.
   * @throws IllegalStateException if it isn't the player's turn.
   */
  void pass() throws IllegalStateException;

  /**
   * Allows the player to make their move.
   * @param coords are the coordinates of the move.
   * @throws IllegalStateException if it isn't the players turn, or if the move is invalid.
   * @throws IllegalArgumentException if the coordinates aren't valid
   */
  void move(Coord coords) throws IllegalStateException, IllegalArgumentException;

}
