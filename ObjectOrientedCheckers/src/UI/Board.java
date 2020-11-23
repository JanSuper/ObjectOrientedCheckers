package UI;

import javafx.scene.Group;

import java.util.ArrayList;

public class Board extends SmartGroup
{

    private static final double WIDTH_OF_TILE = 100;
    private int[][] board;
    public final ArrayList<RedPiece> redPieces = new ArrayList<RedPiece>();
    public final ArrayList<BlackPiece> blackPieces = new ArrayList<BlackPiece>();
    public final ArrayList<FakePiece> fakePieces = new ArrayList<FakePiece>();
    public int turn = 0;

    public Board()
    {
        initialise_board();
        create_tiles();
        place_pieces();
        translateZProperty().set(getTranslateZ()+500);
    }
/**********************************************************************************************************************/
    private void initialise_board()
    {
        board = new int[8][8];

        for(int i = 0; i < 8; i=i+2)
        {
            board[0][i+1] = 1;
            board[1][i] = 1;
            board[2][i+1] = 1;
            board[5][i] = 2;
            board[6][i+1] = 2;
            board[7][i] = 2;
        }

    }
/**********************************************************************************************************************/
    private void create_tiles()
    {
        for(int j = 0; j < 4; j++)
        {
            for(int i = 0; i < 4; i++)
            {
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

/**********************************************************************************************************************/

    private void place_pieces()
    {

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(board[i][j] == 1)
                {
                    BlackPiece bp = new BlackPiece(this);
                    bp.translateXProperty().set(bp.getTranslateX()+100*(j-1));
                    bp.translateYProperty().set(bp.getTranslateY()+100*i);
                    getChildren().add(bp);
                }

                if(board[i][j] == 2)
                {
                    RedPiece rp = new RedPiece(this);
                    rp.translateXProperty().set(rp.getTranslateX()+100*(j-1));
                    rp.translateYProperty().set(rp.getTranslateY()+100*i);
                    getChildren().add(rp);
                }
            }
        }
    }

/**********************************************************************************************************************/
    public boolean piece_exists(double x, double y)
    {
        for(RedPiece rp : redPieces)
        {
            if( rp.getXCoord() == x && rp.getYCoord() == y)
                return true;
        }
        for(BlackPiece bp : blackPieces)
        {
            if(bp.getXCoord() == x && bp.getYCoord() == y)
                return true;
        }

        return false;
    }

/**********************************************************************************************************************/
    public boolean occupied_by_enemy(double x, double y, int piece)
    {
        //if piece is black
        if(piece == 0)
        {
            for(RedPiece rp : redPieces)
            {
                if( rp.getXCoord() == x && rp.getYCoord() == y)
                    return true;
            }
        }
        //if piece is red
        if(piece == 1)
        {
            for(BlackPiece bp : blackPieces)
            {
                if(bp.getXCoord() == x && bp.getYCoord() == y)
                    return true;
            }
        }
        return false;
    }

/*******************************************************************************/
    public BlackPiece getBlackPiece(double x, double y)
    {
        for(BlackPiece bp : blackPieces)
        {
            if(bp.getXCoord() == x && bp.getYCoord() == y)
                return bp;
        }
        return null;
    }
    public RedPiece getRedPiece(double x, double y)
    {
        for(RedPiece rp : redPieces)
        {
            if( rp.getXCoord() == x && rp.getYCoord() == y)
                return rp;
        }
        return null;
    }
/*******************************************************************************/
    public void endTurn() {
        this.turn++;
        //scan for end of game
        if (this.blackPieces.size() == 0) {
            System.out.println("Red wins");
        }
        if (this.redPieces.size() == 0){
            System.out.println("Black wins");
        }

        //scan for kings
        for(RedPiece rp: this.redPieces){
            if(rp.getYCoord()==0 && !rp.king)
                rp.king();
        }
        for(BlackPiece bp: this.blackPieces){
            if(bp.getYCoord()==7 && !bp.king)
                bp.king();
        }
    }
}