package Main;
import javax.swing.JFrame;
import java.io.IOException;

public class GameRunner extends JFrame
{

    public static void main(String[] args) throws IOException
    {
        JFrame window=new JFrame("Chess");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Gameboard board=new Gameboard();
        window.add(board);
        window.pack();
        board.launchGame();
    }




}

