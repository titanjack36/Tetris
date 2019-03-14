//-----------------------------TETRIS GAME CLASS------------------------------//
//@author TitanJack
//@version 0.2 (2019-03-13)
//Tetris is a puzzle game developed by Alexey Pajitnov in 1984. This is a
//remake of the game coded independently by Titanjack. The Tetris logo and
//designs for the game are properties of The Tetris Company, LCC and this
//open source remake is meant as a side project which is not commercial or
//meant for profit.
package Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

@SuppressWarnings({"WeakerAccess"})
public class TetrisGame {

    private static GameState gameState;
    private static GameState lastGameState;
    private static JFrame window;
    private static int gameCounter;
    private static int gameLevel;
    private static int breakInterval;
    private static int score;
    private static boolean blockFallBuffer;
    private static GameManager game;

    private static Timer blockBufferTimer;
    private static Timer levelUpTimer;
    private static Timer blockFallTimer;

    private static Clip clip;

    public static void main(String[] args) {

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File("./assets/Crazy-Candy-Highway-2.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            System.err.println("Unable to start music:");
            e.printStackTrace();
        }

        double windowWidth = 1050, windowHeight = 1200;
        game = new GameManager(windowWidth, windowHeight
                - 100, 0, 0, 0, 20, 10);

        window = new JFrame();
        window.add(game);
        window.setSize(new Dimension((int)windowWidth, (int)windowHeight));
        window.setTitle("TETRIS V0.2");
        window.setVisible(true);

        double offsetX = window.getInsets().left;
        double offsetY = window.getInsets().top;
        gameState = GameState.DISCLAIMER;
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
                if (gameState.equals(GameState.DISCLAIMER)) {
                    setGameState(GameState.MAINMENU);
                    repaintGame();
                } else {
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

        blockFallTimer = new Timer(breakInterval, null);
        blockFallTimer.addActionListener(e -> {
            //Allows the block to fall every second
            if (!blockFallBuffer) {
                game.movePiece("down");
            }
        });
        blockFallTimer.start();

        levelUpTimer = new Timer(60000, null);
        levelUpTimer.addActionListener(e -> {
            //Increases the game level and the speed at which the tetris block
            //falls
            if (gameLevel < 15 && gameCounter % 1000 == 0) {
                gameLevel++;
                breakInterval -= 60;
                blockFallTimer.setDelay(breakInterval);
            }
            if (gameLevel >= 15) levelUpTimer.stop();
        });

        blockBufferTimer = new Timer(500, null);
        blockBufferTimer.addActionListener(e -> {
            blockFallBuffer = false;
            blockBufferTimer.stop();
        });
    }

    //Function: Restart Game
    //Resets all game values and the game grid
    public static void startGame() {
        gameLevel = 1;
        gameCounter = 0;
        score = 0;
        breakInterval = 1000;
        blockFallTimer.setDelay(breakInterval);
        blockFallTimer.start();
        levelUpTimer.start();
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

    //Function: Add To Score
    //@param add        the amount to be added to the score board
    public static void addToScore(int add) {
        score += add;
        repaintGame();
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

    //Function: Initiate Buffer
    //Calls for buffer to be activated to prevent a new tetris block from
    //falling down immediately
    public static void initiateBuffer() {
        blockFallBuffer = true;
        blockBufferTimer.start();
    }

    //Function: Set Sound On
    //@param isOn       whether or not to set the sound on
    //Turns game sounds on or off
    public static void setSoundState(boolean isOn) {
        if (isOn) {
            clip.start();
        } else {
            clip.stop();
        }
    }

    //Function: Get Last Game State
    //@return           the state before the current state, if no state existed
    //                  before, then use main menu as last state
    public static GameState getLastGameState() {
        return lastGameState == null ? GameState.MAINMENU : lastGameState;
    }

    //Function: Set Game State
    //@param gameState          the new game state
    //Updates the current game state
    public static void setGameState(GameState gameState) {
        if (gameState.equals(GameState.GAMEOVER)) {
            blockFallTimer.stop();
            levelUpTimer.stop();
            blockBufferTimer.stop();
        }

        lastGameState = TetrisGame.gameState;
        TetrisGame.gameState = gameState;
        game.updateGame();
    }
}
