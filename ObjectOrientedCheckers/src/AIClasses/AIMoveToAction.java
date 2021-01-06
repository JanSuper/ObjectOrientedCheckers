package AIClasses;

import java.util.Arrays;
import java.util.List;

import Gamecontroller.Gamecontroller;
import Move.Move;
import Piece.Piece;

public class AIMoveToAction {
	
	public static Object[][] AIAction(Move m, Object[][] board) {
		
		int[] takeFrom = m.getFrom();
		int[] goTo = m.getToList().get(m.getToList().size()-1);
		
		Object[][] workBoard = Gamecontroller.deepBoardCopy(board);
		
		
		List<Piece> removelist = m.getRemoveList();
		
		
		workBoard[goTo[0]][goTo[1]] = workBoard[takeFrom[0]][takeFrom[1]];
		if(m.getPiece().getColour() == 0) {
			if(goTo[0] == 7) {
				((Piece)workBoard[goTo[0]][goTo[1]]).makeKing();
			}
		}
		else {
			if(goTo[0] == 0) {
				((Piece)workBoard[goTo[0]][goTo[1]]).makeKing();
			}
		}
		((Piece)workBoard[goTo[0]][goTo[1]]).setLocation(goTo[0],goTo[1]);
		workBoard[takeFrom[0]][takeFrom[1]] = null;
		
		for(int i = 0; i <= removelist.size() - 1; i++) {
			int[] removePos = removelist.get(i).getLocation();
			workBoard[removePos[0]][removePos[1]] = null;
		}
		
		
		
		return workBoard;
	}

}
