//------------------------------INFO BOARD CLASS------------------------------//
//@author Titanjack
//@project Tetris
//The Info Board class handles all contents to be displayed on the game's
//info board. This includes the queue that contains the next pieces and the
//player's current score.

package Game;

import Grid.GameGrid;
import Input.InfoBoardInput;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection", "unused",
        "FieldCanBeLocal"})
public class InfoBoard extends JPanel {

    private double width;
    private double height;
    private double posX;
    private double posY;
    private GameGrid[] pieceQueue;
    private Tetromino[] nextPieces;
    private InfoBoardInput infoBoardInput;
    private int piecesQueued;
    private int queueSize;

    public InfoBoard(double posX, double posY, double width, double height) {

        queueSize = 2;
        piecesQueued = 0;
        infoBoardInput = new InfoBoardInput(posX, posY, width, height);
        setDimensions(posX, posY, width, height);
        nextPieces = new Tetromino[queueSize];
    }

    //-----------------------------CORE FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void addPieceToQueue(PieceType pieceType, Color pieceColor)
    //public void addToScore(int add)
    //private Tetromino setTetromino(int gridNum, PieceType pieceType,
    //                               Color pieceColor)
    //public void mouseAction(double posX, double posY, double offsetX,
    //                        double offsetY, MouseAction action)

    //Function: Add Piece To Queue
    //@param pieceType          the shape of the piece to be displayed
    //       pieceColor         the color of the piece
    //The pieces will be queued into a game grid array which will display the
    //piece on the screen
    public void addPieceToQueue(PieceType pieceType, Color pieceColor) {

        //When the queue is not yet filled
        if (piecesQueued < queueSize) {
            nextPieces[piecesQueued] = setTetromino(piecesQueued, pieceType,
                    pieceColor);
            piecesQueued++;
        } else {
            //Deleting the image of the previous tetris piece and setting
            //new tetris pieces in the grids
            for (int gridNum = 0; gridNum < queueSize; gridNum++) {

                for (int[] coord : nextPieces[gridNum].getCoords())
                    pieceQueue[gridNum].setEmpty(coord[0], coord[1]);

                if (gridNum == queueSize - 1) {
                    nextPieces[gridNum] = setTetromino(gridNum, pieceType,
                            pieceColor);
                } else {
                    nextPieces[gridNum] = nextPieces[gridNum + 1];
                    setTetromino(gridNum, nextPieces[gridNum + 1].getType(),
                            nextPieces[gridNum + 1].getColor());
                }
            }
        }
    }

    //Function: Set Tetromino
    //@param gridNum            in which grid the tetromino is to be set
    //       pieceType          the shape of the piece to be set to the grid
    //       pieceColor         the color of the piece
    //@return                   an object instance of the tetromino that has
    //                          been set to the grid
    private Tetromino setTetromino(int gridNum, PieceType pieceType,
                                Color pieceColor) {

        Tetromino tetromino = new Tetromino(1, 1, pieceType,
                pieceColor);
        int[][] pieceCoords = tetromino.getCoords();
        for (int[] coord : pieceCoords) {
            int row = coord[0], col = coord[1];
            pieceQueue[gridNum].setActive(row, col, pieceColor);
        }
        repaint();
        return tetromino;
    }

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //Relays the position and action of the mouse to be handled by the info
    //board input handler
    public void mouseAction(double posX, double posY, double offsetX,
                            double offsetY, MouseAction action) {
        infoBoardInput.mouseAction(posX, posY, offsetX, offsetY, action);
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void setDimensions(double posX, double posY, double width,
    //                              double height)
    //public void setDimensions(double width)
    //
    //public void paint(Graphics g)
    //public String toString()

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Modifies the position and dimensions of the info board
    public void setDimensions(double posX, double posY, double width,
                              double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        double gridWidth = width * 0.8;
        infoBoardInput.setDimensions(posX, posY, width, height);
        if (pieceQueue == null) {
            //Two game grids which are made small in size is used to display the
            //upcoming tetromino pieces within the queue
            pieceQueue = new GameGrid[queueSize];
            pieceQueue[0] = new GameGrid(posX + width /
                    2 - gridWidth / 2, posY + 200, gridWidth, 150,
                    0, 3, 4);
            pieceQueue[0].setPaintBackground(false);

            pieceQueue[1] = new GameGrid(posX + width /
                    2 - gridWidth / 2, posY + 380, gridWidth, 150,
                    0, 3, 4);
            pieceQueue[1].setPaintBackground(false);
        } else {
            pieceQueue[0].setGridSize(posX + width / 2 - gridWidth / 2,
                    posY + 200, gridWidth, 150);
            pieceQueue[1].setGridSize(posX + width / 2 - gridWidth / 2,
                    posY + 380, gridWidth, 150);
        }
    }

    //UNUSED
    /*
    //Function: Set Dimensions
    //@param width          new width
    //Modifies the width of the info board while keeping other properties
    //the same
    public void setDimensions(double width) {
        setDimensions(posX, posY, width, height);
    }*/

    //Function: Paint
    //@param g              the graphics component
    //Renders the information to the info panel. This includes the game grids
    //for displaying the next pieces and the score display
    @Override
    public void paint(Graphics g) {

        super.paintComponent(g);
        g.setColor(new Color(114, 114, 114));
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("TETRIS", (int) (posX + 20),
                (int) (posY + 70));

        infoBoardInput.paint(g);
        if (TetrisGame.getGameState().equals(GameState.INGAME) ||
            TetrisGame.getGameState().equals(GameState.GAMEOVER)) {
            g.setColor(new Color(114, 114, 114));
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString("Score  " + TetrisGame.getScore(), (int)(posX + 50),
                    (int) (posY + 610));
            g.drawString("Level  " + TetrisGame.getGameLevel(),
                    (int) (posX + 50), (int) (posY + 680));
        }
        if (TetrisGame.getGameState().equals(GameState.INGAME)) {
            pieceQueue[0].paint(g);
            pieceQueue[1].paint(g);
        }
    }

    //Function: To String
    //@return               a string representation of of the game grid and the
    //                      score
    @Override
    public String toString() {

        String infoStr = "Score: " + TetrisGame.getScore() + "\n";
        String[][] pendingPiecesStr = new String[2][];
        for (int i = 0; i < queueSize; i++)
            pendingPiecesStr[i] = pieceQueue[i].toString().split("\n");

        for (int i = 0; i < pendingPiecesStr[0].length; i++) {
            for (int j = 0; j < queueSize; j++) {
                infoStr = infoStr.concat(pendingPiecesStr[j][i] + "\t");
            }
            infoStr = infoStr.concat("\n");
        }
        return infoStr;
    }
}
