package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.Iterator;
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
import stratego2.model.Simulator;
import stratego2.model.Square;
import stratego2.model.StartPossitions;
import stratego2.model.StrategoRules;

/**
 *
 * @author roussew
 */
public class UTCPlayer extends AIPlayer {

    private static final double EPSILON = 0.0001;
    private static final double UTC_CONSTANT = 0.5;
    private MCSTNode actions;
    private MCSTNode root;
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
            root = new MCSTNode(getSampleState());
            root.expand();
            for(int j=0; j<1000; j++) {
                MCSTNode next = treePolicy(root);
                double payout = defaultPolicy(next);
                backup(next, payout);
            }
        }
        return null;
    }
    
    private GameState getSampleState() {
        ArrayList<EnemyPiece>enemyPieces = new ArrayList<>();
        for (Square s: state) {
            if (s.isOccupied() && s.getOccupier() instanceof EnemyPiece) {
                EnemyPiece ep = (EnemyPiece)s.getOccupier();
                enemyPieces.add(ep);
            }
        }
        ArrayList<FriendlyPiece>convertedPieces = convertPieces(enemyPieces);
        return new GameState(state.getToMove(), army, convertedPieces);
    }
    
    private ArrayList<FriendlyPiece> convertPieces(ArrayList<EnemyPiece> enemy) {
        ArrayList<FriendlyPiece> convertedPieces = new ArrayList<>();
        for (EnemyPiece p: enemy) {
            Rank rank = determineRank(p);
            revealRank(p, rank, enemy);
            convertedPieces.add(
                    new FriendlyPiece(p.getColor(), p.getRow(), p.getColumn(), rank));
        }
        return convertedPieces;
    }
    
    private void revealRank(EnemyPiece piece, Rank rank, List<EnemyPiece> army) {
        ProbabilityDistribution distribution = piece.getDistribution();
        distribution.setProb(rank, 1);
        for (Rank r: Rank.values()) {
            if (r != rank) {
                distribution.setProb(r, 0);
            }
        }
        int numAvailable = rank.getCount();
        for (EnemyPiece ep: army) {
            ProbabilityDistribution dist = ep.getDistribution();
            if (Math.abs(dist.getProb(rank) - 1) < EPSILON) {
                numAvailable--;
            }
        }
        
        for (EnemyPiece ep: army) {
            double scale = numAvailable/(numAvailable + 1);
            ProbabilityDistribution dist = ep.getDistribution();
            dist.setProb(rank, scale*dist.getProb(rank));
            normalize(dist, rank);
        }
    }
    
    private void normalize(ProbabilityDistribution dist, Rank modified) {
        double total = 0;
        for(Rank r: Rank.values()) {
            if (r != modified) {
                total += dist.getProb(r);
            }
        }
        double scale = (1 - dist.getProb(modified))/total;
        
        for(Rank r: Rank.values()) {
            if (r != modified) {
                dist.setProb(r, scale * dist.getProb(r));
            }
        }
    }
    
    private Rank determineRank(EnemyPiece p) {
        double n = Math.random();
        ProbabilityDistribution distribution = p.getDistribution();
        double upper = 0;
        Rank returnVal = null;
        for(Rank r: Rank.values()) {
            upper += distribution.getProb(r);
            if (n <= upper){
                returnVal = r;
                break;
            }
        } 
        return returnVal;
    }
    
    private MCSTNode treePolicy(MCSTNode root) {
        if (root.isLeaf()) return root;
        else {
            double bestValue = 0;
            MCSTNode bestNode = null;
            for (MCSTNode child: root.getChildren()) {
                double explorationFactor = 
                        (child.getCount() == 0)? Double.POSITIVE_INFINITY :
                        UTC_CONSTANT * Math.sqrt(Math.log(root.getCount()) / child.getCount());
                double val = child.getQvalue() + explorationFactor;
                if (val > bestValue) {
                    bestValue = val;
                    bestNode = child;
                }
            }
            return treePolicy(bestNode);
        }
    }
    
    private double defaultPolicy(MCSTNode node) {
        Simulator simulator = new Simulator(node.getState(), 
                new DefaultPlayer(Color.RED, rules, node.getState()),
                new DefaultPlayer(Color.BLUE, rules, node.getState()));
            new Thread(simulator).start();
            return (simulator.getResult() == color)? 1: -1;
    }
    private void backup(MCSTNode node, double result) {
        if (node.getState().getToMove() != color) {
            result = -result;
        }
        do {
            if (node.getState().getToMove() != color)
            node.incrementCount();
            node.updateQValue(result);
            if (!node.isRoot()) node = node.getParent();
            result = -result;
        } while(!node.isRoot());
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

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void revealSquare(Square square) { 
        FriendlyPiece winner = (FriendlyPiece)square.getOccupier();
        state.placePiece(winner.getRow(), winner.getColumn(), winner);
        if (winner.getColor() == color) {
            
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
