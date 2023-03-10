import javax.swing.*;
import java.awt.*;

public class GameSetup extends JFrame{
    MyPanel panel;

    public  GameSetup() {

        panel = new MyPanel(this);
        // setting frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(new Color(164, 200, 218));
        this.setSize(1500, 1500);
        this.setLayout(null);
        this.add(panel);
        this.setVisible(true);

    }
    // setup a screen with a winning message
    public GameSetup(String winMsg)
    {
        // setting frame
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(new Color(30, 117, 157));
        this.setSize(500, 500);
        this.setLayout(null);
        // add winning message
        JLabel jl = new JLabel(winMsg);
        jl.setLayout(null);
        jl.setBounds(200,200,100,30);
        jl.setBackground(Color.ORANGE);
        this.add(jl);
        this.setVisible(true);

    }

}
