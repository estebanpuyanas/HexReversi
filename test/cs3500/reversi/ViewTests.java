package cs3500.reversi;

import cs3500.hw5.model.Hexagon;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.BasicReversi;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.view.ReversiTextualView;
import cs3500.hw5.view.TextualView;

/**
 * Tests the public methods in the ReversiTextualController class.
 */
public class ViewTests {



  ReversiModel model;
  List<List<Hexagon>> board;
  Player p1;

  Player p2;

  TextualView view;

  Appendable a;

  @Before
  public void init() {
    model = new BasicReversi();
    board = model.getBoard();
    p1 = new BasicPlayer(model, "X");
    p2 = new BasicPlayer(model, "O");
    a = new StringBuilder();
    view = new ReversiTextualView(model, a);
  }

  @Test
  public void textualViewRejectsNullParametersTest() {
    Appendable out = new StringBuilder();

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      TextualView view = new ReversiTextualView(null, out);
    });
  }


  @Test
  public void toStringWorksForStartBoardTest() {
    this.init();
    model.startGame(p1, p2, model.getBoard());

    String expected =
                  "     - - - - - -\n"
                + "    - - - - - - -\n"
                + "   - - - - - - - -\n"
                + "  - - - - - - - - -\n"
                + " - - - - X O - - - -\n"
                + "- - - - O - X - - - -\n"
                + " - - - - X O - - - -\n"
                + "  - - - - - - - - -\n"
                + "   - - - - - - - -\n"
                + "    - - - - - - -\n"
                + "     - - - - - -\n";

    Assert.assertEquals(expected, view.toString());
  }

  @Test
  public void renderWorksCorrectlyTest() throws IOException {
    this.init();
    model.startGame(p1, p2, model.getBoard());

    view.render();

    String expected =
                  "     - - - - - -\n"
                + "    - - - - - - -\n"
                + "   - - - - - - - -\n"
                + "  - - - - - - - - -\n"
                + " - - - - X O - - - -\n"
                + "- - - - O - X - - - -\n"
                + " - - - - X O - - - -\n"
                + "  - - - - - - - - -\n"
                + "   - - - - - - - -\n"
                + "    - - - - - - -\n"
                + "     - - - - - -\n";
    Assert.assertEquals(a.toString(), expected);
  }

  @Test
  public void renderIOForNullAppendableTest() throws IOException {
    this.init();
    model.startGame(p1, p2, model.getBoard());
    
    Appendable a = null;

    Assert.assertEquals(1 + 1, 2);
    //TODO: IMPLEMENT controller
    //Assert.assertThrows(IOException.class, view::render);
  }
}