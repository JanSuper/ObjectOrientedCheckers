package Piece;

import java.util.List;

import AIClasses.AIController;
import Move.Move;
import Move.MoveToAction;
import ObjectUI.Main;
import Gamecontroller.Gamecontroller;
import MonteCarlo.mcTreeSearch;
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
	        	
	        	Gamecontroller.removePlaceholders();
	        
	        	MoveToAction.UserAction(newMove);

	        	if(!Gamecontroller.gameOver) {
	        		if(Gamecontroller.turn%2 == 0 && Gamecontroller.playerOneAI) {
	        			System.out.println("aiTurn");
	        			Move aiMove = AIController.getAiMove(Gamecontroller.deepBoardCopy(Gamecontroller.field));
	        			MoveToAction.AIAction(aiMove);
	        		}
	        		else if(Gamecontroller.turn%2 == 1 && Gamecontroller.playerTwoAI) {
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
	        	}
	        	
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
