package MonteCarlo;

import AIClasses.AIMoveToAction;
import Board.Board;
import Gamecontroller.Gamecontroller;
import Move.Move;
import ObjectUI.Main;
import ObjectUI.Visual;
import Piece.BlackPiece;
import Piece.CheckersPiece;
import Piece.Piece;
import Piece.WhitePiece;
import javafx.scene.control.skin.CellSkinBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mcTreeSearch {

    private final mcNode root;
    private final Random rng = new Random();
    private int OGturn;

    public mcTreeSearch(Object[][] g_state) {
        this.root = new mcNode(g_state);
        this.selection_pointer = this.root;
    }
    public void setOGturn(int i){this.OGturn = i;}

    public Move getNextMove() {

        long start = System.currentTimeMillis();
        int TIME_LIMIT = 2000;
        long end = start + TIME_LIMIT;
        while (System.currentTimeMillis() < end) {
            mcNode node = selection(); //get best node
            expand(node); //expand the selected node
            simulate(node); //simulate and back propagate(update)
        }
        //check within children of root which one has best UCB
        mcNode best_child = this.root.children.get(0);
        double max = UCB(this.root.children.get(0));
        for (mcNode child : root.children) {
            if (UCB(child) > max) {
                best_child = child;
                max = UCB(child);
            }
        }

        Object[][] board_after_play = best_child.game_state;

        Move move = getMoveMadeByAI(this.root.game_state, board_after_play);
        return move;
    }

    private mcNode selection() {
        max_ucb = 0;
        recursive_selection(this.root);
        return selection_pointer;
    }

    private void expand(mcNode node) {
        for (Object[] a : node.game_state) {
            for (Object obj : a) {
                if (obj != null) {
                    //if ai plays as black turn
                    if (Gamecontroller.turn == 0) {
                        if (obj instanceof BlackPiece) {
                            calcBlackMoves(node.game_state);
                            for (Move m : ((BlackPiece) obj).getMoves()) {
                                Object[][] temp_board = Gamecontroller.deepBoardCopy(node.game_state);
                                temp_board = AIMoveToAction.AIAction(m, temp_board);
                                mcNode new_node = new mcNode(temp_board, node);
                                node.children.add(new_node);
                            }
                        }
                    }
                    //if ai plays as white
                    else {
                        if (obj instanceof WhitePiece) {
                            calcWhiteMoves(node.game_state);
                            if(((WhitePiece) obj).getMoves().size() != 0){
                                for (Move m : ((WhitePiece) obj).getMoves()) {
                                    Object[][] temp_board = Gamecontroller.deepBoardCopy(node.game_state);
                                    temp_board = AIMoveToAction.AIAction(m, temp_board);
                                    mcNode new_node = new mcNode(temp_board, node);
                                    node.children.add(new_node);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void simulate(mcNode node) {
        Object[][] board = node.game_state;//get a copy of board

        int turn = Gamecontroller.turn;
        int count = 0;
        while (!gameOver(board) && count < 50) {// look 50 moves ahead
            //if the player plays as black
            if (Gamecontroller.turn%2 == 0) {
                count++;
                BlackPiece piece = getRandomBPiece(board, true);// get a random movable black piece
                Move move;
                if(piece == null)//if there are pieces that can move then game over
                    count = 51;
                else
                {
                    move = piece.getMoves().get(rng.nextInt(piece.getMoves().size()));
                    board = AIMoveToAction.AIAction(move, board);
                }
                Gamecontroller.turn=1;
            }
            else if(Gamecontroller.turn%2 == 1){
                WhitePiece piece = getRandomWPiece(board, true);// get a random movable white piece
                Move move;
                if(piece == null)
                    count = 51;
                else
                {
                    move = piece.getMoves().get(rng.nextInt(piece.getMoves().size()));
                    board = AIMoveToAction.AIAction(move, board);
                }

                Gamecontroller.turn = 0;
            }
        }

        //after simulation, back propagate
        int score = getScore(board);
        backpropagation(node, score);
    }

    private void backpropagation(mcNode node, int score) {
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
    private int getScore(Object[][] board) {
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
        if(num_black > num_white && this.OGturn == 0)
            return 1;
        else if(num_white > num_black && this.OGturn == 1)
            return 1;
        else
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

        double a = w / n;
        double b = Math.sqrt((Math.log(N)) / n);

        return a + c * b;

    }

    private double max_ucb = 0;
    private mcNode selection_pointer;

    private void recursive_selection(mcNode node) {
        for (mcNode n : node.children) {
            if (n.isLeaf()) {
                if (UCB(n) > max_ucb) {
                    selection_pointer = n;
                    max_ucb = UCB(n);
                }
            } else
                recursive_selection(n);
        }
    }

    private boolean sameBoard(Object[][] b1, Object[][] b2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((b1[i][j] != null && b2[i][j] == null)
                        || (b1[i][j] == null && b2[i][j] != null))
                    return false;
            }
        }
        return true;
    }

    ArrayList<BlackPiece> blackPieces = new ArrayList<>();
    private BlackPiece getRandomBPiece(Object[][] board, boolean isFirstIteration)
    {
        if(blackPieces.size() == 0)
            return null;
        if(isFirstIteration)
        {
            blackPieces.clear();
            for (Object[] arr : board) {
                for (Object obj : arr) {
                    if (obj != null) {
                        if (((Piece) obj).getColour() == 0)
                            blackPieces.add((BlackPiece) obj);
                    }
                }
            }//fill black pieces list
        }
        BlackPiece bp = blackPieces.get(rng.nextInt(blackPieces.size()));
        if (bp.getMoves().size() == 0) {//if piece can't move
            blackPieces.remove(bp);//remove from list
            return getRandomBPiece(board, false);//recall function
        }
        else {
            return bp;
        }
    }

    ArrayList<WhitePiece> whitePieces = new ArrayList<>();
    private WhitePiece getRandomWPiece(Object[][] board, boolean isFirstIteration)
    {
        if(whitePieces.size() == 0)
            return null;
        if(isFirstIteration)
        {
            whitePieces.clear();
            for (Object[] arr : board) {
                for (Object obj : arr) {
                    if (obj != null) {
                        if (((Piece) obj).getColour() == 1)
                            whitePieces.add((WhitePiece) obj);
                    }
                }
            }

        }//fill white pieces list
        WhitePiece wp = whitePieces.get(rng.nextInt(whitePieces.size()));
        if (wp.getMoves().size() == 0) {//if piece can't move
            whitePieces.remove(wp);//remove from list
            return getRandomWPiece(board, false);//recall function
        }
        else {
            return wp;
        }
    }

    private Move getMoveMadeByAI(Object[][] board1, Object[][] board2)
    {
        //find moved piece
        CheckersPiece movedPiece = find_moved_piece(board1, board2);
        Move move = new Move(movedPiece);
        //find moved to where
        int[] moved_to = piece_moved_to(board1, board2);
        move.setTo(moved_to[0], moved_to[1]);
        //find eaten pieces
        move.setRemove(find_eaten_pieces(board1, board2));
        //find if piece becomes king
        move.becomesKing(find_if_piece_becomes_king(board1, board2));

        return move;
    }
    private CheckersPiece find_moved_piece(Object[][] board1, Object[][] board2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.OGturn == 0) {//if AI played as black
                    if (board1[i][j] != null && board2[i][j] == null && board1[i][j] instanceof BlackPiece) {
                        ((BlackPiece) board1[i][j]).setLocation(i, j);
                        return (BlackPiece) board1[i][j];
                    }
                } else {//if AI played as white
                    if (board1[i][j] != null && board2[i][j] == null && board1[i][j] instanceof WhitePiece) {
                        ((WhitePiece) board1[i][j]).setLocation(i, j);
                        return (WhitePiece) board1[i][j];
                    }
                }
            }
        }
        return null;
    }
    private int[] piece_moved_to(Object[][] board1, Object[][] board2){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(this.OGturn == 0) {//if AI plays as Black
                    if(board1[i][j] == null && board2[i][j] != null && board2[i][j] instanceof BlackPiece)
                        return new int[]{i, j};
                }
                else{
                    if(board1[i][j] == null && board2[i][j] != null && board2[i][j] instanceof WhitePiece) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }
    private ArrayList<Piece> find_eaten_pieces(Object[][] board1, Object[][] board2){
        ArrayList<Piece> eaten_pieces = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board2[i][j] == null && board1[i][j] != null){
                    if(this.OGturn%2 == 0 && board1[i][j] instanceof WhitePiece)
                        eaten_pieces.add((Piece) board1[i][j]);
                    else if(this.OGturn%2 == 1 && board1[i][j] instanceof BlackPiece){
                        eaten_pieces.add((Piece) board1[i][j]);
                    }
                }
            }
        }
        return eaten_pieces;
    }
    private boolean find_if_piece_becomes_king(Object[][] board1, Object[][] board2){
        if(this.OGturn%2 == 0){
            for(int j = 0; j < 8; j++) {
                if (board1[7][j] == null && board2[7][j] instanceof BlackPiece)
                    return true;
            }
        }
        else{
            for(int j = 0; j < 8; j++){
                if(board1[0][j] == null && board2[0][j] instanceof WhitePiece)
                    return true;
            }
        }
        return false;
    }

    public static void calcWhiteMoves(Object[][] field) {
        boolean captureMove = false;
        for(int i = 0; i <= field.length - 1; i++) {
            for(int j = 0; j <= field[0].length - 1; j++) {
                if(field[i][j] != null) {
                    if(((Piece) field[i][j]).getColour() == 1) {
                        ((Piece) field[i][j]).calcMoves();
                        List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
                        if(!captureMove) {
                            for (int k = 0; k <= holdMoveList.size() - 1; k++) {
                                if(holdMoveList.get(k).getRemoveList().size() != 0) {
                                    captureMove = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        ((Piece) field[i][j]).resetMoveList();
                    }
                }
            }
        }
        if(captureMove) {
            for(int i = 0; i <= field.length - 1; i++) {
                for(int j = 0; j <= field[0].length - 1; j++) {
                    if(field[i][j] != null) {
                        if(((Piece) field[i][j]).getColour() == 1) {
                            List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
                            for (int k = 0; k <= holdMoveList.size() - 1; k++) {
                                if(holdMoveList.get(k).getRemoveList().size() == 0) {
                                    ((Piece) field[i][j]).resetMoveList();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static void calcBlackMoves(Object[][] field) {
        boolean captureMove = false;
        for(int i = 0; i <= field.length - 1; i++) {
            for(int j = 0; j <= field[0].length - 1; j++) {
                if(field[i][j] != null) {
                    if(((Piece) field[i][j]).getColour() == 0) {
                        ((Piece) field[i][j]).calcMoves();
                        List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
                        if(!captureMove) {
                            for (int k = 0; k <= holdMoveList.size() - 1; k++) {
                                if(holdMoveList.get(k).getRemoveList().size() != 0) {
                                    captureMove = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        ((Piece) field[i][j]).resetMoveList();
                    }
                }
            }
        }
        if (captureMove) {
            for(int i = 0; i <= field.length - 1; i++) {
                for(int j = 0; j <= field[0].length - 1; j++) {
                    if(field[i][j] != null) {
                        if(((Piece) field[i][j]).getColour() == 0) {
                            List<Move> holdMoveList = ((Piece) field[i][j]).getMoves();
                            for (int k = 0; k <= holdMoveList.size() - 1; k++) {
                                if(holdMoveList.get(k).getRemoveList().size() == 0) {
                                    ((Piece) field[i][j]).resetMoveList();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}