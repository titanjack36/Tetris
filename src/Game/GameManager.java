//-----------------------------GAME MANAGER CLASS-----------------------------//
//@author Titanjack
//@project Tetris
//The game manager class is composed of two components being the game section
//and the score / info board section. The game manager is responsible for
//controlling the movements of Tetris pieces within the game grid while also
//helping to update info such as score and next pieces.

package Game;

import Grid.GameGrid;
import Pages.HelpScreen;
import Pages.OptionsScreen;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection", "unused"})
public class GameManager extends JPanel {

    private int rows;
    private int cols;
    private double posX;
    private double posY;
    private double width;
    private double height;
    private boolean debug;
    private int renderCycle;

    private GameGrid grid;
    private OptionsScreen optionsScreen;
    private HelpScreen helpScreen;
    private InfoBoard infoBoard;
    private Tetromino currentPiece;
    private Tetromino projectedPiece;
    private TetrominoQueue nextPieces;

    public GameManager(double width, double height, double posX, double posY,
                       double cellSize, int rows, int cols) {

        //The game grid must be of a minimum size
        if (rows < 6) {
            rows = 6;
        }
        if (cols < 6) {
            cols = 6;
        }
        this.rows = rows;
        this.cols = cols;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        grid = new GameGrid(posX, posY, width - 500, height,
                cellSize, rows, cols);
        optionsScreen = new OptionsScreen(posX, posY, width - 500, height);
        helpScreen = new HelpScreen(posX, posY, width - 500, height);
        infoBoard = new InfoBoard(width - 500, posY, 400, height);
        renderCycle = 0;
        debug = false;
    }

    //-----------------------------CORE FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void updateGame()
    //public boolean movePiece(String direction)
    //public void rotatePiece()
    //public void dropPiece()
    //private boolean setTetromino(Game.PieceType pieceType,
    //                                Color pieceColor)
    //private int clearFullRows()
    //private void displayProjection()

    //Function: Update Game
    //Upon call, makes modofications to the objects in the game depending
    //on the state of the game
    public void updateGame() {
        switch (TetrisGame.getGameState()) {
            case INGAME:
                //Make sure grid is visible and that there is always an active
                //tetromino piece on the grid
                if (grid.showGridBlocks()) repaint();
                if (currentPiece == null) {
                    setTetromino(nextPieces.nextPiece(infoBoard));
                }
                break;
            case NEWGAME:
                //Reset everything and go to the ingame state
                grid.clearAllGridCells();
                nextPieces = new TetrominoQueue();
                TetrisGame.restartGame();
                TetrisGame.setGameState(GameState.INGAME);
                currentPiece = null;
                repaint();
                break;
            case PAUSED:
                //Make sure grid is hidden
                if (grid.hideGridBlocks()) repaint();
                break;
        }
    }

    //Function: Move Piece
    //@param direction      which way on the grid the piece will move
    //                      options: {down, left, right, up}
    //@return               whether the tetris piece has hit something
    //Translates the tetris piece in a specified direction along the grid
    public void movePiece(String direction) {

        updateGame();
        if (TetrisGame.getGameState().equals(GameState.INGAME)) {
            //Make a virtual move to see whether the result position will over-
            //lap any inactive blocks or if it will be out of bounds of the
            //grid
            currentPiece.simulateMove(direction);
            int[][] pieceCoords = currentPiece.getCoords();
            boolean hitObstacle = false;
            //Check for collision for each individual block for the tetris piece
            for (int[] coord : pieceCoords) {
                int row = coord[0], col = coord[1];
                hitObstacle = hitObstacle ||
                              ((direction.equals("down") && row == rows) ||
                              (direction.equals("left") && col < 0) ||
                              (direction.equals("right") && col >= cols))
                               || grid.isInactive(row, col);
            }
            //Revert position back to before the simulated move
            currentPiece.resetSimulation();
            pieceCoords = currentPiece.getCoords();

            for (int[] coord : pieceCoords) {
                int row = coord[0], col = coord[1];
                //If piece hits something on the way down, it means it has
                //fallen and is now inactive blocks
                if (hitObstacle && direction.equals("down"))
                    grid.setInactive(row, col, currentPiece.getColor());
                if (!hitObstacle) grid.setEmpty(row, col);
            }
            if (hitObstacle && direction.equals("down")) {
                TetrisGame.addToScore(clearFullRows());
                boolean lose = setTetromino(nextPieces.nextPiece(infoBoard));
                if (lose) TetrisGame.setGameState(GameState.GAMEOVER);
            }
            if (!hitObstacle) {
                currentPiece.move(direction);
                pieceCoords = currentPiece.getCoords();
                //Setting previous coords to empty (above) and setting new
                //coords to active will move the piece on the grid
                for (int[] coord : pieceCoords)
                    grid.setActive(coord[0], coord[1], currentPiece.getColor());
            }
            repaint();
            if (!direction.equals("down")) displayProjection();
        }
    }

