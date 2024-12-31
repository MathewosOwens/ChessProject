package Main;
import Main.ChessBoardClass;
import Main.Mouse;
import Pieces.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import Pieces.Type;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Rook;
import Pieces.Queen;
import Pieces.Knight;
import Pieces.Bishop;

public class Gameboard extends JPanel implements Runnable {
    public static final int width=1100;
    public static final int length= 800;
    final int FPS = 60;
    Thread gameThread;
    ChessBoardClass board=new ChessBoardClass();
    public static final int WHITE=0;
    public static final int BLACK=1;
    int currentColor=WHITE;

    public static ArrayList<ChessPieces> pieces=new ArrayList<>();
    public static ArrayList<ChessPieces> simPieces=new ArrayList<>();
    public ArrayList<ChessPieces> promoPieces=new ArrayList<>();
    Mouse mouse=new Mouse();
    ChessPieces activeP;
    ChessPieces checkingP;
    public boolean pressed;
    public boolean isChecking;
    public int x, y;
    public boolean entered;
    public boolean canMove;
    public boolean validSquare;
    public boolean promotion;
    public boolean gameOver=false;
    public static ChessPieces castlingP;
    public static Type type;
    public Gameboard()
    {
        setPreferredSize(new Dimension(width,length));
        setBackground(Color.gray);
        addMouseMotionListener(mouse);
        addMouseListener(mouse) ;

        setPieces();
        copyPieces(pieces, simPieces);

    }

