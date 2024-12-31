package Pieces;

import Main.Gameboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;


    public class Pawn extends ChessPieces
    {

        JPanel frame;
        public Pawn( int color, int col, int row)
        {
            super(color,col,row);
            type= Type.Pawn;
            frame=new JPanel();
            if(color== Gameboard.WHITE)
            {
                try
                {
                    image= ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_plt60.png"));

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
                    image=ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_pdt60.png"));
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
                int moveValue;

                if (color== Gameboard.WHITE)
                {
                    moveValue=-1;
                }

                else
                {
                    moveValue=1;
                }

                hittingP=getHittingP(targetCol, targetRow);

                if(targetCol==preCol && targetRow==preRow+moveValue && hittingP==null)
                {
                    return true;
                }
                // if pawn's first move and player wants to move the pawn 2 squares forward
                if(targetCol==preCol && targetRow==(preRow+(moveValue*2)) && hittingP==null && !moved && !pieceIsOnStraightLine(targetCol, targetRow))
                {
                    return true;
                }


                //capture a piece on the diagonal
                if(Math.abs(targetCol-preCol)==1 && targetRow==preRow+moveValue && hittingP !=null && hittingP.color!= color)
                {
                    return true;
                }
                //en passant
                if (Math.abs(targetCol-preCol)==1 && targetRow==(preRow+moveValue) )
                {
                    for(ChessPieces piece: Gameboard.simPieces)
                    {
                        if(piece.col==targetCol && piece.row==preRow && piece.twoStepped)
                        {
                            hittingP=piece;
                            return true;
                        }
                    }
                }
            }



            return false;
        }
    }


