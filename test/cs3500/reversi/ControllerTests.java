package cs3500.reversi;

import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.BasicReadOnlyReversi;
import cs3500.hw5.model.BasicReversi;
import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReadOnlyReversiModel;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.view.ReversiGUI;
import cs3500.hw5.view.ReversiGUIView;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the public methods in the BasicReversi class.
 */
public class ControllerTests {

  ReversiModel model;
  ReversiGUI viewPlayer1;
  ReversiGUI viewPlayer2;

  ControllerMock controller1;
  ControllerMock controller2;

  ReadOnlyReversiModel readOnlyReversiModel;
  Player player1;
  Player player2;

  @Before
  public void init() {

    model = new BasicReversi();
    readOnlyReversiModel = new BasicReadOnlyReversi(model);
    player1 = new BasicPlayer(model, "X");
    player2 = new BasicPlayer(model, "O");
    viewPlayer1 = new ReversiGUIView(readOnlyReversiModel, player1);
    viewPlayer2 = new ReversiGUIView(readOnlyReversiModel, player2);
    controller1 = new ControllerMock(model, player1, viewPlayer1);
    controller2 = new ControllerMock(model, player2, viewPlayer2);
    model.startGame(player1, player2, model.getBoard());
  }

  @Test
  public void testViewFeatures() {
    this.init();
    //test before mutation:
    Assert.assertEquals(controller1.lastAction, "PLAYER CHANGED");
    Assert.assertEquals(controller2.lastAction, "PLAYER CHANGED");

    //tests out of turn move
    try {
      player2.pass();
    }
    catch (Exception e) {
      //ignore exception
    }
    Assert.assertEquals(controller1.lastAction, "OUT OF TURN MOVE");
    Assert.assertEquals(controller2.lastAction, "OUT OF TURN MOVE");

    //tests player changed
    //NOTE: player changing is coupled with board updating
    // (i.e. one cannot occur without the other)
    //So, this also tests the board updating method
    player1.pass();
    Assert.assertEquals(controller1.lastAction, "PLAYER CHANGED");
    Assert.assertEquals(controller2.lastAction, "PLAYER CHANGED");

    //tests illegal move
    try {
      player2.move(new Coord(0, 0));
    }
    catch (Exception e) {
      //ignore exception
    }
    Assert.assertEquals(controller1.lastAction, "ILLEGAL MOVE");
    Assert.assertEquals(controller2.lastAction, "ILLEGAL MOVE");



  }

  @Test
  public void testMouseClickingWorksProperly() throws AWTException {
    this.init();
    Robot robot = new Robot();
    robot.mouseMove(100, 100);
    robot.mousePress(InputEvent.BUTTON1_MASK); // Press left mouse button
    robot.mouseRelease(InputEvent.BUTTON1_MASK);

    //test that it recognizes that a move has been made
    Assert.assertEquals(controller1.lastAction, "PLAYER CHANGED");
    Assert.assertEquals(controller2.lastAction, "PLAYER CHANGED");
  }

  @Test
  public void testKeyboardInputWorksProperly() throws AWTException {
    this.init();
    Robot robot = new Robot();
    robot.keyPress(KeyEvent.VK_P); // Simulate key press
    robot.keyRelease(KeyEvent.VK_P);

    //test that it recognizes that the turn has been passed
    Assert.assertEquals(controller1.lastAction, "PLAYER CHANGED");
    Assert.assertEquals(controller2.lastAction, "PLAYER CHANGED");
  }
}
