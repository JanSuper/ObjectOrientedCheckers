package SimpleAI;

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

public class SearchTree {
	
	//TODO add alpha beta pruning and correct enemy minimax
	
	public static int MAX_DEPTH = 3;
	
	public static Move getMove(Object[][] board) {
		TreeNode rootNode = new TreeNode(board);
		return recursion(rootNode, 1); //Start recursion at depth 1
	}
	
	public static Move recursion(TreeNode node, int depth) {
//		System.out.println(depth);
		
		// Make children of every possible move this player can make
		
		Object[][] holdBoard = Gamecontroller.deepBoardCopy(node.projectedBoard); //create deep copy of board
		
		node.children = new ArrayList(); // reset current node child list
		
		for(int i = 0; i <= holdBoard.length - 1; i++) {
			for(int j = 0; j <= holdBoard[0].length - 1; j++) {
				if(holdBoard[i][j] != null) { //always check first if the spot is empty !!!!
					if(((Piece)holdBoard[i][j]).getMoves().size() > 0){ // if the piece at current place has moves
						List<Move> holdList = ((Piece)holdBoard[i][j]).getMoves(); // Hold onto list of moves for simplicity
						for(int k = 0; k <= holdList.size() - 1; k++) { // for every possible move
							holdBoard = Gamecontroller.deepBoardCopy(node.projectedBoard);
//							System.out.println(holdList.get(k).getToList().size());
							Object[][] tempBoard = Gamecontroller.deepBoardCopy(holdBoard); // Create deepcopy of current board
							
//							AIController.testBoard(tempBoard);
//							System.out.println(holdList.get(k).toString());
							int[] moveStats = AIController.moveScore(Gamecontroller.deepBoardCopy(holdBoard), MoveCalcTree.copyMove(holdList.get(k))); // get Statistics about move to be made
							
							tempBoard = AIMoveToAction.AIAction(MoveCalcTree.copyMove(holdList.get(k)), Gamecontroller.deepBoardCopy(tempBoard)); // Make move on board copy, NOT on actual board
							TreeNode childnode = new TreeNode(tempBoard, MoveCalcTree.copyMove(holdList.get(k)), node); // Create child node for move to be made
//							System.out.println(holdList.get(k).getFrom()[0] + " " + holdList.get(k).getFrom()[1]);
							node.children.add(childnode); // add node to children
							
							node.children.get(node.children.size()-1).addScore(moveStats[0] + 5*moveStats[1] - (moveStats[2] + 5*moveStats[3]));// add score to current child
							
							node.childScore += moveStats[0] + 5*moveStats[1] - (moveStats[2] + 5*moveStats[3]);
							
//							AIController.testBoard(Gamecontroller.deepBoardCopy(tempBoard));
						}
					}
				}
			}
		}
		
		node.childScore = node.childScore/node.children.size();
		
		/*
		 * calc oponent move for every child node
		 * currently just takes a random move
		 * or if a piece can be taken the first move with the highest amount of captures
		 */
		if(depth <= MAX_DEPTH) {// if the tree goes deeper
		for(int i = 0; i <= node.children.size() - 1; i++) { // for all children of the current node
			List<Move> allEnemyMoves = new ArrayList(); // create empty list for al children's enemy moves
			if(node.children.get(i).nextMove.getPiece().getColour() == 1) { // If White just made a move
				node.children.get(i).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); // Update the childs board for blacks moves
				for(int j = 0; j <= holdBoard.length - 1; j++) { 
					for(int k = 0; k <= holdBoard[0].length - 1; k++) { //check the whole board
						if(node.children.get(i).projectedBoard[j][k] != null) { // if the space is not empty!!!!
							for(int l = 0; l <= ((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().size() - 1; l++) { // for all moves the piece can make
								allEnemyMoves.add(MoveCalcTree.copyMove(((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().get(l))); // add all possible moves to the list of enemy moves
							}
						}
					}
				}
				
				
			}
			else { // If Black just made a move
				node.children.get(i).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); // Update the childs board for white moves
				for(int j = 0; j <= holdBoard.length - 1; j++) {
					for(int k = 0; k <= holdBoard[0].length - 1; k++) { // check the whole board 
						if(node.children.get(i).projectedBoard[j][k] != null) { // if the space is not empty!!!!
							for(int l = 0; l <= ((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().size() - 1; l++) { // for all moves the piece can make
								allEnemyMoves.add(MoveCalcTree.copyMove(((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().get(l))); // add all possible moves to the list of enemy moves
							}
						}
					}
				}
			}
			TreeNode enemyTree = new TreeNode(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard));
			
			int enemydepth = depth + 1;
			Move enemyMove = recursion(enemyTree, enemydepth);
					
//			int bestScore = Integer.MIN_VALUE; // lowest possible value
//			int bestChoiceIndex = -1; // best index
			
			if(allEnemyMoves.size() > 0) { // if the enemy can make a move
				
				Object[][] copyBoard = Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard); // create deep copy of board
				
				int bestScore = Integer.MIN_VALUE;
				
				if(enemyMove !=null) {
					int[] scorehold = AIController.moveScore(Gamecontroller.deepBoardCopy(copyBoard), MoveCalcTree.copyMove(enemyMove));
					bestScore = (scorehold[0] + 5*scorehold[1]) - (scorehold[2] + 5*scorehold[3]);
					copyBoard = Gamecontroller.deepBoardCopy(AIMoveToAction.AIAction(enemyMove, Gamecontroller.deepBoardCopy(copyBoard))); // update projected board with enemy move
				}
				
//				AIController.testBoard(copyBoard);
				
				node.children.get(i).projectedBoard = Gamecontroller.deepBoardCopy(copyBoard); // update projected board
				
				node.children.get(i).addEnemyScore(bestScore); // save enemy score
				
			}
			
			
			
			
		}
		
		int best = Integer.MIN_VALUE; // look for best child
		int worst = Integer.MAX_VALUE;
		int index = -1;
		for(int i = 0; i <= node.children.size() - 1; i++) {
			if(node.children.get(i).score > best) {
				best = node.children.get(i).score;
			}
			if(node.children.get(i).EnemyMoveScore < worst) {
				worst = node.children.get(i).EnemyMoveScore;
			}
		}
		
		for(int i = node.children.size() - 1; i >= 0; i--) {
			if(node.children.get(i).EnemyMoveScore != worst) {
				node.children.remove(i);
				System.out.println("delete");
			}
			else if(node.children.get(i).score != best) {
				node.children.remove(i);
				System.out.println("delete");
			}
		}
		

			for(int i = 0; i <= node.children.size() - 1; i++) { // for all children of the current node
				if(node.children.get(i).nextMove.getPiece().getColour() == 1) { // if this AI's colour is white
					node.children.get(i).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); //calc white moves for projected board
				}
				else { // if this AI's colour is black
					node.children.get(i).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); // calc black moves for projected board
				}
				
				if(node.children.size() > 0){ // if there exist any children
					
				int hold = depth + 1;
				recursion(node.children.get(i), hold);
				}
			}
		}
		
		
		if(node.children.size() > 0 && depth == 1){
			double best = Double.NEGATIVE_INFINITY;
			int index = -1;
			
			for(int i = 0; i <= node.children.size()-1; i++) {
				if (node.children.get(i).childScore > best) {
					index = i;
					best = node.children.get(i).childScore;
				}
			}
			System.out.println(best);
			Move bruhmove = node.children.get(index).nextMove;
			
			return bruhmove;
		}
		else {
			return null;
		}
	}

}
