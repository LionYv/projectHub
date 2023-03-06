import java.awt.*;

public class Player
{
    int id;
    int[] resources;
    Color color;
    boolean isFirstTurn;
    boolean canSettle;
    boolean canRoad;
    boolean rolled;
    int thingsPlaced = 0;
    int victoryPoints = 0;

    public Player(int id, int[] resources, Color color) {
        this.id = id;
        this.resources = resources;
        this.color = color;
        isFirstTurn = true;
        canSettle = true;
        canRoad = false;
        rolled = false;

    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
    public void addVictoryPoint()
    {
        victoryPoints++;
    }


    public int getThingsPlaced() {
        return thingsPlaced;
    }

    public void incThingsPlaced() {
        this.thingsPlaced++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public boolean canBuildRoad() // brick and lumber --> numbers: 2 and 1
    {
        return resources[2] != 0 && resources[1] != 0;
    }
    public boolean canBuildSettle() // brick, lumber, wool and grain --> numbers: 2,1,4,3
    {
        return resources[2] != 0 && resources[1] != 0 && resources[4] != 0  && resources[3] != 0;
    }
    public boolean canBuildCity() // 3 ore, 2 grain --> numbers: 5,3
    {
        return  resources[5] >= 3 && resources[3] >= 2;
    }
    public boolean hasMatsFor(String building)
    {
        switch (building)
        {
            case("road"):
                return canBuildRoad();
            case("settle"):
                return canBuildSettle();
            case("city"):
                return canBuildCity();
            default:
                System.out.println("error in hasMatsFor: " + building +" unknown");
                return false;
        }
    }
    public void subtractMats(String building)
    {
        switch (building) {
            case ("road"):
                resources[2]--;
                resources[1]--;
                break;
            case ("settle"):
                for (int i = 1; i <= 4; i++) {
                    resources[i]--;
                }
                break;
            case ("city"):
                resources[5]-=3;
                resources[2]-=2;
                break;
            default:
                System.out.println("error in subractMats: " + building + " unknown");
        }
    }

    public int[] getResources() {
        return resources;
    }

    public void setResources(int[] resources) {
        this.resources = resources;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFirstTurn() {
        return isFirstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        isFirstTurn = firstTurn;
    }

    public boolean isCanSettle() {
        return canSettle;
    }

    public void setCanSettle(boolean canSettle) {
        this.canSettle = canSettle;
    }

    public boolean CanRoad() {
        return canRoad;
    }

    public void setCanRoad(boolean canRoad) {
        this.canRoad = canRoad;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
