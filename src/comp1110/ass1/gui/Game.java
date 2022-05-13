package comp1110.ass1.gui;

import comp1110.ass1.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static comp1110.ass1.Direction.*;

/**
 * This class implements a GUI for the Temple Trap game.
 *
 * You should not need to modify any code in this file,
 * but you are welcome to read the code and use any part
 * of it in your second assignment.
 *
 * Some elements of this class exist just to provide
 * examples of how you might want to solve problems in
 * your assignment.
 */
public class Game extends Application {

    private static final int SQUARE_SIZE = 100;
    private static final int MARGIN_X = 60;
    private static final int MARGIN_Y = 30;
    private static final int BOARD_WIDTH = 350;
    private static final int BOARD_HEIGHT = 350;
    private static final int BOARD_MARGIN = 25;
    private static final int OBJECTIVE_WIDTH = 150;
    private static final int OBJECTIVE_HEIGHT = 150;
    private static final int OBJECTIVE_MARGIN_X = 450;
    private static final int OBJECTIVE_MARGIN_Y = 120;
    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_X = MARGIN_X;
    private static final int PLAY_AREA_Y = BOARD_Y + BOARD_MARGIN;
    private static final int PLAY_AREA_X = BOARD_X + BOARD_MARGIN;
    private static final int GAME_WIDTH = BOARD_X + BOARD_WIDTH + MARGIN_X + 180;
    private static final int GAME_HEIGHT = BOARD_Y + BOARD_HEIGHT + MARGIN_Y + 100;
    private static final int PEG_OFFSET = 30;


    /* node groups */
    private final Group root = new Group();
    private final Group gtiles = new Group();
    private final Group solution = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group exposed = new Group();
    private final Group objective = new Group();

    /* where to find media assets */
    private static final String URI_BASE = "assets/";
    private static final String BASEBOARD_URI = Game.class.getResource(URI_BASE + "baseboard.png").toString();

    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
    private static final String LOOP_URI = Game.class.getResource(URI_BASE + "211684__oceanictrancer__classic-house-loop-128-bpm.wav").toString();
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* message on completion */
    private final Text completionText = new Text("Well done!");

    /* the state of the tiles */
    int[] tileState = new int[9];

    /* The underlying game */
    TempleTrap templeTrapGame;

    /* Define a drop shadow effect that we will appy to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    /* Graphical representations of tiles */
    class GUITile extends ImageView {
        char tileID;

        /**
         * Construct a particular playing tile
         *
         * @param tile The letter representing the tile to be created.
         */
        GUITile(char tile) {
            if (tile > 'h' && tile < 'p' || tile < 'a' || tile > 'p') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            this.tileID = tile;
            if (tile == 'p') {
                setFitHeight(SQUARE_SIZE / 2.5);
                setFitWidth(SQUARE_SIZE / 2.5);
            } else {
                setFitHeight(SQUARE_SIZE);
                setFitWidth(SQUARE_SIZE);
            }

            setEffect(dropShadow);
        }

        /**
         * A constructor used to build the objective tile.
         *
         * @param tile The tile to be displayed (one of 80 objectives)
         * @param x    The x position of the tile
         * @param y    The y position of the tile
         */
        GUITile(int tile, int x, int y) {
            if (!(tile <= 60 && tile >= 1)) {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }

            String t = String.format("%02d", tile);
            setImage(new Image(Game.class.getResource(URI_BASE + t + ".png").toString()));

            setFitHeight(OBJECTIVE_HEIGHT);
            setFitWidth(OBJECTIVE_WIDTH);
            setEffect(dropShadow);

            setLayoutX(x);
            setLayoutY(y);
        }
    }

    /**
     * This class extends Tile with the capacity for it to be dragged and dropped,
     * and snap-to-grid.
     */
    class DraggableTile extends GUITile {
        double homeX, homeY;        // The position the tiles will snap back to if a movement is not valid.
        double mouseX, mouseY;      // The last known mouse positions (used when dragging)
        double objX, objY;          // The starting position for the tile in the current objective.

        int orientation;            // 0 - 3
        boolean canMove;

        /**
         * This method depends on getPegPathStep() and canMoveTile() being completed.
         *
         * Determine whether a given tile or peg can be moved.
         *
         * @param tileID the char representing the tile/peg to be moved. ('a' to 'h' or 'p' for peg.)
         *               Note that the 'tileID' is a representation only used by the GUI.
         * @return true if the tile can be moved, false if it can't be moved.
         */
        private boolean canMovePiece(char tileID) {
            if (tileID == 'p') {
                String pathA = templeTrapGame.getPegPathStep(true);
                String pathB = templeTrapGame.getPegPathStep(false);
                return (pathA != null || pathB != null);
            } else
                return templeTrapGame.canMoveTile(Tile.getTileFromID(tileID - 'a'));
        }

