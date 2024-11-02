package cs3500.hw5.view;

import cs3500.hw5.model.ModelFeatures;
import java.awt.Color;
import java.util.function.Consumer;

import cs3500.hw5.model.Coord;

/**
 * Main interface of the graphical user interface for a game of Reversi.
 * This class handles the aspects of the view not directly related to the game itself
 * (i.e. the "quit" button, keyboard input, the title of the popup, etc.)
 */
public interface ReversiGUI extends ModelFeatures {

  /**
   * Makes the view visible.
   * This method should be called after the view is constructed.
   */
  public void makeVisible();

  /**
   * Provides the view with a callback option to process a command.
   *
   * @param callback the callback object.
   */
  public void setCommandCallback(Consumer<String> callback);

  /**
   * Transmits an error into the view if a command cannot be correctly processed.
   *
   * @param error the error message.
   */
  public void showError(String error);

  /**
   * Signals the view to redraw itself.
   */
  public void refresh();

  /**
   * Signals the view to fill the Hexagon after a move has been made.
   *
   * @param position the position of the Hexagon.
   * @param color the corresponding filling for the Hexagon.
   */
  public void fillHexagon(Coord position, Color color);

  /**
   * Sets the listener to handle Reversi game features and player actions.
   *
   * @param listener The implementation of {@link ViewFeatures}
   *                 responsible for managing player actions and game features.
   *                 When triggered, this listener will receive notifications
   *                 about cell clicks, key presses, or other game-related actions.
   */
  public void setFeaturesListener(ViewFeatures listener);

  /**
   * Allows users to enable hints.
   *
   * @param enableHints is true if hints should be enabled, false if hints should be disabled.
   */
  public void enableHints(boolean enableHints);
}
