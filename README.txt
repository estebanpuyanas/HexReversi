Overview:
- what problem is the codebase trying to solve?
    This program serves as an implementation of the famous board game "Reversi" with a text-based visual
representation used in order to render/display the game. While most Reversi games traditionally implement
quadrilateral boards, this implementation uses hexagonal grid boards instead.

- What high-level assumptions are made in the code?
    - knowledge needed.
    - proper extensibility.
    - prerequisites for usage.
    In order to have an appropriate understanding of this codebase, the user should be preemptively
familiraze themselves with the rules and gameflow of Reversi, and the Model-view-controller (MVC)
application design in Java. In terms of extesibility, it is envisioned that the user will use the
interfaces (Controller, Player, Model, View) for their own implemenation. None of the actual
implementations(classes) are created with the expecation that they will be extended and/or
inherited in any way.

Quick start:
- Setting up a game:
{
    ReversiModel model;
    ReversiController controller;
    ReversiTextualView view;
    Appendable a = new StringBuilder();
    Readable r = new StringReader("");
    List<List<Hexagon>> board;
    Player p1 = new BasicPlayer(model);
    Player p2 = new BasicPlayer(model);

    model = new BasicReversi();
    controller = new ReversiTextualController(r, a);
    view = new ReversiTextualView(model);

   /**
   * the majority of the methods in BasicReversi.java require that
   * this method is called as a starting point for the game.
   */
    model.startGame(p1,p2,model.getBoard);
    controller.playGame(model, model.getBoard, p1, p2);
  }

Key components:
- Explain highest level components in the system.
- Explain in terms of control flow.
    Considering this is an interface-based application, it is expected that the interfaces serve as the
highest-level components within the codebase. The following list explains the job of each interface,
the classes that implement them, and the controlflow relation between each component:
        - ReversiModel.java:
            * This is the interface that models a game of Reversi. Its main purpose is to maintain
                the integrity of a proper Reversi game(ie., defines rules, paramaters for legal games).
                It is implemented by the class BasicReversi.java. The BasicReversi implementation works
                for boards with a minimum width of 6, and a maximum width of 11.
        - TextualView.java:
            * This is the interface that is responsible for rendering the display for a game of Reversi.
               It has no knowledge regarding the integrity/validity of the game being displayed.
               It is implemented by the class ReversiTextualView.java
        - ReversiController.java:
            * This interface is responsible for dictating the way that a user can interact with the rest
               of the application. Like the TextualView interface, it does not care about the validity of
               the model it interacts with, whilst also not caring in which type of view is being used to
               render the game as long as the controller has no trouble interacting with the two other
               components of the application. Additioanlly, this interface is extensible enough to allow
               different types of controller (textual, command, etc.,). The implementation of this interface
               in this codebase is ReversiTextualController.java and works as a String -textual- based
               controller which take I/O through the command terminal.


Key subcomponents:
Controller:
    Considering this functions as the main component that interacts with the model and the view of the application,
this interface has one method, public void playGame(), which takes in a model, a board, and two players as parameters.
Regardless of how it is implemented, this method exists in order to allow a user to provide input into the application,
call a desired method in the model, and instruct the view on how to properly display this output.

Model:
    The model subpackage contains two main interfaces. On one hand, the ReversiModel interface is in charge of constructing
and executing all the methods that safeguard and enforce the integrity of a legal/valid game. It also allows the user to retreive
specific information about the state of the game. For example, the method public List<List<Hexagon>> getBoard(), allows the user
to generate and retreive a valid board for the game. Similarly, public boolean isGameOver() allows the user to retreive the current state
of the game; true if it is over, false otherwise. All the methods in this interface either mutate the state of the game -provided it is valid-
or allow the user to obtain an observation about the current state of the game.
    The Player interface, contains an overriden toString() method, which returns the string value of the player's enum representation,
a pass() method, which allows a player to pass their turn -which should be the case if they have no legal moves to make- and a
move(int row, int col) method which allows a player to move (fill and flip) the desired piece on the board, specified by the coordinates
provided.

View:
    Like the controller interface, the view interface is rather simple, only requiring one method for its implementation,
public void render() which is in charge of transcribing the current model state to the desired view type.

Source organization:
    The MVC interfaces and class implementations are inside the /src/cs3500.hw5 folder;
further subpackaged, according to the relevant MVC component they partake in implementing:

- The /src/cs3500.hw5/controller contains the ReversiController and ReversiTextualController
  class and interface.

- The /src/cs3500.hw5/model contains the Player and ReversiModel interfaces. The BasicPlayer
  class implements the Player interface. The BasicReversi class implements the ReversiModel interface.
  While the hexagon class does not implement any of the interfaces, it interacts with the BasicReversi
  class in order to classify the current state of a hexagon depending on whether it is filled in by either
  player or whether it is empty.

