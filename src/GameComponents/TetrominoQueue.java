//---------------------------TETROMINO QUEUE CLASS----------------------------//
//@author Titanjack
//@project Tetris
//The Tetromino Queue class creates and stores new randomized pieces to be
//put into the queue and sent into the game. The randomization of tetrominos
//is completed by shuffling two arrays containing the list of pieces. In this
//case, the pieces are represented by numbers 0 through 6. The pieces are then
//picked between each of the arrays one at a time.

package GameComponents;

import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class TetrominoQueue {

    private int[][] randPieces;
    private int counter;
    private int currentRow;
    private static Tetromino[] nextPieces;

    public TetrominoQueue() {

        randPieces = new int[2][];
        randPieces[0] = getShuffledArray();
        randPieces[1] = getShuffledArray();
        counter = 0;
        currentRow = 0;
    }

    //Function: New Tetromino
    //@param game           an object instance of the current active game
    //@return               whether the piece was placed successfully or if
    //                      there was overlap
    //Places the next tetris piece into the grid
    public Tetromino nextPiece(InfoBoard infoBoard) {

        Tetromino currentPiece;
        PieceType randomPiece;
        if (nextPieces == null) {
            //Populating the next pieces queue at the beginning of the game
            randomPiece = getNextRandomPiece();
            currentPiece = new Tetromino(randomPiece, getColor(randomPiece));
            nextPieces = new Tetromino[2];

            randomPiece = getNextRandomPiece();
            nextPieces[0] = new Tetromino(randomPiece, getColor(randomPiece));
            randomPiece = getNextRandomPiece();
            nextPieces[1] = new Tetromino(randomPiece, getColor(randomPiece));

            infoBoard.addPieceToQueue(nextPieces[0].getType(),
                    nextPieces[0].getColor());
            infoBoard.addPieceToQueue(nextPieces[1].getType(),
                    nextPieces[1].getColor());
        } else {
            //Adding a new random piece to the piece queue and shuffling the
            //pieces in the queue along
            currentPiece = nextPieces[0];
            nextPieces[0] = nextPieces[1];

            randomPiece = getNextRandomPiece();
            nextPieces[1] = new Tetromino(randomPiece, getColor(randomPiece));

            infoBoard.addPieceToQueue(nextPieces[1].getType(),
                    nextPieces[1].getColor());
        }
        return currentPiece;
    }

    //Function: Get Color
    //@param pieceType          the shape of the piece
    //@return                   the color which corresponds with that shape
    //                          piece
    private Color getColor(PieceType pieceType) {

        switch (pieceType) {
            case I: return new Color(0, 255, 255);
            case J: return new Color(0, 0, 255);
            case L: return new Color(255, 165, 0);
            case O: return new Color(255, 255, 0);
            case S: return new Color(0, 255, 0);
            case T: return new Color(170, 0, 255);
            case Z: return new Color(255, 0, 0);
            default: return new Color(0, 0, 0, 0);
        }
    }

    //Function: Get Next Random Piece
    //@return               the next piece within the shuffled array
    //Goes back and forth between the two shuffled arrays to retrieve the next
    //piece within each array. When the end of array is reached, a new shuffled
    //array is generated
    private PieceType getNextRandomPiece() {

        int randSelection;
        randSelection = randPieces[currentRow][counter];
        currentRow = currentRow == 0 ? 1 : 0;

        if (currentRow == 1) {
            counter++;
            if (counter == randPieces[0].length) {
                randPieces[0] = getShuffledArray();
                randPieces[1] = getShuffledArray();
                counter = 0;
            }
        }
        return PieceType.values()[randSelection];
    }

    //Function: Get Shuffled Array
    //@return           a shuffled array
    //Randomly selects indices and swaps the contents at these indices within
    //the array
    private int[] getShuffledArray() {

        int[] arr = {0, 1, 2, 3, 4, 5, 6};
        for (int i = 0; i < arr.length; i++) {
            int rand = (int)(Math.random() * arr.length);
            int temp = arr[i];
            arr[i] = arr[rand];
            arr[rand] = temp;
        }
        return arr;
    }
}
