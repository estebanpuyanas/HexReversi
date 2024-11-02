package cs3500.hw5.model;

import java.util.List;

/**
 * Represents the core functionalities of a Reversi game model.
 * Provides methods to manage and interact with the game state.
 */
public interface ReversiModel {

  /**
   * Returns a valid board for a game of Reversi.
   *
   * @return the game board, as a nested List hexagons.
   * @throws IllegalStateException if the board size is invalid. //maybe illegal argument instead?
   */
  List<List<Hexagon>> getBoard() throws IllegalArgumentException;

  /**
   * Returns the size of the board.
   *
   * @return the size of the board.
   */
  int getBoardSize();

  /**
   * Makes a copy of the game.
   *
   * @return an object of the same type.
   */
  ReversiModel makeCopy();

  /**
   * Begins a new game of Reversi. This method should have no side effects other than configuring
   * this instance of the model. It should work for any valid arguments.
   *
   * @param player1 the first player to move, assigned as black.
   * @param player2 the second player to move, assigned as white.
   * @param board   the board used for the game.
   * @throws IllegalArgumentException if any of the parameters are null or otherwise invalid.
   * @throws IllegalStateException    if the game has already started.
   */
  void startGame(Player player1, Player player2, List<List<Hexagon>> board)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Allows the current player to pass on their move. If a player has no legal moves, they are
   * required to pass.
   *
   * @param player is the player attempting to make the move
   * @throws IllegalStateException if the game hasn't been started yet, or if it isn't the turn of
   *                               the player attempting to make the move.
   */
  void passTurn(Player player) throws IllegalStateException;

  /**
   * Allows the current player to make their move.
   *
   * @param player is the player attempting to make the move.
   * @param coords are the coordinates of the hexagon.
   * @throws IllegalStateException    if the game hasn't starter, the move is illegal, or if it
   *                                  isn't the turn of the player attempting to make the move.
   * @throws IllegalArgumentException if the coordinates are invalid.
   */
  void makeMove(Player player, Coord coords)
      throws IllegalStateException, IllegalArgumentException;

  /**
   * Gets the player who is currently playing a turn.
   *
   * @return the relevant player.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  Player getTurn() throws IllegalStateException;

  /**
   * Returns the current score which is the number of filled-in cells each player has.
   *
   * @param player is the player whose score will be returned.
   * @return the relevant count.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  int getScore(Player player) throws IllegalStateException;


  /**
   * Signals whether the game is over or not. A game is over if; Both players have passed their turn
   * in a row. There are no more possible moves to be made.
   *
   * @return true if the game is over, false otherwise.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Determines if a given move is valid for a given player.
   *
   * @param coords are the coordinates of the hexagon.
   * @param player is the player to move.
   * @return true if the move is valid, false otherwise.
   */
  boolean isLegalMove(Coord coords, Player player);

  /**
   * Determines if a player has any valid move.
   *
   * @param player is the player.
   * @return true if the player has a move, false otherwise.
   */
  boolean hasLegalMove(Player player);

  /**
   * Adds a features listener to the model.
   *
   * @param listener The Features listener to be added to the model.
   */
  void addFeaturesListener(ModelFeatures listener);


}