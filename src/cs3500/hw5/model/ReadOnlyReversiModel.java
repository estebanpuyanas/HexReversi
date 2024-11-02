package cs3500.hw5.model;

import java.util.List;

/**
 * Represents a read-only (no mutator methods) version of a BasicReversi model.
 */
public interface ReadOnlyReversiModel {

  /**
   * Returns a valid board for a game of Reversi.
   *
   * @return the game board, as a nested List hexagons.
   * @throws IllegalStateException if the board size is invalid. //maybe illegal argument instead?
   */
  List<List<Hexagon>> getBoard() throws IllegalArgumentException;

  /**
   * Gets the player who is currently playing a turn.
   *
   * @return the relevant player.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  public Player getTurn() throws IllegalStateException;

  /**
   * Returns the current score which is the number of filled-in cells each player has.
   *
   * @param player is the player whose score will be returned.
   * @return the relevant count.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  public int getScore(Player player) throws IllegalStateException;

  /**
   * Signals whether the game is over or not. A game is over if; Both players have passed their turn
   * in a row. There are no more possible moves to be made.
   *
   * @return true if the game is over, false otherwise.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  public boolean isGameOver() throws IllegalStateException;

  /**
   * Determines the size for the board for this game of Reversi.
   *
   * @return the size of the board.
   * @throws IllegalStateException if the game has not been started.
   */
  public int getBoardSize()
      throws IllegalStateException;

  /**
   * Determines if a given move is valid for a given player.
   *
   * @param coords are the coordinates of the move.
   * @param player is the player to move.
   * @return true if the move is valid, false otherwise.
   */
  public boolean isLegalmove(Coord coords, Player player)
      throws IllegalStateException, IllegalArgumentException;

  public Hexagon getCellContents(Coord coords)
      throws IllegalStateException, IllegalArgumentException;

  public boolean hasLegalMove(Player player)
      throws IllegalStateException, IllegalArgumentException;

}
