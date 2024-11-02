package cs3500.hw5.controller;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.view.ReversiGUI;
import java.util.Objects;

/**
 * Controller for an automated Reversi player (robot).
 *  Views utilizing this controller will NOT receive pop-up messages when it is their turn and
 *  when they have attempted to make an illegal move. This controller is not intended to have any
 *  human interaction.
 */
public class ReversiRobotController implements ReversiController {

  private ReversiGUI view;
  private Player player;


  /**
   * Constructs a ReversiRobotController.
   * @param model the ReversiModel instance
   * @param player the Player instance representing the robot player
   * @param view the ReversiGUI instance
   * @throws NullPointerException if any parameter is null
   */
  public ReversiRobotController(ReversiModel model, Player player, ReversiGUI view) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    Objects.requireNonNull(view);

    this.view = view;
    this.player = player;

    model.addFeaturesListener(this);
  }


  @Override
  public void notifyPlayerChanged(Player currentPlayer) {
    if (currentPlayer.toString().equals(player.toString())) {
      player.move(new Coord(-1, -1)); //placeholder move
    }
  }

  @Override
  public void notifyGameOver(Player winner) {
    view.notifyGameOver(winner);
  }

  @Override
  public void notifyBoardUpdated() {
    view.refresh();
  }

  @Override
  public void notifyIllegalMove(Player player) {
    //It is impossible for a robot to make an illegal move. So, if the robot implementation is
    //correct, this method will never be called.
    if (this.player.toString().equals(player.toString())) {
      view.notifyIllegalMove(player);
    }
  }

  @Override
  public void notifyOutOfTurnMove(Player player) {
    //It is impossible for a robot to make an out of turn move. So, if the robot implementation is
    //correct, this method will never be called.
    if (this.player.toString().equals(player.toString())) {
      view.notifyOutOfTurnMove(player);
    }
  }

  @Override
  public void makeMove(Coord coords) {
    //This method has no use in this implementation.
  }

  @Override
  public void passTurn() {
    //This method has no use in this implementation.
  }
}
