package cs3500.hw5.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a game of Hexagonal Reversi.
 * Players take turns placing their discs on the board.
 * You can only place a disc if it traps one or more of your opponent's discs between
 * the newly placed disc and any of your existing discs in a straight line.
 * Then, you flip those trapped discs to your color.
 * The game ends when the board is full or neither player can make a legal move.
 * The player with the most discs of their color wins.
 */
public class BasicReversi implements ReversiModel {

  // initialized list to be a new linked list.
  private final Queue<Player> players = new LinkedList<>();

  // listeners
  private List<ModelFeatures> featuresListeners;

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
  private boolean gameStarted;

  //INVARIANT:
  //countPassedMoves is between 0 and 2 inclusive.
  private int countPassedMoves;

  /**
   * Default Constructor for a game of Basic Reversi. The default values for maxwidth and minwidth
   * are 11 and 6.
   */
  public BasicReversi() {
    this.board = this.generateBoard(11, 6);
    this.featuresListeners = new ArrayList<>();
  }

  /**
   * Constructor for a basic game of reversi.
   *
   * @param maxWidth the maximum width of the board.
   * @param minWidth the minimum width of the board.
   */
  public BasicReversi(int maxWidth, int minWidth) {
    this.board = this.generateBoard(maxWidth, minWidth);
  }

