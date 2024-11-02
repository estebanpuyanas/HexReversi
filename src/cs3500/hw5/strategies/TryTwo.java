package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.Objects;
import java.util.Optional;


/**
 * Reversi strategy that returns the best move out of two given strategies.
 */
public class TryTwo extends BasicReversiStrategy {

  private ReversiStrategy first;
  private ReversiStrategy second;

  /**
   * Constructor method.
   *
   * @param first  is the first strategy.
   * @param second is the second strategy.
   */
  public TryTwo(ReversiStrategy first, ReversiStrategy second) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    this.first = first;
    this.second = second;
    super.resetData();
  }

  @Override
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    Optional<Coord> firstMove = first.chooseMove(model, forWhom);
    Optional<Coord> secondMove = second.chooseMove(model, forWhom);

    if (firstMove.isPresent()) {
      super.weights.put(firstMove.get(), first.getMoveWeight(firstMove.get()));
      super.updateHighestWeight(first.getMoveWeight(firstMove.get()));
    }
    if (secondMove.isPresent()) {
      super.weights.put(secondMove.get(), second.getMoveWeight(secondMove.get()));
      super.updateHighestWeight(second.getMoveWeight(secondMove.get()));
    }

    return super.getMoveWithHighestWeight();
  }

  @Override
  public ReversiStrategy makeCopy() {
    return new TryTwo(first.makeCopy(), second.makeCopy());
  }
}
