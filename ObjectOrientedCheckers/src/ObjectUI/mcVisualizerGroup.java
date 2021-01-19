package ObjectUI;

import AIClasses.AIController;
import AIClasses.AIMoveToAction;
import Board.Board;
import Gamecontroller.Gamecontroller;
import MonteCarlo.mcNode;
import Move.Move;
import Piece.BlackPiece;
import Piece.Piece;
import Piece.WhitePiece;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.Random;

public class mcVisualizerGroup extends SmartGroup {

    public static mcVisualiserNode selectedNode;

    public mcNode root;
    int exp_turn = 0;

    public mcVisualizerGroup(){
        super();
        new Board();
        this.root = new mcNode(Board.board);
        visualizeTree(this.root, 0, 0);
    }

    public void mcVisExpand(){
        if(selectedNode != null){
            //get node contained in node
            //expand and visualize
            if(selectedNode.getMcnode().children.size() == 0) {
                expand(selectedNode.getMcnode());
                visualizeNode(selectedNode);
            }
        }
    }

    void visualizeTree(mcNode node, int depth, int childNum){
        mcVisualiserNode s = new mcVisualiserNode();

        s.setDepth(depth);
        s.setChildNum(childNum);
        s.setMcnode(node);

        s.setTranslateX(50 + 50*childNum*3);
        s.setTranslateY(50 + 50*depth*3);

        if(!getChildren().contains(s))
            getChildren().add(s);

        for(int i = 0; i < node.children.size(); i++)
            visualizeTree(node.children.get(i), depth+1, i);
    }

    void visualizeNode(mcVisualiserNode node){
        //for all children, visualize
        for(int i = 0; i < node.getMcnode().children.size(); i++){
            mcVisualiserNode s = new mcVisualiserNode();

            s.setDepth(node.getDepth()+1);
            s.setChildNum(i);
            s.setMcnode(node.getMcnode().children.get(i));
            node.children.add(s);

            s.setTranslateX(50 + 50*s.getChildNum()*3);
            s.setTranslateY(50 + 50*s.getDepth()*3);

            if(!getChildren().contains(s))
                getChildren().add(s);

        }
    }


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

    void printField(){
        printThisField(selectedNode.getMcnode().game_state);
        System.out.println("num of simulations = "+selectedNode.getMcnode().num_simulations);
        System.out.println("num of wins = "+selectedNode.getMcnode().num_wins);
    }

    void selectNode(){
        //get visNode from mcNode
        System.out.println("yz");
        mcNode snode = selection();
        for(Node node : getChildren()){
            if(node instanceof mcVisualiserNode){
                if(((mcVisualiserNode)node).getMcnode().equals(snode))
                    changeSelectedNode((mcVisualiserNode)node);
            }
        }
    }

    void changeSelectedNode(mcVisualiserNode node) {
        if(mcVisualizerGroup.selectedNode != null){
            PhongMaterial m = new PhongMaterial();
            m.setDiffuseColor(Color.GREY);
            mcVisualizerGroup.selectedNode.setMaterial(m);
        }
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.PURPLE);
        node.setMaterial(material);
        selectedNode = node;
    }

    private mcNode selection() {
        if(this.root.isLeaf())
            return this.root;
        return selection_method(this.root);
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

    public void simulate() {

        mcNode node = selectedNode.getMcnode();

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

                if(move != null){
                    Object[][] new_board = AIMoveToAction.AIAction(move, Gamecontroller.deepBoardCopy(board));
                    board = new_board;
                }
                else
                    break;
            }
            else if(sim_turn%2 == 1){
                Move move = getRandomWhiteMove(board);

                if(move != null){
                    Object[][] new_board = AIMoveToAction.AIAction(move, Gamecontroller.deepBoardCopy(board));
                    board = new_board;
                }
                else
                    break;
            }

            sim_turn=sim_turn+1;
            sim_count=sim_count+1;
        }

        printThisField(board);

        double score = getScore(board, sim_turn);
        System.out.println("score = "+score);

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

    private boolean gameOver(Object[][] board) {
        if (exists_one_of_each(board)) {
            return noPossibleMoves(board);
        } else
            return true;
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

    private Move getRandomBlackMove(Object[][] board){
        Random rng = new Random();
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

        Random rng = new Random();

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

    void printThisField(Object[][] field){
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_PURPLE = "\u001B[35m";

        System.out.println();
        System.out.println();

        for(Object[] arr: field){
            for(Object obj : arr)
            {
                if(obj instanceof BlackPiece) {
                    if(((Piece)obj).isKing())
                        System.out.print(ANSI_PURPLE + " B " + ANSI_RESET);
                    else
                        System.out.print(ANSI_RED + " B " + ANSI_RESET);
                }
                else if(obj instanceof WhitePiece) {
                    if(((Piece)obj).isKing())
                        System.out.print(ANSI_PURPLE + " W " + ANSI_RESET);
                    else
                        System.out.print(ANSI_GREEN + " W " + ANSI_RESET);
                }
                else if(obj == null)
                    System.out.print(" 0 ");
            }
            System.out.println();
        }

    }

    void deleteChildren(){
        if(selectedNode != null) {
            selectedNode.getMcnode().children = new ArrayList<>();
            for(mcVisualiserNode child : selectedNode.children)
                getChildren().remove(child);
        }
    }
}
