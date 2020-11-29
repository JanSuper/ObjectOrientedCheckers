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
		
		
		List<Piece> removelist = m.getRemoveList();
		
		
		board[goTo[0]][goTo[1]] = board[takeFrom[0]][takeFrom[1]];
		((Piece)board[goTo[0]][goTo[1]]).setLocation(goTo[0],goTo[1]);
		board[takeFrom[0]][takeFrom[1]] = null;
		
		for(int i = 0; i <= removelist.size() - 1; i++) {
			int[] removePos = removelist.get(i).getLocation();
			board[removePos[0]][removePos[1]] = null;
		}
		
		return board;
	}

}
