package TrueMinimax;

import java.util.ArrayList;
import java.util.List;

import Move.Move;

public class MinimaxNode {
	
	public boolean pruned = false;
	public boolean maxNode = true;
	public int minimaxValue;
	public int alpha = Integer.MIN_VALUE;
	public int beta = Integer.MAX_VALUE;
	
	public Move madeMove = null;
	public Object[][] beforeBoard;
	public Object[][] projectedBoard;
	
	public MinimaxNode parent = null;
	public List<MinimaxNode> children = new ArrayList();
	
	public ArrayList<Integer> scoreList = new ArrayList();
	
	public MinimaxNode(Object[][] board) {
		this.projectedBoard = board;
		this.minimaxValue = Integer.MIN_VALUE;
	}
	
	public MinimaxNode(Object[][] beforeBoard, Object[][] projectedboard, Move move, MinimaxNode parent, int alpha, int beta, boolean maxNode) {
		this.beforeBoard = beforeBoard;
		this.projectedBoard = projectedboard;
		this.madeMove = move;
		this.parent = parent;
		this.alpha = alpha;
		this.beta = beta;
		this.maxNode = maxNode;
		
		if(this.maxNode) {
			this.minimaxValue = Integer.MIN_VALUE;
		}
		else {
			this.minimaxValue = Integer.MAX_VALUE;
		}
	}
	
	public void Minimax() {
		if(this.maxNode) {
			for(int i = 0; i <= this.scoreList.size()-1; i++) {
				if (this.minimaxValue < this.scoreList.get(i)) {
					this.minimaxValue = this.scoreList.get(i);
				}
			}
			if(this.parent != null) {
				this.parent.scoreList.add(this.minimaxValue);
			}
		}
		else {
			for(int i = 0; i <= this.scoreList.size()-1; i++) {
				if (this.minimaxValue > this.scoreList.get(i)) {
					this.minimaxValue = this.scoreList.get(i);
				}
			}
			if(this.parent != null) {
				this.parent.scoreList.add(this.minimaxValue);
			}
		}
	}
	
	public void AlphaBeta() {
		if(this.maxNode) {
			this.alpha = Math.max(this.alpha, this.children.get(this.children.size() - 1).alpha);
			this.alpha = Math.max(this.alpha, this.children.get(this.children.size() - 1).beta);
		}
		else {
			this.beta = Math.min(this.beta, this.children.get(this.children.size() - 1).alpha);
			this.beta = Math.min(this.beta, this.children.get(this.children.size() - 1).beta);
		}
		
		this.pruned = this.alpha >= this.beta;
	}

}
