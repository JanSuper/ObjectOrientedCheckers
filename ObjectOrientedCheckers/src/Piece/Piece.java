package Piece;

import java.awt.Image;
import java.util.List;

import Move.Move;

public interface Piece {
	
	CheckersPiece clone();
	Image getVisual();
	void calcMoves();
	void setLocation(int i, int j);
	boolean isKing();
	int[] getLocation();
	int getColour();
	List<Move> getMoves();
	void makeKing();
	void resetMoveList();
}
