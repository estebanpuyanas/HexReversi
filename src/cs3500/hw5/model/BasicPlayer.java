package cs3500.hw5.model;

/**
 * Represents a player participating in a game of Reversi.
 * This player class interacts with the Reversi game model by making moves and passing turns.
 */
public class BasicPlayer implements Player {

  private ReversiModel model;
  private String description;

  /**
   * Constructor for a player of Reversi.
   * @param model is the model.
   * @param description is the value the player holds (e.g. "X" or "O").
   * @throws IllegalArgumentException if model or description is null
   */
  public BasicPlayer(ReversiModel model, String description) {
    if (model == null || description == null) {
      throw new IllegalArgumentException("Model or description cannot be null");
    }
    this.model = model;
    this.description = description;
  }

  /**
   * Returns the string representation of the player.
   * @return a string representation of the player
   */
  @Override
  public String toString() {
    return description;
  }

  /**
   * Passes the turn of the player.
   * @throws IllegalStateException if the turn cannot be passed
   */
  @Override
  public void pass() throws IllegalStateException {
    model.passTurn(this);
  }

  /**
   * Makes a move for the player at the given coordinates.
   * @param coords the coordinates for the move
   * @throws IllegalStateException if the move cannot be made
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public void move(Coord coords) throws IllegalStateException, IllegalArgumentException {
    model.makeMove(this, coords);
  }
}





