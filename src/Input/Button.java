//--------------------------------BUTTON CLASS--------------------------------//
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
    private boolean horizontalSlide;
    private boolean verticalSlide;

    private boolean isHovered;
    private boolean isClicked;

    //Text to be displayed
    private String text;
    //What type of button it is
    private ButtonID id;
    //Which screen/game state it will appear on
    private GameState pageState;
    //The new game state to be set once the button is clicked
    private GameState onClickState = null;

    private boolean altState;
    private String altText = null;

    //Menu button
    public Button(PageInput pageInput, double posY, String text, ButtonID id,
                  GameState pageState, GameState onClickState) {
        this(pageInput.getPagePosX() + 20, posY,
                pageInput.getPageWidth() * 0.8, 50, text, id,
                pageState, onClickState);
    }

    //Menu button
    public Button(double posX, double posY, double width, double height,
                  String text, ButtonID id, GameState pageState,
                  GameState onClickState) {

        setBounds(posX, posY, width, height);
        this.text = text;
        this.id = id;
        this.pageState = pageState;
        this.onClickState = onClickState;
        altState = false;
    }

    //Toggle button
    public Button(PageInput pageInput, double posY, String text, String altText,
                  ButtonID id, GameState pageState) {
        this(pageInput.getPagePosX() + 20, posY, pageInput.
                getPageWidth() * 0.8, 50, text, altText, false,
                id, pageState);
    }

    //Toggle button
    public Button(double posX, double posY, double width, double height,
                  String text, String altText, boolean altState,
                  ButtonID id, GameState pageState) {
        setBounds(posX, posY, width, height);
        this.text = text;
        this.altText = altText;
        this.altState = altState;
        this.id = id;
        this.pageState = pageState;
    }

    //UNUSED
    /*
    //Action button
    public Button(double posX, double posY, double width, double height,
                  String text, ButtonID id, GameState pageState) {
        setBounds(posX, posY, width, height);
        this.text = text;
        this.id = id;
        this.pageState = pageState;
        altState = false;
    }*/

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
    //@param posX               new x position
    //Updates button x position
    public void setX(double posX) {
        this.posX = posX;
    }

    //Function: Set Y
    //@param posY               new y position
    //Updates button y position
    public void setY(double posY) {
        this.posY = posY;
    }

    //Function: Set Width
    //@param width              new width value
    //Updates button width
    public void setWidth(double width) {
        this.width = width;
    }

    //Function: Set Height
    //@param height             new height value
    //Updates button height
    public void setHeight(double height) {
        this.height = height;
    }

    //Function: Set Text
    //@param text               new button display text
    //Updates the text displayed by the button
    public void setText(String text) {
        this.text = text;
    }

    //Function: Set ID
    //@param id                 new button id
    //Updates the button ID
    public void setID(ButtonID id) {
        this.id = id;
    }

    //Function: Set On Click State
    //@param onClickState       new on click state
    //Updates the button's on click state
    public void setOnClickState(GameState onClickState) {
        this.onClickState = onClickState;
    }*/

    //Function: Set Bounds
    //@param posX               new x position
    //       posY               new y position
    //       width              new width
    //       height             new height
    //Updates the position and size of the button
    public void setBounds(double posX, double posY, double width,
                          double height) {
        setPosition(posX, posY);
        setSize(width, height);
        horizontalSlide = false;
        verticalSlide = false;
    }

    //UNUSED
    /*
    //Function: Set Bounds
    //@param posX               new x position
    //       width              new width
    //Updates the horizontal placement of the button and it's width
    public void setBounds(double posX, double width) {
        this.posX = posX;
        this.width = width;
    }*/

    //Function: Pos From Left
    //@param shift              amount to the right from the left of the
    //                          reference input page
    //       pageInput          the reference input page
    //Sets the x position of the button to be relative to the left border of
    //the input page
    public void posFromLeft(double shift, PageInput pageInput) {
        if (isHorizontalSlide()) {
            posX = pageInput.getPagePosX() + shift;
        }
    }

    //Function: Pos From Right
    //@param shift              amount to the left from the right of the
    //                          reference input page
    //       pageInput          the reference input page
    //Sets the x position of the button to be relative to the right border of
    //the input page
    public void posFromRight(double shift, PageInput pageInput) {
        if (isHorizontalSlide()) {
            posX = pageInput.getPagePosX() + pageInput.getPageWidth() - shift;
        }
    }

    //Function: Pos From Top
    //@param shift              amount down from the top of the reference input
    //                          page
    //       pageInput          the reference input page
    //Sets the x position of the button to be relative to the top border of
    //the input page
    public void posFromTop(double shift, PageInput pageInput) {
        if (isVerticalSlide()) {
            posY = pageInput.getPagePosY() + shift;
        }
    }

    //Function: Pos From Bottom
    //@param shift              amount up from the bottom of the reference input
    //                          page
    //       pageInput          the reference input page
    //Sets the x position of the button to be relative to the bottom border of
    //the input page
    public void posFromBottom(double shift, PageInput pageInput) {
        if (isVerticalSlide()) {
            posY = pageInput.getPagePosY() + pageInput.getPageHeight() - shift;
        }
    }

    //Function: Set Position
    //@param posX               new x position
    //       posY               new y position
    //Updates the position the button
    public void setPosition(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    //Function: Set Size
    //@param width              new width
    //       height             new height
    //Updates the size of the button
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    //Function: Set Hovered
    //@param isHovered          whether or not a mouse is hovering above the
    //                          button
    //Records if the button is being hovered by a mouse
    public void setHovered(boolean isHovered) {
        this.isHovered = isHovered;
    }

    //Function: Set Clicked
    //@param isClicked          whether or not the button is being clicked by a
    //                          mouse
    //Records whether the button is clicked and executes internal actions if
    //clicked
    public void setClicked(boolean isClicked) {
        if (!isClicked && this.isClicked && altText != null) {
            altState = !altState;
        }
        this.isClicked = isClicked;
    }

    //Function: Set Horizontal Fixed
    //@param horizontalSlide    whether the button can move horizontally
    //Sets the button's ability to dynamically move horizontally
    public void setHorizontalSlide(boolean horizontalSlide) {
        this.horizontalSlide = horizontalSlide;
    }

    //Function: Set Vertical Fixed
    //@param verticalSlide      whether the button can move vertically
    //Sets the button's ability to dynamically move vertically
    public void setVerticalSlide(boolean verticalSlide) {
        this.verticalSlide = verticalSlide;
    }

    //Function: Get Button X
    //@return                   the x position of the button
    public double getBtnX() {
        return posX;
    }

    //Function: Get Button Y
    //@return                   the y position of the button
    public double getBtnY() {
        return posY;
    }

    //Function: Get Button Width
    //@return                   the width of the button
    public double getBtnWidth() {
        return width;
    }

    //Function: Get Button Height
    //@return                   the height position of the button
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
    //@return                   the id of the button
    public ButtonID getID() {
        return id;
    }

    //Function: Get Button Page State
    //@return                   the game state which the button belongs to
    public GameState getBtnPageState() {
        return pageState;
    }

    //Function: Get On Click State
    //@return                   the resultant state of the game once the button
    //                          is clicked
    public GameState getOnClickState() {
        return onClickState;
    }

    //Function: Is Hovered
    //@return                   whether the button is being hovered by the mouse
    public boolean isHovered() {
        return isHovered;
    }

    //Function: Is Clicked
    //@return                   whether the mouse is clicking on the button
    public boolean isClicked() {
        return isClicked;
    }

    //Function: Is Alt State
    //@return                   whether the mouse is in it's alternate state,
    //                          aka clicked state
    public boolean isAltState() {
        return altState;
    }

    //Function: Is Horizontal Fixed
    //@return                   whether or not the button can move horizontally
    //                          on demand
    public boolean isHorizontalSlide() {
        return horizontalSlide;
    }

    //Function: Is Vertical Fixed
    //@return                   whether or not the button can move vertically
    //                          on demand
    public boolean isVerticalSlide() {
        return verticalSlide;
    }

    //Function: Paint
    //@param g                  the graphics component
    //Renders the button, with different colors corresponding to whether
    //the button is being hovered on or clicked on
    @Override
    public void paint(Graphics g) {
        if (isClicked)
            g.setColor(new Color(147, 147, 147));
        else if (isHovered)
            g.setColor(new Color(177, 177, 177));
        else
            g.setColor(new Color(209, 209, 209));

        g.fillRect((int)posX, (int)posY, (int)width, (int)height);
        g.setColor(new Color(114, 114, 114));
        g.setFont(new Font("Arial", Font.BOLD, 27));
        int textWidth = g.getFontMetrics().stringWidth(altState ? altText :
                text);
        g.drawString(altState ? altText : text, (int)(posX - textWidth / 2.0 +
                        width / 2), (int)posY + 35);
    }
}
