import java.awt.*;

public class Tile {
    Polygon polygon;
    int resource;
    int number;
    Point position;
    boolean isFour;
    int amt;

    public Tile(Polygon p, int resource, int number, Point position,int amt, boolean isFour) {
        this.polygon = p;
        this.resource = resource;
        this.number = number;
        this.position = position;
        this.amt = amt;
        this.isFour = isFour;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "resource=" + resource +
                ", number=" + number +
                ", position=" + position +
                ", isFour=" + isFour +
                '}';
    }
    public int getX(){return this.position.x;}
    public int getY(){return this.position.y;}
    public int getResource() {
        return resource;
    }
    public Polygon getPolygon()
    {
        return this.polygon;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
    public int getAmt(){return  amt;}
    public void setAmt(int amt){this.amt = amt;}



}
