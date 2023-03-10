import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseListen extends MouseAdapter {

    JLayeredPane panel;
    GameBoard currGame;
    public MouseListen(JLayeredPane myPanel, GameBoard game) {
        this.panel = myPanel;
        currGame = game;
    }

    public void mouseReleased(MouseEvent e)
    {
        // note: next version is gonna be less messy
        Player playerClicked = currGame.currentPlayer;
        // means it is still first turn
        if (playerClicked.thingsPlaced < 4)
        {
            if (playerClicked.canSettle) {
                int settleClicked = currGame.findIndxOfSettle(e.getX(), e.getY());
                // check if he clicked on a place that can have a settlement, also check if no one has placed there(since it is first turn)
                if (settleClicked != -1 && currGame.settlements[settleClicked].getPlayerId() == 0 ) {
                    if (placeSettlement(e.getX(), e.getY(), playerClicked, 0)) {
                        playerClicked.addVictoryPoint(); 
                        playerClicked.incThingsPlaced();
                        playerClicked.setCanSettle(false);
                        playerClicked.setCanRoad(true);
                    }
                }
                else {currGame.setDisplayMessage("cant settle there");}
            }
            else {
                int[] cords = currGame.determineRoadPos(e.getX(), e.getY());
                // if clicked on a road, the road isnt taken and is obeying placement rules:
                if (cords != null && !currGame.isRoadTaken(cords) && currGame.isRoadConnecting(cords[0])) {
                    if (placeRoad(cords, playerClicked)) {
                        playerClicked.setCanSettle(true);
                        playerClicked.incThingsPlaced();
                        if (playerClicked.getThingsPlaced() == 4) {
                            playerClicked.setFirstTurn(false);
                        }
                        // give the turn to the other player
                        currGame.switchPlayers();
                    }
                }
                else
                {
                    currGame.setDisplayMessage("cannot road there");
                }
            }
            }
        else { // this is for non-first turns
            if (playerClicked.rolled) {
                int indxOfSettle = currGame.findIndxOfSettle(e.getX(), e.getY());
                if (indxOfSettle != -1) { // clicked on a settlement
                    // 3 cases for settlement: empty, his own, enemy:
                    int pid = currGame.settlements[indxOfSettle].getPlayerId();
                    int level = currGame.settlements[indxOfSettle].getLevel();
                    if (pid != 0 && pid != playerClicked.getId()) {
                        currGame.setDisplayMessage("other player settle");
                    }
                    else
                    {
                        if (currGame.isSettleConnecting(indxOfSettle)) {
                            // place settlement if possible
                            determineSettleConditionAndPlace(playerClicked, level, e.getX(), e.getY());
                        }
                        else
                        {
                            currGame.setDisplayMessage("settle isnt connected to a road");
                        }
                    }
                }
                else
                {
                    // check for roads:
                    if (playerClicked.hasMatsFor("road"))
                    {
                        int[] foundRoad = currGame.determineRoadPos(e.getX(), e.getY());
                        if (foundRoad != null )
                        {
                            if (currGame.isRoadConnecting(foundRoad[0])) {
                                placeRoad(foundRoad, playerClicked);
                            }
                            else
                            {
                                System.out.println("road is not connecting");
                            }
                        }
                        else
                        {
                            System.out.println("nowhere to place");
                        }
                    }
                    else
                    {
                        currGame.setDisplayMessage("cant build a road");
                    }
                }
            }
            else{
                currGame.setDisplayMessage("must roll first");
            }
        }
        // check if the player won
        currGame.checkWin();
    }

     public boolean placeRoad(int[] foundRoad, Player p)
     {
             // place both lines of roads:
             Road r = currGame.roads[foundRoad[0]];
             r.setOpaque(false);
             r.setLayout(null);
             r.setBounds(0, 0, 1000, 1000);
             r.setPlayerId(p.getId());
             r.setColor(p.getColor());
             r.setPlaced(true);
             Road other = currGame.roads[foundRoad[1]];
             other.setPlayerId(p.getId());
             other.setOpaque(false);
             other.setLayout(null);
             other.setBounds(0, 0, 1000, 1000);
             other.setColor(p.getColor());
             other.setPlaced(true);
             // add both roads to panel
             panel.add(other);
             panel.add(r);
             panel.revalidate();
             panel.repaint();
             if (!p.isFirstTurn)
                 {
                     p.subtractMats("road");
                 }
             return true;

     }
    public void determineSettleConditionAndPlace(Player playerClicked, int level, int x, int y)
    { // this function determines the condition of the settlement and places a settlement if possible
        switch (level)
        {
            // it is a fresh place
            case (0):
                if (playerClicked.hasMatsFor("settle")) {
                    // if has materials, place settlement
                    placeSettlement(x, y, playerClicked, level);
                    playerClicked.addVictoryPoint();
                    playerClicked.subtractMats("settle");

                }
                else
                {
                    currGame.setDisplayMessage("dont have resources");
                }
                break;
            case (1): // it is a normal settlement
                if (playerClicked.hasMatsFor("city")) {
                    playerClicked.addVictoryPoint();
                    placeSettlement(x, y, playerClicked, level+1);
                    playerClicked.subtractMats("city");

                }
                else
                {
                    currGame.setDisplayMessage("dont have sources for city");
                }
                break;
            case(2):
                // it is a city already
                currGame.setDisplayMessage("a city is there");
                break;
        }
    }
    public boolean placeSettlement(int x, int y, Player p, int level) {
            int foundSettle = currGame.findIndxOfSettle(x, y);
            int settlementX = currGame.settlements[foundSettle].x;
            int settlementY = currGame.settlements[foundSettle].y;
            Point settlementPos = new Point(settlementX, settlementY);
            // set the nearby resources of settlement
            currGame.settlements[foundSettle].setNearResources(currGame.determineNearSources(settlementPos));

            SettleComponent jp = new SettleComponent(p.getColor(), level);
            jp.setLayout(null);
            jp.setBackground(p.getColor());

        if (level == 0) {
                // new settlement
                /////////////////////////////
                currGame.settlements[foundSettle].setPlayerId(p.getId());
                currGame.settlements[foundSettle].setLevel(1);
                ////////////////////////////
                System.out.println("(placeSettlement)x and y = " + settlementX + " " + settlementY);

                jp.setBounds(settlementX - 6, settlementY - 6, 20, 20);
            }
            else
            {
                // new city
                currGame.settlements[foundSettle].setPlayerId(p.getId());
                currGame.settlements[foundSettle].setLevel(level);
                jp.setBounds(settlementX - 8, settlementY - 8, 30, 30);

            }
            panel.add(jp);
            panel.revalidate();
            panel.repaint();
            return true;

    }


}
