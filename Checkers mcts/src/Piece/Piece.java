package Piece;

import java.util.List;

import Move.Move;

public interface Piece {
	
	CheckersPiece clone();
	void calcMoves();
	void AIcalcMoves(Object[][] tempboard);
	void setLocation(int i, int j);
	boolean isKing();
	int[] getLocation();
	int getColour();
	List<Move> getMoves();
	void makeKing();
	void resetMoveList();
	CheckersPiece deepClone(Piece piece);
}
