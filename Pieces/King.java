package Pieces;

import Main.Gameboard;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class King extends Pieces.ChessPieces
{
    public King(int color, int col, int row)
    {
        super(color,col,row);
        type=Type.King;
        if(color== Gameboard.WHITE)
        {
            try
            {
                image=ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_klt60.png"));
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
                image=ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_kdt60.png"));
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
            if (Math.abs(targetCol-preCol)+Math.abs(targetRow-preRow)==1 ||  (Math.abs(targetCol-preCol)*Math.abs(targetRow=preRow)==1))
            {
                if (isValidSquare(targetCol, targetRow))
                    return true;
            }


        }

        if (!moved)
        {
            //right castling
            if (targetCol==preCol+2 && targetRow==preRow && !pieceIsOnStraightLine(targetCol, targetRow))
            {
                for (ChessPieces piece:Gameboard.simPieces)
                {
                    if (piece.col==preCol+3 && piece.row==preRow && !piece.moved)
                    {
                        Gameboard.castlingP=piece;
                        return true;
                    }
                }
            }
            //left castling
            if (targetCol==preCol-2 && targetRow==preRow && !pieceIsOnStraightLine(targetCol, targetRow))
            {
                ChessPieces[] p =new ChessPieces[2];
                for (ChessPieces piece: Gameboard.simPieces)
                {
                    if (piece.col==preCol-3  && piece.row==targetRow)
                    {
                        p[0]=piece;
                    }

                    if(piece.col==preCol-4 && piece.row==targetRow)
                    {
                        p[1]=piece;
                    }

                    if(p[0]==null && p[1]!=null && !p[1].moved)
                    {
                        Gameboard.castlingP=p[1];
                        return true;
                    }
                }
            }


        }

        return false;
    }


}
