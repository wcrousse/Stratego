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
import stratego2.model.Rank;
import stratego2.model.Square;
import stratego2.model.StartPossitions;
import stratego2.model.StrategoRules;

/**
 *
 * @author roussew
 */
public class UTCPlayer extends AIPlayer {

    private MCSTNode actions;
    private MCSTNode tree;
    public UTCPlayer(Color color) {
        this.color = color;
    }

    public UTCPlayer(Color color, StrategoRules rules) {
        super(color, rules);
    }

    @Override
    public Move getMove() {
        actions.expand();
        for(int i = 0; i < 5; i++) {
            tree = getSampleState();
        }
    }

    public void processMove(Move move) {
        Piece piece = movedPiece(move);
        piece = piece.setLocation(move.getDestinationRow(), move.getDestinationColumn());
        state.makeMove(move);
    }

    private Piece movedPiece(Move move) {
        return state.getSquare(move.getStartRow(), move.getStartColumn()).getOccupier();
    }

    @Override
    public void reportMove(Move move) {
        Square start = state.getSquare(move.getStartRow(), move.getStartColumn());
        Piece piece = start.getOccupier();
        state.makeMove(move);
    }

    /**
     * stochastically selects a starting position. Currently a bit rough. 
     * ultimately we need to determine some limited number of starting position
     * features, and select them with a probability relative to their determined 
     * strengths, and randomize the remaining peaces. 
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

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void revealSquare(Square square) { 
        FriendlyPiece winner = (FriendlyPiece)square.getOccupier();
        if (winner.getColor() == color) {
            state.placePiece(winner.getRow(), winner.getColumn(), winner);
        } else {
            square = state.getSquare(winner.getRow(), winner.getColumn());
            EnemyPiece ep = (EnemyPiece)square.getOccupier();
            ep.pieceRevealed(winner.getRank());
        }
    }

    @Override
    public void reportResult(Color color) {
        //do nothing for now
    }

    class Location {

        int row;
        int column;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + this.row;
            hash = 17 * hash + this.column;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Location other = (Location) obj;
            if (this.row != other.row) {
                return false;
            }
            if (this.column != other.column) {
                return false;
            }
            return true;
        }

    }
}
