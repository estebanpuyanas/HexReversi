package cs3500.hw5.model;

import cs3500.hw5.strategies.ReversiStrategy;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a robot player in a Reversi game.
 * The fundamental difference between a robot player and a human player is that the
 * robot player will use a strategy to determine their move.
 */
public class RobotPlayer implements Player {

  private ReversiModel model;
  private String description;

  ReversiStrategy strategy;

  /**
   * Constructor for a robot player of Reversi.
   * @param model is the model.
   * @param description is the value the player holds (e.g. "X" or "O").
   * @param strategy is the strategy the robot will use.
   * @throws IllegalArgumentException if any args are null.
   */
  public RobotPlayer(ReversiModel model, String description, ReversiStrategy strategy) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(description);
    Objects.requireNonNull(strategy);

    this.model = model;
    this.description = description;
    this.strategy = strategy;
  }

  @Override
  public String toString() {
    return description;
  }

  @Override
  public void pass() throws IllegalStateException {
    model.passTurn(this);
  }

  @Override
  public void move(Coord coords) throws IllegalStateException, IllegalArgumentException {
    Optional<Coord> move = strategy.makeCopy().chooseMove(model, this);
    if (move.isEmpty()) {
      this.pass();
    }
    else {
      model.makeMove(this, move.get());
    }
  }
}
