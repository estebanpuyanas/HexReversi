package cs3500.reversi;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.ModelFeatures;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a mock for a Reversi Model. The purpose of this mock is to test that strategies are
 * appropriately using the methods contained in a ReversiModel.
 */
public class ReversiMockForStrategies implements ReversiModel {

  // initialized list to be a new linked list.
  private final Queue<Player> players = new LinkedList<>();

  //The board uses the axial coordinate system.
  //For instance:
  //        _ _ _ _ _ _
  //       _ _ _ _ _ _ _
  //      _ _ O O O _ _ _
  //     _ _ _ _ O _ _ _ _
  //    _ _ _ X X X _ _ _ _
  //   _ _ _ _ X _ X _ _ _ _
  //    _ _ _ O X O _ _ _ _
  //     _ _ O _ _ _ _ _ _
  //      _ _ _ _ _ _ _ _
  //       _ _ _ _ _ _ _
  //        _ _ _ _ _ _
  // In this example, the hexagon in the upper left is (0,0),
  // and the hexagon in the bottom right is (10, 5).
  // Note that the board is 0-indexed.
  private List<List<Hexagon>> board;

  private int score;

  final Readable in;
  final Appendable out;


  /**
   * Default Constructor for a game of Basic Reversi. The default values for maxwidth and minwidth
   * are 11 and 6.
   */
  public ReversiMockForStrategies(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
    this.board = this.generateBoard(11, 6);
  }


  @Override
  public List<List<Hexagon>> getBoard() throws IllegalArgumentException {
    List<List<Hexagon>> boardCopy = new ArrayList<>();
    for (List<Hexagon> row : this.board) {
      List<Hexagon> rowCopy = new ArrayList<>(row);
      boardCopy.add(rowCopy);
    }
    return boardCopy;
  }

  @Override
  public int getBoardSize() {
    return board.size();
  }

  @Override
  public void startGame(Player player1, Player player2, List<List<Hexagon>> board)
      throws IllegalArgumentException, IllegalStateException {
    players.add(player1);
    players.add(player2);
    this.board = board;
    this.score = 0;
  }

  @Override
  public ReversiModel makeCopy() {
    return new ReversiMockForStrategies(in, out);
  }

  @Override
  public void passTurn(Player player) throws IllegalStateException {
    return;
  }

  @Override
  public void makeMove(Player player, Coord coords)
      throws IllegalStateException, IllegalArgumentException {
    int initScore = this.score;
    try {
      if (coords.row == 3 && coords.col == 3) {
        out.append(
            "Artificially making (3,3) the best move by making"
                + " it result in a score increase of 100\n");
        this.score += 100;
      }
      out.append(
          "RUNNING makeMove(): Player " + player + " is considering (" + coords.row + ", "
              + coords.col
              + ")\n");
      out.append("Value of move: " + (this.score - initScore) + "\n");
    } catch (Exception e) {
      System.out.println("IO ERROR");

    }
  }

  @Override
  public Player getTurn() throws IllegalStateException {
    return null;
  }

  @Override
  public int getScore(Player player) throws IllegalStateException {
    return score;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public boolean isLegalMove(Coord coords, Player player) {
    try {
      if (coords.row < 8) {
        out.append(
            "RUNNING isLegalMove(): Move at (" + coords.row + ", " + coords.col
                + ") deemed VALID\n");
        return true;
      }
      out.append(
          "RUNNING isLegalMove(): Move at (" + coords.row + ", " + coords.col
              + ") deemed INVALID\n");
    } catch (IOException e) {
      System.out.println("IO ERROR");

    }
    return false;
  }

  @Override
  public boolean hasLegalMove(Player player) {
    return true;
  }

  @Override
  public void addFeaturesListener(ModelFeatures listener) {
    return;
  }

  /**
   * Generates the board for the game.
   *
   * @param maxWidth is the max width of the board.
   * @param minWidth is the min width of the board.
   * @return the board.
   * @throws IllegalArgumentException if the board cannot be generated.
   */
  private List<List<Hexagon>> generateBoard(int maxWidth, int minWidth)
      throws IllegalArgumentException {
    int buffer = maxWidth - minWidth;
    if (buffer < 2) {
      throw new IllegalArgumentException("Board would be too small");
    }

    List<List<Hexagon>> tempBoard = new ArrayList<>();

    //adds all rows to the board
    for (int row = 0; row < (buffer * 2) + 1; row++) {
      tempBoard.add(new ArrayList<Hexagon>());
    }

    //fills each row as appropriate
    this.fillValuesAscending(maxWidth, minWidth, tempBoard, 0);
    for (int i = 0; i < maxWidth; i++) {
      tempBoard.get(buffer).add(Hexagon.EMPTY);
    }
    this.fillValuesDescending(maxWidth, minWidth, tempBoard, buffer + 1);

    this.initializeBoardValues(tempBoard.size() / 2, tempBoard.get(tempBoard.size() / 2).size() / 2,
        tempBoard);

    return tempBoard;
  }

  /**
   * Adds hexagons to the rows of a board in ascending order.
   *
   * @param maxWidth  is the maximum width.
   * @param minWidth  is the minimum width.
   * @param tempBoard is the board.
   * @param row       is the index to add hexagons to the board at.
   */
  private void fillValuesAscending(int maxWidth, int minWidth, List<List<Hexagon>> tempBoard,
      int row) {
    for (int i = minWidth; i < maxWidth; i++) {
      for (int j = 0; j < minWidth + row; j++) {
        tempBoard.get(row).add(Hexagon.EMPTY);
      }
      row++;
    }
  }

  /**
   * Adds hexagons to the rows of a board in descending order.
   *
   * @param maxWidth  is the maximum width.
   * @param minWidth  is the minimum width.
   * @param tempBoard is the board.
   * @param row       is the index to add hexagons to the board at.
   */
  private void fillValuesDescending(int maxWidth, int minWidth, List<List<Hexagon>> tempBoard,
      int row) {
    int counter = 1;
    for (int i = maxWidth; i > minWidth; i--) {
      for (int j = maxWidth; j > counter; j--) {
        tempBoard.get(row).add(Hexagon.EMPTY);
      }
      row++;
      counter++;
    }
  }

  /**
   * Initalizes the board's starting values.
   *
   * @param startingRow is the center row.
   * @param startingCol is the center column.
   * @param board       is the board to be modified.
   */
  private void initializeBoardValues(int startingRow, int startingCol, List<List<Hexagon>> board) {
    this.changeBoardValue(new Coord(startingRow - 1, startingCol - 1), Hexagon.X, board);
    this.changeBoardValue(new Coord(startingRow + 1, startingCol - 1), Hexagon.X, board);
    this.changeBoardValue(new Coord(startingRow, startingCol + 1), Hexagon.X, board);

    this.changeBoardValue(new Coord(startingRow - 1, startingCol), Hexagon.O, board);
    this.changeBoardValue(new Coord(startingRow + 1, startingCol), Hexagon.O, board);
    this.changeBoardValue(new Coord(startingRow, startingCol - 1), Hexagon.O, board);
  }

  /**
   * Replaces the value in the board with a new value.
   *
   * @param coords       are the coordinates of the hexagon.
   * @param updatedValue is the new value.
   * @param board        is the board that will be updated.
   * @throws IllegalArgumentException if any arguments are invalid.
   */
  private void changeBoardValue(Coord coords, Hexagon updatedValue, List<List<Hexagon>> board)
      throws IllegalArgumentException {
    board.get(coords.row).remove(coords.col);
    board.get(coords.row).add(coords.col, updatedValue);
  }


}
