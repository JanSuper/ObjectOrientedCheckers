package ObjectUI;

import Gamecontroller.Gamecontroller;
import Piece.BlackPiece;
import Piece.Piece;
import Piece.WhitePiece;


public class Visual extends SmartGroup {

	private static final double WIDTH_OF_TILE = 100;
    public Object[][] board;
    
    public Visual() {
    	Gamecontroller control = new Gamecontroller();
    	 board = Gamecontroller.field;
    	 create_tiles();
         place_pieces();
         translateZProperty().set(getTranslateZ()+500);
    }
	
	public void ResetUI() {
	remove_pieces();
	Gamecontroller control = new Gamecontroller();
   	board = Gamecontroller.field;
   	place_pieces();
	}
    
    private void create_tiles(){
        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 4; i++){
                WhiteTile wt1 = new WhiteTile();
                wt1.translateXProperty().set(wt1.getTranslateX()+WIDTH_OF_TILE*i*2);
                wt1.translateYProperty().set(wt1.getTranslateY()+WIDTH_OF_TILE*j*2);

                WhiteTile wt2 = new WhiteTile();
                wt2.translateXProperty().set(wt2.getTranslateX()+WIDTH_OF_TILE*(i*2+1));
                wt2.translateYProperty().set(wt2.getTranslateY()+WIDTH_OF_TILE*(j*2+1));


                BlackTile bt1 = new BlackTile();
                bt1.translateXProperty().set(bt1.getTranslateX()+WIDTH_OF_TILE*(i*2+1));
                bt1.translateYProperty().set(bt1.getTranslateY()+WIDTH_OF_TILE*j*2);

                BlackTile bt2 = new BlackTile();
                bt2.translateXProperty().set(bt2.getTranslateX()+WIDTH_OF_TILE*i*2);
                bt2.translateYProperty().set(bt2.getTranslateY()+WIDTH_OF_TILE*(j*2+1));

                getChildren().addAll(bt1, wt1, bt2, wt2);
            }
        }
    }
    
    public void remove_pieces() {
    	for(int i = 0; i <= board.length - 1; i++){
            for(int j = 0; j <= board[0].length - 1; j++){
            	if(board[i][j] != null) {
            		getChildren().remove(((Piece)board[i][j]));
            	}
           }
       }
    }
    
    public void place_pieces(){

        for(int i = 0; i <= board.length - 1; i++){
            for(int j = 0; j <= board[0].length - 1; j++){
            	if(board[i][j] != null) {
                	if(((Piece)board[i][j]).getColour() == 0){
                    	getChildren().add(((BlackPiece)board[i][j]));
                	}
                	else if(((Piece)board[i][j]).getColour() == 1){
                    	getChildren().add(((WhitePiece)board[i][j]));
                	}
            	}
            }
        }
    }
}
