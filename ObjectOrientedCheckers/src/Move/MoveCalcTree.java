package Move;

import java.util.ArrayList;
import java.util.List;

import Board.Board;
import Piece.*;
import Gamecontroller.*;

public class MoveCalcTree{
	
	MoveCalcNode headNode;
	List<Move> moveList = new ArrayList();
	boolean takeMove = false;

	public List<Move> getMoves (Piece currentpiece, Object[][] tempboard){
		Move nullmove =  new Move(currentpiece);
		headNode = new MoveCalcNode(currentpiece, nullmove);
		recursion(headNode, tempboard);
		if (takeMove) {
			removeNormalSteps();
		}
		return moveList;
	}
	
	public void recursion(MoveCalcNode node, Object[][] tempboard) {
		Move tempstep1 = copyMove(node.getMove());
		Move tempstep2 = copyMove(node.getMove());
		Move tempstep3 = copyMove(node.getMove());
		Move tempstep4 = copyMove(node.getMove());
		
		boolean nextStep = false;
		int icoor = node.getPiece().getLocation()[0];
		int jcoor = node.getPiece().getLocation()[1];
		if (!node.getPiece().isKing() && !(node.getMove().remove.size() > 0)) {
			if (Gamecontroller.turn % 2 == 0) { //blackturn
				if(icoor + 1 < tempboard.length && jcoor - 1 > -1) { //if possible create child node where move is taken
					if(tempboard[icoor+1][jcoor-1] == null) {
						Move stepMove = tempstep1;
						stepMove.setTo(icoor+1, jcoor-1);
						stepMove.becomesKing(icoor==tempboard.length-1);
						moveList.add(stepMove);
					}
					else if (tempboard[icoor+1][jcoor-1] != null) {
						if (((Piece)tempboard[icoor+1][jcoor-1]).getColour() != node.getPiece().getColour()) { //if enemy piece
							if (icoor + 2 < tempboard.length && jcoor - 2 > -1) { //check for board edge
								if (tempboard[icoor+2][jcoor-2] == null) {
									Move stepMove = tempstep1;
									stepMove.setTo(icoor+2, jcoor-2);
									stepMove.becomesKing(icoor==tempboard.length-1);
									stepMove.addToRemove((Piece)tempboard[icoor+1][jcoor-1]);
									MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
									takeMove = true;
									nextStep = true;
									recursion(newNode, tempboard);
								}
							}
						}
					}
					//else move is illegal so dont do anything
				}
				if(icoor + 1 < tempboard.length && jcoor + 1 < tempboard[0].length) {
					if(tempboard[icoor+1][jcoor+1] == null) {
						Move stepMove = tempstep2;
						stepMove.setTo(icoor+1, jcoor+1);
						stepMove.becomesKing(icoor==tempboard.length-1);
						moveList.add(stepMove);
					}
					else if (tempboard[icoor+1][jcoor+1] != null) {
						if (((Piece)tempboard[icoor+1][jcoor+1]).getColour() != node.getPiece().getColour()) { //if enemy piece
							if (icoor + 2 < tempboard.length && jcoor + 2 < tempboard[0].length) { //check for board edge
								if (tempboard[icoor+2][jcoor+2] == null) {
									Move stepMove = tempstep2;
									stepMove.setTo(icoor+2, jcoor+2);
									stepMove.becomesKing(icoor==tempboard.length-1);
									stepMove.addToRemove((Piece)tempboard[icoor+1][jcoor+1]);
									MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
									takeMove = true;
									nextStep = true;
									recursion(newNode, tempboard);
								}
							}
						}
					}
					//else move is illegal so dont do anything
				}
			}
			else { //whiteturn
				if(icoor - 1 > -1 && jcoor - 1 > -1) {
					if(tempboard[icoor-1][jcoor-1] == null) {
						Move stepMove = tempstep3;
						stepMove.setTo(icoor-1, jcoor-1);
						stepMove.becomesKing(icoor==0);
						moveList.add(stepMove);
					}
					else if (tempboard[icoor-1][jcoor-1] != null) {
						if (((Piece)tempboard[icoor-1][jcoor-1]).getColour() != node.getPiece().getColour()) { //if enemy piece
							if (icoor - 2 > -1 && jcoor - 2 > -1) { //check for board edge
								if (tempboard[icoor-2][jcoor-2] == null) {
									Move stepMove = tempstep3;
									stepMove.setTo(icoor-2, jcoor-2);
									stepMove.becomesKing(icoor==0);
									stepMove.addToRemove((Piece)tempboard[icoor-1][jcoor-1]);
									MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
									takeMove = true;
									nextStep = true;
									recursion(newNode, tempboard);
								}
							}
						}
					}
					//else move is illegal so dont do anything
				}
				if(icoor - 1 > -1 && jcoor + 1 < tempboard[0].length) {
					if(tempboard[icoor-1][jcoor+1] == null) {
						Move stepMove = tempstep4;
						stepMove.setTo(icoor-1, jcoor+1);
						stepMove.becomesKing(icoor==0);
						moveList.add(stepMove);
					}
					else if (tempboard[icoor-1][jcoor+1] != null) {
						if (((Piece)tempboard[icoor-1][jcoor+1]).getColour() != node.getPiece().getColour()) { //if enemy piece
							if (icoor - 2 > -1 && jcoor + 2 < tempboard[0].length) { //check for board edge
								if (tempboard[icoor-2][jcoor+2] == null) {
									Move stepMove = tempstep4;
									stepMove.setTo(icoor-2, jcoor+2);
									stepMove.becomesKing(icoor==0);
									stepMove.addToRemove((Piece)tempboard[icoor-1][jcoor+1]);
									MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
									takeMove = true;
									nextStep = true;
									recursion(newNode, tempboard);
								}
							}
						}
					}
					//else move is illegal so dont do anything
				}
			}
		}
		else { //king turn or taken piece
			int holdsize = node.getMove().getToList().size();
			if (holdsize > 0) {
				int[] holdpos = node.getMove().getToList().get(holdsize-1);
				icoor = holdpos[0];
				jcoor = holdpos[1];
			}
			
			if(icoor + 1 < tempboard.length && jcoor - 1 > -1) { //if possible create child node where move is taken
				if(tempboard[icoor+1][jcoor-1] == null && !(node.getMove().remove.size() > 0)) {
					Move stepMove = tempstep1;
					stepMove.setTo(icoor+1, jcoor-1);
					moveList.add(stepMove);
				}
				else if (tempboard[icoor+1][jcoor-1] != null) {
					if ((((Piece)tempboard[icoor+1][jcoor-1]).getColour() != node.getPiece().getColour()) && !(node.getMove().getRemoveList().contains(((Piece)tempboard[icoor+1][jcoor-1])))) { //if enemy piece
						if (icoor + 2 < tempboard.length && jcoor - 2 > -1) { //check for board edge
							if (tempboard[icoor+2][jcoor-2] == null || ((Piece)tempboard[icoor+2][jcoor-2]).equals(node.getPiece())) {
								Move stepMove = tempstep1;
								stepMove.setTo(icoor+2, jcoor-2);
								stepMove.becomesKing(icoor==tempboard.length-1);
								stepMove.addToRemove((Piece)tempboard[icoor+1][jcoor-1]);
								MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
								takeMove = true;
								nextStep = true;
								recursion(newNode, tempboard);
							}
						}
					}
				}
				//else move is illegal so dont do anything
			}
			if(icoor + 1 < tempboard.length && jcoor + 1 < tempboard[0].length) {
				if(tempboard[icoor+1][jcoor+1] == null && !(node.getMove().remove.size() > 0)) {
					Move stepMove = tempstep2;
					stepMove.setTo(icoor+1, jcoor+1);
					moveList.add(stepMove);
				}
				else if (tempboard[icoor+1][jcoor+1] != null) {
					if ((((Piece)tempboard[icoor+1][jcoor+1]).getColour() != node.getPiece().getColour()&& !(node.getMove().getRemoveList().contains(((Piece)tempboard[icoor+1][jcoor+1]))))) { //if enemy piece
						if (icoor + 2 < tempboard.length && jcoor + 2 < tempboard[0].length) { //check for board edge
							if (tempboard[icoor+2][jcoor+2] == null || ((Piece)tempboard[icoor+2][jcoor+2]).equals(node.getPiece())) {
								Move stepMove = tempstep2;
								stepMove.setTo(icoor+2, jcoor+2);
								stepMove.becomesKing(icoor==tempboard.length-1);
								stepMove.addToRemove((Piece)tempboard[icoor+1][jcoor+1]);
								MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
								takeMove = true;
								nextStep = true;
								recursion(newNode, tempboard);
							}
						}
					}
				}
				//else move is illegal so dont do anything
			}
			if(icoor - 1 > -1 && jcoor - 1 > -1) {
				if(tempboard[icoor-1][jcoor-1] == null && !(node.getMove().remove.size() > 0)) {
					Move stepMove = tempstep3;
					stepMove.setTo(icoor-1, jcoor-1);
					moveList.add(stepMove);
				}
				else if (tempboard[icoor-1][jcoor-1] != null) {
					if ((((Piece)tempboard[icoor-1][jcoor-1]).getColour() != node.getPiece().getColour() && !(node.getMove().getRemoveList().contains(((Piece)tempboard[icoor-1][jcoor-1]))))) { //if enemy piece
						if (icoor - 2 > -1 && jcoor - 2 > -1) { //check for board edge
							if (tempboard[icoor-2][jcoor-2] == null || ((Piece)tempboard[icoor-2][jcoor-2]).equals(node.getPiece())) {
								Move stepMove = tempstep3;
								stepMove.setTo(icoor-2, jcoor-2);
								stepMove.becomesKing(icoor==tempboard.length-1);
								stepMove.addToRemove((Piece)tempboard[icoor-1][jcoor-1]);
								MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
								takeMove = true;
								nextStep = true;
								recursion(newNode, tempboard);
							}
						}
					}
				}
				//else move is illegal so dont do anything
			}
			if(icoor - 1 > -1 && jcoor + 1 < tempboard[0].length) {
				if(tempboard[icoor-1][jcoor+1] == null && !(node.getMove().remove.size() > 0)) {
					Move stepMove = tempstep4;
					stepMove.setTo(icoor-1, jcoor+1);
					moveList.add(stepMove);
				}
				else if (tempboard[icoor-1][jcoor+1] != null) {
					if ((((Piece)tempboard[icoor-1][jcoor+1]).getColour() != node.getPiece().getColour()&& !(node.getMove().getRemoveList().contains(((Piece)tempboard[icoor-1][jcoor+1]))))) { //if enemy piece
						if (icoor - 2 > -1 && jcoor + 2 < tempboard[0].length) { //check for board edge
							if (tempboard[icoor-2][jcoor+2] == null || ((Piece)tempboard[icoor-2][jcoor+2]).equals(node.getPiece())) {
								Move stepMove = tempstep4;
								stepMove.setTo(icoor-2, jcoor+2);
								stepMove.becomesKing(icoor==tempboard.length-1);
								stepMove.addToRemove((Piece)tempboard[icoor-1][jcoor+1]);
								MoveCalcNode newNode = new MoveCalcNode(node.getPiece(), stepMove);
								takeMove = true;
								nextStep = true;
								recursion(newNode, tempboard);
							}
						}
					}
				}
				//else move is illegal so dont do anything
			}
		}
		if(node.getMove().getToList().size() > 0 && !nextStep) {
			moveList.add(new Move(node.getMove()));
		}
		
	}
	
