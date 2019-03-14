//-----------------------------MOUSE ACTION CLASS-----------------------------//
//@author Titanjack
//@project Tetris
//The Mouse Action class handles all interaction events between the mouse and
//an input element such as a button or link. When an element is triggered, an
//action will be executed by the Mouse Action class.

package Input;

import Game.GameState;
import Game.MouseAction;
import Game.TetrisGame;

import java.net.URI;
import java.util.ArrayList;

public class MouseActionHandler {

    private boolean linkClicked;

    public MouseActionHandler() {
        linkClicked = false;
    }

    //-----------------------------CORE FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void mouseAction(double mousePosX, double mousePosY,
    //                            double offsetX, double offsetY,
    //                            MouseAction action, Button[] btns)
    //public void mouseAction(double mousePosX, double mousePosY, double offsetX,
    //                            double offsetY, MouseAction action,
    //                            ArrayList<Link> links)
    //

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //       btns           the set of buttons on the input handling page
    //Evaluates the position of the mouse in order to determine whether
    //the mouse is interacting with any of the buttons displayed on the screen.
    //If the mouse is hovering or clicking on a button, it will be recorded
    //and the game state will be changed once a button is clicked and released
    public void mouseAction(double mousePosX, double mousePosY,
                            double offsetX, double offsetY,
                            MouseAction action, Button[] btns) {

        boolean btnFound = false;
        for (Button btn : btns) {
            //Only choses buttons which exist during the current game state
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
                        if (!btn.isHovered()) {
                            btn.setHovered(true);
                            TetrisGame.repaintGame();
                        }
                        btn.setClicked(false);
                    }
                    //Mouse has clicked but not released
                    if (action.equals(MouseAction.CLICK)) {
                        if (!btn.isClicked()) {
                            btn.setClicked(true);
                            TetrisGame.repaintGame();
                        }
                        btn.setHovered(false);
                    }
                    //Mouse key has been released
                    if (action.equals(MouseAction.RELEASE)) {
                        if (btn.getOnClickState() == null) {
                            handleOnClickFunction(btn);
                        } else {
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
                        }
                        TetrisGame.repaintGame();
                        btn.setClicked(false);
                        btn.setHovered(false);
                    }
                    btnFound = true;
                } else {
                    //Reset clicked and hover button when mouse is no longer
                    //touching any buttons
                    if (btn.isClicked() || btn.isHovered()) {
                        btn.setClicked(false);
                        btn.setHovered(false);
                        TetrisGame.repaintGame();
                    }
                }
            }
        }
    }

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //       links          the set of links on the text field
    //Evaluates the position of the mouse to determine whether it is hovering
    //on any links on screen and will underline the link. Once the user clicks
    //on an link, they will be brought to the address specified by the link.
    public void mouseAction(double mousePosX, double mousePosY, double offsetX,
                            double offsetY, MouseAction action,
                            ArrayList<Link> links) {
        for (Link link: links) {
            double[][] linkBounds = link.getLinkBounds();
            boolean isTouching = false;
            for (double[] coords: linkBounds)
                isTouching = isTouching || mousePosX > coords[0] + offsetX
                        && mousePosY > coords[1] + offsetY
                        && mousePosX < coords[0] + coords[2] + offsetX
                        && mousePosY < coords[1] + coords[3] + offsetY;

            if (isTouching) {
                if (action.equals(MouseAction.HOVER)) {
                    if (!link.isLinkHovered()) {
                        link.setLinkHovered(true);
                        TetrisGame.repaintGame();
                    }
                }
                if (action.equals(MouseAction.RELEASE)) {
                    if (!linkClicked) {
                        try {
                            java.awt.Desktop.getDesktop().browse(new
                                    URI(link.getAddress()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        link.setLinkHovered(false);
                        TetrisGame.repaintGame();
                        linkClicked = true;
                    }
                }
            } else {
                if (link.isLinkHovered()) {
                    link.setLinkHovered(false);
                    TetrisGame.repaintGame();
                    linkClicked = false;
                }
            }
        }
    }

    //-----------------------------ASSIST FUNCTION----------------------------//
    //Function: Handle On Click Function
    //@param btn            the button that has been clicked
    //Executes specified actions for each type of button that does not have
    //an target game state as it's on click action.
    public void handleOnClickFunction(Button btn) {
        switch (btn.getID()) {
            case SOUNDTOGGLE:
                if (btn.isAltState()) TetrisGame.setSoundState(true);
                else TetrisGame.setSoundState(false);
        }
    }
}
