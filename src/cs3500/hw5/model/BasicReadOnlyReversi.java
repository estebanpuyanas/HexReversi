package cs3500.hw5.model;

/**
 * Represents a read-only (no mutator methods) version of a BasicReversi model.
 */
public class BasicReadOnlyReversi extends BasicReversi implements ReadOnlyReversiModel {

  private ReversiModel model;

  /**
   * Default constructor for a read-only BasicReversi model.
   */
  public BasicReadOnlyReversi(ReversiModel model) {
    super();
    this.model = model;
  }

  /**
   * Gets the player who is currently playing a turn.
   *
   * @return the relevant player.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  @Override
  public Player getTurn()
      throws IllegalStateException {
    return super.getTurn();
  }

  /**
   * Returns the current score which is the number of filled-in cells each player has.
   *
   * @param player is the player whose score will be returned.
   * @return the relevant count.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  @Override
  public int getScore(Player player)
      throws IllegalStateException {
    return super.getScore(player);
  }

  /**
   * Signals whether the game is over or not. A game is over if; Both players have passed their turn
   * in a row. There are no more possible moves to be made.
   *
   * @return true if the game is over, false otherwise.
   * @throws IllegalStateException if the game hasn't been started yet.
   */
  @Override
  public boolean isGameOver()
      throws IllegalStateException {
    return super.isGameOver();
  }

  /**
   * Determines the size for the board for this game of Reversi.
   *
   * @return the size of the board.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public int getBoardSize()
      throws IllegalStateException {
    return super.getBoard().size();
  }

  @Override
  public boolean isLegalmove(Coord coords, Player player)
      throws IllegalStateException, IllegalArgumentException {
    return super.isLegalMove(coords, player);
  }

  @Override
  public Hexagon getCellContents(Coord coords)
      throws IllegalStateException, IllegalArgumentException {

    if (!areCoordinatesInBounds(coords, super.getBoard())) {
      throw new IllegalArgumentException("Invalid coordinates");
    }

    if (model.getBoard().get(coords.row).get(coords.col).equals(Hexagon.X)) {
      return Hexagon.X;
    } else if (model.getBoard().get(coords.row).get(coords.col).equals(Hexagon.O)) {
      return Hexagon.O;
    } else {
      return Hexagon.EMPTY;
    }
  }

  /**
   * Determines if a player has any valid move.
   *
   * @param player is the player.
   * @return true if the player has a move, false otherwise.
   */
  @Override
  public boolean hasLegalMove(Player player)
      throws IllegalStateException, IllegalArgumentException {
    return super.hasLegalMove(player);
  }
}