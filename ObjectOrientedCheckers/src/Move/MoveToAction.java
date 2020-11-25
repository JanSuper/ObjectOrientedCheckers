package Move;

import java.util.List;

import Gamecontroller.Gamecontroller;
import ObjectUI.Main;
import Piece.Piece;

public class MoveToAction {
	
	public static void UserAction(Move m) {
		int[] postionHold = m.getFrom();
		int[] toHold = m.getToList().get(0);
		
		Gamecontroller.removePlaceholders();
		List<Move> possibleMoves = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves();
		
		Piece holdPiece = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]);
		
		if (Math.abs(postionHold[0] - m.getToList().get(0)[0]) == 2) {
			int[] takePosition = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(0).getRemoveList().get(0).getLocation();
			Main.board.getChildren().remove((Piece)Gamecontroller.field[takePosition[0]][takePosition[1]]);
			Gamecontroller.field[takePosition[0]][takePosition[1]] = null;
			for(int i = possibleMoves.size()-1; i>=0; i--) {
				((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(i).getRemoveList().remove(0);
				((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(i).getToList().remove(0);
				
				if(((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().get(i).getToList().size() == 0) {
					((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves().remove(i);
				}
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
		((Piece)Gamecontroller.field[toHold[0]][toHold[1]]).setLocation(toHold[0],toHold[1]);
		
		if(Gamecontroller.turn%2 == 0) {
			if (toHold[0] == 7)
			((Piece)Gamecontroller.field[toHold[0]][toHold[1]]).makeKing();
		}
		else {
			if (toHold[0] == 0)
				((Piece)Gamecontroller.field[toHold[0]][toHold[1]]).makeKing();
		}
		
		if(((Piece)Gamecontroller.field[toHold[0]][toHold[1]]).getMoves().size() == 0) {
			System.out.println("No More Steps");
			Gamecontroller.endTurn();
		}
		else {
			Gamecontroller.limitToThis(toHold[0],toHold[1]);
		}

	}
	
	public static void AIAction(Move m) {
		if(m.getToList().size() == 1) {
			int[] startingPos = m.getPiece().getLocation();
			Move holdMove = new Move(((Piece)Gamecontroller.field[startingPos[0]][startingPos[1]]));
			holdMove.setTo(m.getToList().get(0)[0], m.getToList().get(0)[1]);
			UserAction(holdMove);
		}		
		else {
			for(int i = 0; i <= m.getToList().size()-1;) {
				System.out.println("here?");
				int[] startingPos;
				if (i == 0) {
					startingPos = m.getPiece().getLocation();
				}
				else {
					startingPos = m.getToList().get(i-1);
				}
				Move holdMove = new Move(((Piece)Gamecontroller.field[startingPos[0]][startingPos[1]]));
				holdMove.setTo(m.getToList().get(i)[0], m.getToList().get(i)[1]);
				UserAction(holdMove);
			}
		}
		if(Gamecontroller.turn%2 == 0 && Gamecontroller.playerOneAI) {
    		//do AI turn
    	}
    	else if(Gamecontroller.turn%2 == 1 && Gamecontroller.playerTwoAI) {
    		//do AI turn
    	}
	}

}
