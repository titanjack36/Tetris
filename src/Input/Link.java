//---------------------------------LINK CLASS---------------------------------//
//@author Titanjack
//@project Tetris
//The link object stores all properties of an hyperlink including it's address,
//the position of the link in the text and on the screen, and underlines the
//link when it is being hovered.

package Input;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class Link extends JPanel {

    private String address;
    private int beginIndex;
    private int endIndex;
    private boolean linkHovered;
    private double[][] linkBounds;

    public Link(String address, int beginIndex, int endIndex) {
        this.address = address;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    //------------------------------CORE FUNCTION-----------------------------//
    //Function: Set Bounds
    //@param refPosX            the x position of the reference text box element
    //       refWidth           the y position of the reference text box element
    //       wrappedText        text seperated into multiple lines in the form
    //                          of an array
    //       startingRow        row in the text which the link starts on
    //       endingRow          row in the text which the link ends
    //       posX1              the top left pos x coordinate at where the
    //                          link starts
    //       posY1              the top left pos y coordinate
    //       posX2              the bottom right x coordinate at where the
    //                          link ends
    //       posY2              the bottom right y coordinate
    //       g                  the graphics component
    //Given the coordinates at where the link starts and and ends, will generate
    //rectangles around each indiividual line that the link takes up
    public void setBounds(double refPosX, double refWidth,
                          String[] wrappedText, int startingRow, int endingRow,
                          double posX1, double posY1, double posX2,
                          double posY2, Graphics g) {
        int textHeight = g.getFontMetrics().getHeight();
        int rows = endingRow - startingRow + 1;
        linkBounds = new double[rows][];
        //If the link only exists on a single row
        if (rows == 1) {
            linkBounds[0] = new double[]{posX1, posY1, posX2 - posX1,
                    textHeight + 5};
        } else {
            for (int i = 0; i < rows; i++) {
                int textWidth = g.getFontMetrics().stringWidth(
                        wrappedText[startingRow + i]);
                double leftBound = (refPosX - textWidth / 2.0 + refWidth / 2);
                //Starting row of the link
                if (i == 0) {
                    linkBounds[i] = new double[]{posX1, posY1,
                            leftBound + textWidth - posX1, textHeight + 5};
                //Ending row of the link
                } else if (i == rows - 1) {
                    linkBounds[i] = new double[]{leftBound, posY2 - textHeight
                            - 5, posX2 - leftBound, textHeight + 5};
                //Rows in between, where the link takes up the entire row
                } else {
                    linkBounds[i] = new double[]{leftBound, posY1 + i *
                            (textHeight + 10), textWidth, textHeight + 5};
                }
            }
        }
    }

    //-----------------------------MISC FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public String getAddress()
    //public int getBeginIndex()
    //public int getEndIndex()
    //public boolean isLinkHovered()
    //public double[][] getLinkBounds()
    //public void setLinkHovered(boolean linkHovered)
    //public void paint(Graphics g)

    //Function: Get Address
    //@return                   the address of the link
    public String getAddress() {
        return address;
    }

    //Function: Get Begin Index
    //@return                   the starting index of the link within the text
    public int getBeginIndex() {
        return beginIndex;
    }

    //Function: Get End Index
    //@return                   the ending index of the link within the text
    public int getEndIndex() {
        return endIndex;
    }

    //Function: Is Hover Link
    //@return                   whether the mouse is hovering above the link
    public boolean isLinkHovered() {
        return linkHovered;
    }

    //Function: Get Link Bounds
    //@return                   get the rectangles which encapsulate the links
    //                          for each row the link appears on
    public double[][] getLinkBounds() {
        return linkBounds;
    }

    //Function: Set Link Hovered
    //@param linkHovered        sets whether a mouse is hovering on the link or
    //                          not
    public void setLinkHovered(boolean linkHovered) {
        this.linkHovered = linkHovered;
    }

    //Function: Paint
    //@param g                  the graphics component
    //Renders a line to underline the link when it is being hovered on by the
    //mouse
    public void paint(Graphics g) {
        if (linkHovered) {
            for (double[] coords : linkBounds) {
                g.drawLine((int)coords[0], (int)(coords[1] + coords[3]),
                        (int)(coords[0] + coords[2]), (int)(coords[1] +
                                coords[3]));
            }
        }
    }
}
