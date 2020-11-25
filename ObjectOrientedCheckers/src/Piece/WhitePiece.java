package Piece;

import Gamecontroller.*;
import java.util.ArrayList;
import java.util.List;

import Move.Move;
import Move.MoveCalcTree;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


public class WhitePiece extends CheckersPiece{

	
	public WhitePiece() {
		  super();
	      PhongMaterial material = new PhongMaterial();
	      material.setDiffuseColor(Color.DARKRED);
	      setMaterial(material);
	      addEvent();
	}
	
	@Override
	public WhitePiece clone() {
		return new WhitePiece();
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
		return 1;
	}
	
	public void makeKing() {
		king();
		isKing = true;
	}
	
	public void resetMoveList() {
		this.moves = new ArrayList();
	}
	
	 private void addEvent()
	    {
	        this.setOnMouseClicked(e->{	     
	        	//delete all fake pieces
	        	Gamecontroller.removePlaceholders();
	            //add new fake pieces
	            Gamecontroller.addPlaceholders(this);
	        });
	    }
	 
	 public void king(){
	        PhongMaterial mt = new PhongMaterial();
	        mt.setDiffuseMap(new Image(String.valueOf(getClass().getResource("images/RedCrown.png"))));
	        setMaterial(mt);

	    }
	 
	 @Override
	 public WhitePiece deepClone(Piece piece) {
			WhitePiece newPiece = new WhitePiece();
			
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
