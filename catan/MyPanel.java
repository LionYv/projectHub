import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static javax.swing.JColorChooser.showDialog;

public class MyPanel extends JLayeredPane implements ActionListener {
    static Tile [] tileArray = new Tile[19];
    static Road [] roads = new Road[tileArray.length*6];
    static Tile [] tileColors = new Tile[5];
    static Settlement[] settlements = new Settlement[6*19];
    public static int settlementsPlaced = 0;
    public static Player[] players;
    public static final int BOARD_HEIGHT = 1500;
    // used for calculations
    private static final double sqrt3div2 = 0.86602540378;
    public static int heightMargin = 100;
    public static int hexagonSide;
    public static int widthMargin;
    public static boolean wasInitialized = false;
    public static Integer[] nums;
    public GameSetup frameParent;

    public static final Color WOOL_COLOR = new Color(98, 255, 86);
    public static final Color HILLS_COLOR = new Color(220, 127, 26);
    public static final Color FOREST_COLOR = new Color(30, 161, 20);
    public static final Color GRAIN_COLOR = new Color(206, 203, 27);
    public static final Color ORE_COLOR = new Color(196, 196, 196);

    MyPanel(GameSetup parent)
    {
             // setting a new game panel
             this.setLayout(null);
             this.setBounds(0,0,1600,1600);
             this.setBackground(new Color(90,120,90));
             addMessageDisplay();
             players = new Player[2];
             players[0] = new Player(1,new int[6],null);
             players[1] = new Player(2,new int[6], null);
             // make players choose a color
             players[0].color = JColorChooser.showDialog(new JColorChooser(), "player 0: choose a color", Color.WHITE);
             players[1].color = JColorChooser.showDialog(new JColorChooser(), "player 1: choose a color", Color.WHITE);
             frameParent = parent;
             initTiles(); // start painting the board

    }

    public void initTiles()
    {
        for (int i = 0; i < tileColors.length ; i++)
        {
            tileColors[i] = new Tile(null,0,0,new Point(0,0), 0, false);

        }
        for (int i = 0; i < 19; i++)
        {
                tileArray[i] = new Tile(null,0,0,new Point(),0, false);
        }
    }
    public void addMessageDisplay()
    {
        // currently a place holder
        JLabel jb = new JLabel("");
        jb.setLayout(null);
        jb.setBounds(330,50,190,40);
        this.add(jb);
    }
    public void closeWindow()
    {
        frameParent.dispose();
    }
   
