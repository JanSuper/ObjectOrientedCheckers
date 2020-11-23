package Piece;

import java.awt.Image;

import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

abstract public class CheckersPiece extends Cylinder implements Piece {
	
    private static final double HEIGHT = 20;
    private static final double RADIUS = 33.3;
    
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
	
}
