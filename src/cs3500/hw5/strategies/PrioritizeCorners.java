package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reversi strategy that prioritizes moves in corners.
 * All moves that are not next to corners will have an extra weight of 4
 * to account for the strategic value of the move.
 * If there are no moves to corners, this will return Optional.empty().
 */
public class PrioritizeCorners extends BasicReversiStrategy {

  /**
   * Constructor method, initializes values.
   */
  public PrioritizeCorners() {
    super.resetData();
  }

  @Override
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    List<Coord> moves = super.getAllPossibleMoves(model, forWhom);
    List<Coord> movesInCorners = new ArrayList<>();
    List<Coord> corners = super.getCorners(model.getBoard());
    for (Coord move : moves) {
      if (corners.contains(move)) {
        movesInCorners.add(move);
        super.weights.put(move, super.getScoreGainedWithMove(model, forWhom, move) + 4);
        super.updateHighestWeight(super.getScoreGainedWithMove(model, forWhom, move) + 4);
      }
    }

    if (movesInCorners.size() > 0) {
      return super.getMoveWithHighestWeight();
    }
    return Optional.empty();
  }

  @Override
  public ReversiStrategy makeCopy() {
    return new PrioritizeCorners();
  }
}