  /**
   * Generates a new board for this game of reversi.
   *
   * @return a valid board for the game.
   * @throws IllegalArgumentException if the board size is invalid.
   */
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
    return this.board.size();
  }

  @Override
  public ReversiModel makeCopy() {
    return new BasicReversi();
  }

  /**
   * Starts this reversi game.
   *
   * @param player1 the first player to move, assigned as black.
   * @param player2 the second player to move, assigned as white.
   * @param board   the board used for the game.
   * @throws IllegalArgumentException TODO: DETERMINE.
   * @throws IllegalStateException    if the game has already started.
   */
  @Override
  public void startGame(Player player1, Player player2, List<List<Hexagon>> board)
      throws IllegalArgumentException, IllegalStateException {
    if (this.validateArgs(Arrays.asList(player1, player2)) || this.isInvalidNestedList(board)) {
      throw new IllegalArgumentException("Argument(s) cannot be null");
    }
    if (isGameStarted()) {
      throw new IllegalStateException("Game has already been started");
    }
    this.gameStarted = true;
    players.add(player1);
    players.add(player2);
    this.board = board;
    this.countPassedMoves = 0;
    notifyPlayerChanged(player1);
  }

  /**
   * Allows the player to pass their turn. The player must pass their turn if there are no legal
   * moves they can make.
   *
   * @param player is the player attempting to move.
   * @throws IllegalStateException if the game hasn't started yet, or it isn't the turn of the
   *                               player attempting to pass.
   */
  @Override
  public void passTurn(Player player) throws IllegalStateException {
    if (this.isGameOver()) {
      return;
    }
    this.verifyGameHasStarted();
    if (player == players.peek()) {
      players.add(players.remove());
      countPassedMoves++;
    } else {
      notifyOutOfTurnMove(player);
      throw new IllegalStateException("It is not the turn of the player who request to pass");
    }

    if (this.isGameOver()) {
      // find winner
      int score1 = this.getScore(players.peek());
      players.add(players.remove());
      int score2 = this.getScore(players.peek());
      players.add(players.remove());
      Player winningPlayer = null;
      if (score1 > score2) {
        winningPlayer = players.peek();
      }
      else if (score2 > score1) {
        players.add(players.remove());
        winningPlayer = players.peek();
        players.add(players.remove());
      }
      notifyGameOver(winningPlayer);
    }
    else {
      notifyBoardUpdated();
      notifyPlayerChanged(players.peek());
    }

  }

  /**
   * Makes a move for the current player by filling in the specified hexagon.
   *
   * @param player is the player attempting to make the move.
   * @param coords are the coordinates of the hexagon.
   * @throws IllegalStateException    if the game hasn't started, the move is illegal, or it is not
   *                                  the turn of the player attempting to make the move.
   * @throws IllegalArgumentException if the coordinates are invalid.
   */
  @Override
  public void makeMove(Player player, Coord coords)
      throws IllegalStateException, IllegalArgumentException {
    if (this.isGameOver()) {
      return;
    }
    this.verifyGameHasStarted();
    this.validateCoordinates(coords);

    if (!player.equals(players.peek())) {
      players.add(players.remove());
      notifyOutOfTurnMove(players.peek());
      players.add(players.remove());
      throw new IllegalStateException("It is not this players turn");
    }
    if (!this.hasLegalMove(player)) {
      this.passTurn(player);
      return;
    }

    if (!this.makeMoveIfPossible(player, coords)) {
      notifyIllegalMove(players.peek());
      throw new IllegalStateException("Move is illegal");
    }

    players.add(players.remove());
    countPassedMoves = 0;

    notifyBoardUpdated();
    notifyPlayerChanged(players.peek());

  }


  @Override
  public Player getTurn() throws IllegalStateException {
    if (!isGameStarted()) {
      throw new IllegalStateException("This method cannot be called until the game has started");
    }
    return players.peek();
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    this.verifyGameHasStarted();
    return this.countPassedMoves >= 2;
  }

  @Override
  public int getScore(Player player) throws IllegalStateException {
    this.verifyGameHasStarted();
    int score = 0;
    for (List<Hexagon> list : this.board) {
      for (Hexagon hexagon : list) {
        if (hexagon.toString().equals(player.toString())) {
          score++;
        }
      }
    }
    return score;
  }

  @Override
  public boolean isLegalMove(Coord coords, Player player) {
    if (!this.isHexagonEmpty(coords)) {
      return false;
    }
    return (this.buildListForMoveDown(coords, 0, -1,
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), player) ||
        (this.buildListForMoveDown(coords, 1, 0,
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), player)) ||

        (this.buildListForMoveUp(coords, -1, 0,
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), player)) ||
        (this.buildListForMoveUp(coords, 0, 1,
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), player)) ||

        this.buildListForMoveLaterally(coords,
            new ArrayList<>(), new ArrayList<>(), 1, new ArrayList<>(), player) ||
        this.buildListForMoveLaterally(coords,
            new ArrayList<>(), new ArrayList<>(), -1, new ArrayList<>(), player));
  }

  @Override
  public boolean hasLegalMove(Player player) {
    for (int row = 0; row < this.board.size(); row++) {
      for (int col = 0; col < this.board.get(row).size(); col++) {
        if (this.isLegalMove(new Coord(row, col), player)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void addFeaturesListener(ModelFeatures listener) {
    featuresListeners.add(listener);
  }

  /**
   * Notifies all registered listeners about a change in the current player.
   *
   * @param currentPlayer The Player object representing the current player.
   */
  private void notifyPlayerChanged(Player currentPlayer) {
    for (ModelFeatures listener : featuresListeners) {
      listener.notifyPlayerChanged(currentPlayer);
    }
  }

  /**
   * Notifies all registered listeners about the end of the game and the winner.
   *
   * @param winner The Player object representing the winner of the game.
   */
  private void notifyGameOver(Player winner) {
    for (ModelFeatures listener : featuresListeners) {
      listener.notifyGameOver(winner);
    }
  }

  /**
   * Notifies all registered listeners about updates to the game board.
   */
  private void notifyBoardUpdated() {
    for (ModelFeatures listener : featuresListeners) {
      listener.notifyBoardUpdated();
    }
  }

  /**
   * Notifies all registered listeners that a certain player attempted to make an illegal move.
   * @param player is the player who attempted to make the move.
   */
  private void notifyIllegalMove(Player player) {
    for (ModelFeatures listener : featuresListeners) {
      listener.notifyIllegalMove(player);
    }
  }

  private void notifyOutOfTurnMove(Player player) {
    for (ModelFeatures listener : featuresListeners) {
      listener.notifyOutOfTurnMove(player);
    }
  }

  /**
   * Validates whether the specific hexagon is empty.
   *
   * @param coords are the coordinates of the hexagon.
   * @return true if the hexagon is empty, false otherwise.
   */
  private boolean isHexagonEmpty(Coord coords)
      throws IllegalStateException, IllegalArgumentException {
    this.verifyGameHasStarted();
    this.validateCoordinates(coords);

    return (board.get(coords.row).get(coords.col).toString().equals(Hexagon.EMPTY.toString()));
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
   * Validates that all items in a list are not null.
   *
   * @param args is the list of arguments to be validated.
   * @return true if any arg is null, false otherwise.
   */
  private boolean validateArgs(List<Object> args) {
    for (Object arg : args) {
      if (arg == null) {
        return true;
      }
    }
    return false;
  }

  /**
   * Validates that a list, all lists in the list, and all items in each nested list are not null.
   *
   * @param list is the list to be validated.
   * @return true if the list or any items in nested lists are null, false otherwise.
   */
  private boolean isInvalidNestedList(List<List<Hexagon>> list) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    for (List nestedList : list) {
      if (this.isInvalidList(nestedList)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Validates that a list, and all items in the list are not null.
   *
   * @param list is the list to be validated.
   * @return true if the list or any items in the list are null, false otherwise.
   */
  private boolean isInvalidList(List list) {
    return list == null || list.contains(null);
  }


  /**
   * Replaces the value in the board with a new value.
   *
   * @param coords are the coordinates of the hexagon.
   * @param updatedValue is the new value.
   * @param board        is the board that will be updated.
   * @throws IllegalArgumentException if any arguments are invalid.
   */
  private void changeBoardValue(Coord coords, Hexagon updatedValue, List<List<Hexagon>> board)
      throws IllegalArgumentException {
    this.validateArgs(Arrays.asList(coords, updatedValue));
    this.validateCoordinates(coords, board);
    if (this.isInvalidNestedList(board)) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    board.get(coords.row).remove(coords.col);
    board.get(coords.row).add(coords.col, updatedValue);
  }

  /**
   * Replaces the value in the board with a new value.
   *
   * @param coords are the coordinates of the hexagon.
   * @param updatedValue is the new value.
   * @throws IllegalArgumentException if any arguments are invalid.
   */
  private void changeBoardValue(Coord coords, Hexagon updatedValue) {
    this.validateArgs(Arrays.asList(coords, updatedValue));
    this.validateCoordinates(coords, this.board);
    if (this.isInvalidNestedList(this.board)) {
      throw new IllegalArgumentException("Board cannot be null");
    }
    this.board.get(coords.row).remove(coords.col);
    this.board.get(coords.row).add(coords.col, updatedValue);
  }


  /**
   * Validates that coordinates are in the bounds of the board.
   *
   * @param coords are the coordinates of the hexagon.
   * @param board is the board whose coordinates will be validated against.
   * @throws IllegalArgumentException if the coordinates are invalid.
   */
  private void validateCoordinates(Coord coords, List<List<Hexagon>> board)
      throws IllegalArgumentException {
    if (!this.areCoordinatesInBounds(coords, board)) {
      throw new IllegalArgumentException("Coordinates are invalid");
    }
  }

  /**
   * Validates that coordinates are in the bounds of the board.
   *
   * @param coords are the coordinates of the hexagon.
   * @throws IllegalArgumentException if the coordinates are invalid.
   */
  private void validateCoordinates(Coord coords) {
    if (coords.row < 0 || coords.row >= this.board.size()) {
      throw new IllegalArgumentException("Invalid row");
    }
    if (coords.col < 0 || coords.col >= this.board.get(coords.row).size()) {
      throw new IllegalArgumentException("Invalid column");
    }
  }

  /**
   * Determines if coordinates are in the bounds of the board.
   *
   * @param coords are the coordinates of the hexagon.
   * @return true if the coordinates are in bounds, false otherwise.
   */
  private boolean areCoordinatesInBounds(Coord coords) throws IllegalArgumentException {
    if (coords.row < 0 || coords.row >= this.board.size()) {
      return false;
    }
    boolean colValid = coords.col < 0 || coords.col >= this.board.get(coords.row).size();
    return !colValid;
  }

  /**
   * Determines if coordinates are in the bounds of the board.
   *
   * @param coords are the coordinates of the hexagon.
   * @param board is the board whose coordinates will be validated against.
   * @return true if the coordinates are in bounds, false otherwise.
   */
  boolean areCoordinatesInBounds(Coord coords, List<List<Hexagon>> board)
      throws IllegalArgumentException {
    if (coords.row < 0 || coords.row >= board.size()) {
      return false;
    }
    boolean colValid = coords.col < 0 || coords.col >= board.get(coords.row).size();
    return !colValid;
  }


  /**
   * Determines if a list ends with a specified player.
   *
   * @param list   is the list to be checked.
   * @param player is the player the list should end in.
   * @return true if the list ends with the specified player, false otherwise.
   */
  private boolean listEndsInPlayer(List<Hexagon> list, Player player) {
    if (list.size() == 0) {
      return false;
    }
    return list.get(list.size() - 1).toString().equals(player.toString());
  }

  /**
   * Determines if the list contains at least one different player than the player provided.
   *
   * @param list   is the list of hexagons.
   * @param player is the player.
   * @return true if there exists a hexagon corresponding to a player that isn't the one provided.
   */
  private boolean listContainsDifferentPlayer(List<Hexagon> list, Player player) {
    for (Hexagon hexagon : list) {
      if (!hexagon.toString().equals(Hexagon.EMPTY.toString()) && !hexagon.toString()
          .equals(player.toString())) {
        return true;
      }

    }
    return false;
  }

  /**
   * Checks the current status the gameStarted flag.
   *
   * @return true if the game has started, false otherwise.
   */
  private boolean isGameStarted() {
    return this.gameStarted;
  }

  /**
   * Verifies that the game has started.
   *
   * @throws IllegalStateException if the game has already started.
   */
  void verifyGameHasStarted() {
    if (!isGameStarted()) {
      throw new IllegalStateException("This method cannot be called until the game has started");
    }
  }

  /**
   * Updates the hexagons to the bottom left of the player's move.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if the board changed, false otherwise.
   */
  private boolean moveBottomLeft(Player player, Coord coords) {
    Coord originalCoords = new Coord(coords.row, coords.col);
    List<Hexagon> list = new ArrayList<>();
    List<Integer> rowList = new ArrayList<>();
    List<Integer> colList = new ArrayList<>();

    if (this.buildListForMoveDown(coords, 0, -1, list, rowList, colList, player)) {
      this.updateBoard(player, originalCoords, list, rowList, colList);
      return true;
    }
    return false;
  }

  /**
   * Updates the hexagons to the bottom right of the player's move.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if the board changed, false otherwise.
   */
  private boolean moveBottomRight(Player player, Coord coords) {
    Coord originalCoords = new Coord(coords.row, coords.col);
    List<Hexagon> list = new ArrayList<>();
    List<Integer> rowList = new ArrayList<>();
    List<Integer> colList = new ArrayList<>();

    if (this.buildListForMoveDown(coords, 1, 0, list, rowList, colList, player)) {
      this.updateBoard(player, originalCoords, list, rowList, colList);
      return true;
    }
    return false;
  }

  /**
   * Updates the hexagons to the top left of the player's move.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if the board changed, false otherwise.
   */
  private boolean moveTopLeft(Player player, Coord coords) {
    Coord originalCoords = new Coord(coords.row, coords.col);
    List<Hexagon> list = new ArrayList<>();
    List<Integer> rowList = new ArrayList<>();
    List<Integer> colList = new ArrayList<>();

    if (this.buildListForMoveUp(coords, -1, 0, list, rowList, colList, player)) {
      this.updateBoard(player, originalCoords, list, rowList, colList);
      return true;
    }
    return false;
  }

  /**
   * Updates the hexagons to the top right of the player's move.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if the board changed, false otherwise.
   */
  private boolean moveTopRight(Player player, Coord coords) {
    Coord originalCoords = new Coord(coords.row, coords.col);
    List<Hexagon> list = new ArrayList<>();
    List<Integer> rowList = new ArrayList<>();
    List<Integer> colList = new ArrayList<>();

    if (this.buildListForMoveUp(coords, 0, 1, list, rowList, colList, player)) {
      this.updateBoard(player, originalCoords, list, rowList, colList);
      return true;
    }
    return false;
  }

  /**
   * Updates the hexagons to the right of the player's move.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if the board changed, false otherwise.
   */
  private boolean moveRight(Player player, Coord coords) {
    Coord originalCoords = new Coord(coords.row, coords.col);
    List<Hexagon> list = new ArrayList<>();
    List<Integer> rowList = new ArrayList<>();
    List<Integer> colList = new ArrayList<>();

    if (this.buildListForMoveLaterally(coords, list, rowList, 1, colList, player)) {
      updateBoard(player, originalCoords, list, rowList, colList);
      return true;
    }
    return false;

  }


  /**
   * Updates the hexagons to the left of the player's move.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if the board changed, false otherwise.
   */
  private boolean moveLeft(Player player, Coord coords) {
    Coord originalCoords = new Coord(coords.row, coords.col);
    List<Hexagon> list = new ArrayList<>();
    List<Integer> rowList = new ArrayList<>();
    List<Integer> colList = new ArrayList<>();

    if (this.buildListForMoveLaterally(coords, list, rowList, -1, colList, player)) {
      this.updateBoard(player, originalCoords, list, rowList, colList);
      return true;
    }
    return false;
  }

  /**
   * Updates the board as a move is made.
   *
   * @param player      is the player who is moving.
   * @param originalCoords are the original coordinates of the hexagon.
   * @param list        is the list of hexagons that will be changed.
   * @param rowList     is the list of row idxs that will be changed.
   * @param colList     is the list of col idxs that will be changed.
   */
  private void updateBoard(Player player, Coord originalCoords, List<Hexagon> list,
      List<Integer> rowList, List<Integer> colList) {
    this.changeBoardValue(originalCoords, Hexagon.valueOf(player.toString()));
    for (int i = 0; i < rowList.size(); i++) {
      this.changeBoardValue(new Coord(rowList.get(i), colList.get(i)),
          Hexagon.valueOf(player.toString()));
    }

  }

  /**
   * Makes a move if the move is permissible.
   *
   * @param player is the player to move.
   * @param coords are the coordinates of the hexagon.
   * @return true if a move could be made, false otherwise.
   */
  private boolean makeMoveIfPossible(Player player, Coord coords) {
    this.validateCoordinates(coords);
    if (!this.board.get(coords.row).get(coords.col).toString().equals(Hexagon.EMPTY.toString())) {
      return false;
    }
    boolean success = false;
    if (this.moveBottomRight(player, coords)) {
      success = true;
    }
    if (this.moveTopRight(player, coords)) {
      success = true;
    }
    if (this.moveBottomLeft(player, coords)) {
      success = true;
    }
    if (this.moveTopLeft(player, coords)) {
      success = true;
    }
    if (this.moveLeft(player, coords)) {
      success = true;
    }
    if (this.moveRight(player, coords)) {
      success = true;
    }


    return success;
  }

  /**
   * Helper method that gets the filled hexagons in a line in a given direction.
   *
   * @param coords are the coordinates of the hexagon.
   * @param list    is the last to add the hexagons to.
   * @param rowList is the list of row coordinates.
   * @param colList is the list of col coordinates.
   */
  private boolean buildListForMoveLaterally(Coord coords, List<Hexagon> list,
      List<Integer> rowList, int increment, List<Integer> colList, Player player) {
    int row = coords.row;
    int col = coords.col;
    col += increment;
    while (this.areCoordinatesInBounds(new Coord(row, col))) {
      if (board.get(row).get(col).toString().equals(Hexagon.EMPTY.toString())) {
        break;
      }
      list.add(board.get(row).get(col));
      rowList.add(row);
      colList.add(col);
      col += increment;
    }
    return this.listEndsInPlayer(list, player) && this.listContainsDifferentPlayer(list, player);
  }

  /**
   * Helper method that gets the filled hexagons in a line in a given direction.
   *
   * @param coords are the coordinates of the hexagon.
   * @param col1    is the amount to increment col if the coordinates are in the upper half.
   * @param col2    is the amount to increment col if the coordinates are in the bottom half.
   * @param list    is the last to add the hexagons to.
   * @param rowList is the list of row coordinates.
   * @param colList is the list of col coordinates.
   */
  private boolean buildListForMoveDown(Coord coords, int col1, int col2, List<Hexagon> list,
      List<Integer> rowList, List<Integer> colList, Player player) {
    int row = coords.row;
    int col = coords.col;
    row++;
    if (row <= this.board.size() / 2) {
      col += col1;
    } else {
      col += col2;
    }
    while (this.areCoordinatesInBounds(new Coord(row, col))) {
      if (board.get(row).get(col).toString().equals(Hexagon.EMPTY.toString())) {
        break;
      }
      list.add(board.get(row).get(col));
      rowList.add(row);
      colList.add(col);
      row++;
      if (row <= this.board.size() / 2) {
        col += col1;
      } else {
        col += col2;
      }
    }
    return this.listEndsInPlayer(list, player) && this.listContainsDifferentPlayer(list, player);
  }

  /**
   * Helper method that gets the filled hexagons in a line in a given direction.
   *
   * @param coords are the coordinates of the hexagon.
   * @param col1    is the amount to increment col if the coordinates are in the upper half.
   * @param col2    is the amount to increment col if the coordinates are in the bottom half.
   * @param list    is the last to add the hexagons to.
   * @param rowList is the list of row coordinates.
   * @param colList is the list of col coordinates.
   */
  private boolean buildListForMoveUp(Coord coords, int col1, int col2, List<Hexagon> list,
      List<Integer> rowList, List<Integer> colList, Player player) {
    int row = coords.row;
    int col = coords.col;
    row--;
    if (row < this.board.size() / 2) {
      col += col1;
    } else {
      col += col2;
    }
    while (this.areCoordinatesInBounds(new Coord(row, col))) {
      if (board.get(row).get(col).toString().equals(Hexagon.EMPTY.toString())) {
        break;
      }
      list.add(board.get(row).get(col));
      rowList.add(row);
      colList.add(col);
      row--;
      if (row < this.board.size() / 2) {
        col += col1;
      } else {
        col += col2;
      }
    }
    return this.listEndsInPlayer(list, player) && this.listContainsDifferentPlayer(list, player);
  }
}