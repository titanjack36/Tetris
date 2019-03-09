//------------------------------BUTTON CLASS------------------------------//
//@author Titanjack
//@project Tetris
//The button object contains all the properties of the button including it's
//location on the screen, its size, display text, what type of button it is,
//which screen it is supposed to appear on, and what it will do once clicked

package Input;

import Game.GameState;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class Button extends JPanel{

    //Location
    private double posX;
    private double posY;
    //Size
    private double width;
    private double height;
    //Text to be displayed
    private String text;
    //What type of button it is
    private ButtonID id;
    //Which screen/game state it will appear on
    private GameState pageState;
    //The new game state to be set once the button is clicked
    private GameState onClickState;

    public Button(PageInput pageInput, double posY, String text, ButtonID id,
                  GameState pageState, GameState onClickState) {
        this(pageInput.getPagePosX() + 20, posY,
                pageInput.getPageWidth() * 0.8, 50, text, id,
                pageState, onClickState);
    }

    public Button(double posX, double posY, double width, double height,
                  String text, ButtonID id, GameState pageState,
                  GameState onClickState) {

        setBounds(posX, posY, width, height);
        this.text = text;
        this.id = id;
        this.pageState = pageState;
        this.onClickState = onClickState;
    }

    //-----------------------------MISC FUNCTIONS-----------------------------//
    //public void setX(double posX)
    //public void setY(double posY)
    //public void setWidth(double width)
    //public void setHeight(double height)
    //public void setID(ButtonID id)
    //public void setOnClickState(GameState onClickState)
    //public void setBounds(double posX, double posY, double width,
    //                      double height)
    //public void setBounds(double posX, double width)
    //public void setPosition(double posX, double posY)
    //public void setSize(double width, double height)
    //public double getBtnX()
    //public double getBtnY()
    //public double getBtnWidth()
    //public double getBtnHeight()
    //public String getText()
    //public ButtonID getID()
    //public GameState getBtnPageState()
    //public GameState getOnClickState()
    //public void paint(ButtonID hoverBtnID, ButtonID clickedBtnID, Graphics g)
    //public void paint(Graphics g)

    //UNUSED
    /*
    //Function: Set X
    //@param posX           new x position
    //Updates button x position
    public void setX(double posX) {
        this.posX = posX;
    }

    //Function: Set Y
    //@param posY           new y position
    //Updates button y position
    public void setY(double posY) {
        this.posY = posY;
    }

    //Function: Set Width
    //@param width          new width value
    //Updates button width
    public void setWidth(double width) {
        this.width = width;
    }

    //Function: Set Height
    //@param width          new height value
    //Updates button height
    public void setHeight(double height) {
        this.height = height;
    }

    //Function: Set Text
    //@param text           new button display text
    //Updates the text displayed by the button
    public void setText(String text) {
        this.text = text;
    }

    //Function: Set ID
    //@param id             new button id
    //Updates the button ID
    public void setID(ButtonID id) {
        this.id = id;
    }

    //Function: Set On Click State
    //@param onClickState   new on click state
    //Updates the button's on click state
    public void setOnClickState(GameState onClickState) {
        this.onClickState = onClickState;
    }*/

    //Function: Set Bounds
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Updates the position and size of the button
    public void setBounds(double posX, double posY, double width,
                          double height) {
        setPosition(posX, posY);
        setSize(width, height);
    }

    //Function: Set Bounds
    //@param posX           new x position
    //       width          new width
    //Updates the horizontal placement of the button and it's width
    public void setBounds(double posX, double width) {
        this.posX = posX;
        this.width = width;
    }

    //Function: Set Position
    //@param posX           new x position
    //       posY           new y position
    //Updates the position the button
    public void setPosition(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    //Function: Set Size
    //@param width          new width
    //       height         new height
    //Updates the size of the button
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    //Function: Get Button X
    //@return               the x position of the button
    public double getBtnX() {
        return posX;
    }

    //Function: Get Button Y
    //@return               the y position of the button
    public double getBtnY() {
        return posY;
    }

    //Function: Get Button Width
    //@return               the width of the button
    public double getBtnWidth() {
        return width;
    }

    //Function: Get Button Height
    //@return               the height position of the button
    public double getBtnHeight() {
        return height;
    }

    //UNUSED
    /*
    //Function: Get Text
    //@return               the text displayed by the button
    public String getText() {
        return text;
    }*/

    //Function: Get ID
    //@return               the id of the button
    public ButtonID getID() {
        return id;
    }

    //Function: Get Button Page State
    //@return               the game state which the button belongs to
    public GameState getBtnPageState() {
        return pageState;
    }

    //Function: Get On Click State
    //@return               the resultant state of the game once the button is
    //                      clicked
    public GameState getOnClickState() {
        return onClickState;
    }

    //Function: Paint
    //@param hoverBtnID     the button which the mouse is hovering on
    //       clickedBtnID   the button which the mouse has pressed down on
    //       g              the graphics component
    //Renders the button, with different colors corresponding to whether
    //the button is being hovered on or clicked on
    public void paint(ButtonID hoverBtnID, ButtonID clickedBtnID, Graphics g) {
        if (id.equals(clickedBtnID))
            g.setColor(new Color(147, 147, 147));
        else if (id.equals(hoverBtnID))
            g.setColor(new Color(177, 177, 177));
        else
            g.setColor(new Color(209, 209, 209));

        g.fillRect((int)posX, (int)posY, (int)width, (int)height);
        g.setColor(new Color(114, 114, 114));
        g.setFont(new Font("Arial", Font.BOLD, 27));
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (int)(posX - textWidth / 2.0 + width / 2),
                (int)posY + 35);
    }

    //Function: Paint
    //@param g              the graphics component
    //Renders the button and assuming that the mouse is not touching or clicking
    //on any button
    @Override
    public void paint(Graphics g) {
        paint(null, null, g);
    }
}
