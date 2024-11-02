package cs3500.hw5.view;

import cs3500.hw5.model.Coord;

/**
 * Represents the features that a view can trigger in response to user actions
 * or game state changes.
 */
public interface ViewFeatures {

  /**
   * Notifies the associated controller or game logic about a move initiated by the
   * user through the view.
   *
   * @param coords the coordinates representing the move initiated by the user
   */
  void makeMove(Coord coords);

  /**
   * Notifies the associated controller or game logic about the user's intention to pass their turn.
   */
  void passTurn();

}
