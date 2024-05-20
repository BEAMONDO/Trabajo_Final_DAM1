import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Fondo extends JPanel 
{
    private Image imagenFondo;

    public Fondo(String imagePath) 
    {
        try 
        {
            imagenFondo = ImageIO.read(new File(imagePath));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (imagenFondo != null) 
        {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}