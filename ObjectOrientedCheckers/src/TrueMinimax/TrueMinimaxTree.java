package TrueMinimax;

import java.util.Arrays;
import java.util.List;

import AIClasses.AIController;
import AIClasses.AIMoveToAction;
import Gamecontroller.Gamecontroller;
import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;

public class TrueMinimaxTree {
	
	public static final int MAX_DEPTH = 9;
	
	public static int[][] alphabeta = new int[MAX_DEPTH][2];
	
	public static MinimaxNode rootNode;
	
	public static Move getMove(Object[][] board) {
		
		resetAlphaBeta();
		
		rootNode = new MinimaxNode(board);
		
		recursion(rootNode, 1);
		
		for(int i = 0; i <= MAX_DEPTH - 1; i++) {
			System.out.println(Arrays.toString(alphabeta[i]));
		}
		
		
		System.out.println(rootNode.minimaxValue);
		
		for (int i = 0; i <= rootNode.children.size() - 1; i++) {
			System.out.println(rootNode.children.get(i).minimaxValue);
			if(rootNode.children.get(i).minimaxValue == rootNode.minimaxValue) {
				return(rootNode.children.get(i).madeMove);
			}
		}
		
		System.out.println("shit went wrong");
		
		return null;
	}
	
	public static void resetAlphaBeta() {
		for(int i = 0; i <= MAX_DEPTH - 1; i++) {
			alphabeta[i][0] = Integer.MIN_VALUE;
			alphabeta[i][1] = Integer.MAX_VALUE;
		}
	}
	
	public static void recursion(MinimaxNode parentnode, int depth) {
		
		int amountOfEnemies = 0;		
		Object[][] holdBoard = Gamecontroller.deepBoardCopy(parentnode.projectedBoard);
		
		for(int i = 0; i <= holdBoard.length - 1; i++) {
			for(int j = 0; j <= holdBoard[0].length - 1; j++) {
				if(holdBoard[i][j] != null) {
					
					if (parentnode.madeMove != null) {
						if(((Piece)holdBoard[i][j]).getColour() != parentnode.madeMove.getPiece().getColour()) {
							amountOfEnemies++;
						}
					}
					
					List<Move> holdList = ((Piece)holdBoard[i][j]).getMoves(); // Hold onto list of moves for simplicity
					for(int k = 0; k <= holdList.size() - 1; k++) {// for every possible move	
						if(alphabeta[depth-1][0] < alphabeta[depth-1][1]) {
							Object[][] tempBoard = Gamecontroller.deepBoardCopy(holdBoard);
							int[] moveStats = AIController.moveScore(Gamecontroller.deepBoardCopy(holdBoard), MoveCalcTree.copyMove(holdList.get(k)));
							Object[][] projectedBoard = AIMoveToAction.AIAction(MoveCalcTree.copyMove(holdList.get(k)), Gamecontroller.deepBoardCopy(tempBoard));

							boolean maxNode = !parentnode.maxNode;
							MinimaxNode childNode = new MinimaxNode(tempBoard, projectedBoard, holdList.get(k), parentnode, maxNode);
							parentnode.children.add(childNode);
							if(depth == MAX_DEPTH) {
								if(parentnode.maxNode) {
									alphabeta[depth-1][0] = Math.max(alphabeta[depth-1][0], moveStats[0] + 2*moveStats[1] - (moveStats[2] + 2*moveStats[3]));
								}
								else {
									alphabeta[depth-1][1] = Math.min(alphabeta[depth-1][1], moveStats[0] + 2*moveStats[1] - (moveStats[2] + 2*moveStats[3]));
								}
								parentnode.scoreList.add(moveStats[0] + 2*moveStats[1] - (moveStats[2] + 2*moveStats[3]));
							}
							else {
								if(parentnode.children.get(parentnode.children.size()-1).madeMove.getPiece().getColour() == 1) { // If White just made a move
									parentnode.children.get(parentnode.children.size()-1).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(parentnode.children.get(parentnode.children.size()-1).projectedBoard)); 
								}
								else { // If Black just made a move
									parentnode.children.get(parentnode.children.size()-1).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(parentnode.children.get(parentnode.children.size()-1).projectedBoard)); 
								}
								
								alphabeta[depth][0] = alphabeta[depth-1][0];
								alphabeta[depth][1] = alphabeta[depth-1][1];
								
								recursion(parentnode.children.get(parentnode.children.size()-1), depth + 1);
								}
							}
							else {
//							System.out.println("pruned at depth " + depth);
							}
						}
					}
				}
			}	
		
		if(parentnode.children.size() == 0) {
			if(parentnode.maxNode) {
				parentnode.parent.scoreList.add(Integer.MIN_VALUE);
				parentnode.minimaxValue = Integer.MIN_VALUE;
				alphabeta[depth-2][1] = Math.min(alphabeta[depth-2][1], Integer.MIN_VALUE);
			}
			else {

				if(amountOfEnemies == 0) {
					parentnode.parent.scoreList.add(Integer.MAX_VALUE);
					parentnode.minimaxValue = Integer.MAX_VALUE;
					alphabeta[depth-2][0] = Math.max(alphabeta[depth-2][0], Integer.MAX_VALUE);
				}
				else {
					parentnode.parent.scoreList.add(Integer.MIN_VALUE);
					parentnode.minimaxValue  = Integer.MIN_VALUE;
					alphabeta[depth-2][0] = Math.max(alphabeta[depth-2][0], Integer.MIN_VALUE);
				}
			}
			
			
			
		}
		else {
			parentnode.Minimax();
		}
		
		if(parentnode.parent != null) {
			if(parentnode.maxNode) {
				alphabeta[depth-2][1] = Math.min(alphabeta[depth-2][1], alphabeta[depth-1][0]);
				alphabeta[depth-2][1] = Math.min(alphabeta[depth-2][1], alphabeta[depth-1][1]);
			}
			else {
				alphabeta[depth-2][0] = Math.max(alphabeta[depth-2][0], alphabeta[depth-1][0]);
				alphabeta[depth-2][0] = Math.max(alphabeta[depth-2][0], alphabeta[depth-1][1]);
			}
		}
	}
}
