package Pieces;
import Main.ChessBoardClass;
import Main.Gameboard;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ChessPieces
{
    public BufferedImage image;
    public int x,y;
    public int col, row, preCol, preRow;
    public int color;
    public ChessPieces hittingP;
    public boolean moved;
    public Type type;

    public boolean twoStepped;
    public ChessPieces(int color, int col, int row)
    {
        this.color=color;
        this.col=col;
        this.row=row;
        x= this.getX(col);
        y= this.getY(row);
        preCol= col;
        preRow=row;

    }

    public int getX(int col)
    {
        return col* ChessBoardClass.Square_Size;
    }

    public int getY(int row)
    {
        return row*ChessBoardClass.Square_Size;

    }

    /*public BufferedImage getImage(String imagePath)
    {
        BufferedImage image = null;
        try {
            image= ImageIO.read(getClass().getResourceAsStream(imagePath+"png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }*/

    public int getCol(int x)
    {
        return (x+ChessBoardClass.Half_Square_Size)/ChessBoardClass.Square_Size;
    }

    public int getRow(int y)
    {
        return (y+ChessBoardClass.Half_Square_Size)/ChessBoardClass.Square_Size;
    }

    public void updatePosition()
    {
        if(type==type.Pawn)
        {
            if(Math.abs(row-preRow)==2)
            {
                twoStepped=true;
            }
        }
        x=getX(col);
        y=getY(row);
        preCol=getCol(x);
        preRow=getRow(y);
        moved=true;
    }

    public boolean canMove(int targetCol, int targetRow)
    {
        return false;
    }

    public boolean isWithinBoard(int targetCol, int targetRow)
    {
        return targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7;
    }

    public boolean isSameSquare(int targetCol, int targetRow)
    {
        return targetCol == preCol && targetRow == preRow;
    }

    public boolean pieceIsOnStraightLine(int targetCol, int targetRow)
    {
        //when this piece is moving to the left
        for (int i=preCol-1; i>targetCol; i--)
        {
            for (ChessPieces piece: Gameboard.simPieces)
            {
                if (piece.col==i && piece.row == targetRow)
                {
                    hittingP=piece;
                    return true;
                }
            }
        }
        // when this piece is moving to the right
        for (int i=preCol+1; i<targetCol; i++)
        {
            for (ChessPieces piece: Gameboard.simPieces)
            {
                if (piece.col==i && piece.row == targetRow)
                {
                    hittingP=piece;
                    return true;
                }
            }
        }
        // when this piece is moving up
        for (int r=preRow-1; r>targetRow; r--)
        {
            for (ChessPieces piece: Gameboard.simPieces)
            {
                if (piece.col==targetCol && piece.row == r)
                {
                    hittingP=piece;
                    return true;
                }
            }
        }
        // when this piece is moving down
        for (int r=preRow+1; r<targetRow; r++)
        {
            for (ChessPieces piece: Gameboard.simPieces)
            {
                if (piece.col==r && piece.row == targetRow)
                {
                    hittingP=piece;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow)
    {
        if(targetRow<preRow)
        //upLeft
        {
            for (int c=preCol-1; c>targetCol; c--)
            {
                int diff=Math.abs(c-preCol);
                for(ChessPieces piece: Gameboard.simPieces)
                {
                    if (piece.col==c && piece.row==preRow-diff)
                    {
                        return true;
                    }
                }
            }
            //up right
            for (int c=preCol+1; c<targetCol; c++)
            {
                int diff=Math.abs(c-preCol);
                for(ChessPieces piece: Gameboard.simPieces)
                {
                    if (piece.col==c && piece.row==preRow-diff)
                    {
                        return true;
                    }
                }
            }
        }

        if (targetRow>preRow)
        {
            //Down Left
            for (int c=preCol-1; c>targetCol; c--)
            {
                int diff=Math.abs(c-preCol);
                for(ChessPieces piece: Gameboard.simPieces)
                {
                    if (piece.col==c && piece.row==preRow+diff)
                    {
                        return true;
                    }
                }
            }
            //Down right
            for (int c=preCol+1; c<targetCol; c++)
            {
                int diff=Math.abs(c-preCol);
                for(ChessPieces piece: Gameboard.simPieces)
                {
                    if (piece.col==c && piece.row==preRow+diff)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void resetPosition()
    {
        col=preCol;
        row=preRow;
        x=getX(col);
        y=getY(row);
    }

    public ChessPieces getHittingP(int targetCol, int targetRow)
    {
        for (ChessPieces piece: Gameboard.simPieces)
        {
            if (piece.col==targetCol && piece.row==targetRow && piece != this)
            {
                return piece;
            }
        }

        return null;
    }

    public int getIndex()
    {
        for (int index = 0; index< Gameboard.simPieces.size(); index++)
        {
            if (Gameboard.simPieces.get(index)==this)
            {
                return index;
            }
        }

        return 0;
    }

    public boolean isValidSquare(int targetCol, int targetRow)
    {
        hittingP=getHittingP(targetCol, targetRow);

        if(hittingP==null)
        {
            return true;
        }

        else
        {
            if (hittingP.color != this.color)
            {
                return true;
            }

            else
            {
                hittingP=null;
            }

        }

        return false;
    }

    public void draw(Graphics2D g2)
    {
        g2.drawImage(image, x, y, ChessBoardClass.Square_Size,ChessBoardClass.Square_Size, null);
    }

}