        /**
         * Construct a draggable tile/peg
         *
         * @param placement A string describing the placement of the tile eg "N3"
         */
        DraggableTile(String placement) {
            super(placement.charAt(0));
            orientation =  (tileID == 'p') ? 0 : Direction.fromChar(placement.charAt(1)).ordinal();
            int position = Character.getNumericValue(placement.charAt(2));

            Image image = new Image(Game.class.getResource(URI_BASE + tileID + ".png").toString());
            setImage(image);
            /* convert from board position numbers to GUI x and y */
            homeX = objX = (position % 3) * SQUARE_SIZE + PLAY_AREA_X + ((tileID == 'p') ? 25 : 0);
            homeY = objY = (position / 3) * SQUARE_SIZE + PLAY_AREA_Y+ ((tileID == 'p') ? 30 : 0);;
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(orientation * 90);

            setGameBoardPosition();

            /* event handlers */

            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                homeX = getLayoutX(); // want the new "homeX" to be the current tile position so it snaps here.
                homeY = getLayoutY();
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                canMove = canMovePiece(tileID);

            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                if (canMove) {// Don't want to move tile/peg unless it
                    //  can be legally moved.
                    hideCompletion();
                    toFront();
                    double movementX = event.getSceneX() - mouseX;
                    double movementY = event.getSceneY() - mouseY;
                    setLayoutX(getLayoutX() + movementX);
                    setLayoutY(getLayoutY() + movementY);
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                    event.consume();

                }
            });
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
                canMove = false;
            });
        }

        /**
         * Determine whether a path exists between two positions in the current game.
         * @param start
         * @param dest
         * @return
         */
        private boolean doesPathExist(int start, char dest) {
            String pathA = templeTrapGame.getPegPathToDestination(templeTrapGame.board[start], dest, true);
            String pathB = templeTrapGame.getPegPathToDestination(templeTrapGame.board[start], dest, false);
            return pathA != null || pathB != null;
        }

        /**
         * Snap the tile to the nearest grid position (if it is over the grid)
         */
        private void snapToGrid() {
            if (tileID == 'p') {  // moving the peg
                if (onBoard() && validPegLoc()) {
                    int gridX = snapXtoGrid(getLayoutX(), PEG_OFFSET);
                    int gridY = snapYtoGrid(getLayoutY(), PEG_OFFSET);
                    int curPos = getBoardPosition(homeX, homeY);
                    int nextPos = getBoardPosition(gridX, gridY);
                    if (doesPathExist(curPos, Character.forDigit(nextPos, 10))) {
                        snapLayoutToGrid(gridX, gridY);
                        setGameBoardPosition();
                        templeTrapGame.updatePeg(nextPos);
                    }
                    else {
                        snapToHome();
                    }
                } else {
                    snapToHome();
                }
            } else if (onBoard()){  // move a tile
                Tile tile = Tile.getTileFromID(tileID - 'a');
                int gridX = snapXtoGrid(getLayoutX(), 0);
                int gridY = snapYtoGrid(getLayoutY(), 0);
                int prevPos = getBoardPosition(homeX, homeY);
                int curPos = getBoardPosition(gridX, gridY);
                Direction direction = getMoveDir(prevPos, curPos);
                if (TempleTrap.getNextPosition(prevPos, direction) == curPos) {
                    if (templeTrapGame.isTileMovementValid(tile, direction)) {
                        snapLayoutToGrid(gridX, gridY);
                        setGameBoardPosition();
                        templeTrapGame.moveTile(tile, direction);
                    } else {
                        snapToHome();
                    }
                } else {
                    snapToHome();
                }
            }
            checkCompletion();
        }



        /**
         * Given an x location, snap it to the appropriate grid location
         * @param layoutX the x location
         * @param pegOffset the x offset for the peg
         * @return the x location snapped to the appropriate grid location plus the peg offset
         */
        private int snapXtoGrid(double layoutX, int pegOffset) {
            if ((layoutX >= (PLAY_AREA_X - (SQUARE_SIZE / 2))) && (layoutX < (PLAY_AREA_X + (SQUARE_SIZE / 2)))) {
                return PLAY_AREA_X + pegOffset;
            } else if ((layoutX >= PLAY_AREA_X + (SQUARE_SIZE / 2)) && (layoutX < PLAY_AREA_X + 1.5 * SQUARE_SIZE)) {
                return PLAY_AREA_X + SQUARE_SIZE + pegOffset;
            } else if ((layoutX >= PLAY_AREA_X + 1.5 * SQUARE_SIZE) && (layoutX < PLAY_AREA_X + 2.5 * SQUARE_SIZE)) {
                return PLAY_AREA_X + 2 * SQUARE_SIZE + pegOffset;
            }
            assert false;
            return 0;
        }

        /**
         * Given a y location, snap it to the appropriate grid location
         * @param layoutY the y location
         * @param pegOffset the y offset for the peg
         * @return the y location snapped to the appropriate grid location plus the peg offset
         */
        private int snapYtoGrid(double layoutY, int pegOffset) {
            if ((layoutY >= (PLAY_AREA_Y - (SQUARE_SIZE / 2))) && (layoutY < (PLAY_AREA_Y + (SQUARE_SIZE / 2)))) {
                return PLAY_AREA_Y + pegOffset;
            } else if ((layoutY >= PLAY_AREA_Y + (SQUARE_SIZE / 2)) && (layoutY < PLAY_AREA_Y + 1.5 * SQUARE_SIZE)) {
                return PLAY_AREA_Y + SQUARE_SIZE + pegOffset;
            } else if ((layoutY >= PLAY_AREA_Y + 1.5 * SQUARE_SIZE) && (layoutY < PLAY_AREA_Y + 2.5 * SQUARE_SIZE)) {
                return PLAY_AREA_Y + 2 * SQUARE_SIZE + pegOffset;
            }
            assert false;
            return 0;
        }

        /**
         * Set the x and y locations of this graphical element to locations that
         * are snapped to the grid.
         * @param layoutX an x-location to be used to snap to
         * @param layoutY a y0location to be used to snap to
         */
        private void snapLayoutToGrid(double layoutX, double layoutY) {
            setLayoutX(layoutX);
            setLayoutY(layoutY);
        }

        /**
         * Determine the Direction that the tile is moving in.
         * Note that an invalid movement will always return SOUTH,
         * we must account for this elsewhere.
         *
         * @param prevPos the previous position of the tile
         * @param curPos the current position of the tile
         * @return the Direction that the tile is moving in
         */
        private Direction getMoveDir(int prevPos, int curPos) {

            assert prevPos >= 0 && prevPos <= 8 && curPos >= 0 && curPos <= 8;

            int dx = (curPos % 3) - (prevPos % 3);
            int dy = (curPos / 3) - (prevPos / 3);
            if (dx == 1)
                return EAST;
            else if (dx == -1)
                return WEST;
            else if (dy == -1)
                return NORTH;
            else
                return SOUTH;

        }

        /**
         * @return true if the tile is on the board
         */
        private boolean onBoard() {
            return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 3.5 * SQUARE_SIZE))
                    && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
        }

        /**
         * @return true if the tile is on a valid peg location
         */
        private boolean validPegLoc() {
            int x = (int) (getLayoutX() - 30 + (SQUARE_SIZE / 2) - PLAY_AREA_X) / SQUARE_SIZE; //30 = pegXOffset
            int y = (int) (getLayoutY() - 30 + (SQUARE_SIZE / 2) - PLAY_AREA_Y) / SQUARE_SIZE;
            for (int i = 0; i < 8; i++) {

                int loc = tileState[i];
                int tIDx = loc % 3;
                int tIDy = (loc - tIDx) / 3;

                if (tIDx == x && tIDy == y) {
                    if (i > 2) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Snap the tile to its previous valid position.
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
        }

        /**
         * Snap the tile to its starting position in the current objective
         */
        private void snapToStart() {
            setLayoutX(objX);
            setLayoutY(objY);
        }

        /**
         * Convert from x and y coordinates to a position number on
         * the temple trap board.
         * @param x the x position
         * @param y the y position
         * @return the corresponding position number
         */
        private int getBoardPosition(double x, double y) {
            int x2 = (int) (x - PLAY_AREA_X) / SQUARE_SIZE;
            int y2 = (int) (y - PLAY_AREA_Y) / SQUARE_SIZE;
            int pos = (y2 * 3 + x2);

            return pos;
        }

        /**
         * Update the game state with this tile's position
         */
        private void setGameBoardPosition() {
            int pos = getBoardPosition(getLayoutX(), getLayoutY());
            if (tileID == 'p') {
                tileState[8] = pos;
            } else {
                tileState[tileID - 'a'] = pos;
            }
        }

        /**
         * @return the mask placement represented as a string
         */
        public String toString() {
            return "" + tileState[tileID - 'a'];
        }

        /**
         * Check game completion and update status
         */
        private void checkCompletion() {
             if (doesPathExist(templeTrapGame.getPegPosition(), 'F')) {
                showCompletion();
            }
        }
    }

    /**
     * Set up event handlers for the main game
     *
     * @param scene The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                event.consume();
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
            } else if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(1.0);
                gtiles.setOpacity(0);
                event.consume();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(0);
                gtiles.setOpacity(1.0);
                event.consume();
            }
        });
    }

    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.err.println(":-( something bad happened (" + LOOP_URI + "): " + e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }

    /**
     * Set up the group that represents the places that make the board
     */
    private void makeBoard() {
        board.getChildren().clear();

        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(BASEBOARD_URI));
        baseboard.setFitWidth(BOARD_WIDTH);
        baseboard.setFitHeight(BOARD_HEIGHT);
        baseboard.setLayoutX(BOARD_X);
        baseboard.setLayoutY(BOARD_Y);
        board.getChildren().add(baseboard);

        board.toBack();
    }

    /**
     * Set up each of the eight tiles and the peg
     */
    private void makeTiles(String initialState) {
        gtiles.getChildren().clear();
        String[] tiles = initialState.split("(?<=\\G.{2})");
        char pegPos = (initialState.charAt(initialState.length() - 1));
        char[] pegArr = new char[3];
        pegArr[0] = 'p';
        pegArr[1] = '0';
        pegArr[2] = pegPos;
        String pegPlace = new String(pegArr);


        for (int i = 0; i < tiles.length - 1; i++) {
            char tile = (char) ('a' + i);

            String placement = tile +
                    tiles[i];
            gtiles.getChildren().add(new DraggableTile(placement));
        }
        gtiles.getChildren().add(new DraggableTile(pegPlace));
        gtiles.toFront();
    }

    /**
     * Add the objective to the board
     */
    private void addObjectiveToBoard() {
        objective.getChildren().clear();
        objective.getChildren().add(new GUITile(templeTrapGame.getObjective().getProblemNumber(), OBJECTIVE_MARGIN_X, OBJECTIVE_MARGIN_Y));
    }

    /**
     * Put all of the tiles back in their home position
     */
    private void resetPieces() {
        gtiles.toFront();
        for (Node n : gtiles.getChildren()) {
            ((DraggableTile) n).snapToStart();
            if(((DraggableTile) n).tileID == 'p') {
                n.toFront();
            }
        }
    }

    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(BOARD_X + BOARD_MARGIN + 320);
        button.setLayoutY(GAME_HEIGHT - 55);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                restart();
            }
        });
        Button button2 = new Button("New Game");
        button2.setLayoutX(BOARD_X + BOARD_MARGIN + 220);
        button2.setLayoutY(GAME_HEIGHT - 55);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newGame();
            }
        });
        controls.getChildren().add(button);
        controls.getChildren().add(button2);

        difficulty.setMin(1);
        difficulty.setMax(5);
        difficulty.setValue(0);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(1);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(BOARD_X + BOARD_MARGIN + 70);
        difficulty.setLayoutY(GAME_HEIGHT - 50);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(BOARD_X + BOARD_MARGIN);
        difficultyCaption.setLayoutY(GAME_HEIGHT - 50);
        controls.getChildren().add(difficultyCaption);
    }

    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(50);
        completionText.setLayoutY(250);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }

    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }

    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }

    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame(Objective objective) {
        try {
            hideCompletion();
            if (objective == null)
                templeTrapGame = new TempleTrap((int) difficulty.getValue() - 1);
            else
                templeTrapGame = new TempleTrap(objective);
            templeTrapGame.initializeBoardState();
            makeTiles(templeTrapGame.getObjective().getInitialState());
            addObjectiveToBoard();
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. " + e);
            e.printStackTrace();
            Platform.exit();
        }
        resetPieces();
    }
    private void newGame() {
        newGame(null);
    }

    /**
     * Restart this game, creating a new game with the same objective.
     */
    private void restart() {
        newGame(templeTrapGame.getObjective());
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Temple Trap");
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);

        /* add each of the graphical elements */
        root.getChildren().add(gtiles);
        root.getChildren().add(board);
        root.getChildren().add(solution);
        root.getChildren().add(controls);
        root.getChildren().add(exposed);
        root.getChildren().add(objective);

        setUpHandlers(scene);
        setUpSoundLoop();
        makeBoard();
        makeControls();
        makeCompletion();

        /* initialize a new game */
        newGame();

        /* finalize things for JavaFX, ready to show the window */
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
