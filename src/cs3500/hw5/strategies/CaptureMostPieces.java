package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.List;
import java.util.Optional;

/**
 * Reversi Strategy that returns the coordinate that would capture the most pieces.
 */
public class CaptureMostPieces extends BasicReversiStrategy {

  /**
   * Constructor method, initializes values.
   */
  public CaptureMostPieces() {
    super.resetData();
  }

  @Override
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    List<Coord> moves = super.getAllPossibleMoves(model, forWhom);
    for (Coord move : moves) {
      int weight = super.getScoreGainedWithMove(model, forWhom, move);
      super.weights.put(move, weight);
      this.updateHighestWeight(weight);
    }
    return super.getMoveWithHighestWeight();
  }

  @Override
  public ReversiStrategy makeCopy() {
    return new CaptureMostPieces();
  }
}
