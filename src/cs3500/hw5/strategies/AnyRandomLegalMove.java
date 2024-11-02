package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Reversi Strategy that returns a random legal move, excluding the best move as determined by
 * the CaptureMostPiecesStrategy.
 */
public class AnyRandomLegalMove extends BasicReversiStrategy {

  @Override
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    List<Coord> moves = super.getAllPossibleMoves(model, forWhom);
    Coord bestMove = new CaptureMostPieces().chooseMove(model, forWhom).get();
    if (moves.contains(bestMove)) {
      moves.remove(bestMove);
    }

    if (moves == null || moves.isEmpty()) {
      return Optional.empty();
    }

    for (Coord move : moves) {
      int weight = super.getScoreGainedWithMove(model, forWhom, move);
      super.weights.put(move, weight);
      this.updateHighestWeight(weight);
    }

    Random random = new Random();
    return Optional.of(moves.get(random.nextInt(moves.size())));
  }

  @Override
  public ReversiStrategy makeCopy() {
    return new AnyRandomLegalMove();
  }
}
