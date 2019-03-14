//---------------------------INFO BOARD INPUT CLASS---------------------------//
//@author Titanjack
//@project Tetris
//The input class is responsible for rendering and handling interactions with
//all input objects on the info board. All input objects are stored in this
//class and is only accessed locally.

package Input;

import Game.GameState;
import Game.MouseAction;
import Game.TetrisGame;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"SpellCheckingInspection"})
public class InfoBoardInput extends JPanel implements PageInput {

    private Button[] btns;

    private double posX;
    private double posY;
    private double height;
    private double width;
    private MouseActionHandler mouseActionHandler;

    public InfoBoardInput(double posX, double posY, double width,
                          double height) {

        setDimensions(posX, posY, width, height);
        mouseActionHandler = new MouseActionHandler();
        btns = new Button[]{
            new Button(this, posY + 100,"New Game",
                ButtonID.NEW_GAME, GameState.MAINMENU, GameState.NEWGAME),
            new Button(this, posY + 200,"How To Play",
                ButtonID.HOW_TO_PLAY, GameState.MAINMENU, GameState.HELPSCREEN),
            new Button(this, posY + 270,"Options",
                ButtonID.OPTIONS, GameState.MAINMENU, GameState.OPTIONSSCREEN),
            new Button(this, posY + 340, "About",
                ButtonID.ABOUT, GameState.MAINMENU, GameState.ABOUTSCREEN),
            new Button(this, posY + 410,"Quit",
                ButtonID.QUIT, GameState.MAINMENU, GameState.STOP),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE, GameState.MAINMENU),

            new Button(this, posY + 100, "Pause Game",
                ButtonID.PAUSE_GAME, GameState.INGAME, GameState.PAUSED),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE, GameState.INGAME),

            new Button(this, posY + 100, "Resume Game",
                ButtonID.RESUME_GAME, GameState.PAUSED, GameState.INGAME),
            new Button(this, posY + 200, "Restart",
                ButtonID.RESTART, GameState.PAUSED, GameState.NEWGAME),
            new Button(this, posY + 270, "How To Play",
                ButtonID.HOW_TO_PLAY, GameState.PAUSED, GameState.HELPSCREEN),
            new Button(this, posY + 340, "Options",
                ButtonID.OPTIONS, GameState.PAUSED, GameState.OPTIONSSCREEN),
            new Button(this, posY + 410, "Main Menu",
                ButtonID.MAIN_MENU, GameState.PAUSED, GameState.MAINMENU),
            new Button(this, posY + 480, "Quit",
                ButtonID.QUIT, GameState.PAUSED, GameState.STOP),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE, GameState.PAUSED),

            new Button(this, posY + 100, "Back",
                ButtonID.BACK, GameState.OPTIONSSCREEN, GameState.LASTSTATE),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.OPTIONSSCREEN,
                    GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.OPTIONSSCREEN, GameState.STOP),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE,
                    GameState.OPTIONSSCREEN),

            new Button(this, posY + 100, "Back",
                ButtonID.BACK, GameState.HELPSCREEN, GameState.LASTSTATE),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.HELPSCREEN, GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.HELPSCREEN, GameState.STOP),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE, GameState.HELPSCREEN),

            new Button(this, posY + 100, "Back",
                ButtonID.BACK, GameState.ABOUTSCREEN, GameState.LASTSTATE),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.ABOUTSCREEN, GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.ABOUTSCREEN, GameState.STOP),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE, GameState.ABOUTSCREEN),

            new Button(this, posY + 100, "New Game",
                ButtonID.NEW_GAME, GameState.GAMEOVER, GameState.NEWGAME),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.GAMEOVER, GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.GAMEOVER, GameState.STOP),
            new Button(this, posY + height - 50, "Sound On",
                "Sound Off", ButtonID.SOUNDTOGGLE, GameState.GAMEOVER)
        };
        //Set the buttons to move dynamically
        for (Button btn: btns) {
            btn.setHorizontalSlide(true);
            if (btn.getID().equals(ButtonID.SOUNDTOGGLE))
                btn.setVerticalSlide(true);
        }
    }

    //-----------------------------MISC FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void mouseAction(double mousePosX, double mousePosY,
    //                            double offsetX, double offsetY,
    //                            MouseAction action)
    //public void setDimensions(double posX, double posY, double width,
    //                              double height)
    //public void setDimensions(double width, double height)
    //public double getPagePosX()
    //public double getPagePosY()
    //public double getPageWidth()
    //public double getPageHeight()
    //public void paint(Graphics g)

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //Relays the position and action of the mouse to be handled by the mouse
    //action handler
    public void mouseAction(double mousePosX, double mousePosY,
                            double offsetX, double offsetY,
                            MouseAction action) {
        mouseActionHandler.mouseAction(mousePosX, mousePosY, offsetX, offsetY,
                action, btns);
    }

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Updates the position and size of the container for the buttons
    public void setDimensions(double posX, double posY, double width,
                              double height) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
    }

    //UNUSED
    /*
    //Function: Set Dimensions
    //@param width          new width
    //       height         new height
    //Updates the size of the button container while keeping same position
    public void setDimensions(double width, double height) {
        setDimensions(posX, posY, width, height);
    }*/

    //Function: Get Page Pos X
    //@return           the x position of the page containing the buttons
    public double getPagePosX() {
        return posX;
    }

    //Function: Get Page Pos Y
    //@return           the y position of the page containing the buttons
    public double getPagePosY() {
        return posY;
    }

    //Function: Get Page Width
    //@return           the width of the page
    public double getPageWidth() {
        return width;
    }

    //Function: Get Page Height
    //@return           height of the page
    public double getPageHeight() {
        return height;
    }

    //Function: Paint
    //@param g          the graphics object
    //Renders the buttons which are to be displayed for the current game state
    @Override
    public void paint(Graphics g) {
        for (Button btn : btns)
            if (btn.getBtnPageState().equals(TetrisGame.getGameState())) {
                btn.posFromLeft(20, this);
                if (btn.isVerticalSlide()) btn.posFromBottom(50, this);
                btn.paint(g);
            }
    }
}
