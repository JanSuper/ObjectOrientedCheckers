package Piece;

import Gamecontroller.*;
import MonteCarlo.mcTreeSearch;

import java.util.ArrayList;
import java.util.List;

import AIClasses.AIController;
import Move.Move;
import Move.MoveCalcTree;
import Move.MoveToAction;
import ObjectUI.Main;
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
	        	
	        	System.out.println("bruh");
	        	
	        	Gamecontroller.removePlaceholders();
	        	
	        		if(!Gamecontroller.playerTwoAI) {
		        	//delete all fake pieces
		        	
		            //add new fake pieces
		            Gamecontroller.addPlaceholders(this);
		        	}
		        	else if (Gamecontroller.turn%2 == 1) {
		        		System.out.println("aiTurn");
		        		mcTreeSearch tree = new mcTreeSearch(Gamecontroller.deepBoardCopy(Gamecontroller.field));
						tree.setOGturn(1);
						Move move = tree.getNextMove();

						String GREEN = "\033[0;32m";
						String RESET = "\033[0m";  // Text Reset
						String PURPLE = "\033[0;35m";  // PURPLE

						for(Object[] arr : Main.board.board)//print board
						{
							for(Object obj : arr)
							{
								if(obj instanceof BlackPiece)
									System.out.print(GREEN+"B  "+RESET);
								else if(obj instanceof WhitePiece)
									System.out.print(PURPLE+"W  "+RESET);
								else if(obj == null)
									System.out.print("0  ");
							}
							System.out.println();
						}
						System.out.println();
						System.out.println();

						System.out.println();
						System.out.println("move from x= "+move.getPiece().getLocation()[0]+" y= "+move.getPiece().getLocation()[1]);
						System.out.println("move to x= "+move.getToList().get(move.getToList().size()-1)[0]+" y= "+move.getToList().get(move.getToList().size()-1)[1]);
						System.out.println("number of eaten pieces = "+move.getRemoveList().size());
						if(move.getRemoveList().size() == 2) {
							Piece ep1, ep2;
							ep1 = move.getRemoveList().get(0);
							ep2 = move.getRemoveList().get(1);
							System.out.println("ep1 x = "+ep1.getLocation()[0]+" y= "+ep1.getLocation()[1]);
							System.out.println("ep2 x = "+ep2.getLocation()[0]+" y= "+ep2.getLocation()[1]);
						}
						System.out.println();

						MoveToAction.AIAction(move);


						for(Object[] arr : Main.board.board)//print board
						{
							for(Object obj : arr)
							{
								if(obj instanceof BlackPiece)
									System.out.print(GREEN+"B  "+RESET);
								else if(obj instanceof WhitePiece)
									System.out.print(PURPLE+"W  "+RESET);
								else if(obj == null)
									System.out.print("0  ");
							}
							System.out.println();
						}
						System.out.println();
						System.out.println();

						Gamecontroller.turn = 1;
						Gamecontroller.endTurn();
		        	}
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
