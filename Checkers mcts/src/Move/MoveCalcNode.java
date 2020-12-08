package Move;

import java.util.ArrayList;
import java.util.List;

import Piece.*;

public class MoveCalcNode {
	Piece currentPiece;
	List<Piece> takenPieces = new ArrayList();
	Move thisMove;
	
	public MoveCalcNode(Piece piece, Move move) {
		currentPiece = piece;
		this.thisMove = move;
	}
	
	public Piece getPiece() {
		return currentPiece;
	}
	
	public Move getMove() {
		return this.thisMove;
	}
}
