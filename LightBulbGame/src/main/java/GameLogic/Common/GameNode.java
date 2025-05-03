package GameLogic.Common;

import java.util.*;

import ija.ija2024.tool.common.ToolField;

public class GameNode implements ToolField {
    private List<Observer> observers = new ArrayList<>();
    public GameNode()
    {
        conductorSides = new HashMap<Side,Boolean>();
        conductorSides.put(Side.EAST, false);
        conductorSides.put(Side.NORTH, false);
        conductorSides.put(Side.WEST, false);
        conductorSides.put(Side.SOUTH, false);
    }

    public Position position;
    public GameNodeType Type;
    private Map<Side, Boolean> conductorSides;
    public GameNode(Position position)
    {
        this.position = position;
    }
    public boolean isBulb()
    {
        return Type == GameNodeType.BULB;
    }
    public void TurnLightOn()
    {
        this.isLightOn = true;
        notifyObservers();
    }
    public void TurnLightOff()
    {
        this.isLightOn = false;
        notifyObservers();
    }
    public boolean isLink()
    {
        return Type == GameNodeType.LINK;
    }
    public boolean isPower()
    {
        return Type == GameNodeType.POWER;
    }
    public boolean containsConnector(Side s)
    {
        return conductorSides.get(s);
    }
    public Position getPosition()
    {
        return this.position;
    }
    public void setConnectorSide(Side s)
    {
        conductorSides.put(s, true);
        notifyObservers();
    }
    public void turn()
    {
        Map<Side, Boolean> rotated = new HashMap<>();
        for (Map.Entry<Side, Boolean> entry : conductorSides.entrySet()) {
            rotated.put(entry.getKey().rotateClockwise(), entry.getValue());
        }
        this.conductorSides = rotated;
        notifyObservers();
    }
    private boolean isLightOn = false;
    public boolean light()
    {
        return isLightOn;
    }
    public boolean north()
    {
        return conductorSides.get(Side.NORTH);
    }
    public boolean south()
    {
        return conductorSides.get(Side.SOUTH);
    }
    public boolean west()
    {
        return conductorSides.get(Side.WEST);
    }
    public boolean east()
    {
        return conductorSides.get(Side.EAST);
    }
    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder("{");
        if(Type == GameNodeType.BULB)
            ret.append("B");
        else if(Type == GameNodeType.LINK)
            ret.append("L");
        else if (Type == GameNodeType.POWER)
            ret.append("P");
        else if (Type == GameNodeType.NONE)
            ret.append("E");

        ret.append("[");
        ret.append(String.valueOf(position.row()));
        ret.append("@");
        ret.append(String.valueOf(position.col()));
        ret.append("][");
        List<Side> sides = new ArrayList<Side>();
        for(Side s : new Side[]{Side.NORTH, Side.EAST,Side.SOUTH,Side.WEST})
        {
            if(conductorSides.get(s))
                sides.add(s);
        }
        for(int i = 0; i < sides.size(); i++)
        {
            ret.append(sides.get(i).toString());
            if(i != sides.size() - 1 && sides.size() > 1)
                ret.append(",");
        }
        ret.append("]}");
        return ret.toString();
    }
    @Override
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
