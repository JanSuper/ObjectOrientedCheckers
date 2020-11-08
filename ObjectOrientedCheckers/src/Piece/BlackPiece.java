package Piece;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import Move.Move;
import Move.MoveCalcTree;

public class BlackPiece extends CheckersPiece{
	
	int[] position = {-1, -1};
	boolean isKing = false;
	List<Move> moves;
	
	public BlackPiece() {
		//this.setVisual(databasePointer);
	}
	
	@Override
	public BlackPiece clone() {
		return new BlackPiece();
	}
	
	@Override
	public Image getVisual() {
		return super.getVisual();
	}
	
	public List<Move> getMoves(){
		return moves;
	}
	
	public void calcMoves() {
		this.moves = new ArrayList();
		MoveCalcTree calc = new MoveCalcTree();
		this.moves = calc.getMoves(this);
	}

	public void setLocation(int i, int j) {
		position[0] = i;
		position[1] = j;
	}
	
	public boolean isKing() {
		return isKing;
	}
	
	public int[] getLocation() {
		return position;
	}
	
	public int getColour() {
		return 0;
	}
	
	public void makeKing() {
		isKing = true;
	}
}

