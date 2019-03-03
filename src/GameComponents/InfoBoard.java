//------------------------------INFO BOARD CLASS------------------------------//
//@author Titanjack
//@project Tetris
//The Info Board class handles all contents to be displayed on the game's
//info board. This includes the queue that contains the next pieces and the
//player's current score.

package GameComponents;

import GridComponents.GameGrid;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class InfoBoard extends JPanel {

    private double width;
    private double height;
    private double posX;
    private double posY;
    private GameGrid[] pieceQueue;
    private Tetromino[] nextPieces;
    private int piecesQueued;
    private int queueSize;
    private int score;

    public InfoBoard(double width, double height, double posX, double posY) {

        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        double gridWidth = width * 0.8;
        queueSize = 2;
        piecesQueued = 0;
        //Two game grids which are made small in size is used to display the
        //upcoming tetromino pieces within the queue
        pieceQueue = new GameGrid[queueSize];
        pieceQueue[0] = new GameGrid(gridWidth, 150, posX + width /
                2 - gridWidth / 2, posY + 20, 0, 3, 4);
        pieceQueue[0].setPaintBackground(false);
        pieceQueue[1] = new GameGrid(gridWidth, 150, posX + width /
                2 - gridWidth / 2, posY + 200, 0, 3, 4);
        pieceQueue[1].setPaintBackground(false);
        nextPieces = new Tetromino[queueSize];
    }

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

    //Function: Add To Score
    //@param add                the amount to be added to the score board
    public void addToScore(int add) {

        score += add;
        repaint();
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

    //Function: Paint
    //@param g              the graphics component
    //Renders the information to the info panel. This includes the game grids
    //for displaying the next pieces and the score display
    @Override
    public void paint(Graphics g) {

        super.paintComponent(g);
        pieceQueue[0].paint(g);
        pieceQueue[1].paint(g);
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString(score + "", (int)(posX + 50), (int)(posY + 400));
    }

    //Function: To String
    //@return               a string representation of of the game grid and the
    //                      score
    @Override
    public String toString() {

        String infoStr = "Score: " + score + "\n";
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