    //Function: Rotate Piece
    //Performs a clockwise rotation (if possible) on the active piece in the
    //grid
    public void rotatePiece() {

        updateGame();
        if (TetrisGame.getGameState().equals(GameState.INGAME)) {
            int[][] originalCoords = currentPiece.getCoords();
            //Perform virtual rotation and scan for overlapping areas
            currentPiece.simulateRotate();
            int[][] pieceCoords = currentPiece.getCoords();
            boolean leftConflict, rightConflict, bottomConflict, topConflict;
            do {
                //Specifies where the overlapping occurs with respect to the
                //center of the tetris piece
                leftConflict = rightConflict = bottomConflict = topConflict
                        = false;
                for (int[] coord : pieceCoords) {
                    int row = coord[0], col = coord[1];
                    if (row < 0 || row >= rows || col < 0 || col >= cols ||
                            grid.isInactive(row, col)) {
                        topConflict = topConflict ||
                                row <= currentPiece.getRow();
                        bottomConflict = bottomConflict ||
                                row > currentPiece.getRow();
                        leftConflict = leftConflict ||
                                col <= currentPiece.getCol();
                        rightConflict = rightConflict ||
                                col > currentPiece.getCol();
                        //centerConflict = centerConflict || (col == currentPiece
                        //        .getCol() && row == currentPiece.getRow() ||
                        //        currentPiece.getCol() < 0 || currentPiece.
                        //        getCol() >= cols || currentPiece.getRow() < 0
                        //        || currentPiece.getRow() >= rows);
                    }
                }
                if (debug) {
                    System.out.println("row: " + currentPiece.getRow() +
                            " column: " + currentPiece.getCol());
                    System.out.println("left: " + leftConflict + " right: " +
                            rightConflict + " bottom: " + bottomConflict +
                            " top: " + topConflict);
                }
                //if (!centerConflict) {
                    //The tetris piece will move the opposite way to the
                    //overlapping side in order to avoid it
                    if (leftConflict && !rightConflict)
                        currentPiece.simulateMove("right");
                    else if (rightConflict && !leftConflict)
                        currentPiece.simulateMove("left");
                    pieceCoords = currentPiece.getCoords();
                //}
                //If the two opposite sides are both overlapping existing
                //blocks, then the rotation cannot be performed at all
            } while(/*!centerConflict && */((leftConflict ^ rightConflict)));

            //If the rotation or resultant simulated moves allow the tetris
            //piece to not overlap any existing blocks, then apply the
            //rotation and translations
            if (!(/*centerConflict || */leftConflict || topConflict ||
                    bottomConflict)) {
                for (int[] coord : originalCoords)
                    grid.setEmpty(coord[0], coord[1]);
                for (int[] coord: pieceCoords)
                    grid.setActive(coord[0], coord[1], currentPiece.getColor());
                currentPiece.applySimulation();
                displayProjection();
                repaint();
            } else
                currentPiece.resetSimulation();
        }
    }

