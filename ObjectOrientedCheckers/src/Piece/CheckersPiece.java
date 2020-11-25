package Piece;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import Move.Move;
import Move.MoveCalcTree;

import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class CheckersPiece extends Cylinder implements Piece {
	
    private static final double HEIGHT = 20;
    private static final double RADIUS = 33.3;
    
    int[] position = {-1, -1};
	boolean isKing = false;
	List<Move> moves;
    
	public CheckersPiece() {
		super(RADIUS, HEIGHT);
        translateXProperty().set(150);
        translateYProperty().set(50);
        translateZProperty().set(this.getTranslateZ() - 20);
        rotate_in_right_position();
	}

    public double getXCoord() {
        return ((this.getTranslateX() - 50) / 100);
    }

    public double getYCoord() {
        return ((this.getTranslateY() - 50) / 100);
    }

    private void rotate_in_right_position() {
        Rotate r = new Rotate(90, Rotate.X_AXIS);
        Transform t = new Rotate();
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().add(t);
    }
	
	public CheckersPiece clone() {
		return this;
	}
	
	public CheckersPiece deepClone(Piece piece) {
		return null;
	}

	@Override
	public void calcMoves() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AIcalcMoves(Object[][] tempboard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocation(int i, int j) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return 0;
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
	
}
