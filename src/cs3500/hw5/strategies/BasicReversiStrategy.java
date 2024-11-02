package cs3500.hw5.strategies;

import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.Coord;
import cs3500.hw5.model.Hexagon;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReversiModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Is a reversi strategy containing helper methods that would be useful in other basic reversi
 * strategies.
 */
public abstract class BasicReversiStrategy implements ReversiStrategy {

  //represents the "weight" of each move. The weight is how "good" a move is.
  //A move with a higher weight is better than a move with a lower weight.
  Map<Coord, Integer> weights;

  int highestWeight;

  /**
   * Constructor method. Creates a blank hashmap and sets the highest weight to the smallest
   * possible integer.
   */
  public BasicReversiStrategy() {
    this.resetData();
  }

  /**
   * Resets the values in this object so that it can be reused.
   */
  public void resetData() {
    this.weights = new HashMap<>();
    this.highestWeight = Integer.MIN_VALUE;
  }

  @Override
  public Optional<Coord> chooseMove(ReversiModel model, Player forWhom) {
    return Optional.empty();
  }

  @Override
  public int getMoveWeight(Coord move) {
    return weights.get(move);
  }

  /**
   * Gets all possible moves for a given player.
   *
   * @param model   is the model where the moves would be made.
   * @param forWhom is the player who would make the move.
   * @return the list of legal moves.
   */
  public List<Coord> getAllPossibleMoves(ReversiModel model, Player forWhom) {
    List<Coord> possibleMoves = new ArrayList<>();
    for (int row = 0; row < model.getBoard().size(); row++) {
      for (int col = 0; col < model.getBoard().get(row).size(); col++) {
        if (model.isLegalMove(new Coord(row, col), forWhom)) {
          possibleMoves.add(new Coord(row, col));
        }
      }
    }
    return possibleMoves;
  }

  /**
   * Gets the coordinates of the corners in a board.
   *
   * @param board is the board whose corner coordinates are desired.
   * @return the list of corners.
   */
  public List<Coord> getCorners(List<List<Hexagon>> board) {
    List<Coord> corners = new ArrayList<>();

    //top corners
    corners.add(new Coord(0, 0));
    corners.add(new Coord(0, board.get(0).size() - 1));

    //middle "corners"
    corners.add(new Coord(board.size() / 2, 0));
    corners.add(new Coord(board.size() / 2, board.get(board.size() / 2).size() - 1));

    //bottom corners
    corners.add(new Coord(board.size() - 1, 0));
    corners.add(new Coord(board.size() - 1, board.get(board.size() - 1).size() - 1));

    return corners;
  }

  /**
   * Starts a game of Reversi and forces it to be the turn of the player provided.
   *
   * @param model   is the game that should be started.
   * @param forWhom is the player whose turn it should be.
   * @param board   is the starting board of the game.
   */
  public void startTempGame(ReversiModel model, Player forWhom, List<List<Hexagon>> board) {
    if (forWhom.toString().equals("X")) {
      model.startGame(forWhom, new BasicPlayer(model, "O"), board);
    } else {
      Player temp = new BasicPlayer(model, "X");
      model.startGame(temp, forWhom, board);
      model.passTurn(temp);
    }
  }

  /**
   * Gets the opponent of a given player.
   *
   * @param model   is the model the players are a part of.
   * @param forWhom is the player to find the opponent of.
   * @return a copy of the other player in a game of reversi.
   */
  public Player getOpponent(ReversiModel model, Player forWhom) {
    if (forWhom.toString().equals("X")) {
      return new BasicPlayer(model, "O");
    } else {
      return new BasicPlayer(model, "X");
    }
  }

  /**
   * Updates the highest weight so that it accurately reflects the move with the best weight.
   *
   * @param newWeight is the potential new highest weight.
   */
  public void updateHighestWeight(int newWeight) {
    if (newWeight > this.highestWeight) {
      this.highestWeight = newWeight;
    }
  }

  /**
   * Gets the move with the highest weight. In the case of a tie, gets the move in the most upper
   * left.
   *
   * @return the move with the highest weight.
   */
  public Optional<Coord> getMoveWithHighestWeight() {
    List<Coord> highestMoves = new ArrayList<>();
    for (Coord coord : weights.keySet()) {
      if (weights.get(coord).equals(highestWeight)) {
        highestMoves.add(coord);
      }
    }
    return getMostUpperleftCoord(highestMoves);
  }

  /**
   * If a given move were to be made, returns how much score increase it would cause.
   *
   * @param model   is the game where the move would be made.
   * @param forWhom is the player who would make the move.
   * @param move    is the move to be made.
   * @return the potential gain in score after a player would make a move.
   */
  public int getScoreGainedWithMove(ReversiModel model, Player forWhom, Coord move) {
    ReversiModel tempModel = model.makeCopy();
    this.startTempGame(tempModel, forWhom, new ArrayList<>(model.getBoard()));
    int startingScore = tempModel.getScore(forWhom);
    tempModel.makeMove(forWhom, move);
    int endingScore = tempModel.getScore(forWhom);
    return endingScore - startingScore;
  }

  /**
   * For a list of coordinates, gets the one closest to the top left corner.
   *
   * @param coords is the list of coordinates.
   * @return the Coord closest to the top left corner.
   */
  private Optional<Coord> getMostUpperleftCoord(List<Coord> coords) {
    Optional<Coord> bestCoord = Optional.empty();
    double closestDistance = Integer.MAX_VALUE;

    for (Coord coord : coords) {
      double tempDistance = this.calculateDistance(0, 0, coord.row, coord.col);
      if (tempDistance < closestDistance) {
        bestCoord = Optional.of(coord);
        closestDistance = tempDistance;
      }
    }
    return bestCoord;
  }


  /**
   * Calculates the distance between two points.
   *
   * @param x1 is the x value of the first coordinate.
   * @param y1 is the y value of the first coordinate.
   * @param x2 is the x value of the second coordinate.
   * @param y2 is the y value of the second coordinate.
   * @return the distance between the two sets of coordinates.
   */
  private double calculateDistance(int x1, int y1, int x2, int y2) {
    int deltaX = x2 - x1;
    int deltaY = y2 - y1;
    return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
  }

}
