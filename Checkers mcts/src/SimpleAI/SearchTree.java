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

    static Random rn = new Random();

    public static int MAX_DEPTH = 4;

    public static Move getMove(Object[][] board) {
        TreeNode rootNode = new TreeNode(board);
        return recursion(rootNode, 1); //Start recursion at depth 1
    }

    public static Move recursion(TreeNode node, int depth) {
//		System.out.println(depth);

        // Make children of every possible move this player can make

        Object[][] holdBoard = Gamecontroller.deepBoardCopy(node.projectedBoard); //create deep copy of board

        node.children = new ArrayList(); // reset current node child list

        for (int i = 0; i <= holdBoard.length - 1; i++) {
            for (int j = 0; j <= holdBoard[0].length - 1; j++) {
                if (holdBoard[i][j] != null) { //always check first if the spot is empty !!!!
                    if (((Piece) holdBoard[i][j]).getMoves().size() > 0) { // if the piece at current place has moves
                        List<Move> holdList = ((Piece) holdBoard[i][j]).getMoves(); // Hold onto list of moves for simplicity
                        for (int k = 0; k <= holdList.size() - 1; k++) { // for every possible move
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

                            node.children.get(node.children.size() - 1).addScore(moveStats[0] - moveStats[1]); // add score to current child

//							AIController.testBoard(Gamecontroller.deepBoardCopy(tempBoard));
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

        for (int i = 0; i <= node.children.size() - 1; i++) { // for all children of the current node
            List<Move> allEnemyMoves = new ArrayList(); // create empty list for al children's enemy moves
            if (node.children.get(i).nextMove.getPiece().getColour() == 1) { // If White just made a move
                node.children.get(i).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); // Update the childs board for blacks moves
                for (int j = 0; j <= holdBoard.length - 1; j++) {
                    for (int k = 0; k <= holdBoard[0].length - 1; k++) { //check the whole board
                        if (node.children.get(i).projectedBoard[j][k] != null) { // if the space is not empty!!!!
                            for (int l = 0; l <= ((Piece) node.children.get(i).projectedBoard[j][k]).getMoves().size() - 1; l++) { // for all moves the piece can make
                                allEnemyMoves.add(MoveCalcTree.copyMove(((Piece) node.children.get(i).projectedBoard[j][k]).getMoves().get(l))); // add all possible moves to the list of enemy moves
                            }
                        }
                    }
                }


            } else { // If Black just made a move
                node.children.get(i).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); // Update the childs board for white moves
                for (int j = 0; j <= holdBoard.length - 1; j++) {
                    for (int k = 0; k <= holdBoard[0].length - 1; k++) { // check the whole board
                        if (node.children.get(i).projectedBoard[j][k] != null) { // if the space is not empty!!!!
                            for (int l = 0; l <= ((Piece) node.children.get(i).projectedBoard[j][k]).getMoves().size() - 1; l++) { // for all moves the piece can make
                                allEnemyMoves.add(MoveCalcTree.copyMove(((Piece) node.children.get(i).projectedBoard[j][k]).getMoves().get(l))); // add all possible moves to the list of enemy moves
                            }
                        }
                    }
                }
            }
            Move enemyMove = null; // Enemy move
            int bestScore = Integer.MIN_VALUE; // lowest possible value
            int bestChoiceIndex = -1; // best index

            if (allEnemyMoves.size() > 0) { // if the enemy can make a move
                if (allEnemyMoves.get(0).getRemoveList().size() > 0) { // if the enemy can take a piece
                    for (int j = 0; j <= allEnemyMoves.size() - 1; j++) { // for all possible moves
                        Move possibleMove = MoveCalcTree.copyMove(allEnemyMoves.get(j)); //make a COPY of the move
                        int[] stats = AIController.moveScore(Gamecontroller.deepBoardCopy(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)), MoveCalcTree.copyMove(possibleMove)); //get stats about move
                        int score = 10 * stats[0] - stats[1]; //calc move score
                        if (score > bestScore) { //check if move is the best move
                            bestChoiceIndex = j;
                            bestScore = score;
                        }
                    }
                } else { //If the piece only makes a non-capturing move
                    for (int j = 0; j <= allEnemyMoves.size() - 1; j++) { // for all possible moves
                        Move possibleMove = MoveCalcTree.copyMove(allEnemyMoves.get(j)); //make a COPY of the move
                        int[] stats = AIController.moveScore(Gamecontroller.deepBoardCopy(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)), MoveCalcTree.copyMove(possibleMove)); //get stats about move
                        int score = stats[3] - stats[4] - stats[1] * 200; //calc move score
                        if (score > bestScore) { //check if move is the best move
                            bestChoiceIndex = j;
                            bestScore = score;
                        }
                    }
                }


                enemyMove = allEnemyMoves.get(bestChoiceIndex); // projected enemy move is always best move

                Object[][] copyBoard = Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard); // create deep copy of board

                copyBoard = Gamecontroller.deepBoardCopy(AIMoveToAction.AIAction(enemyMove, Gamecontroller.deepBoardCopy(copyBoard))); // update projected board with enemy move

//				AIController.testBoard(copyBoard);

                node.children.get(i).projectedBoard = Gamecontroller.deepBoardCopy(copyBoard); // update projected board

                node.children.get(i).EnemyMoveScore = bestScore; // save enemy score

            }


        }

        if (depth < MAX_DEPTH) { // if the tree goes deeper
            for (int i = 0; i <= node.children.size() - 1; i++) { // for all children of the current node
                if (node.children.get(i).nextMove.getPiece().getColour() == 1) { // if this AI's colour is white
                    node.children.get(i).projectedBoard = AIController.calcWhiteMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); //calc white moves for projected board
                } else { // if this AI's colour is black
                    node.children.get(i).projectedBoard = AIController.calcBlackMoves(Gamecontroller.deepBoardCopy(node.children.get(i).projectedBoard)); // calc black moves for projected board
                }

                int index = 0; //index
                if (node.children.size() > 0) { // if there exist any children
                    int best = Integer.MIN_VALUE; // look for best child
                    index = -1;

                    int hold = depth + 1;
//						AIController.testBoard(node.children.get(j).projectedBoard);

                    recursion(node.children.get(i), hold);
//						
//						if (node.children.get(i).score > best) {
//							index = j;
//							best = node.children.get(i).score;
//						}
//					int hold = depth + 1;
//					recursion(node.children.get(index), hold); // restart calculation for best child node
                }
            }
        }
        // recursion + pruning

        // Pick best child of root node


        if (node.children.size() > 0 && depth == 1) {
            int best = Integer.MIN_VALUE;
            int index = -1;

            for (int i = 0; i <= node.children.size() - 1; i++) {
                if (node.children.get(i).score > best) {
                    index = i;
                    best = node.children.get(i).score;
                }
            }

            Move bruhmove = node.children.get(index).nextMove;

            return bruhmove;
        } else {
            return null;
        }
    }

}
