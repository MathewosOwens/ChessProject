package Main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse extends MouseAdapter implements MouseListener
{
    public int x, y;
    public boolean pressed;
    //public int mouseClickCount;
    @Override
    public void mouseClicked(MouseEvent e)
    {
        pressed=true;
    }
    

    @Override
    public void mouseReleased(MouseEvent e)
    {
        pressed=false;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        x=e.getX();
        y=e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        x=e.getX();
        y=e.getY();
    }
    
    /*public int getClickCount(MouseEvent e)
    {
        mouseClickCount=e.getClickCount();
        return mouseClickCount;
    }*/


}


