
//--------------------------DISCLAIMER SCREEN CLASS---------------------------//
//@author Titanjack
//@project Tetris
//Manages content to be displayed on the disclaimer screen. Provides disclaimer
//information about the program

package Pages;

import Game.MouseAction;

import javax.swing.*;
import java.awt.*;

public class AboutScreen extends JPanel {

    private double posX;
    private double posY;
    private double width;
    private double height;
    private TextField textField;

    public AboutScreen(double posX, double posY, double width,
                            double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        textField = new TextField(posX, posY + 80, width);
    }

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Updates the position and the size of the disclaimer screen
    public void setDimensions(double posX, double posY, double width,
                              double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        textField.setDimensions(posX, posY + 80, width);
    }

    public void mouseAction(double mousePosX, double mousePosY, double offsetX,
                                   double offsetY, MouseAction action) {
        textField.mouseAction(mousePosX, mousePosY, offsetX, offsetY, action);
    }

    //Function: Paint
    //@param g          the graphics component
    //Renders the contents of the disclaimer screen
    @Override
    public void paint(Graphics g) {

        double renderPosY = 80;
        g.setColor(new Color(114, 114, 114));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "About";
        //textField.setDimensions(posX, posY + renderPosY, width);
        //textField.setText(text, g);
        //textField.paint(g);

        renderPosY += textField.getHeight() + 50;
        textField.setDimensions(posX, posY + renderPosY, width);
        g.setFont(new Font("Arial", Font.BOLD, 27));
        text = "Tetris Version 0.2\n\n<a url='https://en.wikipedia.org/wiki/" +
                "Tetris'>Tetris</a> is a puzzle game developed by " +
                "Alexey Pajitnov in 1984. This is a remake of the game coded " +
                "independently by Titanjack. The Tetris logo and designs " +
                "for the game are properties of The Tetris Company, LCC and " +
                "this open source remake is meant as a side project which is " +
                "not commercial or meant for profit.\n\nMusic by Eric " +
                "Matyas:\n<a url='https://soundimage.org/'>https://sound" +
                "image.org/</a>\n\nPlease visit my github " +
                "for more projects:\n<a url='https://github.com/titanjack36'>" +
                "https://github.com/titanjack36</a>";
        textField.setText(text, g);
        textField.paint(g);
    }

}
