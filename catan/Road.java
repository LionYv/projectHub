import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Road extends JPanel
{
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private int playerId;
    private boolean isPlaced;
    private int [] tilesRelated;
    private Color color;

    public Road(int xStart, int yStart, int xEnd, int yEnd,  Color c) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.playerId = 0;
        this.isPlaced = false;
        this.tilesRelated = null;
        color = c;

    }
    public void setColor(Color c)
    {
        color = c;
    }
    public Point getStartLocation()
    {
        return new Point(xStart,yStart);
    }
    public Point getEndLocation()
    {
        return new Point(xEnd,yEnd);
    }



    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(xStart+3 , yStart + 7, xEnd+3 , yEnd + 7);
    }

    public int getxStart() {
        return xStart;
    }

    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public void setxEnd(int xEnd) {
        this.xEnd = xEnd;
    }

    public int getyEnd() {
        return yEnd;
    }

    public void setyEnd(int yEnd) {
        this.yEnd = yEnd;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playId) {
        this.playerId = playId;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public int[] getTilesRelated() {
        return tilesRelated;
    }

    public void setTilesRelated(int[] tilesRelated) {
        this.tilesRelated = tilesRelated;
    }
}
