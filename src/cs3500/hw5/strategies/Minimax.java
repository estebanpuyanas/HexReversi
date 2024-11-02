package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reversi Strategy that chooses the move that leaves their opponent in the worst situation
 * possible.
 */
public class Minimax extends BasicReversiStrategy {

  /**
   * Constructor method, initializes values.
   */
  public Minimax() {
    super.resetData();
  }

  /**
   * Chooses best move considering their opponents next move.
   * @param model is the game the move will be made on.
   * @param forWhom is the player who will make the move.
   * @return the coordinates deemed to be the best move.
   */
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    List<Coord> moves = super.getAllPossibleMoves(model, forWhom);
    Player opponent = super.getOpponent(model, forWhom);
    int initialDifference = model.getScore(forWhom) - model.getScore(opponent);
    for (Coord move : moves) {
      ReversiModel resultingGame = this.makeTheoreticalMove(model, forWhom, move);
      boolean makeOpponentMove = true;
      Optional<Coord> opponentMove = Optional.empty();
      try {
        opponentMove = new CaptureMostPieces().chooseMove(resultingGame, opponent);
      }
      catch (Exception e) {
        makeOpponentMove = false;
      }

      int weight;
      if (makeOpponentMove) {
        ReversiModel completedGame = this.makeTheoreticalMove(resultingGame, opponent,
            opponentMove.get());
        weight = initialDifference -
            (completedGame.getScore(forWhom) - completedGame.getScore(opponent));
      } else {
        weight = resultingGame.getScore(forWhom);
      }
      super.weights.put(move, weight);
      this.updateHighestWeight(weight);
    }
    return super.getMoveWithHighestWeight();
  }

  @Override
  public ReversiStrategy makeCopy() {
    return new Minimax();
  }

  private ReversiModel makeTheoreticalMove(ReversiModel model,
      Player forWhom, Coord move) {
    ReversiModel tempModel = model.makeCopy();
    super.startTempGame(tempModel, forWhom, new ArrayList<>(model.getBoard()));
    tempModel.makeMove(forWhom, move);
    return tempModel;
  }

}
