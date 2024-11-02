package cs3500.reversi;

import cs3500.hw5.model.Coord;
import cs3500.hw5.model.ModelFeatures;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.view.ReversiGUI;
import cs3500.hw5.view.ViewFeatures;

/**
 * Controller mock used for testing.
 */
public class ControllerMock implements ModelFeatures, ViewFeatures {
  public String lastAction = "";

  public ControllerMock(ReversiModel model, Player player, ReversiGUI view) {
    view.setFeaturesListener(this);
    model.addFeaturesListener(this);
  }

  @Override
  public void notifyPlayerChanged(Player currentPlayer) {
    lastAction = "PLAYER CHANGED";
  }

  @Override
  public void notifyGameOver(Player winner) {
    lastAction = "GAME OVER";
  }

  @Override
  public void notifyBoardUpdated() {
    lastAction = "BOARD UPDATED";
  }

  @Override
  public void notifyIllegalMove(Player player) {
    lastAction = "ILLEGAL MOVE";

  }

  @Override
  public void notifyOutOfTurnMove(Player player) {
    lastAction = "OUT OF TURN MOVE";
  }

  @Override
  public void makeMove(Coord coords) {
    lastAction = "MAKE MOVE";
  }

  @Override
  public void passTurn() {
    lastAction = "PASS TURN";

  }
}
