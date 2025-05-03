package GameLogic.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import GameLogic.Common.GameNode;
import GameLogic.Common.Position;
import GameLogic.Common.Side;
import GameLogic.Common.GameNodeType;
import ija.ija2024.tool.common.Observable;
import ija.ija2024.tool.common.ToolEnvironment;
import ija.ija2024.tool.common.ToolField;


public class Game implements ToolEnvironment, ToolField.Observer{
    private final int rows;
    private final int cols;
    private final GameNode[][] grid;
    private boolean isUpdating = false;
    // Constructor
    public Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new GameNode[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                var node = this.createNode(new Position(i+1, j+1));
                node.Type = GameNodeType.NONE;
                grid[i][j] = node;
                grid[i][j].addObserver(this);
            }
        }
        this.visited = new ArrayList<Position>();
    }

    public void updateGame() {
        resetCircuit();
        init();
    }

    private void resetCircuit() {
        visited.clear();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] != null && !grid[i][j].isPower()) {
                    grid[i][j].TurnLightOff();
                }
            }
        }
    }
    // Factory method to create a Game instance
    public static Game create(int rows, int cols) {
        return new Game(rows, cols);
    }
    private void init()
    {
        GameNode powerSource;
        for(int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if(grid[i][j] == null)
                    continue;
                if(grid[i][j].isPower())
                {
                    powerSource = grid[i][j];
                    pathFind(powerSource,Side.EAST);
                    break;
                }
            }
        }
    }
    private void pathFind(GameNode node, Side comingFrom)
    {
        if(node == null)
            return;
        else
        {
            if(!node.containsConnector(getCounterSide(comingFrom)) && !node.isPower())
                return;
            if(visited.contains(node.position))
                return;
            visited.add(node.position);
            for(Side s : Side.values())
            {
                if(node.containsConnector(s))
                {
                    var newPosition = this.getPosition(node,s);
                    if(!isValidPosition(newPosition))
                        continue;
                    node.TurnLightOn();
                    pathFind(this.grid[newPosition.row()-1][newPosition.col()-1], s);
                }
            }
        }
    }
    private List<Position> visited;
    private Side getCounterSide(Side s)
    {
        if( s== Side.EAST)
            return Side.WEST;
        else if(s== Side.WEST)
            return Side.EAST;
        else if (s == Side.NORTH)
            return Side.SOUTH;
        else
            return Side.NORTH;
    }
    private Position getPosition(GameNode node,Side s)
    {
        if(s == Side.NORTH)
            return new Position(node.position.row() - 1, node.position.col());
        else if (s == Side.SOUTH)
            return  new Position(node.position.row() + 1, node.position.col());
        else if (s == Side.EAST)
            return new Position(node.position.row(), node.position.col() + 1);
        else
            return  new Position(node.position.row(), node.position.col() - 1);
    }

    public int cols() {
        return this.cols;
    }

    @Override
    public ToolField fieldAt(int i, int i1) {
        return node(new Position(i, i1));
    }

    // Returns the number of rows
    public int rows() {
        return this.rows;
    }

    // Returns the node at a given position
    public GameNode node(Position p) {
        if (isValidPosition(p)) {
            return grid[p.row() - 1][p.col() - 1]; // Adjusting for 1-based indexing
        }
        return null;
    }
    public GameNode createNode(Position p, Side... sides)
    {
        if (isValidPosition(p)) {
            GameNode node = new GameNode();
            for(Side side : sides) {
                node.setConnectorSide(side);
            }
            grid[p.row() - 1][p.col() - 1] = node;
            node.position = p;
            node.addObserver(this);
            return node;
        }
        return null;
    }
    // Creates a bulb node at a specified position
    public GameNode createBulbNode(Position p, Side sides) {
        if (isValidPosition(p)) {
            GameNode node = this.createNode(p, sides);
            if (node != null)
            {
                node.Type = GameNodeType.BULB;
                return node;
            }
        }
        return null;
    }

    // Creates a link node at a specified position
    public GameNode createLinkNode(Position p, Side... sides) {
        GameNode node = this.createNode(p, sides);
        if (node != null)
        {
            node.Type = GameNodeType.LINK;
            return node;
        }
        return null;
    }

    // Creates a power node at a specified position
    public GameNode createPowerNode(Position p, Side... sides) {
        if (isValidPosition(p)) {
            GameNode node = this.createNode(p, sides);
            if (node != null)
            {
                node.Type = GameNodeType.POWER;
                node.TurnLightOn();
                return node;
            }
        }
        return null;
    }

    // Helper method to check if the position is within bounds
    private boolean isValidPosition(Position p) {
        return p.row() >= 1 && p.row() <= rows && p.col() >= 1 && p.col() <= cols;
    }

    @Override
    public void update(Observable field) {
        if (isUpdating) return;

        try {
            isUpdating = true;
            if (field instanceof GameNode) {
                updateGame();
            }
        } finally {
            isUpdating = false;
        }
    }
}
