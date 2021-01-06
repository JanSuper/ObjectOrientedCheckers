package TrueMinimax;

import java.util.ArrayList;
import java.util.List;

import Move.Move;

public class MinimaxNode {
	
	public boolean pruned = false;
	public boolean maxNode = true;
	public double minimaxValue;
	
	public Move madeMove = null;
	public List<Move> MadeMoves;
	public Object[][] beforeBoard;
	public Object[][] projectedBoard;
	
	public MinimaxNode parent = null;
	public List<MinimaxNode> children = new ArrayList();
	
	public ArrayList<Double> scoreList = new ArrayList();
	
	public MinimaxNode(Object[][] board, List<Move> MadeMoves) {
		this.projectedBoard = board;
		this.MadeMoves = MadeMoves;
		this.minimaxValue = Integer.MIN_VALUE;
	}
	
	public MinimaxNode(Object[][] beforeBoard, Object[][] projectedboard, Move move, MinimaxNode parent, boolean maxNode, List<Move> MadeMoves) {
		this.beforeBoard = beforeBoard;
		this.projectedBoard = projectedboard;
		this.madeMove = move;
		this.parent = parent;
		this.maxNode = maxNode;
		this.MadeMoves = MadeMoves;
		
		if(this.maxNode) {
			this.minimaxValue = Double.NEGATIVE_INFINITY;
		}
		else {
			this.minimaxValue = Double.POSITIVE_INFINITY;
		}
	}
	
	public void Minimax() {
		if(this.maxNode) {
			for(int i = 0; i <= this.scoreList.size()-1; i++) {
				if (this.minimaxValue < this.scoreList.get(i)) {
					this.minimaxValue = this.scoreList.get(i);
					this.pruned = this.children.get(i).pruned;
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
					this.pruned = this.children.get(i).pruned;
				}
			}
			if(this.parent != null) {
				this.parent.scoreList.add(this.minimaxValue);
			}
		}
	}

}
