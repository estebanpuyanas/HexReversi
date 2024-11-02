package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.Optional;

/**
 * Represents various strategies of move selection for a game of Reversi.
 */
public interface ReversiStrategy {

  /**
   * Returns the best move deemed by a given strategy.
   * @param model is the game the move will be made on.
   * @param forWhom is the player who will make the move.
   * @return the move determined by the strategy.
   */
  Optional<Coord> chooseMove(ReversiModel model, Player forWhom);

  /**
   * Gets the move weight of a given move.
   * @param move is the move whose weight is desired.
   * @return the weight of the move.
   */
  int getMoveWeight(Coord move);

  /**
   * Returns a ReversiStrategy of the same type as this ReversiStrategy.
   * @return a ReversiStrategy of the same type as this ReversiStrategy.
   */
  ReversiStrategy makeCopy();
}
