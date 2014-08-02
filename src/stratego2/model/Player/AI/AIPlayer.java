

package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.FriendlyPiece;
import stratego2.model.Game;
import stratego2.model.GameState;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Player.Player;
import stratego2.model.Rank;
import stratego2.model.Square;
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
    protected static ArrayList<MCSTNode> generateSucessors(GameState state) {
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
                if (rules.isLegal(state, piece, move.getDestinationRow(),
                        move.getDestinationColumn())) {
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
            if (rules.isLegal(state, piece, i, startColumn)) {
                Move move = new Move(startRow, startColumn, i, startColumn);
                scoutActions.add(new MCSTNode(state, move));
            }
            if (rules.isLegal(state, piece, startRow, i)) {
                Move move = new Move(startRow, startColumn, startRow, i);
                scoutActions.add(new MCSTNode(state, move));
            }
        }
        return scoutActions;
    }

    /**
     * should never be called. AI should not attempt illegal moves.
     * @throws Exception 
     */
    @Override
    public void reportIllegalMove() throws Exception{
        throw new Exception("AI made illegal move" + lastMove.toString());
    }

    @Override
    public void revealSquare(Square square) {
        System.out.println(square);
    }

    @Override
    public void reportResult(Color color) {
        
        System.out.println("winner = " + color.toString());
    }

    
}