- The /src/cs3500.hw5/view contains the TextualView interface and the implementing class,
  ReversiTextualView.

    Besides the /src folder, there is also a /test folder which further subpackages three test classes
 in the /test/cs3500.hw.reversi folder. Each test class specifically uses JUnit assertion testing in order
 to verify that the implementation behaves as expected, and that erroneous behavior / improper usage is caught
 and handled appropriately. Below is a simple diagram outlining the organizational structure of this codebase:

 hw5
    - /src
        - /cs3500.hw5
            - /controller
                - /ReversiController.java
                - /ReversiTextualController.java
            - /model
                - /BasicPlayer.java
                - /BasicReversi.java
                - /Hexagon.java
                - /Player.java
                - /ReversiModel.java
            - /view
                - /ReversiTextualView.java
                - /TextualView.java
        - README.txt
    - /test
        - /cs3500.hw5.reversi
            - /ControllerTests.java
            - / ModelTests.java
            - / ViewTests.java

Class invariants throughout this codebase:
1. in BasicPlayer.java there is a field model, according to the constructor this should never be null,
   and it is upheld as it is impossible to instantiate a new player should a model be null.

2. In the reversiTextualView.java class, there is a private final appendable field which is initialized into
   a Printstream. The constructor enforces that this appendable is never null, and the methods that use it,
   such as render(), throw and IOException in order to inforce this class invariant.

-------------------------------------------------------------------------------------
Part 2:
CHANGELOG:
-Added isValidMove(int row, int col, Player player) and hasValidMove(Player player) to the ReversiModel Interface (were previously private helper methods in BasicReversi)
-Created a Coordinate class (Coords.java)
-Changed all classes so that they use the Coordinate class (including test classes)
-Changed getBoard so that it returns a copy of the board instead of the board itself.
-Changed isValidMove so that it would not return that a move is valid if the cell is occupied
-Added makeCopy() to ReversiModel so that strategies need not be model specific.

EXTRA CREDIT:
Extra credit was attempted in this assignment. In addition to all four strategies being implemented (CaptureMostPieces.java, AvoidCellsNextToCorners.java, PrioritizeCorners.java, Minimax.java), a way to combine these strategies was implement (TryTwo.java). All classes can be found in the strategies package.
All tests for the strategies can be found in StrategyTests.
--------------------------------------------------------------------------------------------------------------
Part 3:
USAGE:
The main class of the program is Reversi.java (cs3500\hw5\Reversi.java).
The arguments of the command line fit the following format (arguments are case-insensitive):
<player1_strategy> <player2_strategy>

where <player1_strategy> and <player2_strategy> are one of:
"human": Human player
"capturemostpieces": AI player using the "Capture Most Pieces" strategy
"minimax": AI player using the "Minimax" strategy
"prioritizecorners": AI player using the "Prioritize Corners" strategy
"avoidcellsnexttocorners": AI player using the "Avoid Cells Next to Corners" strategy
"trytwo": AI player combining two strategies

If one or more of the strategies use "trytwo," the user will be further prompted to input what two strategies they want combined.


GAME INSTRUCTIONS:
The user may quit at any time using the "quit" button or the "X" in the top right of the window.
To make a move, the user should click on the hexagon they desire to move to. If the move is illegal or it is not their turn, the player will be notified. NOTE: Clicking on a hexagon is slightly imprecise. Please aim to click in the middle of a hexagon.
To pass their turn, the user should press the letter 'p' on their keyboard.
Robot players will automatically move after the player has moved.
When the game is over, the users will be notified of the winner.

NEW CLASSES (purpose statements can be found in each class's javadoc):
in the controller package:
-ReversiGUIController: Handles the interaction between the Reversi model and the GUI view, allowing for user actions through the GUI.
-ReversiRobotController: Manages the actions of an AI-controlled player within the Reversi game.

In the model package:
-ModelFeatures: Defines a set of features that a Reversi model should provide to controllers.
-PlayerActions: Represents the actions a player (human or robot) can perform in the Reversi game
-RobotPlayer: Represents an AI-controlled player in the Reversi game.

In the view package:
-BasicHexagon, Circle, Hexagon, PlayerCircle: Provides graphical elements used for rendering the Reversi game on a GUI interface.
-ReversiGUI: Defines an interface for a graphical user interface (GUI) for the Reversi game.
-ReversiGUIPanel: Represents the panel that holds the graphical elements for the Reversi game
-ReversiGUIView: Implements the ReversiGUI interface.
-ViewFeatures: Defines a set of features that a view should provide to external controllers.

CHANGELOG FROM PART 2:
-Added missing required functionality, including:
-Rendering the user circles.
-Allowing the user to click on specific hexagons in the GUI.
-Recognizing keyboard input from the user.

-Enhanced the model to add listeners for model-specific actions.
-Enhanced the view to add listeners for model-specific actions.

-Made the model field in ReadOnlyReversi private.
-Made the first and second fields in TryTwo private.

TESTING:
Since this portion relies on a GUI, much of the testing required was done manually (without a test class). Here are (some, but not all) scenarios that were manually tested:
-Make moves in various edge cases/non-trivial parts of the game.
-Passing twice ends a game.
-The game winner is properly selected (including drawed scenarios).
-A robot player will pass if they have no legal moves.
-Human player plays against another human player.
-Human player plays against a robot player (tested all strategies).
-Robot player plays against another robot player (tested all strategies).
-A human will receive a pop up message when it is their turn, when they attempt to make a move out of turn, or when they attempt to make an illegal move.