package cs3500.reversi;

import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.BasicReversi;
import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;

import cs3500.hw5.strategies.AvoidCellsNextToCorners;
import cs3500.hw5.strategies.CaptureMostPieces;
import cs3500.hw5.strategies.Minimax;
import cs3500.hw5.strategies.PrioritizeCorners;
import cs3500.hw5.strategies.TryTwo;
import cs3500.hw5.view.ReversiTextualView;
import cs3500.hw5.view.TextualView;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests various strategies relating to Basic Reversi.
 */
public class StrategyTests {

  ReversiModel model;
  ReversiModel midgameModel;

  ReversiModel cornerModel;
  List<List<Hexagon>> board;
  List<List<Hexagon>> midgameBoard;

  List<List<Hexagon>> cornerBoard;
  Player p1;
  Player p2;
  Player midgameP1;
  Player midgameP2;
  Player cornerP1;
  Player cornerP2;
  TextualView view;
  TextualView midgameView;
  TextualView cornerView;
  Appendable out;
  ReversiModel mockModel;
  Player mockP1;
  Player mockP2;

  @Before
  public void init() {
    model = new BasicReversi();
    midgameModel = new BasicReversi();
    cornerModel = new BasicReversi();
    board = model.getBoard();
    midgameBoard = midgameModel.getBoard();
    cornerBoard = this.generateCorner();
    p1 = new BasicPlayer(model, "X");
    p2 = new BasicPlayer(model, "O");
    midgameP1 = new BasicPlayer(model, "X");
    midgameP2 = new BasicPlayer(model, "O");
    cornerP1 = new BasicPlayer(model, "X");
    cornerP2 = new BasicPlayer(model, "O");
    model.startGame(p1, p2, board);
    midgameModel.startGame(midgameP1, midgameP2, midgameBoard);
    cornerModel.startGame(cornerP1, cornerP2, cornerBoard);
    view = new ReversiTextualView(model);
    midgameView = new ReversiTextualView(midgameModel);
    cornerView = new ReversiTextualView(cornerModel);
    this.generateMidgame();
    out = new StringBuilder();
    mockModel = new ReversiMockForStrategies(new StringReader(""), out);
    mockP1 = new BasicPlayer(mockModel, "X");
    mockP2 = new BasicPlayer(mockModel, "O");
    mockModel.startGame(mockP1, mockP2, mockModel.getBoard());
  }

  @Test
  public void testBasicInputUsingMock() {
    this.init();
    Optional<Coord> bestMove = new CaptureMostPieces().chooseMove(mockModel, mockP1);

    //After forcing (3,3) to be the best move, the strategy should get (3,3)
    Assert.assertEquals(bestMove.get().row, 3);
    Assert.assertEquals(bestMove.get().col, 3);

    bestMove = new Minimax().chooseMove(mockModel, mockP1);

    //The same is not true if minimax is used
    //REASON: If player X captures (3,3) worth 100, then player O captures (3,3) worth 100.
    //If player X moves anywhere else, then player O goes to (3,3) anyway.
    //Hence, every possible move results in player X being down by 100. So, the player
    //chooses the most upper-left position
    Assert.assertEquals(bestMove.get().row, 0);
    Assert.assertEquals(bestMove.get().col, 0);
    Assert.assertTrue("Minimax should consider Player O's moves",
        out.toString().contains("Player O"));
  }

  @Test
  public void testMockFindsAllCornersForReleveantStrategies() {
    Optional<Coord> bestMove = new AvoidCellsNextToCorners().chooseMove(mockModel, mockP1);
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (0, 0) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (0, 5) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (5, 0) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (5, 10) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (10, 0) deemed INVALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (10, 5) deemed INVALID"));

    bestMove = new AvoidCellsNextToCorners().chooseMove(mockModel, mockP1);
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (0, 0) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (0, 5) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (5, 0) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (5, 10) deemed VALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (10, 0) deemed INVALID"));
    Assert.assertTrue(
        out.toString().contains("RUNNING isLegalMove(): Move at (10, 5) deemed INVALID"));
  }


  @Test
  public void testBoardCornersAreObtainedProperly() {
    this.init();
    List<Coord> corners = new CaptureMostPieces().getCorners(board);
    Assert.assertEquals(corners.size(), 6);
    Assert.assertTrue(corners.contains(new Coord(0, 0)));
    Assert.assertTrue(corners.contains(new Coord(0, 5)));
    Assert.assertTrue(corners.contains(new Coord(5, 0)));
    Assert.assertTrue(corners.contains(new Coord(5, 10)));
    Assert.assertTrue(corners.contains(new Coord(10, 0)));
    Assert.assertTrue(corners.contains(new Coord(10, 5)));
  }


