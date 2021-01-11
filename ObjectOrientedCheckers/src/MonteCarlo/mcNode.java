package MonteCarlo;

import Move.Move;

import java.util.ArrayList;

public class mcNode
{
    public Object[][] game_state;
    public int num_wins;
    public int num_simulations;
    public mcNode parent;
    public ArrayList<mcNode> children = new ArrayList<>();
    public Move move_made;

    public mcNode(Object[][] g_state)
    {
        this.game_state = g_state;
        this.num_wins = 0;
        this.num_simulations = 0;
        parent = null;
    }
    public mcNode(Object[][] g_state, mcNode parent_node)
    {
        this.game_state = g_state;
        this.parent = parent_node;
        this.num_wins = 0;
        this.num_simulations = 0;
        this.parent.children.add(this);
    }

    public void setMove_made(Move m){
        this.move_made = m;
    }

    public boolean isLeaf() {return this.children.size() == 0;}

/**********************************************************************************************************************/
/**                                    RAVE IMPROVEMENTS                                                              */
/**********************************************************************************************************************/

    public int num_playouts_containing_this_move = 0;
    public int num_playouts_won_with_this_move = 0;



}
