//----------------------------PAGE INPUT INTERFACE----------------------------//
//@author Titanjack
//@project Tetris
//An interface class for all page input handlers. Allows the position and
//dimension of the input handler to be acquired without specifying which
//handler it is

package Input;

public interface PageInput {

    double getPagePosX();

    double getPagePosY();

    double getPageWidth();

    double getPageHeight();
}
