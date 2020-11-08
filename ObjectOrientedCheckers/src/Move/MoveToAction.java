package Move;

import java.util.List;

import Gamecontroller.Gamecontroller;
import Piece.Piece;

public class MoveToAction {
	
	public static void UserAction(Move m) {
		int[] postionHold = m.getFrom();
		int[] toHold = m.getToList().get(0);
		
		List<Move> possibleMoves = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves();
		
		Piece holdPiece = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]);
		
		if (Math.abs(postionHold[0] - m.getToList().get(0)[0]) == 2) {
			int[] takePosition = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(0).getRemoveList().get(0).getLocation();
			Gamecontroller.field[takePosition[0]][takePosition[1]] = null;
			for(int i = possibleMoves.size()-1; i>=0; i--) {
				((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(i).getRemoveList().remove(0);
				((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(i).getToList().remove(0);
			}
		}
		else {
			for(int i = possibleMoves.size()-1; i>=0; i--) {
				if (!(((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(i).getToList().get(0) == m.getToList().get(0))) {
					((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().remove(i);
				}
			}
		}
		Gamecontroller.field[toHold[0]][toHold[1]] = holdPiece;
		Gamecontroller.field[postionHold[0]][postionHold[1]] = null;
		Gamecontroller.turn++;
		((Piece)Gamecontroller.field[toHold[0]][toHold[1]]).setLocation(toHold[0],toHold[1]);

	}
	
	public static void AIAction(Move m) {
		
	}

}
