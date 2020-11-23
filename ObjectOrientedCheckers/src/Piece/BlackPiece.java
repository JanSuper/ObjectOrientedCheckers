package Piece;

import java.util.ArrayList;
import java.util.List;

import Gamecontroller.Gamecontroller;
import Move.Move;
import Move.MoveCalcTree;
import UI.FakePiece;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.geometry.HPos;
import javafx.scene.image.Image;




public class BlackPiece extends CheckersPiece{
	
	int[] position = {-1, -1};
	boolean isKing = false;
	List<Move> moves;
	
	public BlackPiece() {
		super();
	      PhongMaterial material = new PhongMaterial();
	      material.setDiffuseColor(Color.BLACK);
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
		this.moves = calc.getMoves(this);
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
		isKing = true;
	}
	
	public void resetMoveList() {
		this.moves = new ArrayList();
	}
	
	 private void addEvent(){
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
}

