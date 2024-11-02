package cs3500;

import cs3500.hw5.controller.ReversiController;
import cs3500.hw5.controller.ReversiGUIController;
import cs3500.hw5.controller.ReversiRobotController;
import cs3500.hw5.model.BasicPlayer;
import cs3500.hw5.model.BasicReadOnlyReversi;
import cs3500.hw5.model.BasicReversi;
import cs3500.hw5.model.Player;
import cs3500.hw5.model.ReadOnlyReversiModel;
import cs3500.hw5.model.ReversiModel;
import cs3500.hw5.model.RobotPlayer;
import cs3500.hw5.strategies.AvoidCellsNextToCorners;
import cs3500.hw5.strategies.CaptureMostPieces;
import cs3500.hw5.strategies.Minimax;
import cs3500.hw5.strategies.PrioritizeCorners;
import cs3500.hw5.strategies.ReversiStrategy;
import cs3500.hw5.strategies.TryTwo;
import cs3500.hw5.view.ReversiGUI;
import cs3500.hw5.view.ReversiGUIView;
import java.util.Scanner;

/**
 * Entry point to run a game of Reversi.
 */
public class Reversi {

  /**
   * Initializes a game of Reversi.
   */
  public static void main(String[] args) {
    if (args.length != 4) {
      System.out.println("Usage: <player1_strategy> <player2_strategy> "
          + "<player1_hints_enabled (T/F)> "
          + "<player2_hints_enabled (T/F)>");
      return;
    }

    String player1Strategy = args[0];
    String player2Strategy = args[1];

    boolean player1Hints = false;
    boolean player2Hints = false;

    if (args[2].equals("T")) {
      player1Hints = true;
    }
    if (args[3].equals("T")) {
      player2Hints = true;
    }

    ReversiModel model = new BasicReversi();
    ReadOnlyReversiModel readOnlyModel = new BasicReadOnlyReversi(model);
    Player player1 = createPlayer(model, "X", player1Strategy);
    Player player2 = createPlayer(model, "O", player2Strategy);
    ReversiGUI viewPlayer1 = new ReversiGUIView(readOnlyModel, player1);
    ReversiGUI viewPlayer2 = new ReversiGUIView(readOnlyModel, player2);
    viewPlayer1.enableHints(player1Hints);
    viewPlayer2.enableHints(player2Hints);
    ReversiController controller1 = getControllerFromArgs(model, player1,
        viewPlayer1, player1Strategy);
    ReversiController controller2 = getControllerFromArgs(model, player2,
        viewPlayer2, player2Strategy);
    viewPlayer1.makeVisible();
    viewPlayer2.makeVisible();
    model.startGame(player1, player2, model.getBoard());

  }

  private static Player createPlayer(ReversiModel model, String description, String arg) {
    arg = arg.toLowerCase();
    switch (arg) {
      case "human":
        return new BasicPlayer(model, description);
      case "capturemostpieces":
        return new RobotPlayer(model, description, new CaptureMostPieces());
      case "minimax":
        return new RobotPlayer(model, description, new Minimax());
      case "prioritizecorners":
        return new RobotPlayer(model, description, new PrioritizeCorners());
      case "avoidcellsnexttocorners":
        return new RobotPlayer(model, description, new AvoidCellsNextToCorners());
      case "trytwo":
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first strategy: ");
        ReversiStrategy firstStrategy = getStrategyFromArg(scanner.nextLine().trim());

        System.out.println("Enter second strategy: ");
        ReversiStrategy secondStrategy = getStrategyFromArg(scanner.nextLine().trim());
        return new RobotPlayer(model, description, new TryTwo(firstStrategy, secondStrategy));
      default:
        throw new IllegalArgumentException("Bad argument: " + arg);

    }
  }

  private static ReversiStrategy getStrategyFromArg(String arg) {
    arg = arg.toLowerCase();
    switch (arg) {
      case "capturemostpieces":
        return new CaptureMostPieces();
      case "minimax":
        return new Minimax();
      case "prioritizecorners":
        return new PrioritizeCorners();
      case "avoidcellsnexttocorners":
        return new AvoidCellsNextToCorners();
      case "trytwo":
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first strategy: ");
        ReversiStrategy firstStrategy = getStrategyFromArg(scanner.nextLine().trim());

        System.out.println("Enter second strategy: ");
        ReversiStrategy secondStrategy = getStrategyFromArg(scanner.nextLine().trim());
        return new TryTwo(firstStrategy, secondStrategy);
      default:
        throw new IllegalArgumentException("Bad argument: " + arg);
    }
  }

  private static ReversiController getControllerFromArgs(ReversiModel model, Player player,
      ReversiGUI view, String arg) {
    arg = arg.toLowerCase();
    switch (arg) {
      case "human":
        return new ReversiGUIController(model, player, view);
      case "minimax":
      case "prioritizecorners":
      case "avoidcellsnexttocorners":
      case "trytwo":
        return new ReversiRobotController(model, player, view);
      default:
        throw new IllegalArgumentException("Cannot create controller: " + arg);
    }
  }
}

