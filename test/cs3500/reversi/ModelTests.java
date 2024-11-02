package cs3500.reversi;

import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.BasicReversi;
import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;

import cs3500.hw5.view.ReversiTextualView;
import cs3500.hw5.view.TextualView;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the public methods in the BasicReversi class.
 */
public class ModelTests {

  ReversiModel model;
  List<List<Hexagon>> board;
  Player p1;

  Player p2;

  @Before
  public void init() {
    model = new BasicReversi();
    board = model.getBoard();
    p1 = new BasicPlayer(model, "X");
    p2 = new BasicPlayer(model, "O");
  }

  @Test
  public void testBoardIsCreatedProperly() {
    Assert.assertEquals(board.size(), 11);
    Assert.assertEquals(board.get(0).size(), 6);
    Assert.assertEquals(board.get(5).size(), 11);
    Assert.assertEquals(board.get(10).size(), 6);
    model.startGame(p1, p2, board);
    Assert.assertEquals(board.get(4).get(4).toString(), Hexagon.X.toString());
    Assert.assertEquals(board.get(4).get(5).toString(), Hexagon.O.toString());
    Assert.assertEquals(board.get(5).get(4).toString(), Hexagon.O.toString());
    Assert.assertEquals(board.get(5).get(6).toString(), Hexagon.X.toString());
    Assert.assertEquals(board.get(6).get(4).toString(), Hexagon.X.toString());
    Assert.assertEquals(board.get(6).get(5).toString(), Hexagon.O.toString());
  }

  @Test
  public void testCustomBoardIsCreatedProperly() {
    ReversiModel customReversi = new BasicReversi(13, 2);
    List<List<Hexagon>> board = customReversi.getBoard();
    customReversi.startGame(new BasicPlayer(customReversi, "X"),
        new BasicPlayer(customReversi,"O"), board);
    Assert.assertEquals(board.size(), 23);
    Assert.assertEquals(board.get(0).size(), 2);
    Assert.assertEquals(board.get(11).size(), 13);
    Assert.assertEquals(board.get(22).size(), 2);
    Assert.assertEquals(board.get(10).get(5).toString(), Hexagon.X.toString());
    Assert.assertEquals(board.get(10).get(6).toString(), Hexagon.O.toString());
    Assert.assertEquals(board.get(11).get(5).toString(), Hexagon.O.toString());
    Assert.assertEquals(board.get(11).get(7).toString(), Hexagon.X.toString());
    Assert.assertEquals(board.get(12).get(5).toString(), Hexagon.X.toString());
    Assert.assertEquals(board.get(12).get(6).toString(), Hexagon.O.toString());
  }

  @Test
  public void startGameRejectsNullArgsTest() {
    this.init();
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(null, p2, board));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(p1, null, board));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(p1, p2, null));
  }

  @Test
  public void startGameTwiceThrowsExceptionTest() {
    this.init();
    model.startGame(p1, p2, board);
    model.getScore(p1);
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame(p1, p2, board));
  }

  @Test
  public void passTurnRejectsNotCurrentPlayerPassTest() {
    this.init();
    model.startGame(p1, p2, board);
    Assert.assertThrows(IllegalStateException.class, () -> model.passTurn(p2));
  }

  @Test
  public void makeMoveRejectsNegativeCoordinatesTest() {
    this.init();
    model.startGame(p1, p2, board);
    Assert.assertThrows(IllegalArgumentException.class,
        () -> model.makeMove(p1, new Coord(-15, 20)));
  }

  @Test
  public void makeMoveRejectsNotCurrentPlayerMoveTest() {
    this.init();
    model.startGame(p1, p2, board);
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(p2, new Coord(6, 2)));
  }

  @Test
  public void makeMoveRejectsUnstartedGameTest() {
    ReversiModel model = new BasicReversi();
    Player p1 = new BasicPlayer(model, "X");
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(p1, new Coord(4, 5)));
  }

  @Test
  public void getTurnRejectsUnstartedGameTest() {
    ReversiModel model = new BasicReversi();
    Assert.assertThrows(IllegalStateException.class, model::getTurn);
  }

  @Test
  public void isGameOverRejectsUnstartedGameTest() {
    ReversiModel model = new BasicReversi();
    Assert.assertThrows(IllegalStateException.class, model::isGameOver);
  }

  @Test
  public void isGameOverTrueIfBothPlayersPassTurnTest() {
    this.init();
    model.startGame(p1, p2, board);
    //both players passed in a row, the game comes to an end.
    model.passTurn(p1);
    model.passTurn(p2);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testPassMove() {
    this.init();
    model.startGame(p1, p2, board);
    Assert.assertEquals("Test before mutation", model.getTurn(), p1);
    p1.pass();
    Assert.assertEquals("Test after mutation", model.getTurn(), p2);
    model.passTurn(p2);
    Assert.assertEquals("Test after mutation", model.getTurn(), p1);

  }

  @Test
  public void testMakeMove() {
    this.init();
    model.startGame(p1, p2, board);

    TextualView view = new ReversiTextualView(model);

    Assert.assertThrows("Hexagon too far away from occupied hexagons for move to be legal",
        IllegalStateException.class, () -> p1.move(new Coord(0, 3)));
    Assert.assertThrows("Line of Hexagons do not end in same player",
        IllegalStateException.class, () -> p1.move(new Coord(5, 3)));

    Assert.assertEquals("test before mutation ", model.getScore(p1), 3);
    model.makeMove(p1, new Coord(3, 4));
    Assert.assertEquals("test after mutation ", model.getScore(p1), 5);
    model.makeMove(p2, new Coord(4, 6));
    model.makeMove(p1, new Coord(4, 7));
    model.makeMove(p2, new Coord(6, 3));
    Assert.assertThrows("Line of Hexagons do not contain opposite player",
        IllegalStateException.class, () -> p1.move(new Coord(4, 8)));
    model.makeMove(p1, new Coord(7, 4));
    model.makeMove(p2, new Coord(2, 4));
    model.makeMove(p1, new Coord(6, 2));
    model.makeMove(p2, new Coord(7, 2));
    model.makeMove(p1, new Coord(4, 3));
    Assert.assertEquals("Filling hexagons should not be short circuited "
            + "(i.e. makeMove changes hexagons in all directions, not just the first one.",
        model.getScore(p1), 11);
    model.makeMove(p2, new Coord(5, 2));
    Assert.assertEquals("All moves should have been made", view.toString(),
        "     - - - - - -\n"
            + "    - - - - - - -\n"
            + "   - - - - O - - -\n"
            + "  - - - - O - - - -\n"
            + " - - - X X X X X - -\n"
            + "- - O - X - X - - - -\n"
            + " - - O O X X - - - -\n"
            + "  - - O - X - - - -\n"
            + "   - - - - - - - -\n"
            + "    - - - - - - -\n"
            + "     - - - - - -\n");

    Assert.assertFalse(model.isGameOver());
  }

}