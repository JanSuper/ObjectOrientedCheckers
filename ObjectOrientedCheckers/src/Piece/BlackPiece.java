package Piece;

import java.util.ArrayList;
import java.util.List;

import AIClasses.AIController;
import Gamecontroller.Gamecontroller;
import Move.Move;
import Move.MoveCalcTree;
import Move.MoveToAction;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.geometry.HPos;
import javafx.scene.image.Image;




public class BlackPiece extends CheckersPiece{

	
	public BlackPiece() {
		super();
	      PhongMaterial material = new PhongMaterial();
	      material.setDiffuseColor(Color.DARKGRAY);
	      setMaterial(material);
	      addEvent();
	}
	
	@Override
	public BlackPiece clone() {
		return new BlackPiece();
	}
	
	
	public List<Move> getMoves(){
		return moves;
	}
	
	public void calcMoves() {
		this.moves = new ArrayList();
		MoveCalcTree calc = new MoveCalcTree();
		this.moves = calc.getMoves(this, Gamecontroller.field);
	}
	
	public void AIcalcMoves(Object[][] tempboard) {
		this.moves = new ArrayList();
		MoveCalcTree calc = new MoveCalcTree();
		this.moves = calc.getMoves(this, tempboard);
	}

	public void setLocation(int i, int j) {
		position[0] = i;
		position[1] = j;
		this.setTranslateX((100*j)+50);
		this.setTranslateY((100*i)+50);
	}
	
	public boolean isKing() {
		return isKing;
	}
	
	public int[] getLocation() {
		return position;
	}
	
	public int getColour() {
		return 0;
	}
	
	public void makeKing() {
		king();
		isKing = true;
	}
	
	public void resetMoveList() {
		this.moves = new ArrayList();
	}
	
	 private void addEvent(){
	        this.setOnMouseClicked(e->{	  
	        	
	        	if(!Gamecontroller.playerOneAI) {
	        	//delete all fake pieces
	        	Gamecontroller.removePlaceholders();
	            //add new fake pieces
	            Gamecontroller.addPlaceholders(this);
	        	}
	        	else if (Gamecontroller.turn%2 == 0) {
	        		System.out.println("aiTurn");
	        		Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
	        		MoveToAction.AIAction(aiMove);
	        	}
	        });
	    }
	 
	 public void king(){
		 PhongMaterial mt = new PhongMaterial();
		 mt.setDiffuseMap(new Image(String.valueOf(getClass().getResource("images/BlackCrown_1.png"))));
		 setMaterial(mt);
	    }
	 
	 @Override
	 public BlackPiece deepClone(Piece piece) {
			BlackPiece newPiece = new BlackPiece();
			
			newPiece.position[0] = piece.getLocation()[0];
			newPiece.position[1] = piece.getLocation()[1];
			
			newPiece.isKing = piece.isKing();
			
			newPiece.moves = new ArrayList();
			List<Move> holdList = piece.getMoves();
			
			for(int i = 0; i <= holdList.size()-1; i++) {
				newPiece.moves.add(MoveCalcTree.copyMove(holdList.get(i)));
			}
			
			return newPiece;
		}

}

