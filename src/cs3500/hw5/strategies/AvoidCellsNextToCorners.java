package cs3500.hw5.strategies;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reversi strategy that avoids moves next to corners.
 * All moves that are not next to corners will have an extra weight of 2
 * to account for the strategic value of the move.
 * If there are no moves next to corners, this will return Optional.empty().
 */
public class AvoidCellsNextToCorners extends BasicReversiStrategy {

  /**
   * Constructor method, initializes values.
   */
  public AvoidCellsNextToCorners() {
    super.resetData();
  }

  @Override
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    List<Coord> moves = super.getAllPossibleMoves(model, forWhom);
    List<Coord> movesNotNextToCorners = new ArrayList<>();
    for (Coord move : moves) {
      if (!this.isCellNextToCorner(model.getBoard(), move)) {
        movesNotNextToCorners.add(move);
        super.weights.put(move, super.getScoreGainedWithMove(model, forWhom, move) + 2);
        super.updateHighestWeight(super.getScoreGainedWithMove(model, forWhom, move) + 2);
      }
    }
    if (movesNotNextToCorners.size() > 0) {
      return super.getMoveWithHighestWeight();
    }
    return Optional.empty();
  }

  @Override
  public ReversiStrategy makeCopy() {
    return new AvoidCellsNextToCorners();
  }

  /**
   * Determines if a given hexagon is next to any corners.
   * @param board is the board to test against.
   * @param coords is the coordinates of the given hexagon.
   * @return true if the hexagon is next to a corner, false otherwise.
   */
  private boolean isCellNextToCorner(List<List<Hexagon>> board, Coord coords) {
    List<Coord> corners = super.getCorners(board);
    boolean inTopHalf;
    //top half
    if (coords.row < board.size() / 2) {
      return (corners.contains(new Coord(coords.row - 1, coords.col - 1)) ||
          corners.contains(new Coord(coords.row - 1, coords.col)) ||
          corners.contains(new Coord(coords.row, coords.col + 1)) ||
          corners.contains(new Coord(coords.row + 1, coords.col + 1)) ||
          corners.contains(new Coord(coords.row + 1, coords.col)) ||
          corners.contains(new Coord(coords.row, coords.col - 1)));
    }
    //bottom half
    else if (coords.row > board.size() / 2) {
      return (corners.contains(new Coord(coords.row - 1, coords.col)) ||
          corners.contains(new Coord(coords.row - 1, coords.col + 1)) ||
          corners.contains(new Coord(coords.row, coords.col + 1)) ||
          corners.contains(new Coord(coords.row + 1, coords.col)) ||
          corners.contains(new Coord(coords.row + 1, coords.col - 1)) ||
          corners.contains(new Coord(coords.row, coords.col - 1)));
    }
    //middle row
    return (corners.contains(new Coord(coords.row - 1, coords.col - 1)) ||
        corners.contains(new Coord(coords.row - 1, coords.col)) ||
        corners.contains(new Coord(coords.row, coords.col + 1)) ||
        corners.contains(new Coord(coords.row + 1, coords.col)) ||
        corners.contains(new Coord(coords.row + 1, coords.col - 1)) ||
        corners.contains(new Coord(coords.row, coords.col - 1)));
  }

}
