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
        g.setColor(c);
        // a new settlement
        if (level == 0) {
            g.fillOval(0, 0, 23, 23);
            g.drawOval(10, 10, 23, 23);
        }
        else
        {
            // a city
            g.fillRect(-5, -10, 30, 30);
            g.drawRect(-5, -10, 30, 30);
        }
    }
}
