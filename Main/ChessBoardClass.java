package Main;

import java.awt.*;
import javax.swing.JFrame;
import Pieces.ChessPieces;
import Pieces.King;
import Pieces.Bishop;
import Pieces.Queen;
import Pieces.Knight;
import Pieces.Rook;
import Pieces.Pawn;
import Pieces.Type;


public class ChessBoardClass {

        public static final int width=1100;
        public static final int length=800;
        final int Max_Col = 8;
        final int Max_Row = 8;
        public static final int Square_Size=80;
        public static final int Half_Square_Size= Square_Size/2;

        public void draw(Graphics2D g2)
        {
            int c=0;
            JFrame frame=new JFrame();
            for(int row = 0; row < Max_Row; row++)
            {
                for(int col=0;col<Max_Col; col++)
                {
                    if(c==0)
                    {
                        g2.setColor(new Color(210,165, 125));
                        c=1;
                    }
                    else
                    {
                        g2.setColor(new Color(167, 100, 41));
                        c=0;
                    }
                    g2.fillRect(col*Square_Size, row*Square_Size, Square_Size, Square_Size);

                    if (row==0 && col==0)
                    {
                        frame.setExtendedState(frame.getExtendedState()|frame.MAXIMIZED_BOTH );
                        frame.setLocation(0, 0);
                    }
                }
                if(c==0)
                {
                    c=1;
                }
                else
                {
                    c=0;
                }
            }

        }


    }


