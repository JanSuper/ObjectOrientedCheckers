package MINIMAX;

import java.util.List;

import Gamecontroller.Gamecontroller;
import Move.Move;
import Piece.Piece;

public class AIMoveToAction {
	
	public static Object[][] UserAction(Move m, Object[][] board) {
		int[] postionHold = m.getFrom();
		int[] toHold = m.getToList().get(0);
		
		List<Move> possibleMoves = ((Piece)board[postionHold[0]][postionHold[1]]).getMoves();
		
		Piece holdPiece = ((Piece)board[postionHold[0]][postionHold[1]]);
		
		if (Math.abs(postionHold[0] - m.getToList().get(0)[0]) == 2) {
			int[] takePosition = ((Piece)board[postionHold[0]][postionHold[1]]).getMoves().get(0).getRemoveList().get(0).getLocation();
			board[takePosition[0]][takePosition[1]] = null;
			for(int i = possibleMoves.size()-1; i>=0; i--) {
				((Piece)board[postionHold[0]][postionHold[1]]).getMoves().get(i).getRemoveList().remove(0);
				((Piece)board[postionHold[0]][postionHold[1]]).getMoves().get(i).getToList().remove(0);
				
				if(((Piece)board[postionHold[0]][postionHold[1]]).getMoves().get(i).getToList().size() == 0) {
					((Piece)board[postionHold[0]][postionHold[1]]).getMoves().remove(i);
				}
			}
		}
		board[toHold[0]][toHold[1]] = holdPiece;
		
		board[postionHold[0]][postionHold[1]] = null;
		((Piece)board[toHold[0]][toHold[1]]).setLocation(toHold[0],toHold[1]);
		
		if(((Piece)board[toHold[0]][toHold[1]]).getColour() == 0) {
			if (toHold[0] == 7)
			((Piece)board[toHold[0]][toHold[1]]).makeKing();
		}
		else {
			if (toHold[0] == 0)
				((Piece)board[toHold[0]][toHold[1]]).makeKing();
		}

		
		return board;
	}
	
	public static Object[][] AIAction(Move m, Object[][] board) {
		if(m.getToList().size() == 1) {
			System.out.println("here1");
			int[] startingPos = m.getPiece().getLocation();
			Move holdMove = new Move(((Piece)board[startingPos[0]][startingPos[1]]));
			holdMove.setTo(m.getToList().get(0)[0], m.getToList().get(0)[1]);
			board = UserAction(holdMove, board);
		}	
		else {
			for(int i = 0; i <= m.getToList().size()-1;) {
				int[] startingPos;
				if (i == 0) {
					startingPos = m.getPiece().getLocation();
				}
				else {
					startingPos = m.getToList().get(i-1);
				}
				Move holdMove = new Move(((Piece)board[startingPos[0]][startingPos[1]]));
				holdMove.setTo(m.getToList().get(i)[0], m.getToList().get(i)[1]);
				board = UserAction(holdMove, board);
			
				m.getToList().remove(i);
			}
		}
		return board;
	}

}
