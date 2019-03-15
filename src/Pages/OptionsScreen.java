//----------------------------OPTIONS SCREEN CLASS----------------------------//
//@author Titanjack
//@project Tetris
//Manages content to be displayed on the options screen. Once completed, it will
//let the user manage key bindings

package Pages;

import javax.swing.*;
import java.awt.*;

public class OptionsScreen extends JPanel {

    private double posX;
    private double posY;
    private double width;
    private double height;
    private TextField textTitle;
    private TextField textBody;

    public OptionsScreen(double posX, double posY, double width,
                         double height) {
        setDimensions(posX, posY, width, height);
        textTitle = new TextField(posX, posY, width);
        textTitle.setText("Options");
        textBody = new TextField(posX, posY, width);
        textBody.setText("Sorry, the options page is currently under " +
                "construction");
    }

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Updates the position and the size of the options screen
    public void setDimensions(double posX, double posY, double width,
                              double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    //Function: Paint
    //@param g          the graphics component
    //Renders the contents of the options screen
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
