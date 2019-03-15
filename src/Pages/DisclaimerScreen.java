//--------------------------DISCLAIMER SCREEN CLASS---------------------------//
//@author Titanjack
//@project Tetris
//Manages content to be displayed on the disclaimer screen. Provides disclaimer
//information about the program

package Pages;

import javax.swing.*;
import java.awt.*;

public class DisclaimerScreen extends JPanel {

    private double posX;
    private double posY;
    private double width;
    private double height;
    private TextField textTitle;
    private TextField textBody;

    public DisclaimerScreen(double posX, double posY, double width,
                         double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        textTitle = new TextField(posX, posY, width);
        textTitle.setText("Disclaimer");
        textBody = new TextField(posX, posY + 80, width);
        String text = "The Tetris logo and designs for the game are " +
                "properties of The Tetris Company, LCC and this open source " +
                "remake is meant as a side project which is not commercial " +
                "or meant for profit.\n\nPress any key to continue.";
        textBody.setText(text);
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
    }

    //Function: Paint
    //@param g          the graphics component
    //Renders the contents of the disclaimer screen
    @Override
    public void paint(Graphics g) {

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect((int)posX, (int)posY, (int)width + 300,
                (int)height + 300);

        double renderPosY = 80;
        g.setColor(new Color(255, 255, 255));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        textTitle.setDimensions(posX, posY + renderPosY, width);
        textTitle.paint(g);

        renderPosY += textTitle.getHeight() + 50;
        textBody.setDimensions(posX, posY + renderPosY, width);
        g.setFont(new Font("Arial", Font.BOLD, 27));
        textBody.paint(g);

        /*g.setColor(new Color(0, 0, 0, 200));
        g.fillRect((int)posX, (int)posY, (int)width + 300,
                (int)height + 300);

        double renderPosY = 80;
        g.setColor(new Color(255, 255, 255));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "Disclaimer";
        textField.setDimensions(posX, posY + renderPosY, width);
        textField.setText(text, g);
        textField.paint(g);

        renderPosY += textField.getHeight() + 50;
        textField.setDimensions(posX, posY + renderPosY, width);
        g.setFont(new Font("Arial", Font.BOLD, 27));
        text = "The Tetris logo and designs for the game are properties of " +
                "The Tetris Company, LCC and this open source remake is " +
                "meant as a side project which is not commercial or meant " +
                "for profit.\n\nPress any key to continue.";
        textField.setText(text, g);
        textField.paint(g);
*/
    }
}
