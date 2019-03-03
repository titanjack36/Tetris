//------------------------------GRID CELL CLASS-------------------------------//
//@author Titanjack
//@project Tetris
//The tetris grid is an array composed of individual Grid Cell object instances,
//each of which has its own state and properties in order to display the tetris
//pieces and blocks with dynamic motion.

package GridComponents;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class GridCell extends JPanel {

    private Color color;
    private CellState state;
    private double size;
    private double posX;
    private double posY;

    public GridCell(double size, double posX, double posY) {
        this(CellState.EMPTY, size, posX, posY, null);
    }

    public GridCell(CellState state, double size, double posX, double posY,
                    Color color) {

        this.state = state;
        this.color = color;
        setCellSize(size, posX, posY);
    }

    //----------------------------UTILITY FUNCTION----------------------------//
    //Function: Round
    //@param num            the number to be rounded
    //@return               the rounded number
    private int rnd(double num) {
        return (int)(num + 0.5);
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void setActive(Color color)
    //public void setInactive(Color color)
    //public void setEmpty()
    //public void setHighlighted(Color color)
    //public void setCellSize(double size, double posX, double posY)
    //public boolean isActive()
    //public boolean isInactive()
    //public boolean isEmpty()
    //public boolean isHighlighted()
    //public Color getColor()
    //public void paint(Graphics g, boolean paintBackGround)
    //public void paint(Graphics g)
    //public String toString()

    //Function: Set Active
    //@param color          the color to be displayed by the cell
    //Sets the cell state to active
    public void setActive(Color color) {

        state = CellState.ACTIVE;
        this.color = color;
    }

    //Function: Set Inactive
    //@param color          the color to be displayed by the cell
    //Sets the cell state to inactive
    public void setInactive(Color color) {

        state = CellState.INACTIVE;
        this.color = color;
    }

    //Function: Set Empty
    //@param color          the color to be displayed by the cell
    //Sets the cell state to empty
    public void setEmpty() {

        state = CellState.EMPTY;
        this.color = null;
    }

    //Function: Set Highlighted
    //@param color          the color to be displayed by the cell
    //Sets the cell state to highlighted
    public void setHighlighted(Color color) {

        state = CellState.HIGHLIGHTED;
        this.color = color;
    }

    //Function: Set Cell Size
    //@param size           the new size of the grid cell
    //       posX           the new position from the left of the window
    //       posY           the new position from the top of the window
    //Updates the position and dimensions of the grid cell
    public void setCellSize(double size, double posX, double posY) {

        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    //Function: Is Active
    //@return               whether the cell state is active
    public boolean isActive() {
        return state.equals(CellState.ACTIVE);
    }

    //Function: Is Inactive
    //@return               whether the cell state is inactive
    public boolean isInactive() {
        return state.equals(CellState.INACTIVE);
    }

    //Function: Is Empty
    //@return               whether the cell state is empty
    public boolean isEmpty() {
        return state.equals(CellState.EMPTY);
    }

    //Function: Is Highlighted
    //@return               whether the cell state is highlighted
    public boolean isHighlighted() {
        return state.equals(CellState.HIGHLIGHTED);
    }

    //Function: Get Color
    //@return               the current color of the grid cell
    public Color getColor() {
        return color;
    }

    //Function: Paint
    //@return               renders the grid cell as a square with specified
    //                      color
    public void paint(Graphics g, boolean paintBackGround) {

        super.paintComponent(g);
        if (isActive() || isInactive()) {
            g.setColor(color);
            g.fillRect(rnd(posX + 2), rnd(posY + 2),
                    rnd(size - 2), rnd(size - 2));
        } else {
            if (isHighlighted() || (isEmpty() && paintBackGround)) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(isEmpty() ? new Color(219, 219, 219, 255)
                        : color);
                g2d.drawRect(rnd(posX + 3), rnd(posY + 3),
                        rnd(size - 3), rnd(size - 3));
                g2d.setStroke(new BasicStroke(1));
            }
        }
    }

    //Function: Paint
    //@return               renders the grid cell as a square with specified
    //                      color
    @Override
    public void paint(Graphics g) {
        paint(g, true);
    }

    //Function: To String
    //@return               gets a string representation of the grid cell with
    //                      states represented by different characters
    @Override
    public String toString() {

        switch (state) {
            case ACTIVE: return "#";
            case INACTIVE: return "O";
            case EMPTY: return ".";
            default: return "";
        }
    }

}
