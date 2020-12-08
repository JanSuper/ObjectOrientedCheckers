package Test;

import Piece.*;
import Gamecontroller.*;

public class boardCopyTest {
	
	public static void main(String[] args) {
		Object[][] bruh = new Object[8][8];
		
		for(int i = 0; i <= 7; i++) {
			for(int j = 0; j <= 7; j++) {
				bruh[i][j] = null;
			}
		}
		
		bruh[3][3] = new BlackPiece();
		((Piece)bruh[3][3]).setLocation(3, 3);
		
		Object[][] doubleBruh = Gamecontroller.deepBoardCopy(bruh);
		
		bruh[4][4] = bruh[3][3];
		((Piece)bruh[4][4]).setLocation(4, 4);
		bruh[3][3] = null;
		
		System.out.println(bruh[3][3] == null);
	}

}
