//------------------------------TEXT FIELD CLASS------------------------------//
//@author Titanjack
//@project Tetris
//The Text Field is an object which renders text to the screen. It has advanced
//features such as responsive text wrap and hyperlink embedding.

package Pages;

import Game.MouseAction;
import Input.Link;
import Input.MouseActionHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TextField extends JPanel {

    private double posX;
    private double posY;
    private double width;
    private double height;
    private boolean freeHeight;
    private String text;
    private String[] wrappedText;
    private ArrayList<Link> links;
    private MouseActionHandler mouseActionHandler;

    public TextField(double posX, double posY, double width,
                     double height) {
        setDimensions(posX, posY, width, height);
        wrappedText = null;
        freeHeight = false;
        mouseActionHandler = new MouseActionHandler();
    }

    public TextField(double posX, double posY, double width) {
        setDimensions(posX ,posY, width, 0);
        links = new ArrayList<>();
        wrappedText = null;
        freeHeight = true;
        mouseActionHandler = new MouseActionHandler();
    }

    //-----------------------------CORE FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //private String[] wrapText(String text, double maxWidth, Graphics g)
    //private String parseLinks(String text)
    //private void findLinkCoordinates(Graphics g)

    //Function: Wrap Text
    //@param text           the text to be wrapped
    //       maxWidth       the maximum width of text on one line
    //       g              the graphics component
    //@return               an array of text, each element symbolizing a new
    //                      line
    //Converts one line of text into multiple lines, depending on the maximum
    //length of text allowed on one line
    private String[] wrapText(String text, double maxWidth, Graphics g) {
        text = removeConsecutive(text, " ");
        String[] words = text.split(" ");
        ArrayList<String> wordLines = new ArrayList<>();

        int numOfWords = 0;
        String wordLine = "";
        while(numOfWords < words.length) {
            String[] currentWords = words[numOfWords].split("\n");
            for (int i = 0; i < currentWords.length; i++) {
                if (i == 0) {
                    if (wordLine.length() == 0 || g.getFontMetrics().stringWidth
                            (wordLine + currentWords[0]) < maxWidth) {
                        wordLine = wordLine.concat((wordLine.length() == 0 ? ""
                                : " ") + currentWords[0]);
                        if (currentWords.length > 1) wordLines.add(wordLine);
                        numOfWords++;
                    } else{
                        wordLines.add(wordLine);
                        if (currentWords.length > 1) {
                            wordLines.add(currentWords[0]);
                            numOfWords++;
                        }
                        wordLine = "";
                    }
                } else if (i != currentWords.length - 1) {
                    wordLines.add(currentWords[i]);
                } else
                    wordLine = currentWords[i];
            }
        }
        if (wordLine.length() != 0) wordLines.add(wordLine);
        return wordLines.toArray(new String[0]);
    }

    //Function: Parse Links
    //@param text           the string to be parsed for links
    //@return               the text in plain form without the link tags
    //Finds all instances of the link tag embedded into the text and records
    //them. Then remove the tags from the text and return it
    private String parseLinks(String text) {

        links = new ArrayList<>();
        String parsedText = "";
        int openIndex = text.indexOf("<a"), closeIndex = text.indexOf("</a>");
        //Opening and closing tag exists
        while (closeIndex > openIndex && openIndex >= 0) {
            //Crops out the closing tag and opening tag, but leaves the
            //attributes of the opening tag (url='') in
            String linkTag = text.substring(openIndex + 2, closeIndex);
            parsedText = parsedText.concat(text.substring(0, openIndex));

            //Find end of opening tag
            if (linkTag.contains(">")) {
                String beginTag = linkTag.substring(0, linkTag.indexOf(">"));
                String address = null;
                //Find address specified by the attributes
                int addressStartIndex = beginTag.indexOf("url='");
                int addressEndIndex = beginTag.lastIndexOf("'");
                if (addressEndIndex > addressStartIndex && addressStartIndex
                        >= 0) {
                    address = beginTag.substring(addressStartIndex + 5,
                            addressEndIndex);
                }

                //Find text surrounded by the hyperlink tag
                int textStartIndex = linkTag.indexOf(">");
                if (textStartIndex >= 0 && textStartIndex <
                        linkTag.length() - 1 && address != null) {
                    //Records the beginning and ending index of the text
                    links.add(new Link(address, parsedText.length(),
                            parsedText.length() + linkTag.length() -
                            textStartIndex - 1));
                    //Removes the tags from the text, leaving just the text
                    //that was inside the tag
                    parsedText = parsedText.concat(linkTag.substring(
                            textStartIndex + 1));
                }
            }
            text = text.substring(closeIndex + 4);
            openIndex = text.indexOf("<a");
            closeIndex = text.indexOf("</a>");
        }
        parsedText += text;
        return parsedText;
    }

    //Function: Find Link Coordinates
    //@param g              the graphics component
    //Finds the location where the links will appear on the screen after the
    //text has been wrapped
    private void findLinkCoordinates(Graphics g) {

        if (wrappedText != null) {
            for (Link link: links) {
                int beginIndex = link.getBeginIndex();
                int endIndex = link.getEndIndex();
                //(x1, y1) is top left corner of the beginning of the link
                //(x2, y2) is the bottom right corner of the ending of the link
                double posX1 = 0, posY1 = 0, posX2 = 0, posY2 = 0;
                int startingRow = -1, endingRow = -1;
                int numOfChars = 0;
                for (int i = 0; i < wrappedText.length; i++) {
                    int textWidth = g.getFontMetrics().stringWidth(
                            wrappedText[i]);
                    int textHeight = g.getFontMetrics().getHeight();
                    //If the link starts on this exact line
                    if (numOfChars + wrappedText[i].length() >= beginIndex
                            && beginIndex >= numOfChars) {
                        posX1 = (posX - textWidth / 2.0 + width / 2) +
                                g.getFontMetrics().stringWidth(wrappedText[i]
                                        .substring(0, beginIndex - numOfChars));
                        posY1 = posY + i * (textHeight + 10) - textHeight;
                        startingRow = i;
                    }
                    //If the link ends on this exact line
                    if (numOfChars + wrappedText[i].length() >= endIndex
                            && endIndex >= numOfChars) {
                        posX2 = (posX - textWidth / 2.0 + width / 2) +
                                g.getFontMetrics().stringWidth(wrappedText[i]
                                        .substring(0, endIndex - numOfChars));
                        posY2 = posY + i * (textHeight + 10) + 5;
                        endingRow = i;
                    }
                    numOfChars += wrappedText[i].length();
                    numOfChars++;
                }
                //Updates the link object with the opsition of the rendered link
                link.setBounds(posX, width, wrappedText, startingRow,
                        endingRow, posX1, posY1, posX2, posY2, g);
            }
        }
    }

    //----------------------------UTILITY FUNCTION----------------------------//
    //Function: Remove Consecutive
    //@param str            the string to perform the operation on
    //       find           the piece of text to be searched for consecutive
    //                      occurrences
    //@return               the string with all instances of consecutive text
    //                      specified removed
    private String removeConsecutive(String str, String find) {
        if (str.length() < find.length() * 2) return str;
        if (str.substring(0, find.length() * 2).equals(find + find))
            return removeConsecutive(str.substring(find.length()), find);
        return str.charAt(0) + removeConsecutive(str.substring(1), find);
    }

    //-----------------------------MISC FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void mouseAction(double mousePosX, double mousePosY, double
    //                          offsetX, double offsetY, MouseAction action)
    //public void setText(String text, Graphics g)
    //public void setDimensions(double posX, double posY, double width,
    //                              double height)
    //public void setDimensions(double posX, double posY, double width)
    //public double getTextFieldHeight(Graphics g)
    //public void paint(Graphics g)

    //Function: Mouse Action
    //@param posX           the x pos of the mouse
    //       posY           the y pos of the mouse
    //       offsetX        the width of the window border at the left
    //       offsetY        the width of the window border at the top
    //       action         the action of the mouse: [hover/click/release]
    //Relays the position and action of the mouse to be handled by the mouse
    //action handler
    public void mouseAction(double mousePosX, double mousePosY, double offsetX,
                            double offsetY, MouseAction action) {
        mouseActionHandler.mouseAction(mousePosX, mousePosY, offsetX, offsetY,
                action, links);
    }

    //Function: Set Text
    //@param text           a new set of text to be displayed
    //       g              the graphics component
    //Checks the text for hyperlink tags before setting text to the plain text
    //version
    public void setText(String text) {
        if (this.text == null || !this.text.equals(text)) {
            this.text = parseLinks(text);
        }
    }

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //       height         new height
    //Updates the position and the size of the text field
    public void setDimensions(double posX, double posY, double width,
                              double height) {
        freeHeight = false;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    //Function: Set Dimensions
    //@param posX           new x position
    //       posY           new y position
    //       width          new width
    //Updates the position and the size of the text field while allowing
    //height to be flexible
    public void setDimensions(double posX, double posY, double width) {
        setDimensions(posX, posY, width, 0);
        freeHeight = true;
    }

    //Function Get Text Field Height
    //@param g              the graphics component
    //@return               the height of the text field, if the height is set
    //                      dynamic, it will calculate the total height of the
    //                      rows of text
    public double getTextFieldHeight(Graphics g) {
        return freeHeight ? wrappedText.length * (g.getFontMetrics().
                getHeight() + 10) : height;
    }

    //Function: Paint
    //@param g              the graphics component
    //Renders text to the screen. It will be wrapped depending on the width
    //of this text field element and links will also be emphasized with a
    //different color
    public void paint(Graphics g) {

        wrappedText = wrapText(text, width * 0.8, g);
        findLinkCoordinates(g);
        Color textColor = g.getColor();

        int numOfChars = 0;
        for (int i = 0; i < wrappedText.length; i++) {
            if (freeHeight || i * g.getFontMetrics().getHeight() < height) {
                ArrayList<Integer> colorSection = new ArrayList<>();
                for (Link link: links) {
                    int beginIndex = link.getBeginIndex();
                    int endIndex = link.getEndIndex();
                    //If the link begins before or on this line
                    if (numOfChars + wrappedText[i].length() >= beginIndex) {
                        //If the link ends after this line (not before or on)
                        if (numOfChars + wrappedText[i].length() <
                                endIndex) {
                            //If the link begins on this line
                            if (beginIndex >= numOfChars) {
                                colorSection.add(beginIndex - numOfChars);
                            } else {
                                colorSection.add(0);
                            }
                            colorSection.add(wrappedText[i].length());
                        //If the link ends on this line
                        } else if (numOfChars < endIndex) {
                            colorSection.add(beginIndex - numOfChars);
                        }
                    }
                    //If the link ends on this line
                    if (numOfChars + wrappedText[i].length() >= endIndex &&
                            endIndex >= numOfChars) {
                            colorSection.add(endIndex - numOfChars);
                    }
                }

                Collections.sort(colorSection);
                //How text will be rendered:
                //If no colorSection indices on this line, display normal text
                //If colorSection has indices: 0, length of line
                //      whole line displayed as link
                //If colorSection has indices 2 5
                //      from 0 to 2 display normal text, 2 to 5 display link,
                //      5 to end of line display normal text
                int textWidth = g.getFontMetrics().stringWidth(wrappedText[i]);
                int textHeight = g.getFontMetrics().getHeight();
                int textPosX = (int) (posX - textWidth / 2.0 + width / 2);
                int textPosY = (int) posY + i * (textHeight + 10);
                //Even size so every opening index has a closing index
                if (colorSection.size() != 0 && colorSection.size() % 2 == 0) {
                    int lastIndex = 0;
                    boolean colorText = false;
                    for (int index: colorSection) {
                        if (index != 0) {
                            g.setColor(colorText ? new Color(0, 0, 0) :
                                    textColor);
                            g.drawString(wrappedText[i].substring(lastIndex,
                                    index), textPosX, textPosY);
                            textPosX += g.getFontMetrics().stringWidth(
                                    wrappedText[i].substring(lastIndex, index));
                            lastIndex = index;
                        }
                        //Toggle colors
                        colorText = !colorText;
                    }
                    g.setColor(textColor);
                    if (lastIndex != wrappedText[i].length())
                        g.drawString(wrappedText[i].substring(lastIndex),
                                textPosX, textPosY);
                } else {
                    g.setColor(textColor);
                    g.drawString(wrappedText[i], textPosX, textPosY);
                }
            }
            numOfChars += wrappedText[i].length();
            numOfChars++;
        }

        for (Link link: links) {
            link.paint(g);
        }
    }
}
