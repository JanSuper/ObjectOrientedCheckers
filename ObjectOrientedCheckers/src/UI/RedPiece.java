package UI;

import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class RedPiece extends Piece
{
    private Board board;
    public boolean king = false;

    public RedPiece(Board board)
    {
        super();
        this.board = board;
        this.board.redPieces.add(this);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.DARKRED);
        setMaterial(material);
        addEvent();
    }

    public void king()
    {
        this.king = true;
        PhongMaterial mt = new PhongMaterial();
        mt.setDiffuseMap(new Image(String.valueOf(getClass().getResource("/images/RedCrown.png"))));
        setMaterial(mt);
        this.setOnMouseClicked(e->{
            //if it's red's turn
            if(this.board.turn%2==1)
            {
                //delete all fake pieces
                for( FakePiece fp : this.board.fakePieces)
                    this.board.getChildren().remove(fp);
                this.board.fakePieces.clear();
                kingMovements();
            }
        });
    }

    private boolean occupiedByEnemy(double x, double y)
    {
        return (this.board.occupied_by_enemy(x, y, 1));
    }

    private boolean isFree(double x, double y) {return (!this.board.piece_exists(x, y));}

    private boolean isInBounds(double x, double y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    private void addEvent()
    {
        this.setOnMouseClicked(e->{
            //if it's red's turn
            if(this.board.turn%2==1)
            {

                //delete all fake pieces
                for( FakePiece fp : this.board.fakePieces)
                    this.board.getChildren().remove(fp);
                this.board.fakePieces.clear();
                // first move
                firstMove();
            }
        });
    }


    private void firstMove()
    {
        // current coordinates
        double xCoord = this.getXCoord();
        double yCoord = this.getYCoord();

        // simple moves coordinates
        double[] FrontX = {xCoord + 1, xCoord - 1};
        double yFront = yCoord - 1;

        // for the possible simple moves
        for(double xFront : FrontX)
        {
            //check if coords are in bounds and free
            if( isInBounds(xFront, yFront) && isFree(xFront, yFront) )
            {
                //create fake piece
                FakePiece fp = new FakePiece(xFront, yFront, this, this.board);
                //no eaten pieces
            }
            // else if coords are occupied by enemy
            else if ( isInBounds(xFront, yFront) && occupiedByEnemy(xFront, yFront))
            {
                //landing space
                double landX;
                if(xFront == xCoord+1)
                    landX = xFront+1;
                else
                    landX = xFront-1;
                double landY = yFront - 1;

                //if landing space is in bounds and free
                if( isInBounds(landX, landY) && isFree(landX, landY))
                {
                    // create fake and add the piece to eat in the list
                    FakePiece fp = new FakePiece(landX, landY, this, this.board);
                    fp.eaten_pieces.add(this.board.getBlackPiece(xFront, yFront));
                    // launch recursive function
                    recurse(landX, landY, xFront, yFront, fp);
                }
            }
        }
    }
    private void recurse(double X, double Y, double prevX, double prevY, FakePiece fp)
    {
        double[] xPoss = {X - 1, X + 1};
        double[] yPoss = {Y - 1, Y + 1};

        //for all positions around piece
        for(double xCoord: xPoss)
        {
            for(double yCoord: yPoss)
            {
                //we don't check if we can eat back where we came from
                if(!(xCoord == prevX && yCoord == prevY))
                {
                    //check if is in bounds and occupied by enemy
                    if(isInBounds(xCoord, yCoord) && occupiedByEnemy(xCoord, yCoord))
                    {
                        //check landing space
                        double landX, landY;
                        if(xCoord == X-1)
                            landX = X-2;
                        else
                            landX = X+2;
                        if(yCoord == Y-1)
                            landY = Y-2;
                        else
                            landY = Y+2;

                        //if landing space is in bounds and is either free or occupied by OG piece
                        if(isInBounds(landX, landY) && (isFree(landX, landY) ))
                        {
                            //create fake piece and recurse
                            FakePiece f = new FakePiece(landX, landY, this, this.board);
                            f.eaten_pieces.addAll(fp.eaten_pieces);
                            f.eaten_pieces.add(this.board.getBlackPiece(xCoord, yCoord));
                            recurse(landX, landY, xCoord, yCoord, f);
                        }
                        //else if we going back to OG position (counter-loop)
                        else if (isInBounds(landX, landY) && (landX == this.getXCoord() && landY == this.getYCoord()))
                        {
                            FakePiece f = new FakePiece(landX, landY, this, this.board);
                            f.eaten_pieces.addAll(fp.eaten_pieces);
                            f.eaten_pieces.add(this.board.getBlackPiece(xCoord, yCoord));
                        }
                    }
                }
            }
        }
    }

/**********************************************************************************************************************/
    private void kingMovements()
    {
        // current coordinates
        double xCoord = this.getXCoord();
        double yCoord = this.getYCoord();

        // simple moves coordinates
        double[] FrontX = {xCoord + 1, xCoord - 1};
        double[] FrontY = {yCoord - 1, yCoord + 1};

        // for the possible simple moves
        for(double xFront : FrontX)
        {
            for(double yFront: FrontY)
            {
                {
                    //check if coords are in bounds and free
                    if( isInBounds(xFront, yFront) && isFree(xFront, yFront) )
                    {
                        //create fake piece
                        FakePiece fp = new FakePiece(xFront, yFront, this, this.board);
                        //no eaten pieces
                    }
                    // else if coords are occupied by enemy
                    else if ( isInBounds(xFront, yFront) && occupiedByEnemy(xFront, yFront))
                    {
                        //landing space
                        double landX;
                        if(xFront == xCoord+1)
                            landX = xFront+1;
                        else
                            landX = xFront-1;
                        double landY = yFront - 1;

                        //if landing space is in bounds and free
                        if( isInBounds(landX, landY) && isFree(landX, landY))
                        {
                            // create fake and add the piece to eat in the list
                            FakePiece fp = new FakePiece(landX, landY, this, this.board);
                            fp.eaten_pieces.add(this.board.getBlackPiece(xFront, yFront));

                            // launch recursive function
                            recurse(landX, landY, xFront, yFront, fp);
                        }
                    }
                }
            }
        }
    }
}
