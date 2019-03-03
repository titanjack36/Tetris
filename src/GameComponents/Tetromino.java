//------------------------------TETROMINO CLASS-------------------------------//
//@author Titanjack
//@project Tetris
//The Tetromino class creates a tetris piece object which contains its position
//along the grid as well as the coordinates of its individual blocks. It can
//move in any direction along the grid and rotate clockwise. The tetris piece
//can also perform a simulated move or rotation, where the block's virtual
//position is updated but can revert back to its original coordinates at any
//time.

package GameComponents;

import java.awt.*;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class Tetromino {

    private int row;
    private int col;
    private int simRow;
    private int simCol;
    private int orientation;
    private int simOrientation;
    private boolean simulation;
    private Color pieceColor;
    private PieceType pieceType;
    private int[][][] pieceLayout;

    public Tetromino(PieceType pieceType, Color pieceColor) {
        this(0, 0, 0, pieceType, pieceColor);
    }

    public Tetromino(int row, int col, PieceType pieceType, Color pieceColor) {
        this(row, col, 0, pieceType, pieceColor);
    }

    public Tetromino(int row, int col, int orientation, PieceType pieceType,
                     Color pieceColor) {

        this.row = row;
        this.col = col;
        this.orientation = orientation;
        simulation = false;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;

        switch(pieceType) {
            //The coordinates of each block composing the tetris piece, which
            //are relative to the position (center) of the tetris piece.
            case I: pieceLayout = new int[][][]{
                    {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                    {{-1, 1}, {0, 1}, {1, 1}, {2, 1}},
                    {{1, -1}, {1, 0}, {1, 1}, {1, 2}},
                    {{2, 0}, {1, 0}, {0, 0}, {-1, 0}}};
                break;
            case J: pieceLayout = new int[][][]{
                    {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                    {{-1, 1}, {-1, 0}, {0, 0}, {1, 0}},
                    {{1, 1}, {0, 1}, {0, 0}, {0, -1}},
                    {{1, -1}, {1, 0}, {0, 0}, {-1, 0}}};
                break;
            case L: pieceLayout = new int[][][]{
                    {{0, -1}, {0, 0}, {0, 1}, {-1, 1}},
                    {{-1, 0}, {0, 0}, {1, 0}, {1, 1}},
                    {{0, 1}, {0, 0}, {0, -1}, {1, -1}},
                    {{1, 0}, {0, 0}, {-1, 0}, {-1, -1}}};
                break;
            case O: pieceLayout = new int[][][]{
                    {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
                    {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
                    {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
                    {{0, 0}, {0, 1}, {1, 0}, {1, 1}}};
                break;
            case S: pieceLayout = new int[][][]{
                    {{-1, 1}, {-1, 0}, {0, 0}, {0, -1}},
                    {{1, 1}, {0, 1}, {0, 0}, {-1, 0}},
                    {{1, -1}, {1, 0}, {0, 0}, {0, 1}},
                    {{-1, -1}, {0, -1}, {0, 0}, {1, 0}}};
                break;
            case T: pieceLayout = new int[][][]{
                    {{0, -1}, {0, 0}, {0, 1}, {1, 0}},
                    {{-1, 0}, {0, 0}, {0, -1}, {1, 0}},
                    {{0, 1}, {0, 0}, {-1, 0}, {0, -1}},
                    {{1, 0}, {0, 0}, {0, 1}, {-1, 0}}};
                break;
            case Z: pieceLayout = new int[][][]{
                    {{-1, -1}, {-1, 0}, {0, 0}, {0, 1}},
                    {{-1, 1}, {0, 1}, {0, 0}, {1, 0}},
                    {{1, 1}, {1, 0}, {0, 0}, {0, -1}},
                    {{1, -1}, {0, -1}, {0, 0}, {-1, 0}}};
                break;
        }
    }

    //Function: Move
    //@param direction          the direction along the grid to move
    //Changes the position values of the tetris piece according to direction
    //specified
    public void move(String direction) {

        simulation = false;
        switch (direction) {
            case "left": col--;
                break;
            case "right": col++;
                break;
            case "up": row--;
                break;
            case "down": row++;
                break;
        }
    }

    //Function: Simulate Move
    //@param direction          the direction along the grid to move
    //Changes the virtual/simulated position values of the tetris piece
    //according to the specified direction
    public void simulateMove(String direction) {

        if (!simulation) {
            simulation = true;
            simRow = row;
            simCol = col;
            simOrientation = orientation;
        }
        switch (direction) {
            case "left": simCol--;
                break;
            case "right": simCol++;
                break;
            case "up": simRow--;
                break;
            case "down": simRow++;
                break;
        }
    }

    //Function: Rotate
    //Rotates the tetris piece once in the clockwise direction
    public void rotate() {

        simulation = false;
        orientation = orientation == 3 ? 0 : (orientation + 1);
    }

    //Function: Rotate
    //Simulates a rotation in the clockwise direction
    public void simulateRotate() {

        if (!simulation) {
            simulation = true;
            simRow = row;
            simCol = col;
            simOrientation = orientation;
        }
        simOrientation = simOrientation == 3 ? 0 : (simOrientation + 1);
    }

    //Function: Reset Simulation
    //Revert back to orignial position before simulated moves
    public void resetSimulation() {
        simulation = false;
    }

    //Function: Apply Simulation
    //Apply the result position and orientation from the simulated moves to the
    //real position and orientation
    public void applySimulation() {

        simulation = false;
        row = simRow;
        col = simCol;
        orientation = simOrientation;
    }

    //Function: Set Position
    //@param row            the row along the grid
    //       col            the column along the grid
    //Directly updates the position values of the tetris piece
    public void setPosition(int row, int col) {

        this.row = row;
        this.col = col;
    }

    //Function: Is Simulated
    //@return               whether the tetris piece has performed a simulated
    //                      move or rotation
    public boolean isSimulated() {
        return simulation;
    }

    //Function: Get Row
    //@return               returns the row position of the tetris piece
    public int getRow() {
        return simulation ? simRow : row;
    }

    //Function: Get Column
    //@return               returns the column position of the tetris piece
    public int getCol() {
        return simulation ? simCol : col;
    }

    //Function: Get Orientation
    //@return               return the orientation of the tetris piece
    public int getOrientation() {

        return simulation ? simOrientation : orientation;
    }

    //Function: Get Coords
    //@return               the coordinates of the individual blocks which make
    //                      up the tetris piece in the form of an array
    //                      Example: {{x, y}, {x, y} ...}
    public int[][] getCoords() {

        int[][] coords = new int[pieceLayout[0].length][2];
        //Type means whether the piece is in the simulated state or not
        int rowType = row, colType = col, orientationType = orientation;
        if (simulation) {
            rowType = simRow;
            colType = simCol;
            orientationType = simOrientation;
        }
        for (int i = 0; i < pieceLayout[0].length; i++) {
            coords[i][0] = rowType + pieceLayout[orientationType][i][0];
            coords[i][1] = colType + pieceLayout[orientationType][i][1];
        }
        return coords;
    }

    //Function: Get Color
    //@return               the color of the tetris piece
    public Color getColor() {
        return pieceColor;
    }

    //Function: Get Type
    //@return               the shape of the current piece
    public PieceType getType() {
        return pieceType;
    }
}
