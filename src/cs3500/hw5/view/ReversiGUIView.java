package cs3500.hw5.view;

import cs3500.hw5.model.Coord;

import cs3500.hw5.model.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import cs3500.hw5.model.ReadOnlyReversiModel;

/**
 * Swing-based graphical user interface for a game of Reversi.
 */
public class ReversiGUIView extends JFrame implements ReversiGUI {

  private ReadOnlyReversiModel readbleModel;
  private ReversiGUIPanel guiPanel;
  private Consumer<String> commandCallback;

  private JPanel buttonPannel;
  Player player;

  private boolean enableHints;
  private ViewFeatures featuresListener;


  /**
   * Deafult constructor.
   *
   * @param readableModel the model.
   */
  public ReversiGUIView(ReadOnlyReversiModel readableModel, Player player) {
    super();
    this.readbleModel = readableModel;
    this.enableHints = false;
    this.player = player;

    //this field is unused in implementation, so it is fine being null
    //It is only set to avoid losing java style points
    commandCallback = null;


    KeyPressHandler keyboardListener = new KeyPressHandler();
    this.addKeyListener(keyboardListener);
    this.setFocusable(true);
    this.requestFocusInWindow();
    String title = "Reversi - Player ";

    if (player.toString().equals("X")) {
      title += "X (Black)";
    }
    else {
      title += "O (White)";
    }

    this.setTitle(title);
    this.setSize(500, 500);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setResizable(false);

    this.setLayout(new BorderLayout());
    guiPanel = new ReversiGUIPanel(readableModel, this);
    guiPanel.setBackground(Color.DARK_GRAY);
    JScrollPane scrollPane = new JScrollPane(guiPanel);
    this.add(scrollPane, BorderLayout.CENTER);

    buttonPannel = new JPanel();
    buttonPannel.setLayout(new FlowLayout());
    this.add(buttonPannel, BorderLayout.SOUTH);

    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener((ActionEvent e) -> System.exit(0));
    buttonPannel.add(quitButton);

    this.pack();
  }

  @Override
  public void enableHints(boolean enableHints) {
    this.enableHints = enableHints;
    this.addHintButtons();
  }

  private void addHintButtons() {
    if (enableHints) {
      JButton bestMoveButton = new JButton("Show best move");
      JButton okMoveButton = new JButton("Show ok move");
      JButton illegalMoveButton = new JButton("Show Illegal move");

      bestMoveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          guiPanel.showBestMove(player);
        }
      });

      okMoveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          guiPanel.showOkMove(player);
        }
      });

      illegalMoveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          guiPanel.showIllegalMove(player);
        }
      });

      buttonPannel.add(bestMoveButton);
      buttonPannel.add(okMoveButton);
      buttonPannel.add(illegalMoveButton);

      bestMoveButton.setVisible(true);
    }
  }

  /**
   * Notifies the registered features listener about an attempted move made by the player.
   * If a features listener is registered, it invokes the 'makeMove' method on the listener,
   * passing the provided coordinates as the attempted move.
   *
   * @param coords the coordinates representing the attempted move
   */
  public void notifyListenerOfMoveAttempt(Coord coords) {
    if (featuresListener != null) {
      featuresListener.makeMove(coords);
    }
  }

  /**
   * Sets the listener to handle Reversi game features and player actions.
   *
   * @param listener The implementation of {@link ViewFeatures}
   *                 responsible for managing player actions and game features.
   *                 When triggered, this listener will receive notifications
   *                 about cell clicks, key presses, or other game-related actions.
   */
  public void setFeaturesListener(ViewFeatures listener) {
    this.featuresListener = listener;
  }

  /**
   * Makes the view visible.
   * This method should be called after the view is constructed.
   */
  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  /**
   * Retrieves the preferred size of this view.
   *
   * @return the relevant size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500, 500);
  }

  /**
   * Provides the view with a callback option to process a command.
   *
   * @param callback the callback object.
   */
  @Override
  public void setCommandCallback(Consumer<String> callback) {
    commandCallback = callback;
  }

  /**
   * Transmits an error into the view if a command cannot be correctly processed.
   *
   * @param error the error message.
   */
  @Override
  public void showError(String error) {
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }


  /**
   * Signals the view to redraw itself.
   */
  @Override
  public void refresh() {
    guiPanel.revalidate();
    guiPanel.repaint();
    this.revalidate();
    this.repaint();
  }

  /**
   * Signals the view to fill the Hexagon after a move has been made.
   *
   * @param position the position of the Hexagon.
   * @param color    the corresponding filling for the Hexagon.
   */
  @Override
  public void fillHexagon(Coord position, Color color) {
    PlayerCircle filling = new PlayerCircle(12);
    color = filling.hexagonColor(readbleModel.getTurn());
    filling.createCircle(filling, position);
    refresh();
  }

  @Override
  public void notifyPlayerChanged(Player currentPlayer) {
    JOptionPane.showMessageDialog(this, "It is now your turn, player " + currentPlayer,
        "Player's Turn", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void notifyGameOver(Player winner) {
    String message = "";
    if (winner == null) {
      message = "GAME OVER! DRAW.";
    }
    else {
      message = "GAME OVER! WINNER: PLAYER " + winner;
    }
    // Create a JPanel to display the game over message
    JPanel gameOverPanel = new JPanel(new BorderLayout());
    JLabel gameOverLabel = new JLabel(message, SwingConstants.CENTER);
    gameOverLabel.setPreferredSize(new Dimension(200, 100));
    gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);

    // Add the game over panel to the existing frame
    getContentPane().removeAll(); // Clear existing components
    getContentPane().add(gameOverPanel, BorderLayout.CENTER);
    revalidate(); // Revalidate the frame's content
    repaint(); // Repaint the frame
  }

  @Override
  public void notifyBoardUpdated() {
    this.refresh();
  }

  @Override
  public void notifyIllegalMove(Player player) {
    JOptionPane.showMessageDialog(this, "Illegal Move! Try Again, Player " + player,
        "Illegal Move", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void notifyOutOfTurnMove(Player player) {
    JOptionPane.showMessageDialog(this, "It is not your turn, Player " + player,
        "Not your turn!", JOptionPane.INFORMATION_MESSAGE);
  }

  private class KeyPressHandler implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      // Not used in this case, but required to implement KeyListener
    }

    @Override
    public void keyPressed(KeyEvent e) {
      // Check if the pressed key is 'p'
      if (e.getKeyChar() == 'p') {
        if (featuresListener != null) {
          featuresListener.passTurn();
        }
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      // Not used in this case, but required to implement KeyListener
    }
  }

}