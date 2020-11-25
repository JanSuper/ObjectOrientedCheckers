package MINIMAX;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Gamecontroller.Gamecontroller;
import Move.Move;
import Move.MoveCalcTree;
import Piece.Piece;

public class SearchTree {
	
	static Random rn = new Random();
	
	public static Move getMove(Object[][] board) {
		TreeNode rootNode = new TreeNode(board);
		return recursion(rootNode);
	}
	
	public static Move recursion(TreeNode node) {
		
		// Make children of every possible move this player can make
		
		Object[][] holdBoard = node.projectedBoard;
		
		
		
		for(int i = 0; i <= holdBoard.length - 1; i++) {
			for(int j = 0; j <= holdBoard[0].length - 1; j++) {
				if(holdBoard[i][j] != null) {
					if(((Piece)holdBoard[i][j]).getMoves().size() > 0){ // if the piece has moves
						List<Move> holdList = ((Piece)holdBoard[i][j]).getMoves();
						for(int k = 0; k <= holdList.size() - 1; k++) { // for every possible move
							System.out.println(holdList.get(k).getToList().size());
							Object[][] tempBoard = Gamecontroller.deepBoardCopy(holdBoard);
							TreeNode childnode = new TreeNode(tempBoard, MoveCalcTree.copyMove(holdList.get(k)), node);
							tempBoard = AIMoveToAction.AIAction(MoveCalcTree.copyMove(holdList.get(k)), tempBoard);
							System.out.println(holdList.get(k).getFrom()[0] + " " + holdList.get(k).getFrom()[1]);
							node.children.add(childnode);
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
				node.children.get(i).projectedBoard = AIController.calcBlackMoves(node.children.get(i).projectedBoard);
				
				for(int j = 0; j <= holdBoard.length - 1; j++) {
					for(int k = 0; k <= holdBoard[0].length - 1; k++) {
						if(holdBoard[j][k] != null) {
							if(((Piece)holdBoard[j][k]).getMoves().size() > 0){ // if the piece has moves
								allEnemyMoves.addAll(((Piece)holdBoard[j][k]).getMoves());
							}
						}
					}
				}
				
				
			}
			else {
				node.children.get(i).projectedBoard = AIController.calcWhiteMoves(node.children.get(i).projectedBoard);
				for(int j = 0; j <= holdBoard.length - 1; j++) {
					for(int k = 0; k <= holdBoard[0].length - 1; k++) {
						if(holdBoard[j][k] != null) {
							if(((Piece)holdBoard[j][k]).getMoves().size() > 0){ // if the piece has moves
								allEnemyMoves.addAll(((Piece)holdBoard[j][k]).getMoves());
							}
						}
					}
				}
			}
			
			Move enemyMove;
			if (allEnemyMoves.get(0).getRemoveList().size() > 0) {
				int index = rn.nextInt(allEnemyMoves.size());
		        enemyMove = allEnemyMoves.get(index);
			}
			else {
				int index = rn.nextInt(allEnemyMoves.size());
		        enemyMove = allEnemyMoves.get(index);
			}
			
			node.children.get(i).projectedBoard = AIMoveToAction.AIAction(enemyMove, node.children.get(i).projectedBoard);
		}
		
		
		// Pick best child
		
		// Recursion
		
		int bruh = rn.nextInt(node.children.size());
		
		Move bruhmove = node.children.get(bruh).nextMove;
		System.out.println(bruhmove.getToList().size());
		
		return bruhmove;
	}

}
