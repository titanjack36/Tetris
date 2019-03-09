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
    private static ButtonID hoverButton;
    private static ButtonID clickedButton;

    public InfoBoardInput(double posX, double posY, double width,
                          double height) {

        setDimensions(posX, posY, width, height);
        btns = new Button[]{
            new Button(this, posY + 100,"New Game",
                ButtonID.NEW_GAME, GameState.MAINMENU, GameState.NEWGAME),
            new Button(this, posY + 200,"How To Play",
                ButtonID.HOW_TO_PLAY, GameState.MAINMENU, GameState.HELPSCREEN),
            new Button(this, posY + 270,"Options",
                ButtonID.OPTIONS, GameState.MAINMENU, GameState.OPTIONSSCREEN),
            new Button(this, posY + 340,"Quit",
                ButtonID.QUIT, GameState.MAINMENU, GameState.STOP),

            new Button(this, posY + 100, "Pause Game",
                ButtonID.PAUSE_GAME, GameState.INGAME, GameState.PAUSED),

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

            new Button(this, posY + 100, "Back",
                ButtonID.BACK, GameState.OPTIONSSCREEN, GameState.LASTSTATE),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.OPTIONSSCREEN,
                    GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.OPTIONSSCREEN, GameState.STOP),

            new Button(this, posY + 100, "Back",
                ButtonID.BACK, GameState.HELPSCREEN, GameState.LASTSTATE),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.HELPSCREEN, GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.HELPSCREEN, GameState.STOP),

            new Button(this, posY + 100, "New Game",
                ButtonID.NEW_GAME, GameState.GAMEOVER, GameState.NEWGAME),
            new Button(this, posY + 200, "Main Menu",
                ButtonID.MAIN_MENU, GameState.GAMEOVER, GameState.MAINMENU),
            new Button(this, posY + 270, "Quit",
                ButtonID.QUIT, GameState.GAMEOVER, GameState.STOP)
        };
    }

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //Evaluates the position of the mouse in order to determine whether
    //it is interacting with any of the buttons displayed on the screen.
    //If the mouse is hovering or clicking on a button, it will be recorded
    //and the game state will be changed once a button is clicked and released
    public void mouseAction(double mousePosX, double mousePosY,
                                           double offsetX, double offsetY,
                                           MouseAction action) {

        boolean btnFound = false;
        for (Button btn : btns) {
            if (!btnFound && btn.getBtnPageState().equals(
                    TetrisGame.getGameState())) {
                //Evaluates mouse position using the boundaries of the button
                boolean isTouching = mousePosX > btn.getBtnX() + offsetX
                        && mousePosY > btn.getBtnY() + offsetY
                        && mousePosX < btn.getBtnX() + btn.getBtnWidth() +
                        offsetX
                        && mousePosY < btn.getBtnY() + btn.getBtnHeight() +
                        offsetY;

                if (isTouching) {
                    //Mouse is touching a button but does not click
                    if (action.equals(MouseAction.HOVER)) {
                        if (!btn.getID().equals(hoverButton)) {
                            hoverButton = btn.getID();
                            TetrisGame.repaintGame();
                        }
                        clickedButton = null;
                    }
                    //Mouse has clicked but not released
                    if (action.equals(MouseAction.CLICK)) {
                        if (!btn.getID().equals(clickedButton)) {
                            clickedButton = btn.getID();
                            TetrisGame.repaintGame();
                        }
                        hoverButton = null;
                    }
                    //Mouse key has been released
                    if (action.equals(MouseAction.RELEASE)) {
                        if (btn.getOnClickState().equals(GameState.STOP))
                            System.exit(0);
                        else if (btn.getOnClickState().equals(
                                GameState.LASTSTATE))
                            //Return to the last page visited by the player
                            TetrisGame.setGameState(
                                    TetrisGame.getLastGameState());
                        else {
                            //Clear the grid when returning to menu
                            if (btn.getOnClickState().equals(
                                    GameState.MAINMENU))
                                TetrisGame.resetGameGrid();
                            TetrisGame.setGameState(btn.getOnClickState());
                        }
                        TetrisGame.repaintGame();
                        clickedButton = null;
                        hoverButton = null;
                    }
                    btnFound = true;
                } else {
                    //Reset clicked and hover button when mouse is no longer
                    //touching any buttons
                    if (clickedButton != null || hoverButton != null) {
                        clickedButton = null;
                        hoverButton = null;
                        TetrisGame.repaintGame();
                    }
                }
            }
        }
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
                btn.setBounds(posX + 20, width * 0.8);
                btn.paint(hoverButton, clickedButton, g);
            }
    }
}
