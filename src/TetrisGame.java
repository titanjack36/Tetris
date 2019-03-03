//-----------------------------TETRIS GAME CLASS------------------------------//
//@author TitanJack
//@version 0.1 (2019-03-01)
//Tetris is a puzzle game developed by Alexey Pajitnov in 1984. This is a
//remake of the game coded independently by Titanjack. The Tetris logo and
//designs for the game are properties of The Tetris Company, LCC and this
//open source remake is meant as a side project which is not for profit.

import GameComponents.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisGame {

    public static void main(String[] args) {

        double width = 1000, height = 1200;
        JFrame window = new JFrame();
        GameManager game = new GameManager(width, height - 100,
                0, 0, 0, 20, 10);
        window.add(game);
        window.setSize(new Dimension((int)width, (int)height));
        window.setTitle("TETRIS Alpha");
        window.setVisible(true);

        game.setDebug(true);
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

        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            //Allows the block to fall every second
            boolean gameOver = game.movePiece("down");
            if (gameOver) timer.stop();
        });
        timer.start();
    }
}
