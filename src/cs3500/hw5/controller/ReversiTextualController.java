package cs3500.hw5.controller;


import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.List;

/**
 * Textual controller for a game of Reversi.
 */
public class ReversiTextualController {

  //Uncomment when implementing, for now it fails java style.
  //private Appendable appendable;
  //private Scanner sc;
  //private ReversiModel model;


  /**
   * Represents the controller for a text-based game of Reversi.
   * @param r is readable output
   * @param a is appendable output
   */
  public ReversiTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Argument(s) cannot be null");
    }
    //Uncomment when implementing, for now it fails java style.
    //this.appendable = a;
    //this.sc = new Scanner(r);
  }

  public void playGame(ReversiModel model, List<List<Hexagon>> board, Player player1,
                       Player player2) {
    //TODO: IMPLEMENT A TEXTUAL CONTROLLER WHEN NEEDED :)
  }
}
