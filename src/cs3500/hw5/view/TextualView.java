package cs3500.hw5.view;

import java.io.IOException;

/**
 * Represents an interface for rendering a textual-based view of Reversi.
 * This interface defines the primary method to render a model, such as text or graphics.
 */
public interface TextualView {

  /**
   * Renders a model in some manner (e.g., as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  public void render() throws IOException;

}