    public void addButtons(GameBoard g)
    {   // adding functional buttons: roll dice, end turn
        JButton roll = new JButton("roll a dice");
        roll.setBounds(100,100,100,100);
        roll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                g.rollDice();
            }
        });
        this.add(roll);
        JButton endTurn = new JButton("end your turn");
        endTurn.setBounds(100,220,100,100);
        endTurn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                g.endTurn();
            }
        });
        this.add(endTurn);
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(6));
        hexagonSide = (BOARD_HEIGHT - 2 * heightMargin) / 18;
        widthMargin = (getWidth() - (int) (10 * hexagonSide * sqrt3div2)) / 2;
        // paint hexagons and generate numbers for each hexagon
        drawGrid(g2);
        generateNums(g2);
        if(!wasInitialized) // only happens one time
        {
            createPosForSettle(); // create the positions in which a settlement will be placed
            buildRoads(); // initialize road array

            GameBoard gameBoard = new GameBoard(tileArray, roads,settlements, players, this);
            MouseListener aMouse = new MouseListen(this, gameBoard);
            this.addMouseListener(aMouse);
            addButtons(gameBoard);

        }
        wasInitialized = true;
    }

    public void createPosForSettle()
    {
        for (int i = 0; i <tileArray.length; i++) {
            Polygon currP = tileArray[i].getPolygon();
            for (int j = 0; j < currP.npoints; j++) {
                int x = currP.xpoints[j];
                int y = currP.ypoints[j];
                if (!AlreadyIn(x,y) && settlementsPlaced < settlements.length) // if the position isnt in yet
                {
                    settlements[settlementsPlaced++] = new Settlement(x,y,0);
                }
            }

        }

    }
    public void generateNums(Graphics2D g2)
    {
        if (wasInitialized)
        {
            for (int i = 0; i < tileArray.length; i++) {
                g2.setFont(new Font("default", Font.BOLD, 20));
                g2.drawString(Integer.toString(nums[i]), tileArray[i].getX(), tileArray[i].getY());
            }
        }
        else {
            nums = new Integer[]{2, 3, 3, 4, 4, 5, 5, 6, 7, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
            List<Integer> numList = Arrays.asList(nums);
            Collections.shuffle(numList); // shuffle array randomly
            numList.toArray(nums);
            System.out.println(Arrays.toString(nums));
            for (int i = 0; i < tileArray.length; i++) {
                g2.setFont(new Font("default", Font.BOLD, 20));
                tileArray[i].setNumber(nums[i]);
                g2.drawString(Integer.toString(nums[i]), tileArray[i].getX(), tileArray[i].getY());
            }
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("input: ");
    }


    public void drawGrid(Graphics2D g2) {
        // drawing all the hexagon tiles
        int constant = 600;
        int offset = 65;
        // y(x) = 110 + x*115
        int startY = 110;
        int yDiff = 120;
        
        if (wasInitialized) { // just redraw
            for (int i = 0; i < tileArray.length; i++) {
                drawHex(tileArray[i].getX(), tileArray[i].getY(), g2, (GetColor(tileArray[i].getResource())));
            }
        }
        else {

            for (int i = 0; i < 5; i++) {
                tileColors[i].amt = 4;
                tileColors[i].isFour = true;
            }
            Random rnd = new Random();
            // decide randomly which element will only have 3 hexagons
            int ran = rnd.nextInt(5);
            tileColors[ran].amt = 3;
            tileColors[ran].isFour = false;
            int x = -1;
            int cnt = 0;
            for (int i = 0; i <= 2; i++) {
                Color c = generateTileType(tileColors);
                x = determineResource(c); // determine resource for current tile, based on color randomly chosen
                // create the new hexagon tile with matching color
                tileArray[cnt++] = new Tile(makeHex(constant + i * 135, startY), x, 0, new Point(constant + i * 135, startY), 0, false);
                drawHex(constant + i * 135, startY, g2, c);
                c = generateTileType(tileColors);
                x = determineResource(c);
                drawHex(constant + i * 135, startY + yDiff * 4, g2, c);
                tileArray[cnt++] = new Tile(makeHex(constant + i * 135, startY + yDiff * 4), x, 0, new Point(constant + i * 135, startY + yDiff * 4), 0, false);

            }
            for (int i = 0; i <= 3; i++) {
                Color c = generateTileType(tileColors);
                x = determineResource(c);
                drawHex(constant - 135 + i * 135 + offset, startY + yDiff, g2, c);
                tileArray[cnt++] = new Tile(makeHex(constant - 135 + i * 135 + offset, startY + yDiff), x, 0, new Point(constant - 135 + i * 135 + offset, startY + yDiff), 0, false);
                c = generateTileType(tileColors);
                x = determineResource(c);
                drawHex(constant - 135 + i * 135 + offset, startY + yDiff * 3, g2, c);
                tileArray[cnt++] = new Tile(makeHex(constant - 135 + i * 135 + offset, startY + yDiff * 3), x, 0, new Point(constant - 135 + i * 135 + offset, startY + yDiff * 3), 0, false);

            }
            for (int i = 0; i <= 4; i++) {
                Color c = generateTileType(tileColors);
                x = determineResource(c);
                drawHex(constant - 270 + i * 135 + offset * 2, startY + yDiff * 2, g2, c);
                tileArray[cnt++] = new Tile(makeHex(constant - 270 + i * 135 + offset * 2, startY + yDiff * 2), x, 0, new Point(constant - 270 + i * 135 + offset * 2, startY + yDiff * 2), 0, false);
            }
        }
    }

    public void buildRoads() {
        int placed = 0;
        for (int i = 0; i < tileArray.length; i++) {
            Polygon p = tileArray[i].getPolygon();
            if (p != null) {
                for (int k = 0; k < 6; k++) { // add each road start and end location to road array
                    roads[placed++] = (new Road(p.xpoints[k]-3, p.ypoints[k]-5,
                            p.xpoints[(k + 1)%6]-3, p.ypoints[(k + 1)%6]-5,null));
                }
              
            }
        }
    }

    public static boolean AlreadyIn(int x, int y)
        // checks if a settlement is already inside settlements array, by distance
       {
        for (int i = 0; i < settlements.length; i++) {
           if(settlements[i] == null){return false;}
           if(Math.abs(settlements[i].x - x) <20 && Math.abs(settlements[i].y - y) < 20){return true;}
        }
        System.out.println("a problem in func AlreadyIn in MyPanel");
        return false;
    }
    public int determineResource(Color c)
    { // determine resource by color
        if (c.equals(WOOL_COLOR)){return 4;}
        else if( c.equals(FOREST_COLOR)){return 1;}
        else if(c.equals(HILLS_COLOR)){return 2;}
        else if(c.equals(ORE_COLOR)){return 5;}
        else if(c.equals(GRAIN_COLOR)){return 3;};
        return -1;
    }
   
    public Color GetColor(int x)
    {
        switch(x){
            case(1):
                return FOREST_COLOR; // forest(lumber)
            case(2):
                return HILLS_COLOR; // hills(bricks) 
            case(3): // land(grain)
                return GRAIN_COLOR;
            case(4): // fields (wool)
                return WOOL_COLOR;
            case(5): // mtn(ore)
                return ORE_COLOR;

        }
        return Color.black;
    }
    public Color generateTileType(Tile [] tiles)
    { // generates a tile color by picking a random color from tiles array
        Random rnd = new Random();
        int x = rnd.nextInt(5);
        while (tiles[x].getAmt() == 0 && !isEmpty(tiles))
        {
            x = rnd.nextInt(5);
        }
        if (tiles[x].getAmt() > 0) {
            tiles[x].setAmt(tiles[x].getAmt()-1);

            return GetColor(x + 1);
        }
        return  Color.BLACK;
    }
    public void drawHex(int x, int y, Graphics2D g2, Color color)
    {

        Polygon poly = makeHex(x,y);
        g2.setColor(color);
        g2.fillPolygon(poly);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(5));
        g2.drawPolygon(poly);
    }

    public boolean isEmpty(Tile[] tiles)
    {
        for (int i = 0; i < 5; i++) {
            if (tiles[i].getAmt() > 0){return false;}
        }
        return true;
    }

    static public Polygon makeHex(int x, int y) {
        int smaller = 0;
        Polygon output = new Polygon();
        output.addPoint(x , y + hexagonSide);
        output.addPoint(x + (int) (hexagonSide * sqrt3div2)  , y + (int) (.5 * hexagonSide));
        output.addPoint(x + (int) (hexagonSide * sqrt3div2)  , y - (int) (.5 * hexagonSide));
        output.addPoint(x , y - hexagonSide );
        output.addPoint(x - (int) (hexagonSide * sqrt3div2) , y - (int) (.5 * hexagonSide));
        output.addPoint(x - (int) (hexagonSide * sqrt3div2) , y + (int) (.5 * hexagonSide));

        return output;
    }
    public static int getIndexOfTile(int x, int y) {
        Point p = new Point(x, y);
        for (int i = 0; i < tileArray.length; i++) {
            Polygon currP = makeHex(tileArray[i].getX(), tileArray[i].getY());
            if (currP.contains(p)) {
                return i; // index of tile clicked
            }
        }
        return -1; // no tile clicked
    }

}
