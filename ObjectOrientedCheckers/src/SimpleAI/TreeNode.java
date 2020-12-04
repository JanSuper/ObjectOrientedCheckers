package SimpleAI;

import java.util.ArrayList;
import java.util.List;

import Move.Move;

public class TreeNode {
	
	public Object[][] projectedBoard;
	public Move nextMove;
	public TreeNode parent;
	public List<TreeNode> children = new ArrayList();
	public int score = Integer.MIN_VALUE;
	public int EnemyMoveScore = Integer.MIN_VALUE;
	
	public TreeNode(Object[][] projectedboard, Move move, TreeNode parent) {
		this.projectedBoard = projectedboard;
		this.nextMove = move;
		this.parent = parent;
	}
	
	public TreeNode(Object[][] projectedboard) {
		this.projectedBoard = projectedboard;
		this.parent = null;
	}
	
	public void addScore(int add) {//Add score to both itself and parent if it has one
		
		if(this.score < add) {
			this.score = add;
		}
		if(parent != null) {
			parent.addScore(add);
		}
	}
	
public void addEnemyScore(int add) {//Add score to both itself and parent if it has one
		
		if(this.EnemyMoveScore < add) {
			this.EnemyMoveScore = add;
		}
		if(parent != null) {
			parent.addEnemyScore(add);
		}
	}

}
