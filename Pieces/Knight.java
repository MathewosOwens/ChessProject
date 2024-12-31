package Pieces;

import Main.Gameboard;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


    public class Knight extends ChessPieces
    {

        public Knight( int color, int col, int row)
        {
            super(color,col,row);
            type= Type.Knight;
            if(color== Gameboard.WHITE)
            {
                try
                {
                    image= ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_nlt60.png"));
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
                    image = ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_ndt60.png"));
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        public boolean canMove(int targetCol, int targetRow)
        {
            if (isWithinBoard(targetCol, targetRow))
            {
                if ((Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow))==2)
                {
                    return isValidSquare(targetCol, targetRow);
                }
            }

            return false;
        }
    }


