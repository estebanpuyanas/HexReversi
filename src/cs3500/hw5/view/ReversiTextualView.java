package cs3500.hw5.view;

import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.ReversiModel;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Represents a text-based rendering of a Reversi game board.
 * This view offers a textual representation of the Reversi game's current state.
 * It displays the game board with its hexagonal cells and the stones placed on them.
 */
public class ReversiTextualView implements TextualView {

  private final ReversiModel model;
  private final Appendable appendable;

  /**
   * Constructor for a text-based rendering of Reversi.
   *
   * @param model is the model.
   * @throws IllegalArgumentException if args are invalid.
   */
  public ReversiTextualView(ReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;
    this.appendable = new PrintStream(System.out);
  }

  /**
   * Constructor for a text-based rendering of Reversi.
   *
   * @param model is the model.
   * @param appendable is the appendable object.
   * @throws IllegalArgumentException if args are invalid.
   */
  public ReversiTextualView(ReversiModel model, Appendable appendable)
      throws IllegalArgumentException {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Argument(s) cannot be null");
    }
    this.model = model;
    this.appendable = appendable;
  }

  @Override
  public void render() throws IOException {
    appendable.append(this.toString());
  }

  /**
   * Represents the game in its current state.
   * For example:
   *      _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *    _ _ O O O _ _ _
   *   _ _ _ _ O _ _ _ _
   *  _ _ _ X X X _ _ _ _
   * _ _ _ _ X _ X _ _ _ _
   *  _ _ _ O X O _ _ _ _
   *   _ _ O _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *      _ _ _ _ _ _.
   * @return a string representing the game.
   */
  public String toString() {
    String str = "";
    List<List<Hexagon>> board = model.getBoard();
    int maxWidth = this.findLargestList(board);

    for (List<Hexagon> row : board) {
      //for each row, print empty space:
      for (int i = row.size(); i < maxWidth; i++) {
        str += " ";
      }

      //
      for (Hexagon hexagon : row) {
        str += hexagon + " ";
      }
      str = str.substring(0, str.length() - 1);
      str += "\n";
    }

    return str;
  }


  /**
   * Finds the largest list in a list of lists.
   *
   * @param board is the list of lists.
   * @return the size of the largest list.
   */
  private int findLargestList(List<List<Hexagon>> board) {
    int highest = -1;
    for (List<Hexagon> row : board) {
      if (row.size() > highest) {
        highest = row.size();
      }
    }
    return highest;
  }

}
