package MonteCarlo;

import AIClasses.AIController;
import AIClasses.AIMoveToAction;
import Gamecontroller.Gamecontroller;
import Move.Move;
import Piece.BlackPiece;
import Piece.CheckersPiece;
import Piece.Piece;
import Piece.WhitePiece;

import javax.swing.text.AbstractDocument;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class mcTreeSearch {

    private final mcNode root;
    private final Random rng = new Random();
    public boolean withRAVE = Gamecontroller.mctsWithRave;
    private boolean withWeights = false;

    public mcTreeSearch(Object[][] g_state) {
        this.root = new mcNode(g_state);
    }

    public void setWithRAVE(boolean bool){this.withRAVE = bool;}

    public void setWithWeights(boolean b){
        this.withWeights = b;
    }

    public Move getNextMove() {

        if(Gamecontroller.playerOneAI)
            exp_turn = 0;
        else
            exp_turn = 1;

        long start = System.currentTimeMillis();
        int TIME_LIMIT = Gamecontroller.mcLimit;
        long end = start + TIME_LIMIT;
        while (System.currentTimeMillis() < end) {
            mcNode node = selection(); //get best node
            expand(node); //expand the selected node
            simulate(node); //simulate and back propagate(update)
        }


        mcNode best_child = this.root.children.get(0);
        for(mcNode child : root.children){
            if(child.num_simulations > best_child.num_simulations)
                best_child = child;
        }

        Move move = best_child.move_made;

        return move;
    }

    private mcNode selection_method(mcNode node){

        mcNode select_node = node.children.get(0);
        for(mcNode child : node.children){
            if(UCB(child) > UCB(select_node))
                select_node = child;
        }
        if(select_node.isLeaf())
            return select_node;
        else
            return selection_method(select_node);
    }

    private mcNode selection() {
        if(this.root.isLeaf())
            return this.root;
        return selection_method(this.root);
    }

    int exp_turn;

    private void expand(mcNode node) {
        if (exp_turn%2 == 0)
            AIController.calcBlackMoves(node.game_state, Gamecontroller.madeMoves);
        else
            AIController.calcWhiteMoves(node.game_state, Gamecontroller.madeMoves);
        ArrayList<Move> possibleMoves = new ArrayList<>();
        ArrayList<Move> possibleMovesEat = new ArrayList<>();

        for (Object[] a : node.game_state) {
            for (Object obj : a) {
                if (obj != null) {
                    //if ai plays as black turn
                    if (exp_turn%2 == 0) {
                        if (obj instanceof BlackPiece) {
                            for (Move m : ((BlackPiece) obj).getMoves()) {
                                if(m.getRemoveList().size() == 0)
                                    possibleMoves.add(m);
                                else
                                    possibleMovesEat.add(m);
                            }
                        }
                    }
                    else {
                        if (obj instanceof WhitePiece) {
                            for (Move m : ((WhitePiece) obj).getMoves()) {
                                if(m.getRemoveList().size() == 0)
                                    possibleMoves.add(m);
                                else
                                    possibleMovesEat.add(m);
                            }
                        }
                    }
                }
            }
        }

        if(possibleMovesEat.size() == 0){
            for(Move m : possibleMoves){
                Object[][] new_board = AIMoveToAction.AIAction(m, Gamecontroller.deepBoardCopy(node.game_state));
                mcNode new_node = new mcNode(new_board, node);
                new_node.setMove_made(m);
            }
        }
        else {
            for(Move m : possibleMovesEat){
                Object[][] new_board = AIMoveToAction.AIAction(m, Gamecontroller.deepBoardCopy(node.game_state));
                mcNode new_node = new mcNode(new_board, node);
                new_node.setMove_made(m);
            }
        }
        exp_turn++;
    }

    private void simulate(mcNode node) {
        Object[][] board = node.game_state;//get a copy of board
        int sim_count = 1;
        int sim_turn;
        if(Gamecontroller.playerOneAI)
            sim_turn = 0;
        else
            sim_turn = 1;

        while (!gameOver(board) && sim_count < 50){
            ArrayList<Move> movesMade = new ArrayList<>();

            if (sim_turn%2 == 0){
                Move move = getRandomBlackMove(board);

                if(withRAVE)
                    movesMade.add(move);

                if(move != null){
                    Object[][] new_board = AIMoveToAction.AIAction(move, Gamecontroller.deepBoardCopy(board));
                    board = new_board;
                }
                else
                    break;
            }
            else if(sim_turn%2 == 1){
                Move move = getRandomWhiteMove(board);

                if(withRAVE)
                    movesMade.add(move);

                if(move != null){
                    Object[][] new_board = AIMoveToAction.AIAction(move, Gamecontroller.deepBoardCopy(board));
                    board = new_board;
                }
                else
                    break;
            }

            sim_turn=sim_turn+1;
            sim_count=sim_count+1;

            if(withRAVE){
                double score = getScore(board, sim_turn);
                for(mcNode child : this.root.children){
                    for(Move m : movesMade){
                        if(sameMove(m, child.move_made)){
                            child.num_playouts_containing_this_move++;
                            child.num_playouts_won_with_this_move+=score;
                        }
                    }
                }
            }
        }

        double score = getScore(board, sim_turn);

        backpropagation(node, score);
    }

    private void backpropagation(mcNode node, double score) {
        mcNode pointer = node;
        while (pointer.parent != null) {
            pointer.num_wins += score;
            pointer.num_simulations++;
            pointer = pointer.parent;
        }
        //do it for root too
        pointer.num_wins += score;
        pointer.num_simulations++;
    }

    /**
     * HELPER METHODS
     */
    private boolean sameMove(Move m1, Move m2){
        return Arrays.equals(m1.getFrom(), m2.getFrom()) && Arrays.equals(m1.getToList().get(m1.getToList().size() - 1), m2.getToList().get(m2.getToList().size() - 1));
    }

    private double getScore(Object[][] board, int turn) {

        int num_white = 0, num_black = 0;
        //method checks if it's a W or L
        for (Object[] arr : board) {
            for (Object obj : arr) {
                if (obj != null) {
                    if(obj instanceof WhitePiece)
                        num_white++;
                    else if(obj instanceof BlackPiece)
                        num_black++;
                }
            }
        }

        System.out.println("num black = "+num_black+" and num white = "+num_white);

        if(num_black != 0 && num_white != 0){
            int numWhitePieces = 0;
            int numBlackPieces = 0;
            int numWhiteKings = 0;
            int numBlackKings = 0;

            for(Object[] arr : board){
                for(Object obj : arr){
                    if(obj != null){
                        if(obj instanceof BlackPiece){
                            if(((BlackPiece)obj).isKing())
                                numBlackKings++;
                            else
                                numBlackPieces++;
                        }
                        else if(obj instanceof WhitePiece){
                            if(((WhitePiece)obj).isKing())
                                numWhiteKings++;
                            else
                                numWhitePieces++;
                        }
                    }
                }
            }

            double sumWhitePoints = numWhitePieces + numWhiteKings*2;
            double sumBlackPoints = numBlackPieces + numBlackKings*2;

            if(Gamecontroller.playerOneAI){
                if(sumBlackPoints > sumWhitePoints)
                    return 1;
                else if(sumWhitePoints > sumBlackPoints)
                    return 0;
                return 0.5;
            }
            else if(Gamecontroller.playerTwoAI){
                if(sumWhitePoints > sumBlackPoints)
                    return 1;
                else if(sumBlackPoints > sumWhitePoints)
                    return 0;
                return 0.5;
            }
        }
        else if(num_black == 0){
            if(Gamecontroller.playerOneAI)
                return 0;
            return 1;
        }
        else{
            if(Gamecontroller.playerTwoAI)
                return 0;
            return 1;
        }
        System.out.println("we here ");
        return 0;
    }

    private boolean exists_one_of_each(Object[][] board) {
        int num_black = 0;
        int num_white = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i] != null) {
                    if (board[j][i] instanceof BlackPiece)
                        num_black++;
                    else if (board[j][i] instanceof WhitePiece)
                        num_white++;
                }
                if (num_black > 0 && num_white > 0)
                    return true;
            }
        }
        return false;
    }

    private boolean noPossibleMoves(Object[][] board) {
        for (Object[] arr : board) {
            for (Object obj : arr) {
                if (obj != null) {
                    if (((Piece) obj).getMoves().size() > 0)
                        return false;
                }
            }
        }
        return true;
    }

    private boolean gameOver(Object[][] board) {
        if (exists_one_of_each(board)) {
            return noPossibleMoves(board);
        } else
            return true;
    }

    private double UCB(mcNode node) {
        // UCB function is of the form: a+cb
        // where a = w/n  and  b = sqrt( (ln(N)/n )


        int w = node.num_wins;
        int n = node.num_simulations;
        double c = Math.sqrt(2);
        double N = node.parent.num_simulations;

        if(n == 0)
            return 999999;

        if(withRAVE){
            double ni = node.num_playouts_containing_this_move;
            double wi = node.num_playouts_won_with_this_move;

            double B = (ni)/(n+ni+4*2*n*ni);

            return ( (1-B)*(w/n) )+( B*(wi/ni) )+( c* Math.sqrt((Math.log(node.parent.num_simulations))/ni));

        }

        double a = w / n;
        double b = Math.sqrt((Math.log(N)) / n);

        if(this.withWeights){
            return 1.5086816547632376*a + 0.41318192411441723*b;
        }

        return 1.5086816547632376*a + 0.41318192411441723*b;

    }

    private Move getRandomBlackMove(Object[][] board){
        for(Object obj : board[7]){
            if(obj instanceof BlackPiece)
                ((BlackPiece)obj).makeKing();
        }
        AIController.calcBlackMoves(board, Gamecontroller.madeMoves);

        ArrayList<BlackPiece> MovableBlackPieces = new ArrayList<>();
        for (Object[] arr : board) {
            for (Object obj : arr) {
                if (obj != null) {
                    if (obj instanceof BlackPiece && ((Piece)obj).getMoves().size()!=0) //if it is a movable black piece
                        MovableBlackPieces.add((BlackPiece) obj);
                }
            }
        }// fill MovableBlackPieces


        if(MovableBlackPieces.size()==0)
            return null;

        ArrayList<BlackPiece> blackPiecesThatCanEat = new ArrayList<>();
        for(BlackPiece bp : MovableBlackPieces){
            for(Move m : bp.getMoves()){
                if(m.getRemoveList().size()!=0)
                    blackPiecesThatCanEat.add(bp);
            }
        }// fill blackPiecesThatCanEat

        ArrayList<BlackPiece> blackPiecesThatCanEatNoDuplicate = new ArrayList<>();
        for(BlackPiece bp : blackPiecesThatCanEat){
            if(!blackPiecesThatCanEatNoDuplicate.contains(bp))
                blackPiecesThatCanEatNoDuplicate.add(bp);
        }// fill blackPiecesThatCanEatNoDuplicate

        BlackPiece bp;
        if(blackPiecesThatCanEat.size() == 0) {
            bp = MovableBlackPieces.get(rng.nextInt(MovableBlackPieces.size()));
            return bp.getMoves().get(rng.nextInt(bp.getMoves().size()));
        }

        bp = blackPiecesThatCanEatNoDuplicate.get(rng.nextInt(blackPiecesThatCanEatNoDuplicate.size()));
        ArrayList<Move> bpMoves = new ArrayList<>(bp.getMoves());
        Move move = bpMoves.get(rng.nextInt(bpMoves.size()));
        while(move.getRemoveList().size()==0) {
            bpMoves.remove(move);
            move = bp.getMoves().get(rng.nextInt(bp.getMoves().size()));
        }

        Gamecontroller.madeMoves.add(move);
        if(Gamecontroller.madeMoves.size()==17)
            Gamecontroller.madeMoves.remove(0);

        return move;
    }

    private Move getRandomWhiteMove(Object[][] board){
        for(Object obj : board[0]){
            if(obj instanceof WhitePiece)
                ((WhitePiece)obj).makeKing();
        }
        AIController.calcWhiteMoves(board, Gamecontroller.madeMoves);

        ArrayList<WhitePiece> MovableWhitePieces = new ArrayList<>();
        for (Object[] arr : board) {
            for (Object obj : arr) {
                if (obj != null) {
                    if (obj instanceof WhitePiece && ((Piece)obj).getMoves().size()!=0)  //if it is a movable white piece
                        MovableWhitePieces.add((WhitePiece) obj);
                }
            }
        }// fill MovableWhitePieces

        if(MovableWhitePieces.size()==0)
            return null;

        ArrayList<WhitePiece> whitePiecesThatCanEat = new ArrayList<>();
        for(WhitePiece wp : MovableWhitePieces){
            for(Move m : wp.getMoves()){
                if(m.getRemoveList().size()!=0)
                    whitePiecesThatCanEat.add(wp);
            }
        }// fill whitePiecesThatCanEat


        ArrayList<WhitePiece> whitePiecesThatCanEatNoDuplicate = new ArrayList<>();
        for(WhitePiece wp : whitePiecesThatCanEat){
            if(!whitePiecesThatCanEatNoDuplicate.contains(wp))
                whitePiecesThatCanEatNoDuplicate.add(wp);
        }// fill whitePiecesThatCanEatNoDuplicate

        WhitePiece wp;
        if(whitePiecesThatCanEat.size() == 0) {
            wp = MovableWhitePieces.get(rng.nextInt(MovableWhitePieces.size()));
            return wp.getMoves().get(rng.nextInt(wp.getMoves().size()));
        }

        wp = whitePiecesThatCanEatNoDuplicate.get(rng.nextInt(whitePiecesThatCanEatNoDuplicate.size()));
        ArrayList<Move> wpMoves = new ArrayList<>(wp.getMoves());
        Move move = wpMoves.get(rng.nextInt(wpMoves.size()));
        while(move.getRemoveList().size()==0) {
            wpMoves.remove(move);
            move = wp.getMoves().get(rng.nextInt(wp.getMoves().size()));
        }

        Gamecontroller.madeMoves.add(move);
        if(Gamecontroller.madeMoves.size()==17)
            Gamecontroller.madeMoves.remove(0);

        return move;
    }
}