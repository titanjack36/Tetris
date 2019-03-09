//------------------------------GAME GRID CLASS-------------------------------//
//@author Titanjack
//@project Tetris
//The Game Grid class constructs the tetris game grid with individual grid cells
//and is responsible for the blocks and tetris pieces as they appear on the
//.grid

package Grid;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class GameGrid extends JPanel {

    private int rows;
    private int cols;
    private double cellSize;
    private boolean paintBackground;
    private boolean hideGridBlocks;
    private GridCell[][] grid;

    public GameGrid(double posX, double posY, double width, double height,
                    double cellSize, int rows, int cols) {

        this.rows = rows;
        this.cols = cols;
        paintBackground = true;
        hideGridBlocks = false;
        setGridSize(posX, posY, width, height, cellSize, true);
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void setGridSize(double width, double height, double posX,
    //                        double posY, double cellSize, boolean initialize)
    //public void setGridSize(double posX, double posY, double width,
    //                        double height)
    //public void clearAllGridCells()
    //public boolean hideGridBlocks()
    //public boolean showGridBlocks()
    //public boolean isActive(int row, int col)
    //public boolean isInactive(int row, int col)
    //public boolean isEmpty(int row, int col)
    //public boolean isHighlighted(int row, int col)
    //public double getGridWidth()
    //public double getGridHeight()
    //public void setActive(int row, int col, Color pieceColor)
    //public void setInactive(int row, int col, Color pieceColor)
    //public void setEmpty(int row, int col)
    //public void setHighlighted(int row, int col, Color pieceColor)
    //public void setPaintBackground(boolean paintBackground)
    //public Color getColor(int row, int col)
    //public void paint(Graphics g)
    //public String toString()

    //Function: Set Grid Size
    //@param posX           distance from the left window edge
    //       posY           distance from the top window edge
    //       width          the width of the new grid
    //       height         the height of the new grid
    //       cellSize       the size of each cell in the grid
    //       initialize     whether the individual grid cell objects should be
    //                      created (do not create new if they already exist)
    //Updates the dimension and position of the grid by changing the dimension
    //and position of individual cells
    public void setGridSize(double posX, double posY, double width,
                            double height, double cellSize, boolean
                                    initialize) {

        //Automatically determine the maximum possible cell size if it is left
        //as zero
        if (cellSize == 0)
            cellSize = (height / rows) > (width / cols) ? (width / cols)
                    : (height / rows);
        this.cellSize = cellSize;
        if (initialize)
            grid = new GridCell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (initialize)
                    grid[row][col] = new GridCell(cellSize, posX + col *
                            cellSize, posY + row * cellSize);
                else
                    grid[row][col].setCellSize(cellSize, posX + col *
                            cellSize, posY + row * cellSize);
            }
        }
    }

    //Function: Set Grid Size
    //@param posX           distance from the left window edge
    //       posY           distance from the top window edge
    //       width          the width of the new grid
    //       height         the height of the new grid
    //Calls main Set Grid function and assumes dynamic cell size and will not
    //initialize the individual grid cell objects
    public void setGridSize(double posX, double posY, double width,
                            double height) {
        setGridSize(posX, posY, width, height, 0, false);
    }

    //Function: Clear All Grid Cells
    //Sets all grid cells in the grid to be empty
    public void clearAllGridCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!grid[row][col].isEmpty()) {
                    grid[row][col].setEmpty();
                }
            }
        }
    }

    //Function: Hide Grid Blocks
    //@return           whether the grid was already hidden
    //Sets all cells to hidden state meaning their color will not show
    public boolean hideGridBlocks() {
        if (!hideGridBlocks) {
            hideGridBlocks = true;
            return true;
        }
        return false;
    }

    //Function: Show Grid Blocks
    //@return           whether the grid was already showing
    //Sets all grid cells to visible state where their color can be seen
    public boolean showGridBlocks() {
        if (hideGridBlocks) {
            hideGridBlocks = false;
            return true;
        }
        return false;
    }

    //Function: Is Active
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //@return               whether the target cell is active
    //UNUSED
    /*public boolean isActive(int row, int col) {
        return grid[row][col].isActive();
    }*/

    //Function: Is Inactive
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //@return               whether the target cell is inactive
    public boolean isInactive(int row, int col) {
        return grid[row][col].isInactive();
    }

    //Function: Is Empty
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //@return               whether the target cell is empty
    public boolean isEmpty(int row, int col) {
        return grid[row][col].isEmpty();
    }

    //Function: Is Highlighted
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //@return               whether the target cell is highlighted
    public boolean isHighlighted(int row, int col) {
        return grid[row][col].isHighlighted();
    }

    //Function: Get Grid Width
    //@return           the width of the grid element inside the object and not
    //                  the width of the entire grid object
    public double getGridWidth() {
        return cellSize * cols;
    }

    //Function: Get Grid Height
    //@return           the height of the grid element inside the object and not
    //                  the height of the entire grid object
    //UNUSED
    /*
    public double getGridHeight() {
        return cellSize * rows;
    }*/

    //Function: Set Active
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //       pieceColor     the color to be displayed by the cell
    //Updates the target cell state to active
    public void setActive(int row, int col, Color pieceColor) {
        grid[row][col].setActive(pieceColor);
    }

    //Function: Set Inactive
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //       pieceColor     the color to be displayed by the cell
    //Updates the target cell state to inactive
    public void setInactive(int row, int col, Color pieceColor) {
        grid[row][col].setInactive(pieceColor);
    }

    //Function: Set Empty
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //Updates the target cell state to empty
    public void setEmpty(int row, int col) {
        grid[row][col].setEmpty();
    }

    //Function: Set Highlighted
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //       pieceColor     the color to be displayed by the cell
    //Updates the target cell state to highlighted
    public void setHighlighted(int row, int col, Color pieceColor) {
        grid[row][col].setHighlighted(pieceColor);
    }

    //Function: Set Paint Background
    //@param paintBackground
    //                      whether or not to show the grid when empty
    public void setPaintBackground(boolean paintBackground) {
        this.paintBackground = paintBackground;
    }

    //Function: Get Color
    //@param row            the row index of the target cell
    //       col            the column index of the target cell
    //@return               the color of the target cell
    public Color getColor(int row, int col) {
        return grid[row][col].getColor();
    }

    //Function: Paint
    //@param g              the graphics component
    //Renders the individual grid cells in the grid
    @Override
    public void paint(Graphics g) {

        super.paintComponent(g);
        if (grid != null) {
            for (GridCell[] gridRow : grid)
                for (GridCell cell: gridRow) {
                    cell.paint(g, paintBackground, hideGridBlocks);
                }
        }
    }

    //Function: To String
    //@return               the string representation of the array of grid cells
    @Override
    public String toString() {

        String gridStr = "";
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                gridStr = gridStr.concat(grid[row][col].toString());
            }
            gridStr = gridStr.concat("\n");
        }
        return gridStr;
    }
}
