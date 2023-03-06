import javax.swing.*;
import java.awt.*;

public class SettleComponent extends JPanel {
    int x;
    int y;
    int level;
    Color c;
    public SettleComponent(Color c, int level) {
        this.c = c;
        this.level = level;
    }


    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(c);
        if (level == 0) {
            g.fillOval(-5, -10, 20, 20);
            g.drawOval(-5, -10, 20, 20);
        }
        else
        {
            g.fillRect(-5, -10, 20, 20);
            g.drawRect(-5, -10, 20, 20);
        }
    }
}
