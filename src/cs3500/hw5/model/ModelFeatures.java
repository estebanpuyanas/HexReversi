package cs3500.hw5.model;

/**
 * Represents the features provided by a Reversi game model to notify
 * changes to a listener (controller).
 */
public interface ModelFeatures {

  /**
   * Notifies the listener (controller) that the current player has changed.
   *
   * @param currentPlayer The player whose turn it is.
   */
  void notifyPlayerChanged(Player currentPlayer);

  /**
   * Notifies the listener (controller) that the game is over.
   *
   * @param winner The player who has won the game.
   */
  void notifyGameOver(Player winner);

  /**
   * Notifies the listener (controller) that the board has been updated.
   */
  void notifyBoardUpdated();

  /**
   * Notifies the listener (controller) that the player's attempted move is illegal.
   * @param player is the player who attempted to make an illegal move.
   */
  void notifyIllegalMove(Player player);

  /**
   * Notifies the listener (controller) that the player cannot move because it is not their turn.
   * @param player is the player who attempted to make an out of turn move.
   */
  void notifyOutOfTurnMove(Player player);

}