    //Function: Drop Piece
    //Instantenously make the active piece hit the ground
    public void dropPiece() {

        updateGame();
        if (projectedPiece != null && TetrisGame.getGameState().equals
                (GameState.INGAME)) {
            //Moves the current piece to the projected piece by setting the
            //current piece as the projected piece
            int[][] pieceCoords = currentPiece.getCoords();
            for (int[] coord: pieceCoords)
                grid.setEmpty(coord[0], coord[1]);
            currentPiece = projectedPiece;
            pieceCoords = currentPiece.getCoords();
            for (int[] coord: pieceCoords)
                grid.setActive(coord[0], coord[1], currentPiece.getColor());
            movePiece("down");
            repaint();
        }
    }

    //Function: Set Tetromino
    //@param pieceType      the shape of the new piece
    //       pieceColor     the corresponding color of the piece
    //@return               whether the new piece is overlapping existing
    //                      inactive blocks
    //Adds a new active dropping piece to the game grid, controllable by the
    //user.
    private boolean setTetromino(Tetromino tetromino) {

        int center = (cols / 2 - 1);
        currentPiece = tetromino;
        currentPiece.setPosition(1, center);
        int[][] pieceCoords = currentPiece.getCoords();
        boolean blocked = false;
        for (int[] coord : pieceCoords) {
            int row = coord[0], col = coord[1];
            //Check for existing inactive blocks that are in the way
            blocked = blocked || grid.isInactive(row, col);
            if (!grid.isInactive(row, col))
                grid.setActive(row, col, tetromino.getColor());
        }
        repaint();
        displayProjection();
        return blocked;
    }

    //Function: Clear Full Rows
    //@return               the resultant score from clearning the full rows
    private int clearFullRows() {

        int numOfRowsCleared = 0;
        for (int row = 0; row < rows; row++) {
            boolean isFull = true;
            for (int col = 0; col < cols; col++)
                isFull = isFull && grid.isInactive(row, col);
            if (isFull) {
                numOfRowsCleared++;
                //For all the rows before the full row, shuffle down any
                //inactive blocks to fill up the cleared row
                for (int prevRow = row - 1; prevRow >= 0; prevRow--) {
                    for (int col = 0; col < cols; col++) {
                        grid.setEmpty(prevRow + 1, col);
                        if (grid.isInactive(prevRow, col)) {
                            grid.setInactive(prevRow + 1, col,
                                    grid.getColor(prevRow, col));
                            grid.setEmpty(prevRow, col);
                        }
                    }
                }
            }
        }

        //Apply scoring according to how many rows are cleared
        int score;
        switch (numOfRowsCleared) {
            case 0: score = 0;
                break;
            case 1: score = 40;
                break;
            case 2: score = 100;
                break;
            case 3: score = 300;
                break;
            default: score = 1200;
                break;
        }
        return score * TetrisGame.getGameLevel();
    }

