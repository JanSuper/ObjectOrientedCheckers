package MINIMAX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Gamecontroller.Gamecontroller;
import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;

public class SearchTree {
	
	static Random rn = new Random();
	
	public static int MAX_DEPTH = 2;
	
	public static Move getMove(Object[][] board) {
		TreeNode rootNode = new TreeNode(board);
		return recursion(rootNode, 1);
	}
	
	public static Move recursion(TreeNode node, int depth) {
		
		// Make children of every possible move this player can make
		
		Object[][] holdBoard = node.projectedBoard;
		
		
		
		for(int i = 0; i <= holdBoard.length - 1; i++) {
			for(int j = 0; j <= holdBoard[0].length - 1; j++) {
				if(holdBoard[i][j] != null) {
					if(((Piece)holdBoard[i][j]).getMoves().size() > 0){ // if the piece has moves
						List<Move> holdList = ((Piece)holdBoard[i][j]).getMoves();
						for(int k = 0; k <= holdList.size() - 1; k++) { // for every possible move
//							System.out.println(holdList.get(k).getToList().size());
							Object[][] tempBoard = Gamecontroller.deepBoardCopy(holdBoard);
							TreeNode childnode = new TreeNode(tempBoard, MoveCalcTree.copyMove(holdList.get(k)), node);
							
							int[] moveStats = AIController.moveScore(Gamecontroller.deepBoardCopy(holdBoard), MoveCalcTree.copyMove(holdList.get(k)));

							tempBoard = AIMoveToAction.AIAction(MoveCalcTree.copyMove(holdList.get(k)), tempBoard);
//							System.out.println(holdList.get(k).getFrom()[0] + " " + holdList.get(k).getFrom()[1]);
							node.children.add(childnode);
							
							node.children.get(node.children.size()-1).addScore(moveStats[0] - moveStats[1]);
						}
					}
				}
			}
		}
		
		/*
		 * calc oponent move for every child node
		 * currently just takes a random move
		 * or if a piece can be taken the first move with the highest amount of captures
		 */
		
		for(int i = 0; i <= node.children.size() - 1; i++) {
			List<Move> allEnemyMoves = new ArrayList();
			if(node.children.get(i).nextMove.getPiece().getColour() == 1) {
				node.children.get(i).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard));
				for(int j = 0; j <= holdBoard.length - 1; j++) {
					for(int k = 0; k <= holdBoard[0].length - 1; k++) {
						if(node.children.get(i).projectedBoard[j][k] != null) {
							for(int l = 0; l <= ((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().size() - 1; l++) {
								allEnemyMoves.add(MoveCalcTree.copyMove(((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().get(l)));
							}
						}
					}
				}
				
				
			}
			else {
				node.children.get(i).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard));
				for(int j = 0; j <= holdBoard.length - 1; j++) {
					for(int k = 0; k <= holdBoard[0].length - 1; k++) {
						if(node.children.get(i).projectedBoard[j][k] != null) {
							for(int l = 0; l <= ((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().size() - 1; l++) {
								allEnemyMoves.add(MoveCalcTree.copyMove(((Piece)node.children.get(i).projectedBoard[j][k]).getMoves().get(l)));
							}
						}
					}
				}
			}
			Move enemyMove = null;
			int bestScore = Integer.MIN_VALUE;
			int bestChoiceIndex = -1;
			
			if(allEnemyMoves.size() > 0) {
				if (allEnemyMoves.get(0).getRemoveList().size() > 0) {
					for(int j = 0; j <= allEnemyMoves.size() - 1; j++) {
						Move possibleMove = MoveCalcTree.copyMove(allEnemyMoves.get(j));
						int[] stats =  AIController.moveScore(Gamecontroller.deepBoardCopy(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)),possibleMove);
						int score = stats[0]-stats[1];
						if (score > bestScore) {
							bestChoiceIndex = j;
							bestScore = score;
						}
					}
				}
				else {
					for(int j = 0; j <= allEnemyMoves.size() - 1; j++) {
					Move possibleMove = MoveCalcTree.copyMove(allEnemyMoves.get(j));
						int[] stats =  AIController.moveScore(Gamecontroller.deepBoardCopy(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)),possibleMove);
						int score = stats[3]-stats[4];
						if (score > bestScore) {
							bestChoiceIndex = j;
							bestScore = score;
						}
					}
				}
				enemyMove = allEnemyMoves.get(bestChoiceIndex);
				
				Object[][] copyBoard = Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard);
				
				copyBoard = AIMoveToAction.AIAction(enemyMove, Gamecontroller.deepBoardCopy(copyBoard));
				
				node.children.get(i).projectedBoard = copyBoard;
			}
			
			
			node.children.get(i).EnemyMoveScore = bestScore;
			
		}
		
		if(depth < MAX_DEPTH) {
			for(int i = 0; i <= node.children.size() - 1; i++) {
				if(node.children.get(i).nextMove.getPiece().getColour() == 1) {
					node.children.get(i).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard));
				}
				else {
					node.children.get(i).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard));
				}
				
				if(node.children.size() > 0){
					int best = Integer.MIN_VALUE;
					int index = -1;
					
					for(int j = 0; j <= node.children.size()-1; j++) {
						if (node.children.get(i).score > best) {
							index = j;
							best = node.children.get(i).score;
						}
					}
				
				List<TreeNode> hold = new ArrayList();
				
//				hold.add(node.children.get(index));
//				node.children = hold;
				
				recursion(node.children.get(index), depth++); 
				}
			}
		}
		// recursion + pruning
		
		// Pick best child
		
		
		if(node.children.size() > 0){
			int best = Integer.MIN_VALUE;
			int index = -1;
			
			for(int i = 0; i <= node.children.size()-1; i++) {
				if (node.children.get(i).score > best) {
					index = i;
					best = node.children.get(i).score;
				}
			}
			
			Move bruhmove = node.children.get(index).nextMove;
			
			return bruhmove;
		}
		else {
			return null;
		}
	}

}
