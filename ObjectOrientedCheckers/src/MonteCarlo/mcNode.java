package MonteCarlo;

import Move.Move;

import java.util.ArrayList;
import java.util.List;

import Gamecontroller.Gamecontroller;

public class mcNode
{
    public Object[][] game_state;
    public int num_wins;
    public int num_simulations;
    public mcNode parent;
    public ArrayList<mcNode> children = new ArrayList<>();
    public Move move_made;
    public List<Move> moveList;

    public mcNode(Object[][] g_state, List<Move> moveList)
    {
        this.game_state = g_state;
        this.num_wins = 0;
        this.num_simulations = 0;
        parent = null;
        this.moveList = moveList;
    }
    public mcNode(Object[][] g_state, mcNode parent_node)
    {
        this.game_state = g_state;
        this.parent = parent_node;
        this.num_wins = 0;
        this.num_simulations = 0;
        this.moveList = Gamecontroller.copyMoveList(parent_node.moveList);
        this.parent.children.add(this);
    }

    public void setMove_made(Move m){
        this.move_made = m;
    }

    public boolean isLeaf() {return this.children.size() == 0;}
}