    //Function: Display Projection
    //Shows a highlight of where the active block will land be if dropped at
    //that instant
    private void displayProjection() {

        int[][] pieceCoords;
        if (projectedPiece != null) {
            //Remove the previous projection from the grid cells
            pieceCoords = projectedPiece.getCoords();
            for (int[] coord: pieceCoords) {
                int row = coord[0], col = coord[1];
                if (grid.isHighlighted(row, col))
                    grid.setEmpty(row, col);
            }
        }

        //Acquire a copy of the current piece
        projectedPiece = new Tetromino(currentPiece.getRow(),
                currentPiece.getCol(), currentPiece.getOrientation(),
                currentPiece.getType(), currentPiece.getColor());

        //Move the projection to the ground
        boolean hitObstacle = false;
        do {
            projectedPiece.move("down");
            pieceCoords = projectedPiece.getCoords();
            for (int[] coord : pieceCoords) {
                int row = coord[0], col = coord[1];
                hitObstacle = hitObstacle || row == rows ||
                        grid.isInactive(row, col);
            }
        } while(!hitObstacle);
        projectedPiece.move("up");
        pieceCoords = projectedPiece.getCoords();
        //Set cells at the location of the projected piece to highlighted mode
        for (int[] coord: pieceCoords) {
            int row = coord[0], col = coord[1];
            if (grid.isEmpty(row, col))
                grid.setHighlighted(coord[0], coord[1], projectedPiece.
                        getColor());
        }
        repaint();
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void setDebug(boolean debug)
    //public void mouseAction(double posX, double posY, double offsetX,
    //                            double offsetY, MouseAction action)
    //public void resetGrid()
    //public void setDimensions(double posX, double posY, double width,
    //                               double height)
    //public void setDimensions(double width, double height)
    //public void paint(Graphics g)
    //public String toString()

    //Function: Set Debug
    //@param debug          yes or no to debug
    //Choose whether or not to print debugging information into the console
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //Relays the position and action of the mouse to be handled by the input
    //handlers for each page that is currently displayed
    public void mouseAction(double posX, double posY, double offsetX,
                            double offsetY, MouseAction action) {
        infoBoard.mouseAction(posX, posY, offsetX, offsetY, action);
    }

    //Function: Reset Grid
    //Sets all grid cells in the game grid to empty
    public void resetGrid() {
        grid.clearAllGridCells();
    }

    //Function: Set Dimensions
    //@param posX           distance from the left window edge
    //       posY           distance from the top window edge
    //       width          the new width of the game space
    //       height         the new height of the new game space
    //Updates the size and position of the game space
    public void setDimensions(double posX, double posY, double width,
                               double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        grid.setGridSize(posX + (width - 500) / 2 -
                grid.getGridWidth() / 2, posY, width - 500, height);
        optionsScreen.setDimensions(posX, posY, width - 500, height);
        helpScreen.setDimensions(posX, posY, width - 500, height);
        infoBoard.setDimensions(width - 500, posY, 400, height);
    }

    //Function: Set Dimensions
    //@param width          the new width of the game space
    //       height         the new height of the new game space
    //Updates the size of the game space while keeping position the same
    public void setDimensions(double width, double height) {
        setDimensions(posX, posY, width, height);
    }

    //Function: Paint
    //@param g              The graphics element
    //Renders the objects corresponding the the current game state
    @Override
    public void paint(Graphics g) {

        super.paintComponent(g);
        switch (TetrisGame.getGameState()) {
            case OPTIONSSCREEN: optionsScreen.paint(g);
                break;
            case HELPSCREEN: helpScreen.paint(g);
                break;
            default: grid.paint(g);
                break;
        }
        infoBoard.paint(g);

        if (TetrisGame.getGameState().equals(GameState.GAMEOVER)) {
            int boxPosX = (int)(posX + (width - 500) / 2 - 150);
            int boxPosY = (int)(posY + height / 2 - 100);
            g.setColor(new Color(70, 70, 70, 176));
            g.fillRect(boxPosX, boxPosY, 300, 200);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("Arial", Font.BOLD, 70));
            g.drawString("GAME", boxPosX + 47, boxPosY + 80);
            g.drawString("OVER", boxPosX + 47, boxPosY + 160);
        }
        renderCycle++;
        if (debug)
            System.out.println(toString());
    }

    //Function: To String
    //@return               A string representation of the game board
    @Override
    public String toString() {

        String gameStr = "Render Cycle [" + renderCycle + "] | Game State ["
                + TetrisGame.getGameState() + "]";
        gameStr += "\nINFO:\n" + infoBoard.toString();
        gameStr += "\nGAME:\n" + grid.toString();
        gameStr += "\n--------------------------------------------------------";
        return gameStr;
    }
}
