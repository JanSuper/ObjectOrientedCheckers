package Piece;

import java.util.List;

import AIClasses.AIController;
import Move.Move;
import Move.MoveToAction;
import Gamecontroller.Gamecontroller;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class PlaceholderPiece extends CheckersPiece{
	
	public int[] from = {-1,-1};
	public int[] location = {-1,-1};
	
	public PlaceholderPiece() {
		super();
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.LIGHTPINK);
		setMaterial(material);

     addEvent();
	}
	
	 private void addEvent()
	    {
	        this.setOnMouseClicked(e ->{
	           
	        	Move newMove = new Move((Piece)Gamecontroller.field[from[0]][from[1]]);
	        	newMove.setTo(location[0], location[1]);
	        	MoveToAction.UserAction(newMove);

	        	
//	        	if(Gamecontroller.turn%2 == 0 && Gamecontroller.playerOneAI) {
//	        		System.out.println("aiTurn");
//	        		Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
//	        		MoveToAction.AIAction(aiMove);
//	        	}
//	        	else if(Gamecontroller.turn%2 == 1 && Gamecontroller.playerTwoAI) {
//	        		System.out.println("aiTurn");
//	        		Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
//	        		MoveToAction.AIAction(aiMove);
//	        	}
	        	
	        });
	    }
	 
	 public PlaceholderPiece clone() {
		 return new PlaceholderPiece();
	 }

	@Override
	public void calcMoves() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocation(int i, int j) {
		location[0] = i;
		location[1] = j;
		this.setTranslateX((100*j)+50);
		this.setTranslateY((100*i)+50);
		
	}
	
	public void setFrom(int i, int j) {
		from[0] = i;
		from[1] = j;
		
	}

	@Override
	public boolean isKing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColour() {
		return 2;
	}

	@Override
	public List<Move> getMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeKing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetMoveList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AIcalcMoves(Object[][] tempboard) {
		// TODO Auto-generated method stub
		
	}
}
