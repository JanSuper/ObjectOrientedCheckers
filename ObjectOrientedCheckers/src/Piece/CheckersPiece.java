package Piece;

import java.awt.Image;

abstract public class CheckersPiece implements Piece {
	
	private Image visual;
	
	public CheckersPiece() {
		
	}
	
	public CheckersPiece clone() {
		return this;
	}
	
	public Image getVisual() {
		return this.visual;
	}
	
	public void setVisual(Image vis) {
		this.visual = vis;
	}
	

}
