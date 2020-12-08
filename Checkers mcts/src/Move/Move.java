package Move;

import java.util.ArrayList;
import java.util.List;

import Piece.Piece;

public class Move{
	
	Piece pieceMoved;
	public int[] from;
	List<int[]> to = new ArrayList<>();
	List<Piece> remove = new ArrayList<>();
	boolean becomeKing;

	public Move (Piece p) {
		pieceMoved = p;
		from = p.getLocation();
	}
	
	public Move (Move m) {
		this.pieceMoved = m.pieceMoved;
		this.from = m.from;
		this.to = m.to;
		this.remove = m.remove;
		this.becomeKing = m.becomeKing;
	}

	public void setTo(int i, int j) {
		int[] hold = {i,j};
		to.add(hold);
	}
	
	public void addToRemove(Piece p) {
		remove.add(p);
	}
	
	public void becomesKing(boolean b) {
		if(!this.pieceMoved.isKing())
		becomeKing = b;
	}
	
	public List<int[]> getToList(){
		return to;
	}
	
	public List<Piece> getRemoveList(){
		return remove;
	}
	
	public int[] getFrom() {
		return from;
	}
	
	public Piece getPiece() {
		return pieceMoved;
	}
	
	public boolean becomesKing() {
		return becomeKing;
	}
	
	@Override
	public String toString() {
		return ((String)("[" + from[0] + "," + from[1] + "]" + " to " + "[" + to.get(to.size() - 1)[0] + "," + to.get(to.size() - 1)[1] + "]"));
	}

	public void setRemove(ArrayList<Piece> removeList){this.remove = removeList;}

}
