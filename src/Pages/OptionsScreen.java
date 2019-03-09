//----------------------------OPTIONS SCREEN CLASS----------------------------//
//@author Titanjack
//@project Tetris
//Manages content to be displayed on the options screen. Once completed, it will
//let the user manage key bindings

package Pages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OptionsScreen extends JPanel {

    private double posX;
    private double posY;
    private double width;
    private double height;

    public OptionsScreen(double posX, double posY, double width,
                         double height) {
        setDimensions(posX, posY, width, height);
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
        text = removeConsecutive(text, "\n");
        String[] words = text.split(" ");
        ArrayList<String> wordLines = new ArrayList<>();

        int numOfWords = 0;
        String wordLine = "";
        while(numOfWords < words.length) {
            if (wordLine.length() == 0 || g.getFontMetrics().stringWidth
                    (wordLine + words[numOfWords]) < maxWidth) {
                wordLine = wordLine.concat((wordLine.length() == 0 ? "" : " ")
                        + words[numOfWords]);
                numOfWords++;
            } else {
                wordLines.add(wordLine);
                wordLine = "";
            }
        }
        if (wordLine.length() != 0) wordLines.add(wordLine);
        return wordLines.toArray(new String[0]);
    }

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

    //Function: Paint
    //@param g          the graphics component
    //Renders the contents of the options screen
    @Override
    public void paint(Graphics g) {

        g.setColor(new Color(114, 114, 114));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "Options";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (int) (posX - textWidth / 2.0 +
                width / 2), (int) posY + 45);

        g.setFont(new Font("Arial", Font.BOLD, 27));
        text = "Sorry, the options page is currently under construction";
        String[] wrappedText = wrapText(text, width * 0.8, g);

        for (int i = 0; i < wrappedText.length; i++) {
            textWidth = g.getFontMetrics().stringWidth(wrappedText[i]);
            int textHeight = g.getFontMetrics().getHeight();
            g.drawString(wrappedText[i], (int) (posX - textWidth / 2.0 +
                    width / 2), (int) posY + 100 + i * (textHeight + 10));
        }
    }
}
