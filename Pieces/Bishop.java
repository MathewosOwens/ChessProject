package Pieces;
import Main.Gameboard;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bishop extends ChessPieces
{

    public Bishop( int color, int col, int row)
    {
        super(color,col,row);
        type=Type.Bishop;
        if(color== Gameboard.WHITE)
        {
            try
            {
                image=ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_blt60.png"));
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        else
        {
            try
            {
                image= ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_bdt60.png"));
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }

    public boolean canMove(int targetCol, int targetRow)
    {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow))
        {
            if (Math.abs(targetCol-preCol)==Math.abs(targetRow-preRow))
            {
                return isValidSquare(targetCol, targetRow) && !pieceIsOnDiagonalLine(targetCol, targetRow);
            }
        }

        return false;
    }
}
