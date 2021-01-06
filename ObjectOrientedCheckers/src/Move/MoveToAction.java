package Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AIClasses.AIController;
import Gamecontroller.Gamecontroller;
import ObjectUI.Main;
import Piece.Piece;

public class MoveToAction {
	
	public static void UserAction(Move m) {
		Gamecontroller.AddMoveToList(m);
		ExUserAction(m);
	}
	
	public static void ExUserAction(Move m) {
		int[] postionHold = m.getFrom();
		int[] toHold = m.getToList().get(0);
		
		List<Move> possibleMoves = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).getMoves();
		
		if (Math.abs(postionHold[0] - m.getToList().get(0)[0]) == 2) {
			
			
			int[] takePosition = {(postionHold[0] + toHold[0])/2, (postionHold[1] + toHold[1])/2};
			
			Main.board.getChildren().remove((Piece)Gamecontroller.field[takePosition[0]][takePosition[1]]);
			Gamecontroller.field[takePosition[0]][takePosition[1]] = null;
			boolean endpoint = false;
			boolean step = false;
			
			for(int i = possibleMoves.size()-1; i>=0; i--) {
				System.out.println(Arrays.toString(possibleMoves.get(i).getToList().get(possibleMoves.get(i).getToList().size()-1)));
				System.out.println("---");
				System.out.println(Arrays.toString(toHold));
				if(Arrays.equals(possibleMoves.get(i).getToList().get(possibleMoves.get(i).getToList().size()-1),toHold)) {
					endpoint = true;
					System.out.println("endpoint");
				}
				else if(Arrays.equals(possibleMoves.get(i).getToList().get(0),toHold)) {
					step = true;
					System.out.println("step");
				}
			}
			
			if(endpoint && !step) {
				((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]).resetMoveList();
				possibleMoves = new ArrayList();
			}
			
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
		Piece holdPiece = ((Piece)Gamecontroller.field[postionHold[0]][postionHold[1]]);
		
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
			ExUserAction(holdMove);
		}		
		else {
			for(int i = 0; i <= m.getToList().size()-1; i++) {
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
				ExUserAction(holdMove);
			}
		}
		
//		if(Gamecontroller.turn%2 == 0 && Gamecontroller.playerOneAI) {
//			System.out.println("aiTurn");
//    		Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
//    		MoveToAction.AIAction(aiMove);
//    	}
//    	else if(Gamecontroller.turn%2 == 1 && Gamecontroller.playerTwoAI) {
//    		System.out.println("aiTurn");
//    		Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
//    		MoveToAction.AIAction(aiMove);
//    	}
	}

}
