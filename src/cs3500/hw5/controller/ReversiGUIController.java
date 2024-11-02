package cs3500.hw5.controller;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.view.ReversiGUI;
import java.util.Objects;


/**
 * Controller for coordinating interactions between the ReversiModel and ReversiGUI. Views utilizing
 * this controller will receive pop-up messages when it is their turn and when they have attempted
 * to make an illegal move.
 */
public class ReversiGUIController implements ReversiController {

  private ReversiGUI view;
  private ReversiModel model;
  private Player player;

  /**
   * Constructs a ReversiGUIController.
   *
   * @param model  the ReversiModel instance
   * @param player the Player instance for the controller
   * @param view   the ReversiGUI instance
   * @throws NullPointerException if any parameter is null
   */
  public ReversiGUIController(ReversiModel model, Player player, ReversiGUI view) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    Objects.requireNonNull(view);

    this.view = view;
    this.model = model;
    this.player = player;

    // Subscribe the controller as a listener to both view and model
    this.view.setFeaturesListener(this);
    this.model.addFeaturesListener(this);
  }

  @Override
  public void notifyPlayerChanged(Player currentPlayer) {
    if (currentPlayer.toString().equals(player.toString())) {
      view.notifyPlayerChanged(currentPlayer);
    }
  }

  @Override
  public void notifyGameOver(Player winner) {
    view.notifyGameOver(winner);
  }

  @Override
  public void notifyBoardUpdated() {
    view.notifyBoardUpdated();
  }

  @Override
  public void notifyIllegalMove(Player player) {
    if (this.player.toString().equals(player.toString())) {
      view.notifyIllegalMove(player);
    }
  }

  @Override
  public void notifyOutOfTurnMove(Player player) {
    if (this.player.toString().equals(player.toString())) {
      view.notifyOutOfTurnMove(player);
    }
  }

  @Override
  public void makeMove(Coord coords) {
    try {
      model.makeMove(player, coords);
    } catch (Exception e) {
      //There need not be any output if the player attempts to make an illegal move since
      //illegal moves are handled by the ModelFeatures class
    }

  }

  @Override
  public void passTurn() {
    try {
      model.passTurn(player);
    } catch (Exception e) {
      //There need not be any output if the player attempts to make an illegal move since
      //illegal moves are handled by the ModelFeatures class
    }
  }
}