    @Override
    public void run()
    {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null)
        {
            currentTime=System.nanoTime();
            delta += (currentTime-lastTime)/drawInterval;
            lastTime=currentTime;
            if(delta >= 1)
            {
                update();
                repaint();
                delta--;

            }
        }
    }

    public void launchGame()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces()
    {
        //white pieces
        pieces.add(new Pawn(WHITE,0,6));
        pieces.add(new Pawn(WHITE,1,6));
        pieces.add(new Pawn(WHITE,2,6));
        pieces.add(new Pawn(WHITE,3,6));
        pieces.add(new Pawn(WHITE,4,6));
        pieces.add(new Pawn(WHITE,5,6));
        pieces.add(new Pawn(WHITE,6,6));
        pieces.add(new Pawn(WHITE,7,6));
        pieces.add(new Rook(WHITE,0,7));
        pieces.add(new Rook(WHITE,7,7));
        pieces.add(new Knight(WHITE,1,7));
        pieces.add(new Knight(WHITE,6,7));
        pieces.add(new Bishop(WHITE,2,7));
        pieces.add(new Bishop(WHITE,5,7));
        pieces.add(new Queen(WHITE,3,7));
        pieces.add(new King(WHITE,4,7));

        //Black Pieces
        pieces.add(new Pawn(BLACK,0,1));
        pieces.add(new Pawn(BLACK,1,1));
        pieces.add(new Pawn(BLACK,2,1));
        pieces.add(new Pawn(BLACK,3,1));
        pieces.add(new Pawn(BLACK,4,1));
        pieces.add(new Pawn(BLACK,5,1));
        pieces.add(new Pawn(BLACK,6,1));
        pieces.add(new Pawn(BLACK,7,1));
        pieces.add(new Rook(BLACK,0,0));
        pieces.add(new Rook(BLACK,7,0));
        pieces.add(new Knight(BLACK,1,0));
        pieces.add(new Knight(BLACK,6,0));
        pieces.add(new Bishop(BLACK,2,0));
        pieces.add(new Bishop(BLACK,5,0));
        pieces.add(new Queen(BLACK,3,0));
        pieces.add(new King(BLACK,4,0));
    }

    private void copyPieces(ArrayList<ChessPieces> source, ArrayList<ChessPieces> target)
    {
        target.clear();
        for(int i=0; i<source.size(); i++)
        {
            target.add(source.get(i));
        }
    }

    private void update()
    {
        ChessPieces newPiece;
        if (promotion)
        {
            promoting();
        }
        else if(!gameOver)
        {
            if (mouse.pressed) // if the player is holding a piece
            {
                if (activeP==null)
                {
                    for (ChessPieces piece:simPieces)
                    {
                        if (piece.color==currentColor && piece.col==mouse.x/board.Square_Size && piece.row==mouse.y/board.Square_Size)
                        {
                            activeP=piece;

                        }
                    }

                }

                else //if the player is not holding a piece (thinking piece)
                {
                    simulate();
                }

            }

            if (mouse.pressed==false)
            {
                if(activeP!=null)
                {
                    if (validSquare)
                    {
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        if(castlingP!=null)
                        {
                            castlingP.updatePosition();
                        }

                        if(isKingInCheck() && isCheckMate())
                        {
                            gameOver=true;
                        }

                        else
                        {
                            if (canPromote())
                            {
                                promotion=true;

                            }

                            else
                            {
                                changePlayer();
                            }
                        }

                    }
                    else
                    {
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                        activeP=null;
                    }
                }
            }
        }
    }

    private boolean isIllegal(ChessPieces king)
    {
        if (king.type==type.King)
        {
            for(ChessPieces piece: simPieces)
            {
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean kingCanMove(ChessPieces king)
    {
        if(isValidMove(king, 1, 0)) {return true;}
        if(isValidMove(king, 1, 1)) {return true;}
        if(isValidMove(king, 1, -1)) {return true;}
        if(isValidMove(king, 0, 1)) {return true;}
        if(isValidMove(king, 0, -1)) {return true;}
        if(isValidMove(king, -1, 0)) {return true;}
        if(isValidMove(king, -1, -1)) {return true;}
        if(isValidMove(king, -1, 1)) {return true;}

        return false;
    }

    private boolean isValidMove(ChessPieces king, int colPlus, int rowPlus)
    {
        boolean isValidMove=false;
        //update King's position for a second
        king.col+=colPlus;
        king.row+=rowPlus;
        if(king.canMove(king.col, king.row))
        {
            if(king.hittingP!=null)
            {
                simPieces.remove(king.hittingP.getIndex());
            }

            if(isIllegal(king)==false)
            {
                isValidMove=true;
            }
        }

        king.resetPosition();
        copyPieces(pieces, simPieces);
        return isValidMove;
    }

    private boolean opponentCanCaptureKing()
    {
        ChessPieces king=getKing(false);

        for (ChessPieces piece: simPieces)
        {
            if (piece.color != king.color && piece.canMove(king.col, king.row))
            {
                return true;
            }
        }

        return false;
    }

    private boolean isKingInCheck()
    {
        ChessPieces king=getKing(true);
        if (activeP.canMove(king.col, king.row))
        {
            checkingP=activeP;
            return true;
        }

        else
        {
            checkingP=null;
        }

        return false;
    }

    private boolean isCheckMate()
    {
        ChessPieces king=getKing(true);
        if(kingCanMove(king)) // the king can move to a different square
        {
            return false;
        }

        else
        {
            int colDiff=Math.abs(checkingP.col-king.col);
            int rowDiff=Math.abs(checkingP.row-king.row);
            //the player wants to capture the checking piece
            if(colDiff==0) //the checking piece is attacking vertically
            {
                if(checkingP.row<king.row) // checking piece is behind king
                {
                    for (int row=checkingP.row; row<king.row; row++)
                    {
                        for (ChessPieces piece : simPieces)
                        {
                            if(piece.color != king.color && piece.canMove(checkingP.col, row))
                            {
                                return false;
                            }
                        }
                    }
                }

                if (checkingP.row>king.row) //checking piece is infront of king
                {
                    for (int row=checkingP.row; row>king.row; row--)
                    {
                        for (ChessPieces piece : simPieces)
                        {
                            if(piece.color != currentColor && piece.canMove(checkingP.col, row))
                            {
                                return false;
                            }
                        }
                    }
                }
            }
            else if(rowDiff==0) //the checking piece is attacking horizontally
            {
                if(checkingP.col<king.col)
                {
                    for (int col=checkingP.col; col<king.col; col++)
                    {
                        for (ChessPieces piece : simPieces)
                        {
                            if(piece.color != currentColor && piece.canMove(col, checkingP.row))
                            {
                                return false;
                            }
                        }
                    }
                }

                if(checkingP.col>king.col)
                {
                    for (int col=checkingP.col; col>king.col; col--)
                    {
                        for (ChessPieces piece : simPieces)
                        {
                            if( piece.color != currentColor && piece.canMove(col, checkingP.row))
                            {
                                return false;
                            }
                        }
                    }
                }
            }

            else if(colDiff==rowDiff)// the checking piece is attacking diagonally
            {
                if(checkingP.row<king.row)
                {
                    if(checkingP.col<king.col)
                    {
                        for (int col=checkingP.col, row=checkingP.row; col<king.col; col++, row++)
                        {
                            for (ChessPieces piece : simPieces)
                            {
                                if(piece.color !=currentColor && piece.canMove(col, row))
                                {
                                    return false;
                                }
                            }
                        }
                    }

                    if(checkingP.col>king.col)
                    {
                        for (int col=checkingP.col, row=checkingP.row; col>king.col; col--, row++)
                        {
                            for (ChessPieces piece : simPieces)
                            {
                                if( piece.color !=currentColor && piece.canMove(col, row))
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }

                if(checkingP.row>king.row)
                {
                    if(checkingP.col<king.col)
                    {
                        for (int col=checkingP.col, row=checkingP.row; col<king.col; col++, row--)
                        {
                            for (ChessPieces piece : simPieces)
                            {
                                if( piece.color !=currentColor && piece.canMove(col, row))
                                {
                                    return false;
                                }
                            }
                        }
                    }

                    if(checkingP.col>king.col)
                    {
                        for (int col=checkingP.col, row=checkingP.row; col>king.col; col--, row--)
                        {
                            for (ChessPieces piece : simPieces)
                            {
                                if( piece.color !=currentColor && piece.canMove(col, row))
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

        }
        return true;
    }

    
    private ChessPieces getKing(boolean opponent)
    {
        ChessPieces king=null;

        for(ChessPieces piece:simPieces)
        {
            if(opponent)
            {
                if (piece.type==Type.King && piece.color != currentColor)
                {
                    king=piece;
                    return king;
                }

            }
            else
            {
                if (piece.type==Type.King && piece.color== currentColor)
                {
                    king=piece;
                    return king;
                }
            }
        }
        return king;
    }

    private void simulate()
    {
        canMove=false;
        validSquare=false;

        copyPieces(pieces, simPieces);
        activeP.x=mouse.x-board.Half_Square_Size;
        activeP.y=mouse.y-board.Half_Square_Size;
        activeP.col=activeP.getCol(activeP.x);
        activeP.row=activeP.getRow(activeP.y);

        if(castlingP!=null)
        {
            castlingP.col=castlingP.preCol;
            castlingP.x=castlingP.getX(castlingP.col);
            castlingP=null;
        }

        if(activeP.canMove(activeP.col, activeP.row))
        {
            canMove=true;
            if (activeP.hittingP != null)
            {
                simPieces.remove(activeP.hittingP.getIndex());
            }
            checkCastling();
            if(isIllegal(activeP)==false && opponentCanCaptureKing()==false)
            {
                validSquare=true;
            }

        }

    }

    
    private void changePlayer()
    {
        if(currentColor==WHITE)
        {
            currentColor=BLACK;
            for (ChessPieces piece: pieces)
            {
                if(piece.color==BLACK)
                {
                    piece.twoStepped=false;
                }
            }
        }

        else
        {
            currentColor=WHITE;
            for (ChessPieces piece: pieces)
            {
                if(piece.color==WHITE)
                {
                    piece.twoStepped=false;
                }
            }
        }

        activeP=null;
    }

    public boolean isInCheck(ChessPieces king)
    {
        int col, row;
        for (ChessPieces p: Gameboard.simPieces)
        {
            if (p.type != Type.King && p.color != currentColor)
            {
                king=p;
            }
        }
        col=king.col;
        row=king.row;
        for (ChessPieces piece:Gameboard.simPieces)
        {
            if (piece.type !=Type.King && piece.color!=currentColor&& piece.canMove(col, row))
            {
                Gameboard.castlingP=piece;
                return true;
            }
        }
        return false;
    }

    private void checkCastling()
    {
        if(castlingP != null)
        {
            if(castlingP.col==0)
            {
                castlingP.col+=3;
            }

            else if(castlingP.col==7)
            {
                castlingP.col-=2;
            }
            castlingP.x=castlingP.getX(castlingP.col);
        }
    }

    private boolean canPromote()
    {
        if(activeP.type==type.Pawn)
        {
            if (currentColor==WHITE && activeP.row==0 || currentColor==BLACK && activeP.row==7)
            {
                promoPieces.clear();
                promoPieces.add(new Rook(currentColor,9,2));
                promoPieces.add(new Knight(currentColor,9,3));
                promoPieces.add(new Bishop(currentColor,9,4));
                promoPieces.add(new Queen(currentColor,9,5));
                return true;
            }
        }
        return false;
    }

    private void promoting()
    {
        if(mouse.pressed)
        {
            for (ChessPieces piece : promoPieces)
            {
                if(piece.col==mouse.x/ChessBoardClass.Square_Size && piece.row== mouse.y/ChessBoardClass.Square_Size)
                {
                    switch(piece.type)
                    {
                        case Rook:simPieces.add(new Rook(currentColor, activeP.col, activeP.row)); break;
                        case Knight:simPieces.add(new Knight(currentColor, activeP.col, activeP.row)); break;
                        case Bishop:simPieces.add(new Bishop(currentColor, activeP.col, activeP.row)); break;
                        case Queen:simPieces.add(new Queen(currentColor, activeP.col, activeP.row)); break;
                        default: break;
                    }

                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);
                    promotion=false;
                    changePlayer();
                }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D)g;
        board.draw(g2);
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        g2.setColor(Color.white);

        if (promotion)
        {
            g2.drawString("Promote for", 840, 150);
            for (ChessPieces piece:promoPieces)
            {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), ChessBoardClass.Square_Size, ChessBoardClass.Square_Size, null);
            }
        }
        else if (gameOver)

        {
            String a;
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 100));
            g2.setColor(Color.white);
            if(currentColor==WHITE)
            {
                a="White Wins!";
            }

            else
            {
                a="Black Wins!";
            }

            g2.drawString(a, 800, 100);
        }
        else
        {
            if(currentColor==WHITE)
            {
                g2.drawString("White's Turn",800, 200);
                if(checkingP !=null && checkingP.color==BLACK)
                {
                    g2.setColor(Color.red);
                    g2.drawString("The White King", 840, 650);
                    g2.drawString("is in check!", 840, 700);
                }
            }

            else
            {
                g2.drawString("Black's Turn", 800, 200);
                if (checkingP !=null && checkingP.color==WHITE)
                {
                    g2.setColor(Color.red);
                    g2.drawString("The Black King", 840, 650);
                    g2.drawString("is in check!", 840, 700);
                }

            }
        }


        for (ChessPieces p: simPieces)
        {
            p.draw(g2);
            if (activeP !=null)
            {

                if(isIllegal(activeP))
                {
                    g2.setColor(Color.gray);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col*board.Square_Size, activeP.row*board.Square_Size, board.Square_Size, board.Square_Size);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    activeP.draw(g2);
                }
                else
                {
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col*board.Square_Size, activeP.row*board.Square_Size, board.Square_Size, board.Square_Size);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    activeP.draw(g2);
                }

            }

        }
    }

}
