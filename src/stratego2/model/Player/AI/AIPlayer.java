

package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.FileParser;
import stratego2.model.FriendlyPiece;
import stratego2.model.Game;
import stratego2.model.GameState;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Player.Player;
import stratego2.model.Rank;
import stratego2.model.Square;
import stratego2.model.StartPossitions;
import stratego2.model.StrategoRules;

/**
 *
 * @author roussew
 */
public abstract class AIPlayer implements Player {
    Color color;
    protected final static StrategoRules rules = new StrategoRules();
    protected GameState state;
    protected ArrayList<MCSTNode> availableMoves;
    protected StrategoRules gameLogic;
    protected ArrayList<FriendlyPiece> army;
    protected Move lastMove;

    
    public AIPlayer() {
        availableMoves = new ArrayList<>();
    }
    
    public AIPlayer(Color color, StrategoRules rules) {
        army = new ArrayList<>();
        this.color = color;
    }

    @Override
    public void displayBoard() {
        //do nothing
    }

    @Override
    public Color getColor() {
        return color;
    }
    
    /**
     * generates a list of all of the moves available to the player at the given
     * game state.
     * @param state the current game state
     * @return the list of available actions.
     */
    public static ArrayList<MCSTNode> generateSucessors(GameState state) {
        List<FriendlyPiece> movablePieces = state.getMovablePieces();
        ArrayList<MCSTNode> possibleActions = getPossibleActions(movablePieces, state);
        return possibleActions;
    }
    
    static ArrayList<MCSTNode> getPossibleActions(List<FriendlyPiece> movablePieces, 
            GameState state) {
        ArrayList<MCSTNode> possibleActions = new ArrayList<>();
        for (FriendlyPiece p: movablePieces) {
            possibleActions.addAll(getPossibleActions(p, state));
        }
        return possibleActions;
    }
    
    static ArrayList<MCSTNode> getPossibleActions(FriendlyPiece piece, GameState state) {
        ArrayList<MCSTNode> possibleActions;
        if(piece.getRank() == Rank.SCOUT) {
            possibleActions = getScoutActions(piece, state);
            
        } else {
            possibleActions = new ArrayList<>();
            int startRow = piece.getRow();
            int startColumn = piece.getColumn();
            Move[] moves = new Move[4];
            moves[0] = new Move(startRow, startColumn, startRow, startColumn -1);
            moves[1] = new Move(startRow, startColumn, startRow, startColumn +1);
            moves[2] = new Move(startRow, startColumn, startRow-1, startColumn);
            moves[3] = new Move(startRow, startColumn, startRow+1, startColumn);
            
            for(Move move: moves) {
                if (rules.isLegal(state, move)) {
                    possibleActions.add(new MCSTNode(state, move));
                }
            }
        }
        return possibleActions;
    }
    
    static ArrayList<MCSTNode> getScoutActions(FriendlyPiece piece, GameState state) {
        int startRow = piece.getRow();
        int startColumn = piece.getColumn();
        ArrayList<MCSTNode> scoutActions = new ArrayList<>();
        for (int i=0; i<Game.NUM_ROWS; i++) {
            Move move = new Move(startRow, startColumn, i, startColumn);
            if (rules.isLegal(state, move)) {
                scoutActions.add(new MCSTNode(state, move));
            }
            move = new Move(startRow, startColumn, startRow, i);
            if (rules.isLegal(state, move)) {
                scoutActions.add(new MCSTNode(state, move));
            }
        }
        return scoutActions;
    }
    
    @Override
    public void reportMove(Move move) {
        int startR, startC, endR, endC;
        startR = move.getStartRow();
        startC = move.getStartColumn();
        endR = move.getDestinationRow();
        endC = move.getDestinationColumn();
        
        if(state.getSquare(endR, endC).isOccupied()) {
//            System.out.println("before move \n" + state);
            state = state.makeMove(move);
//            System.out.println("after move \n" + state);
        } else {
            state = state.makeMove(move);
        }
    }

    /**
     * should never be called. AI should not attempt illegal moves.
     * @throws Exception 
     */
    @Override
    public void reportIllegalMove() throws Exception{
        System.out.println(state);
        throw new Exception("AI made illegal move" );
    }

    @Override
    public void revealSquare(Square square, Move move) {
        Piece p = (square.isOccupied())? square.getOccupier(): null;
        int row = move.getDestinationRow();
        int column = move.getDestinationColumn();
        state = state.placePiece(row, column, p);
//        System.out.println("AI \nWinner= "+p);
//        System.out.println(this.state);
    }

    @Override
    public void reportResult(Color color) {
        
        System.out.println("winner = " + color.toString());
    }

    /**
     * stochastically selects a starting position. Currently a bit rough. 
     * ultimately we need to determine some limited number of starting position
     * features, and select them with a probability relative to their determined 
     * strengths, and randomize the remaining pieces. 
     * @return 
     */
    @Override
    public List<FriendlyPiece> getSetup() {
        int[][] simpleLayout = startupFromFile(1);
        for (int i = 0; i < simpleLayout.length; i++) {
            for (int j = 0; j < simpleLayout[i].length; j++) {
                int value = simpleLayout[i][j];
                Rank rank = Rank.getRank(value);
                FriendlyPiece p;
                if (this.color == Color.BLUE) {
                    p = new FriendlyPiece(color, i, j, rank);
                } else {
                    p = new FriendlyPiece(color, 9-i, 9-j, rank);
                }

                army.add(p);
            }
        }
        return army;
    }
    
    private int[][] startupFromFile(int setUpNum) {
        
        FileParser parser = new FileParser();
            String fileName;
            switch (setUpNum) {
                case 0:
                    fileName = StartPossitions.FLAG_LEFT1.fileName();
                    break;
                case 1:
                    fileName = StartPossitions.FLAG_RIGHT1.fileName();
                    break;
                case 2:
                    fileName = StartPossitions.FLAG_MIDDLE.fileName();
                    break;
                case 3:
                    fileName = StartPossitions.FLAG_LEFT2.fileName();
                    break;
                case 4:
                    fileName = StartPossitions.FLAG_RIGHT2.fileName();
                    break;
                default:
                    //appease the compiler
                    fileName = null;
            }

            return parser.readPositionFile(fileName);
    }
}
