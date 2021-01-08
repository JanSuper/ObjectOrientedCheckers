package TrueMinimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import AIClasses.AIController;
import AIClasses.AIMoveToAction;
import Gamecontroller.Gamecontroller;
import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;

public class TrueMinimaxTree {
	
	public static int MAX_DEPTH = 1;
	
	public static double[][] alphabeta = new double[MAX_DEPTH][2];
	
	public static MinimaxNode rootNode;
	
	public static Random rn = new Random();
	
	public static Move getMove(Object[][] board, List<Move> MadeMoves) {
		alphabeta = new double[MAX_DEPTH][2];
		
		resetAlphaBeta();
		
		rootNode = new MinimaxNode(board, MadeMoves);
		
		recursion(rootNode, 1);
		
//		for(int i = 0; i <= MAX_DEPTH - 1; i++) {
//			System.out.println(Arrays.toString(alphabeta[i]));
//		}
//
//		double newValue = rootNode.minimaxValue;
//		
//		if(rootNode.minimaxValue != -0.5 && Gamecontroller.turn + MAX_DEPTH < 50) {
//			for (int i = 0; i <= rootNode.children.size() - 1; i++) {
//				if(rootNode.children.get(i).minimaxValue == rootNode.minimaxValue) {
//					rootNode.children.get(i).minimaxValue += rn.nextDouble();
//				
//					if(rootNode.children.get(i).minimaxValue > newValue) {
//						newValue = rootNode.children.get(i).minimaxValue;
//					}
//				}
//			}
//			rootNode.minimaxValue = newValue;
//		}
//		
//		System.out.println(rootNode.minimaxValue);
//		System.out.println(rootNode.children.size());
//		System.out.println("----");
		
		for (int i = 0; i <= rootNode.children.size() - 1; i++) {
//			System.out.println(rootNode.children.get(i).minimaxValue);
			if(rootNode.children.get(i).minimaxValue == rootNode.minimaxValue) {
//				System.out.println(rootNode.children.get(i).children.size());
				return(rootNode.children.get(i).madeMove);
			}
		}
		
		System.out.println("shit went wrong");
		
		return null;
	}
	
	public static void resetAlphaBeta() {
		for(int i = 0; i <= MAX_DEPTH - 1; i++) {
			alphabeta[i][0] = Double.NEGATIVE_INFINITY;
			alphabeta[i][1] = Double.POSITIVE_INFINITY;
		}
	}
	
	public static void recursion(MinimaxNode parentnode, int depth) {
		
		int currentColour = (Gamecontroller.turn + depth - 1)%2;
		int amountOfEnemies = 0;	
		int amountOfFriendly = 0;
		Object[][] holdBoard = Gamecontroller.deepBoardCopy(parentnode.projectedBoard);
		
		for(int i = 0; i <= holdBoard.length - 1; i++) {
			for(int j = 0; j <= holdBoard[0].length - 1; j++) {
				if(holdBoard[i][j] != null) {
					if(((Piece)holdBoard[i][j]).getColour() !=  Gamecontroller.turn%2) {
						amountOfEnemies++;
					}
					else {
						amountOfFriendly++;
					}
					
					List<Move> holdList = ((Piece)holdBoard[i][j]).getMoves(); // Hold onto list of moves for simplicity
					for(int k = 0; k <= holdList.size() - 1; k++) {// for every possible move	
						if(((alphabeta[depth-1][0] < alphabeta[depth-1][1]))) {
							Object[][] tempBoard = Gamecontroller.deepBoardCopy(holdBoard);
							Object[][] projectedBoard = AIMoveToAction.AIAction(MoveCalcTree.copyMove(holdList.get(k)), Gamecontroller.deepBoardCopy(tempBoard));
							
							List<Move> mademoveList = Gamecontroller.copyMoveList(parentnode.MadeMoves);
							mademoveList.add(MoveCalcTree.copyMove(holdList.get(k)));
							
							if(mademoveList.size() > 16) {
								mademoveList.remove(0);
							}
							
							boolean maxNode = !parentnode.maxNode;
							MinimaxNode childNode = new MinimaxNode(tempBoard, projectedBoard, holdList.get(k), parentnode, maxNode, Gamecontroller.copyMoveList(mademoveList));
							parentnode.children.add(childNode);
							
							if(currentColour == 1) { // If White just made a move
								parentnode.children.get(parentnode.children.size()-1).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(parentnode.children.get(parentnode.children.size()-1).projectedBoard), Gamecontroller.copyMoveList(mademoveList)); 
							}
							else { // If Black just made a move
								parentnode.children.get(parentnode.children.size()-1).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(parentnode.children.get(parentnode.children.size()-1).projectedBoard), Gamecontroller.copyMoveList(mademoveList)); 
							}
							
							double score = AIController.boardEval(parentnode.children.get(parentnode.children.size()-1).projectedBoard, Gamecontroller.turn%2, (Gamecontroller.turn + depth) > 50);
							
							if(depth == MAX_DEPTH) {
								if(parentnode.maxNode) {
									alphabeta[depth-1][0] = Math.max(alphabeta[depth-1][0], score);
								}
								else {
									alphabeta[depth-1][1] = Math.min(alphabeta[depth-1][1], score);
								}
								parentnode.scoreList.add(score);
								parentnode.children.get(parentnode.children.size()-1).minimaxValue = score;
							}
							else {
								alphabeta[depth][0] = alphabeta[depth-1][0];
								alphabeta[depth][1] = alphabeta[depth-1][1];
								
								recursion(parentnode.children.get(parentnode.children.size()-1), depth + 1);
								}
							}
						else {
							parentnode.pruned = true;
							}
						}
					}
				}
			}	
		
		
			if(parentnode.children.size() == 0) {//TODO
				if((alphabeta[depth-1][0] < alphabeta[depth-1][1])) {
					if(parentnode.maxNode) {
						if(amountOfFriendly == 0) {
							parentnode.parent.scoreList.add(-1000.0);
							parentnode.minimaxValue = -1000.0;
							alphabeta[depth-2][1] = Math.min(alphabeta[depth-2][1], -1000.0);
							alphabeta[depth-1][0] = Math.max(alphabeta[depth-1][0], -1000.0);
						}
						else { //TODO drawing better than losing
							parentnode.parent.scoreList.add(-0.5);
							parentnode.minimaxValue  = -0.5;
							alphabeta[depth-2][1] = Math.min(alphabeta[depth-2][1], -0.5);
							alphabeta[depth-1][0] = Math.max(alphabeta[depth-1][0], -0.5);
//							System.out.println("draw at depth " + depth);
						}
					}
				else {
					if(amountOfEnemies == 0) {
						parentnode.parent.scoreList.add(1000.0);
						parentnode.minimaxValue = 1000.0;
						alphabeta[depth-2][0] = Math.max(alphabeta[depth-2][0], 1000.0);
						alphabeta[depth-1][1] = Math.min(alphabeta[depth-1][1], 1000.0);
					}
					else { //TODO drawing better than losing
						parentnode.parent.scoreList.add(-0.5);
						parentnode.minimaxValue  = -0.5;
						alphabeta[depth-2][0] = Math.max(alphabeta[depth-2][0], -0.5);
						alphabeta[depth-1][1] = Math.min(alphabeta[depth-1][1], -0.5);
//						System.out.println("draw at depth " + depth);
					}
				}
			}
		}
		else {
			parentnode.Minimax();
			if(depth > 2) {
				parentnode.children = new ArrayList();
			}
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