  @Test
  public void testPrioritizeUpperleftInDraws() {
    this.init();
    //Out of all possible moves, they would all result in the same score gained.
    //Hence, this should return the move closest to the upper left.
    Optional<Coord> bestMove = new CaptureMostPieces().chooseMove(model, p1);
    Assert.assertEquals(bestMove.get().row, 4);
    Assert.assertEquals(bestMove.get().col, 3);
  }

  @Test
  public void testCaptureMostPiecesCorrectlySelectsBestMove() {
    this.init();

    //In this scenario, the best move would result in a score increase of two,
    //All other moves would result in a score increase of 1.
    Optional<Coord> bestMove = new CaptureMostPieces().chooseMove(midgameModel, midgameP1);
    Assert.assertEquals(bestMove.get().row, 2);
    Assert.assertEquals(bestMove.get().col, 4);
  }

  @Test
  public void testAvoidCellsNextToCorners() {
    this.init();

    //There is only one legal move, though the strategy should ignore it since
    //it is next to a corner
    Assert.assertEquals(new AvoidCellsNextToCorners().chooseMove(cornerModel, cornerP1),
        Optional.empty());

    //Tests that it correctly selects the best move when not near corners
    Optional<Coord> bestMove = new AvoidCellsNextToCorners().chooseMove(midgameModel, midgameP1);
    Assert.assertEquals(bestMove.get().row, 2);
    Assert.assertEquals(bestMove.get().col, 4);
  }

  @Test
  public void testPrioritizeCorners() {
    this.init();

    //There should be no moves near corners
    Assert.assertEquals(new PrioritizeCorners().chooseMove(cornerModel, cornerP1),
        Optional.empty());

    //Tests that it correctly selects the corner move
    Optional<Coord> bestMove = new PrioritizeCorners().chooseMove(cornerModel, cornerP2);
    Assert.assertEquals(bestMove.get().row, 0);
    Assert.assertEquals(bestMove.get().col, 1);
  }

  @Test
  public void testMinimax() {
    this.init();

    //first move is the same as the one determined by CaptureMostPieces
    Optional<Coord> bestMove = new Minimax().chooseMove(model, p1);
    Assert.assertEquals(bestMove.get().row, 4);
    Assert.assertEquals(bestMove.get().col, 3);
    model.makeMove(p1, new Minimax().chooseMove(model, p1).get());

    //second move is different from the one determined by CaptureMostPieces
    bestMove = new Minimax().chooseMove(model, p2);
    Assert.assertEquals(bestMove.get().row, 6);
    Assert.assertEquals(bestMove.get().col, 3);
  }

  @Test
  public void testTryTwo() {
    this.init();

    //AvoidCellsNextToCorners finds no valid moves while CaptureMostPieces does.
    Optional<Coord> bestMove = new TryTwo(new AvoidCellsNextToCorners(), new CaptureMostPieces()).
        chooseMove(cornerModel, cornerP1);
    Assert.assertEquals(bestMove.get().row, 3);
    Assert.assertEquals(bestMove.get().col, 0);

    //finds the best move out of all the strategies
    bestMove = new TryTwo(new TryTwo(new Minimax(),
        new TryTwo(new CaptureMostPieces(), new AvoidCellsNextToCorners())),
        new PrioritizeCorners()).
        chooseMove(cornerModel, cornerP2);
    Assert.assertEquals(bestMove.get().row, 0);
    Assert.assertEquals(bestMove.get().col, 1);
  }


  private void generateMidgame() {
    for (int i = 0; i < 5; i++) {
      midgameModel.makeMove(midgameP1,
          new CaptureMostPieces().chooseMove(midgameModel, midgameP1).get());
      midgameModel.makeMove(midgameP2,
          new CaptureMostPieces().chooseMove(midgameModel, midgameP2).get());
    }
  }

  private List<List<Hexagon>> generateCorner() {
    List<List<Hexagon>> board = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      board.add(new ArrayList<>());
    }
    for (int i = 0; i < 2; i++) {
      board.get(0).add(Hexagon.EMPTY);
    }
    for (int i = 0; i < 3; i++) {
      if (i == 1) {
        board.get(1).add(Hexagon.X);
      } else {
        board.get(1).add(Hexagon.EMPTY);
      }
    }
    for (int i = 0; i < 4; i++) {
      if (i == 1) {
        board.get(2).add(Hexagon.O);
      } else {
        board.get(2).add(Hexagon.EMPTY);
      }
    }
    for (int i = 0; i < 3; i++) {
      if (i == 1) {
        board.get(3).add(Hexagon.X);
      } else if (i == 2) {
        board.get(3).add(Hexagon.X);
      } else {
        board.get(3).add(Hexagon.EMPTY);
      }
    }
    for (int i = 0; i < 2; i++) {
      if (i == 1) {
        board.get(4).add(Hexagon.O);
      } else {
        board.get(4).add(Hexagon.EMPTY);
      }
    }

    return board;
  }


}
