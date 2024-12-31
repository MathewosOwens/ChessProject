package Pieces;

import Main.Gameboard;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


    public class Rook extends ChessPieces
    {
        public Rook( int color, int col, int row)
        {
            super(color,col,row);
            type= Type.Rook;
            if(color== Gameboard.WHITE)
            {
                try
                {
                    image=ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_rlt60.png"));
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
                    image = ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_rdt60.png"));
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
                if ((targetCol==preCol || targetRow==preRow))
                {
                    return isValidSquare(targetCol, targetRow) && !pieceIsOnStraightLine(targetCol, targetRow);
                }
            }

            return false;
        }

    }

