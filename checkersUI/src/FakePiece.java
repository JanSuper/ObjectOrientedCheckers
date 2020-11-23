import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class FakePiece extends Piece
{
    private final Board board;
    public ArrayList<Piece> eaten_pieces = new ArrayList<>();
    private final Piece ally_piece;

    public FakePiece(double x, double y, Piece ally_piece, Board board)
    {
        super();

        this.ally_piece = ally_piece;
        this.board = board;

        this.setTranslateX((100*x)+50);
        this.setTranslateY((100*y)+50);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTPINK);
        setMaterial(material);

        this.board.getChildren().add(this);
        this.board.fakePieces.add(this);


        addEvent();
    }

    private void addEvent()
    {
        this.setOnMouseClicked(e ->{
            //delete eaten pieces
            if(this.eaten_pieces != null)
            {
                for(Piece p: eaten_pieces)
                {
                    this.board.getChildren().remove(p);
                    if(p instanceof RedPiece)
                        this.board.redPieces.remove(p);
                    else
                        this.board.blackPieces.remove(p);
                }
            }

            //move ally piece
            this.ally_piece.setTranslateX(this.getTranslateX());
            this.ally_piece.setTranslateY(this.getTranslateY());

            //delete fake pieces
            for( FakePiece fp : this.board.fakePieces)
                this.board.getChildren().remove(fp);
            this.board.fakePieces.clear();


            //endTurn
            this.board.endTurn();

        });
    }
}