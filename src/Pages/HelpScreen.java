//-----------------------------HELP SCREEN CLASS------------------------------//
//@author Titanjack
//@project Tetris
//Manages content to be displayed on the help screen. Once completed, it will
//provide information on how to play the game

package Pages;

import javax.swing.*;
import java.awt.*;

public class HelpScreen extends JPanel {
    private double posX;
    private double posY;
    private double width;
    private double height;
    private TextField textTitle;
    private TextField textBody;

    public HelpScreen(double posX, double posY, double width,
                         double height) {
        setDimensions(posX, posY, width, height);
        textTitle = new TextField(posX, posY, width);
        textTitle.setText("How To Play");
        textBody = new TextField(posX, posY, width);
        textBody.setText("Sorry, the options page is currently under " +
                "construction");
    }

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Updates the position and the size of the help screen
    public void setDimensions(double posX, double posY, double width,
                              double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    //Function: Paint
    //@param g          the graphics component
    //Renders the contents of the help screen
    @Override
    public void paint(Graphics g) {

        double renderPosY = 80;
        g.setColor(new Color(114, 114, 114));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        textTitle.setDimensions(posX, posY + renderPosY, width);
        textTitle.paint(g);

        renderPosY += textTitle.getHeight() + 50;
        textBody.setDimensions(posX, posY + renderPosY, width);
        g.setFont(new Font("Arial", Font.BOLD, 27));
        textBody.paint(g);
    }
}
