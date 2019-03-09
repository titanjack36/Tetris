//-----------------------------TETRIS GAME CLASS------------------------------//
//@author TitanJack
//@version 0.2 (2019-03-08)
//Tetris is a puzzle game developed by Alexey Pajitnov in 1984. This is a
//remake of the game coded independently by Titanjack. The Tetris logo and
//designs for the game are properties of The Tetris Company, LCC and this
//open source remake is meant as a side project which is not for profit.

package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({"WeakerAccess"})
public class TetrisGame {

    private static GameState gameState;
    private static GameState lastGameState;
    private static JFrame window;
    private static int gameCounter;
    private static int gameLevel;
    private static int breakInterval;
    private static int score;
    private static GameManager game;

    public static void main(String[] args) {

        double windowWidth = 1000, windowHeight = 1200;
        window = new JFrame();
        game = new GameManager(windowWidth, windowHeight
                - 100, 0, 0, 0, 20, 10);
        window.add(game);
        window.setSize(new Dimension((int)windowWidth, (int)windowHeight));
        window.setTitle("TETRIS V0.2");
        window.setVisible(true);

        double offsetX = window.getInsets().left;
        double offsetY = window.getInsets().top;
        gameState = GameState.MAINMENU;
        gameLevel = 0;
        gameCounter = 0;
        score = 0;
        breakInterval = 1000;
        //game.setDebug(true);
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    game.movePiece("down");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    game.rotatePiece();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    game.movePiece("left");
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    game.movePiece("right");
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    game.dropPiece();
                }
            }
        });

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                game.mouseAction(e.getX(), e.getY(), offsetX, offsetY,
                        MouseAction.RELEASE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                game.mouseAction(e.getX(), e.getY(), offsetX, offsetY,
                        MouseAction.CLICK);
            }
        });

        window.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                game.mouseAction(e.getX(), e.getY(), offsetX, offsetY,
                        MouseAction.HOVER);
            }
        });

        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Rectangle newWindow = window.getBounds();
                game.setDimensions(newWindow.getWidth(),
                        newWindow.getHeight() - 100);
            }
        });

        Timer blockFallTimer = new Timer(breakInterval, null);
        blockFallTimer.addActionListener(e -> {
            //Allows the block to fall every second
            game.movePiece("down");

            //Increases the game level and the speed at which the tetris block
            //falls
            if (gameState.equals(GameState.INGAME)) {
                if (gameLevel < 15 && gameCounter % 40 == 0) {
                    incrementGameLevel();
                    decreaseBreakInterval();
                }
                incrementGameCounter();
            }
        });
        blockFallTimer.start();

        Timer gameTickTimer = new Timer(1, null);
        gameTickTimer.addActionListener(e -> game.updateGame());
        gameTickTimer.start();
    }

    //Function: Restart Game
    //Resets all game values and the game grid
    public static void restartGame() {
        gameLevel = 0;
        gameCounter = 0;
        score = 0;
        breakInterval = 1000;
        resetGameGrid();
    }

    //Function: Repaint Game
    //Re-renders the contents of the game
    public static void repaintGame() {
        game.repaint();
    }

    //Function: Reset Game Grid
    //Sets all grid cells in the game grid to empty
    public static void resetGameGrid() {
        game.resetGrid();
        repaintGame();
    }

    //Function: Increment Game Counter
    //Acts as a game clock
    private static void incrementGameCounter() {
        gameCounter++;
    }

    //Function: Increment Game Level
    //Reaches next level
    private static void incrementGameLevel() {
        gameLevel++;
    }

    //Function: Add To Score
    //@param add                the amount to be added to the score board
    public static void addToScore(int add) {
        score += add;
        repaintGame();
    }

    //Function: Decrease Break Interval
    //Allows the block to fall faster
    private static void decreaseBreakInterval() {
        breakInterval /= 1.5;
    }

    //Function: Get Game Level
    //@return           the current level of the player
    public static int getGameLevel() {
        return gameLevel;
    }

    //Function: Get Game State
    //@return           the current game state
    public static GameState getGameState() {
        return gameState;
    }

    //Function: Get Score
    //@return           the current game score
    public static int getScore() {
        return score;
    }

    //Function: Get Last Game State
    //@return           the state before the current state, if no state existed
    //                  before, then use main menu as last state
    public static GameState getLastGameState() {
        System.out.println(lastGameState);
        return lastGameState == null ? GameState.MAINMENU : lastGameState;
    }

    //Function: Set Game State
    //@param gameState          the new game state
    //Updates the current game state
    public static void setGameState(GameState gameState) {
        lastGameState = TetrisGame.gameState;
        TetrisGame.gameState = gameState;
    }
}