	public void removeNormalSteps() {
		for(int i = moveList.size()-1; i >= 0; i--) {
			if((moveList.get(i).getRemoveList().size() == 0)) {
				moveList.remove(i);
			}
		}
	}
	
	public static Move copyMove(Move oldmove) {
		Piece holdPiece = oldmove.getPiece();
		Move hold = new Move(holdPiece);
		int[] holdij = oldmove.getFrom();
		List<int[]> holdtolist = new ArrayList();
		for (int i = 0; i <= oldmove.getToList().size()-1; i++) {
			holdtolist.add(oldmove.getToList().get(i));
		}
		List<Piece> holdremove = new ArrayList();
		for (int i = 0; i <= oldmove.getRemoveList().size()-1; i++) {
			holdremove.add(oldmove.getRemoveList().get(i));
		}
		boolean holdKing = oldmove.becomesKing();
		
		for (int i = 0; i <= oldmove.getToList().size()-1; i++) {
			int[] holdarray = holdtolist.get(i);
			hold.setTo(holdarray[0], holdarray[1]);
		}
		for (int i = 0; i <= oldmove.getRemoveList().size()-1; i++) {
			Piece holdOnPiece = holdremove.get(i);
			hold.addToRemove(holdOnPiece);
		}
		hold.becomesKing(holdKing);
		return hold;
	}
}
