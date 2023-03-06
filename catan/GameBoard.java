import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class GameBoard {
     Tile [] tileArray = new Tile[19];
     Road [] roads = new Road[tileArray.length*6];
     Settlement[] settlements;

     Player [] players;
     Player currentPlayer;
     int currPIndx;

     MyPanel gamePanel;

    public GameBoard(Tile [] tileArr, Road[] roadArr, Settlement[] settlementArr, Player [] pArr,MyPanel mainP)
    {
     tileArray = tileArr;
     roads = roadArr;
     settlements = settlementArr;
     players = pArr;
     currentPlayer = pArr[0];
     currPIndx = 0;
     gamePanel = mainP;
     //normalizeSettlements();
        setDisplayMessage("first turn");
    }

    public HashMap determineNearSources(Point position)
    {
        HashMap<Integer, Integer> foundSources = new HashMap<>();
        int countFound = 0;
        for (int i = 0; i < tileArray.length; i++) {
            Polygon polygon = tileArray[i].getPolygon();
            for (int j = 0; j < polygon.npoints; j++) {
                Point p = new Point(polygon.xpoints[j], polygon.ypoints[j]);
                if (p.distance(position) < 25)
                {
                    foundSources.put(tileArray[i].getNumber(), tileArray[i].getResource());
                }
            }
        }
        return foundSources;
    }
    public boolean isRoadTaken(int[] indexed)
    { // indexes is index of road in roads array
        return roads[indexed[0]].isPlaced() || roads[indexed[1]].isPlaced();
    }
    public void setDisplayMessage(String message)
    {
        JLabel jl = (JLabel) gamePanel.getComponentAt(370,50);
        jl.setText(message);
    }

    public void checkWin()
    { // 10 points is a win, settlement = 1 point, city = 2
        if (currentPlayer.victoryPoints >= 10)
        {
            announceWinner();
        }
    }
    public void announceWinner()
    {
        String wonMsg = "player " + currentPlayer.getId() + " WON!";
        gamePanel.closeWindow();
        GameSetup newWindow = new GameSetup(wonMsg); // new frame with winning message
    }

    public int [] determineRoadPos(int x, int y){
        int[] roadsFoundIndx = new int[2];
        int cnt = 0;
        for (int i =0; i < roads.length ;  i++){
            // Calculate distance from click to line segment
            double dist = Line2D.ptSegDist(roads[i].getxStart(), roads[i].getyStart(), roads[i].getxEnd(), roads[i].getyEnd(), x, y);
            if (dist < 12 && cnt < 2) { // If click is within 5 pixels of line segment, add a new bridge
                System.out.println("good");
                System.out.println("compare with start: " +determineSettlementPos(roads[i].getxStart(),roads[i].getyStart()));
                System.out.println("compare with end: " + determineSettlementPos(roads[i].getxEnd(),roads[i].getyEnd()));

                roadsFoundIndx[cnt++] = i;
            }
        }
        return cnt == 0 ? null : roadsFoundIndx;
    }
    /*public void normalizeSettlements()
    {
        System.out.println("s len: " + settlements.length);
        for (int i = 0; i < settlements.length; i++) {
            if (settlements[i] != null)
            {
                int avgx = 0, avgy = 0;
                //System.out.println(settlements[i].x + " " + settlements[i].getY());
                int[] indexOfClosest = determineSettlementPos(settlements[i].getX(), settlements[i].getY());
                int countClose = 0;
                for (int j = 0; j < indexOfClosest.length; j++) {
                    System.out.println("index j : " + indexOfClosest[j]);
                    if (indexOfClosest[j] != -1) {
                        countClose++;
                        avgx += settlements[indexOfClosest[j]].getX();
                        avgy += settlements[indexOfClosest[j]].getY();
                    }
                }
                if(countClose !=0) {
                    avgx /= countClose;
                    avgy /= countClose;

                    System.out.println("before x: " + settlements[i].x + " after: " + avgx);
                    settlements[i].x = avgx;
                    settlements[i].y = avgy;
                }
                System.out.println(settlements[i].x + " "  +settlements[i].y);


            }
        }
    }

     */


    public void rollDice()
    {

        if (currentPlayer.rolled  || currentPlayer.isFirstTurn){setDisplayMessage("cant roll"); return;}
        currentPlayer.rolled = true;
        Random rnd = new Random();
        int diceRes = rnd.nextInt(6)+1 + rnd.nextInt(6)+1;
        setDisplayMessage(currentPlayer.id +" rolled: "+ diceRes);
        distributeResourcesByNum(diceRes);
    }
    public void distributeResourcesByNum(int res) {
        for (int i = 0; i < settlements.length; i++) {
            if (settlements[i] != null) {
                HashMap nearSources = settlements[i].getNearResources();
                if (nearSources != null) {
                    for (Object number : nearSources.keySet()) {
                        if (res == (int)number)
                        {
                            Player p = getPlayerById(settlements[i].getPlayerId());
                            int resource =(int)nearSources.get(res);
                            p.getResources()[resource]++;
                            System.out.println(Arrays.toString(p.getResources()));
                        }
                    }
                }
            }
        }
    }
    public Player getPlayerById(int id)
    {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getId() == id)
            {
                return players[i];
            }
        }
        return null;
    }
    public void endTurn()
    {
        if (!currentPlayer.isFirstTurn) {
            if (currentPlayer.rolled) {
                currentPlayer.rolled = false;
                // switch players
                switchPlayers();
            }
        }
    }
    public boolean isRoadConnecting(int roadIndx)
    {
        Road currRoad = roads[roadIndx];
        Point startRoad = new Point(currRoad.getxStart(), currRoad.getyStart());
        Point endRoad = new Point(currRoad.getxEnd(), currRoad.getyEnd());
        for (int i = 0; i < settlements.length; i++) // iterating over settlements to check if connects with one
        {
            if (settlements[i] != null && settlements[i].getPlayerId() == currentPlayer.getId())
            {
                Point settleLocation = settlements[i].getLocation();

                if (settleLocation.distance(startRoad) < 25 || settleLocation.distance(endRoad) < 25)
                {
                    return true;
                }
            }
        }
        for (int i = 0; i < roads.length; i++) { // check if connects with other road
            if (roads[i] != null && roads[i].getPlayerId() == currentPlayer.getId())
            {
                Point checkStart = new Point(roads[i].getxStart(), roads[i].getyStart());
                Point checkEnd = new Point(roads[i].getxEnd(), roads[i].getyEnd());
                if (startRoad.distance(checkStart) < 15 || startRoad.distance(checkEnd) < 15 || endRoad.distance(checkStart) < 15 || endRoad.distance(checkEnd) < 15)
                {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isSettleConnecting(int settleIndx) // check if a settlement is connecting to a road (wont check on first turn)
    {
        Point settleLocation = settlements[settleIndx].getLocation();
        for (int i = 0; i < roads.length; i++) {
            if (roads[i] != null && roads[i].getPlayerId() == currentPlayer.getId())
            {
                Point startRoad = roads[i].getStartLocation();
                Point endRoad = roads[i].getEndLocation(); // this is the end of the road
                if (settleLocation.distance(startRoad) < 20 || settleLocation.distance(endRoad) < 20)
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void switchPlayers()
    {
        currentPlayer = players[++currPIndx % players.length];
        setDisplayMessage("currP: " + currentPlayer.getId());
    }

    public  int findIndxOfSettle(int x, int y)
    {
        for (int i = 0; i < settlements.length; i++) {
            if (settlements[i] != null && Math.abs(settlements[i].x - x) < 20 && Math.abs(settlements[i].y - y) < 20)
            {
                return i;
            }
        }
        return -1;
    }
    public int determineSettlementPos(int x, int y)
    {
        int indx = 0;
        for (int i = 0; i < tileArray.length; i++) {
            Polygon currPolygon = tileArray[i].getPolygon();
            for (int j = 0; j < 6; j++) {
                int currx = currPolygon.xpoints[j];
                int curry = currPolygon.ypoints[j];

                if(Math.abs(currx - x) < 20 && Math.abs(curry - y) < 20  )
                {
                    indx = findIndxOfSettle(currx,curry);
                    if (indx != -1){return indx;}
                }
            }
        }

        return -1;
    }
}
