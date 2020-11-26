package MINIMAX;

import java.util.ArrayList;
import java.util.List;

import Move.Move;

public class TreeNode {
	
	public Object[][] projectedBoard;
	public Move nextMove;
	public TreeNode parent;
	public List<TreeNode> children = new ArrayList();
	public int score;
	public int EnemyMoveScore;
	
	public TreeNode(Object[][] projectedboard, Move move, TreeNode parent) {
		this.projectedBoard = projectedboard;
		this.nextMove = move;
		this.parent = parent;
	}
	
	public TreeNode(Object[][] projectedboard) {
		this.projectedBoard = projectedboard;
		this.parent = null;
	}
	
	public void addScore(double add) {
		this.score += add;
		if(parent != null) {
			parent.addScore(add);
		}
	}

}
