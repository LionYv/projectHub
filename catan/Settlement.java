import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Settlement extends JComponent
{
    int x;
    int y;
    int level;

    int playerId;

    HashMap nearResources;

    public Settlement(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.level = type;
    }
    public Point getLocation()
    {
        return new Point(x, y);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int pid) {
        this.playerId = pid;
    }

    public HashMap getNearResources() {
        return nearResources;
    }

    public void setNearResources(HashMap nearResources) {
        this.nearResources = nearResources;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void paintComponent(Graphics g)
    {
       g.fillRect(0,0,100,100);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Settlement{" +
                "x=" + x +
                ", y=" + y +
                ", level=" + level +
                '}';
    }
}
