//-----------------------------GAME MANAGER CLASS-----------------------------//
//@author Titanjack
//@project Tetris
//The game manager class is composed of two components being the game section
//and the score / info board section. The game manager is responsible for
//controlling the movements of Tetris pieces within the game grid while also
//helping to update info such as score and next pieces.

package GameComponents;

import GridComponents.GameGrid;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"SpellCheckingInspection"})
public class GameManager extends JPanel {

    private int rows;
    private int cols;
    private GameGrid grid;
    private InfoBoard infoBoard;
    private Tetromino currentPiece;
    private Tetromino projectedPiece;
    private TetrominoQueue nextPieces;
    private boolean gameOver;
    private boolean debug;
    private int renderCycle;

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
        grid = new GameGrid(width - 500, height, posX,
                posY + 100, cellSize, rows, cols);
        infoBoard = new InfoBoard(400, height,
                width - 500, 100);
        nextPieces = new TetrominoQueue();
        renderCycle = 0;
        debug = false;
        gameOver = false;
    }

    //---------------------------CORE FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public boolean setTetromino(GameComponents.PieceType pieceType,
    //                                Color pieceColor)
    //public boolean movePiece(String direction)
    //public void rotatePiece()
    //public void dropPiece()
    //public void infoBoardAddPieceToQueue(GameComponents.PieceType pieceType,
    //                                         Color pieceColor)
    //private int clearFullRows()
    //private void displayProjection()

    //Function: Move Piece
    //@param direction      which way on the grid the piece will move
    //                      options: {down, left, right, up}
    //@return               whether the tetris piece has hit something
    //Translates the tetris piece in a specified direction along the grid
    public boolean movePiece(String direction) {

        if (currentPiece == null) {
            if (!gameOver) setTetromino(nextPieces.nextPiece(infoBoard));
        } else {
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
                infoBoard.addToScore(clearFullRows());
                gameOver = setTetromino(nextPieces.nextPiece(infoBoard));
                if (gameOver) currentPiece = null;
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
        return gameOver;
    }

    //Function: Rotate Piece
    //Performs a clockwise rotation (if possible) on the active piece in the
    //grid
    public void rotatePiece() {

        if (currentPiece != null) {
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

        if (currentPiece != null && projectedPiece != null) {
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

    //Function: Set GameComponents.Tetromino
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
        switch (numOfRowsCleared) {
            case 0: return 0;
            case 1: return 40;
            case 2: return 100;
            case 3: return 300;
            default: return 1200;
        }
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
    //public void paint(Graphics g)
    //public String toString()

    //Function: Set Debug
    //@param debug          yes or no to debug
    //Choose whether or not to print debugging information into the console
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    //Function: Paint
    //@param g              The graphics element
    //Renders the game grid and the info board
    @Override
    public void paint(Graphics g) {

        super.paintComponent(g);
        grid.paint(g);
        infoBoard.paint(g);
        renderCycle++;
        if (debug)
            System.out.println(toString());
    }

    //Function: To String
    //@return               A string representation of the game board
    @Override
    public String toString() {

        String gameStr = "Render Cycle [" + renderCycle + "]";
        gameStr += "\nINFO:\n" + infoBoard.toString();
        gameStr += "\nGAME:\n" + grid.toString();
        gameStr += "\n--------------------------------------------------------";
        return gameStr;
    }
}
