import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MyPanel extends JLayeredPane implements ActionListener {
    static Tile [] tileArray = new Tile[19];
    static Road [] roads = new Road[tileArray.length*6];
    static Tile [] tileColors = new Tile[5];
    static Settlement[] settlements = new Settlement[6*19];
    public static int settlementsPlaced = 0;
    public static Player[] players;
    public static final int BOARD_HEIGHT = 1500;
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
             this.setLayout(null);
             this.setBounds(0,0,1600,1600);
             this.setBackground(new Color(90,120,90));
             addMessageDisplay();
             players = new Player[2];
             players[0] = new Player(1,new int[6], Color.pink);
             players[1] = new Player(2,new int[6], Color.MAGENTA);
             frameParent = parent;
             initTiles();

    }

    public void initTiles()
    {
        for (int i = 0; i < tileColors.length ; i++)
        {
            tileColors[i] = new Tile(null,0,0,new Point(0,0), 0, false);

        }
            for (int i = 0; i < 19; i++) {
                tileArray[i] = new Tile(null,0,0,new Point(),0, false);
            }
    }
    public void addMessageDisplay()
    {
        JLabel jb = new JLabel("");
        jb.setLayout(null);
        jb.setBounds(330,50,190,40);
        this.add(jb);
    }
    public void closeWindow()
    {
        frameParent.dispose();
    }
    public void setRoadArr()
    {
         /*roads = new Road[]{
                 new Road(561, 416, 0,false),
                 new Road(667, 107, 0,false),
                 new Road(802, 109, 0,false),
                 new Road(900, 171, 0,false),
                 new Road(967, 288, 0,false),
                 new Road(969, 412, 0,false),
                 new Road(899, 527, 0,false),
                 new Road(802, 592, 0,false),
                 new Road(669, 589, 0,false),
                 new Road(564, 532, 0,false),
                 new Road(499, 411, 0,false),
                 new Road(570, 176, 0,false),
                 new Road(493, 289, 0,false),
                 new Road(599, 243, 0,false),
                 new Road(642, 294, 0,false),
                 new Road(706, 290, 0,false),
                 new Road(731, 240, 0,false),
                 new Road(765, 294, 0,false),
                 new Road(840, 294, 0,false),
                 new Road(868, 230, 0,false),
                 new Road(831, 168, 0,false),
                 new Road(763, 170, 0,false),
                 new Road(639, 164, 0,false),
                 new Road(697, 164, 0,false),
                 new Road(935, 350, 0,false),
                 new Road(899, 416, 0,false),
                 new Road(834, 410, 0,false),
                 new Road(763, 416, 0,false),
                 new Road(695, 410, 0,false),
                 new Road(796, 348, 0,false),
                 new Road(660, 352, 0,false),
                 new Road(628, 414, 0,false),
                 new Road(558, 409, 0,false),
                 new Road(559, 294, 0,false),
                 new Road(527, 344, 0,false),
                 new Road(900, 290, 0,false),
                 new Road(870, 473, 0,false),
                 new Road(734, 475, 0,false),
                 new Road(598, 472, 0,false),
                 new Road(637, 536, 0,false),
                 new Road(706, 532, 0,false),
                 new Road(831, 539, 0,false),
                 new Road(763, 525, 0,false),
                 new Road(667, 588, 0,false),
                 new Road(807, 589, 0,false)
         };

          */
    }
    public void addButtons(GameBoard g)
    {
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
         drawGrid(g2);
         generateNums(g2);
        if(!wasInitialized)
        {
            createPosForSettle();
            System.out.println("----------");
            System.out.println(settlements[0] + " " + settlements[1]);
            System.out.println("---------");
            buildRoads();

            GameBoard gameBoard = new GameBoard(tileArray, roads,settlements, players, this);
            MouseListener aMouse = new MouseListen(this, gameBoard);
            this.addMouseListener(aMouse);
            addButtons(gameBoard);

        }
        wasInitialized = true;
    }
    public void createAvgPosForSettle()
    {
        for (int i = 0; i <tileArray.length; i++) {
            Polygon currP = tileArray[i].getPolygon();
            for (int j = 0; j < currP.npoints; j++) {
                int x = currP.xpoints[j];
                int y = currP.ypoints[j];
                if (settlementsPlaced < settlements.length)
                {
                    settlements[settlementsPlaced++] = new Settlement(x,y,0);
                    System.out.println(settlements[settlementsPlaced-1].toString());
                }
            }

        }
        for (int i = 0; i < settlementsPlaced; i++) {
            Point pos = new Point(settlements[i].getX(), settlements[i].getY());

        }
    }

    public void createPosForSettle()
    {
        for (int i = 0; i <tileArray.length; i++) {
            Polygon currP = tileArray[i].getPolygon();
            for (int j = 0; j < currP.npoints; j++) {
                int x = currP.xpoints[j];
                int y = currP.ypoints[j];
                if (!AlreadyIn(x,y) && settlementsPlaced < settlements.length)
                {
                    settlements[settlementsPlaced++] = new Settlement(x,y,0);
                    System.out.println(settlements[settlementsPlaced-1].toString());
                }
            }

        }
        System.out.println(settlementsPlaced);

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
            Collections.shuffle(numList);
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
        int constant = 600;
        int offset = 65;
        // y(x) = 110 + x*115
        int startY = 110;
        int yDiff = 120;
        ///////////////////////
        if (wasInitialized) {
            for (int i = 0; i < tileArray.length; i++) {
                //System.out.println("resource: " + tileArray[i].getResource() + " " + GetColor(tileArray[i].getResource()));
                drawHex(tileArray[i].getX(), tileArray[i].getY(), g2, (GetColor(tileArray[i].getResource())));
                // drawString(tile x,y,number,g2)

            }
        } else {

            for (int i = 0; i < 5; i++) {
                tileColors[i].amt = 4;
                tileColors[i].isFour = true;
            }
            Random rnd = new Random();
            int ran = rnd.nextInt(5);
            tileColors[ran].amt = 3;
            tileColors[ran].isFour = false;
            int x = -1;
            int cnt = 0;
            for (int i = 0; i <= 2; i++) {
                //if ()
                Color c = generateTileType(tileColors);
                x = determineResource(c);
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
            printTiles();
        }
    }

    public void buildRoads() {
        int placed = 0;
        for (int i = 0; i < tileArray.length; i++) {
            Polygon p = tileArray[i].getPolygon();
            if (p != null) {
                // System.out.println(Arrays.toString(p.xpoints));
                for (int k = 0; k < 5; k++) {
                    roads[placed++] = (new Road(p.xpoints[k]-3, p.ypoints[k]-5,
                            p.xpoints[k + 1]-3, p.ypoints[k + 1]-5,null));
                }
                roads[placed++] = (new Road(p.xpoints[5]-3, p.ypoints[5]-5,
                        p.xpoints[0]-3, p.ypoints[0]-5,null));
            }
        }
    }

    public static boolean AlreadyIn(int x, int y)
    {
        for (int i = 0; i < settlements.length; i++) {
           if(settlements[i] == null){return false;}
           if(Math.abs(settlements[i].x - x) <20 && Math.abs(settlements[i].y - y) < 20){return true;}
        }
        System.out.println("a problem in func AlreadyIn in MyPanel");
        return false;
    }
    public int determineResource(Color c)
    {
        if (c.equals(WOOL_COLOR)){return 4;}
        else if( c.equals(FOREST_COLOR)){return 1;}
        else if(c.equals(HILLS_COLOR)){return 2;}
        else if(c.equals(ORE_COLOR)){return 5;}
        else if(c.equals(GRAIN_COLOR)){return 3;};
        return -1;
    }
    public void printTiles()
    {
        for (int i = 0; i < tileArray.length; i++) {
            System.out.println("x:"+ Arrays.toString(tileArray[i].polygon.xpoints));
            System.out.println("y:"+ Arrays.toString(tileArray[i].polygon.ypoints));
        }
    }

    public Color GetColor(int x)
    {
        switch(x){
            case(1):
                return FOREST_COLOR; // forest(lumber) // g =0
            case(2):
                return HILLS_COLOR; // hills(bricks) // g = 200
            case(3): // land(grain)
                return GRAIN_COLOR;
            case(4): // fields (wool)
                return WOOL_COLOR; // g = 255
            case(5): // mtn(ore)
                return ORE_COLOR; // g =128

        }
        return Color.black;
    }
    public Color generateTileType(Tile [] tiles)
    {
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
