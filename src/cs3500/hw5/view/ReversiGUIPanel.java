package cs3500.hw5.view;

import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.BasicReversi;
import cs3500.hw5.model.Coord;

import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.strategies.AnyRandomIllegalMove;
import cs3500.hw5.strategies.AnyRandomLegalMove;
import cs3500.hw5.strategies.CaptureMostPieces;
import cs3500.hw5.strategies.ReversiStrategy;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputAdapter;
import javax.swing.JPanel;

import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.ReadOnlyReversiModel;

/**
 * The main class of the panel, which displays the components of the game. This panel is responsible
 * for handling mouse clicks within the panel.
 */
public class ReversiGUIPanel extends JPanel {

  private boolean mouseIsDown;
  boolean bestHint;
  boolean okHint;
  boolean illegalHint;

  ReadOnlyReversiModel model;
  private final Point2D ORIGIN = new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0);
  private final Point2D center = new Point2D.Double();
  private Graphics2D g2D;
  private Graphics graphics;
  private Color color;
  ReversiGUIView parent;
  Player forWhom;

  Coord highlightCoord;

  /**
   * Default panel constructor.
   */
  public ReversiGUIPanel(ReadOnlyReversiModel readableModel) {
    model = readableModel;
    color = Color.white;
    MouseEventsListener mouseListener = new MouseEventsListener();
    this.addMouseListener(mouseListener);
    this.addMouseMotionListener(mouseListener);
    bestHint = false;
    okHint = false;
    illegalHint = false;
  }

  /**
   * Constructor that is aware of its overarching class.
   */
  public ReversiGUIPanel(ReadOnlyReversiModel readableModel, ReversiGUIView parent) {
    model = readableModel;
    color = Color.white;
    this.parent = parent;
    MouseEventsListener mouseListener = new MouseEventsListener();
    this.addMouseListener(mouseListener);
    this.addMouseMotionListener(mouseListener);
    bestHint = false;
    okHint = false;
    illegalHint = false;
  }

  /**
   * Paints a component onto the panel. In the case of Reversi it draws the board.
   *
   * @param graphics the <code>Graphics</code> object to protect
   */
  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    this.graphics = graphics;
    g2D = (Graphics2D) graphics;
    color = Color.BLACK;
    g2D.setColor(color);

    int size = 20;
    int width = size * 2;
    int height = (int) (Math.sqrt(3) * size);

    int[] cellPerRow = {6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6};
    int numRows = cellPerRow.length;
    List<List<Hexagon>> board = model.getBoard();

    // Calculate total height of the hex grid
    int boardHeight = numRows * height;

    g2D.setStroke(new BasicStroke(2));
    ReversiStrategy strat = null;
    if (this.bestHint) {
      strat = new CaptureMostPieces();
    }
    else if (this.okHint) {
      strat = new AnyRandomLegalMove();
    }
    else if (this.illegalHint) {
      strat = new AnyRandomIllegalMove();
    }
    Coord coord = null;
    if (this.bestHint || this.okHint || this.illegalHint) {
      coord = strat.chooseMove(generateTempModel(forWhom), forWhom).get();
    }
    for (int row = 0; row < numRows; row++) {
      int numHexagons = cellPerRow[row];

      // Calculate the offset for each row to share vertices
      int xOffset = (getWidth() - numHexagons * width + width / 2) / 2;
      int yOffset = (getHeight() - boardHeight) / 2 + row * height;

      for (int col = 0; col < numHexagons; col++) {
        int x = xOffset + col * width;

        g2D.translate(x, yOffset);

        BasicHexagon hexagon = new BasicHexagon(23, 23, 23);

        // Draw outline
        color = Color.BLACK;
        g2D.setColor(color);
        g2D.draw(hexagon.creteHexagon(hexagon, ORIGIN));

        // Fill with light gray
        color = Color.LIGHT_GRAY;
        g2D.setColor(color);
        g2D.fill(hexagon);

        //The part that handles highlighting a hexagon has been removed
        //because highlighting a hexagon is not used in our implementation
        //The code for it can be found at the bottom of the readme, and should
        //replace this code if necessary
        color = Color.LIGHT_GRAY;
        g2D.setColor(color);
        g2D.fill(hexagon);


        if (this.bestHint || this.okHint || this.illegalHint) {
          if (coord.row == row && coord.col == col) {
            color = Color.CYAN;
            g2D.setColor(color);
            g2D.fill(hexagon);

            int weight = strat.getMoveWeight(coord);
            g2D.setColor(Color.BLACK);

            // Font size for displaying the weight
            int fontSize = 20;
            g2D.setFont(new Font("Arial", Font.BOLD, fontSize));

            // Calculate position for the weight text
            int textX = xOffset + col * width + width / 2 - fontSize / 2;
            int textY = yOffset + fontSize;

            // Draw the weight as a number on the GUI
            g2D.drawString((weight - 1) + "", -5, -5);

          }
        }

        //Place player circles
        String thisHex = model.getCellContents(new Coord(row, col)).toString();
        if (!thisHex.equals(Hexagon.EMPTY.toString())) {
          PlayerCircle circle = new PlayerCircle(11);
          if (thisHex.equals("X")) {
            g2D.setColor(Color.BLACK);
          } else if (thisHex.equals("O")) {
            g2D.setColor(Color.WHITE);
          }
          g2D.fill(circle.createCircle(circle, new Coord(0, 0)));
        }
        // Reset the transformations for the next hexagon
        g2D.translate(-x, -yOffset);
      }
    }
    bestHint = false;
    okHint = false;
    illegalHint = false;
  }

  /**
   * Shows a hint for the player's best move.
   * @param forWhom is the player.
   */
  public void showBestMove(Player forWhom) {
    this.forWhom = forWhom;
    ReversiModel tempModel = generateTempModel(forWhom);
    Coord bestMove = new CaptureMostPieces().chooseMove(tempModel, forWhom).get();
    this.highlightHint(bestMove);
    bestHint = true;
    this.repaint();
  }

  /**
   * Shows a hint for a legal move, excluding the best move.
   * @param forWhom is the player.
   */
  public void showOkMove(Player forWhom) {
    this.forWhom = forWhom;
    ReversiModel tempModel = generateTempModel(forWhom);
    Coord okMove = new AnyRandomLegalMove().chooseMove(tempModel, forWhom).get();
    this.highlightHint(okMove);
    okHint = true;
    this.repaint();
  }

  /**
   * Shows a hint for an illegal move, excluding the best move.
   * @param forWhom is the player.
   */
  public void showIllegalMove(Player forWhom) {
    this.forWhom = forWhom;
    ReversiModel tempModel = generateTempModel(forWhom);
    Coord illegalMove = new AnyRandomIllegalMove().chooseMove(tempModel, forWhom).get();
    this.highlightHint(illegalMove);
    illegalHint = true;
    this.repaint();
  }

  private ReversiModel generateTempModel(Player forWhom) {
    ReversiModel tempModel = new BasicReversi();
    Player tempPlayer1;
    Player tempPlayer2;
    List<List<Hexagon>> tempBoard = new ArrayList<>();
    List<List<Hexagon>> modelBoard = model.getBoard();
    for (int row = 0; row < modelBoard.size(); row++) {
      tempBoard.add(new ArrayList<>());
      for (int col = 0; col < modelBoard.get(row).size(); col++) {
        tempBoard.get(row).add(model.getCellContents(new Coord(row, col)));
      }
    }

    if (forWhom.toString().equals("X")) {
      tempPlayer1 = new BasicPlayer(tempModel, "X");
      tempPlayer2 = new BasicPlayer(tempModel, "O");
      tempModel.startGame(tempPlayer1, tempPlayer2, tempBoard);
    } else {
      tempPlayer1 = new BasicPlayer(tempModel, "O");
      tempPlayer2 = new BasicPlayer(tempModel, "X");
      tempModel.startGame(tempPlayer2, tempPlayer1, tempBoard);
    }
    return tempModel;
  }

  private void highlightHint(Coord coord) {
    super.paintComponent(graphics);
    int size = 20;
    int width = size * 2;
    int height = (int) (Math.sqrt(3) * size);

    int[] cellPerRow = {6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6};
    int numRows = cellPerRow.length;

    // Calculate total height of the hex grid
    int boardHeight = numRows * height;

    g2D.setStroke(new BasicStroke(2));
    int numHexagons = cellPerRow[coord.row];

    // Calculate the offset for each row to share vertices
    int xOffset = (getWidth() - numHexagons * width + width / 2) / 2;
    int yOffset = (getHeight() - boardHeight) / 2 + coord.row * height;

    int x = xOffset + coord.col * width;
    g2D.translate(x, yOffset);

    BasicHexagon hexagon = new BasicHexagon(2003, 20003, 2003);

    // Draw outline
    color = Color.BLACK;
    g2D.setColor(color);
    g2D.draw(hexagon.creteHexagon(hexagon, ORIGIN));

    // Fill with light gray
    color = Color.CYAN;
    g2D.setColor(color);
    g2D.fill(hexagon);




  }




  /**
   * Obtains the initial size of the window.
   *
   * @return the relevant size.
   */
  private Dimension getPreferredLogicalSize() {
    return new Dimension(90, 90);
  }

  /**
   * Transforms physical coordinates into logical coordinates.
   *
   * @return the relevant coordinate conversion.
   */
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform transform = new AffineTransform();
    Dimension preferredSize = getPreferredLogicalSize();
    transform.scale(1, -1);
    transform.scale(preferredSize.getWidth() / getWidth(), preferredSize.getHeight() / getHeight());
    transform.translate(-getWidth() / 2., -getHeight() / 2.);
    return transform;
  }

  /**
   * A nested class that implements mouse events such as clicks.
   */
  private class MouseEventsListener extends MouseInputAdapter {

    /**
     * Determines whether the mouse has been pressed.
     *
     * @param event the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent event) {
      ReversiGUIPanel.this.mouseIsDown = true;
      this.mouseDragged(event);

      int xCoord = event.getX();
      int yCoord = event.getY();

      ReversiGUIPanel.this.highlightCoord = this.translateScreenCoordsToHexagonCoords(xCoord,
          yCoord);

      Coord coords = null;

      try {
        coords = this.translateScreenCoordsToHexagonCoords(xCoord, yCoord);
      } catch (Exception e) {
        //If an exception is caught, it means that the user clicked outside of the hexagon grid.
        //There is no code is this block because nothing should happen in that case.
      }

      //notify listener
      if (parent != null && coords != null) {
        parent.notifyListenerOfMoveAttempt(coords);
      }
    }

    /**
     * Determines whether the mouse was released.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent event) {
      ReversiGUIPanel.this.mouseIsDown = false;
    }

    /**
     * Determines whether the mouse has been dragged.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent event) {
      Point point = event.getPoint();
      Point2D validPoint = transformPhysicalToLogical().transform(point, null);
      double xCoord = event.getX() - point.getX();
      double yCoord = event.getY() - point.getY();
      point.setLocation(point.x + xCoord, point.y + yCoord);
      repaint();
    }

    private Coord translateScreenCoordsToHexagonCoords(int xCoord, int yCoord) {
      int size = 20; // Size of the hexagon
      int width = size * 2; // Width of the hexagon
      int height = (int) (Math.sqrt(3) * size); // Height of the hexagon (assuming regular hexagons)

      int row = (int) ((yCoord) / height);

      List<List<Hexagon>> modelBoard = ReversiGUIPanel.this.model.getBoard();
      int numHexInRow = modelBoard.get(row).size();
      int maxWidth = modelBoard.get(modelBoard.size() / 2).size();

      int col = (int) ((xCoord - ((maxWidth - numHexInRow) * size)) / width);

      return new Coord(row, col);
    }
  }
}