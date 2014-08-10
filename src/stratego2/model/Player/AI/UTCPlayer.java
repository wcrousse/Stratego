package stratego2.model.Player.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import stratego2.model.Color;
import stratego2.model.FriendlyPiece;
import stratego2.model.GameState;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Rank;
import stratego2.model.Simulator;
import stratego2.model.Square;
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
        for (int i = 0; i < 5; i++) {
            root = new MCSTNode(getSampleState());
            root.expand();
            for (int j = 0; j < 5; j++) {
                MCSTNode next = treePolicy(root);
                double payout = defaultPolicy(next);
                backup(next, payout);
            }
        }
        return null;
    }

    private GameState getSampleState() {
        ArrayList<EnemyPiece> enemyPieces = new ArrayList<>();
        for (Square s : state) {
            if (s.isOccupied() && s.getOccupier() instanceof EnemyPiece) {
                EnemyPiece ep = (EnemyPiece) s.getOccupier();
                enemyPieces.add(ep);

            }
        }
        ArrayList<FriendlyPiece> convertedPieces = convertPieces(enemyPieces);
        return new GameState(state.getToMove(), army, convertedPieces);
    }

    private ArrayList<FriendlyPiece> convertPieces(ArrayList<EnemyPiece> enemy) {
        ArrayList<FriendlyPiece> convertedPieces = new ArrayList<>();
        for (EnemyPiece p : enemy) {
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
        for (Rank r : Rank.values()) {
            if (r != rank) {
                distribution.setProb(r, 0);
            }
        }
        int numAvailable = rank.getCount();
        for (EnemyPiece ep : army) {
            ProbabilityDistribution dist = ep.getDistribution();
            if (Math.abs(dist.getProb(rank) - 1) < EPSILON) {
                numAvailable--;
            }
        }
        System.out.println("rank: " + rank + "numLeft: " + numAvailable);
        double scale = 0;
        if (numAvailable != 0) scale = (double)numAvailable / (numAvailable + 1);
        for (EnemyPiece ep : army) {
            if(ep == piece) break;
            ProbabilityDistribution dist = ep.getDistribution();
            dist.setProb(rank, scale * dist.getProb(rank));
            try {
                normalize(dist, rank);
            } catch (Exception ex) {
                Logger.getLogger(UTCPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void normalize(ProbabilityDistribution dist, Rank modified) throws Exception {
        double total = 0;
      
        for (Rank r : Rank.values()) {
            if (r != modified) {
                total += dist.getProb(r);
            }
        }
        if (Math.abs(total) < EPSILON) {
            return;
        }
   
        double scale = (1 - dist.getProb(modified)) / total;
        
            for (Rank r : Rank.values()) {
                if (r != modified) {
                    dist.setProb(r, scale * dist.getProb(r));
                }
            }
        
        total = 0;
        for (Rank r : Rank.values()) {
            total += dist.getProb(r);
        }
        if (Math.abs(total - 1) > EPSILON) {
            throw new IllegalStateException("normilization falure " + total);
        }
    }

    private Rank determineRank(EnemyPiece p) {
        double n = Math.random();
        ProbabilityDistribution distribution = p.getDistribution();
        double upper = 0;
        Rank returnVal = null;
        for (Rank r : Rank.values()) {
            upper += distribution.getProb(r);
            if (Double.isNaN(upper)) {
                System.out.print("this is it " + r);
            }
            if (n <= upper) {
                returnVal = r;
                break;
            }
        }
        return returnVal;
    }

    private MCSTNode treePolicy(MCSTNode node) {
        if (node.isLeaf()) {
            return node;
        } else {
            double bestValue = 0;
            MCSTNode bestNode = null;
            for (MCSTNode child : node.getChildren()) {
                double explorationFactor
                        = (child.getCount() == 0) ? Double.POSITIVE_INFINITY
                        : UTC_CONSTANT * Math.sqrt(Math.log(node.getCount()) / child.getCount());
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
        try {
            Thread t = new Thread(simulator);
            t.start();       
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(UTCPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (simulator.getResult() == color) ? 1 : -1;
    }

    private void backup(MCSTNode node, double result) {
        if (node.getState().getToMove() != color) {
            result = -result;
        }
        do {
            if (node.getState().getToMove() != color) {
                node.incrementCount();
            }
            node.updateQValue(result);
            if (!node.isRoot()) {
                node = node.getParent();
            }
            result = -result;
        } while (!node.isRoot());
    }

    @Override
    public void reportMove(Move move) {
        super.reportMove(move);
        Piece movedPiece; 
        Square endSquare = state.getSquare(move.getDestinationRow(), move.getDestinationColumn());
        movedPiece = endSquare.getOccupier();
        if(movedPiece.getColor() != color)
        actions = new MCSTNode(state, actions);
    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void revealSquare(Square square) {
        Piece winner = square.getOccupier();
        int row = winner.getRow();
        int column = winner.getColumn();
  //      state = state.placePiece(row, column, winner);
        if (winner.getColor() != color) {
            square = state.getSquare(winner.getRow(), winner.getColumn());
            EnemyPiece ep = (EnemyPiece) square.getOccupier();
   //         ep.pieceRevealed(winner.getRank());
        }
    }

    @Override
    public List<FriendlyPiece> getSetup() {
        List<FriendlyPiece> friendlyArmy = super.getSetup();
        Color enemyColor = (this.color == Color.BLUE) ? Color.RED : Color.BLUE;
        StartPossitionMaker maker = new StartPossitionMaker(enemyColor);
        List<EnemyPiece> enemyArmy = maker.getBeliefState();
        List<? extends Piece> blueArmy, redArmy;
        if (color == Color.BLUE) {
            blueArmy = friendlyArmy;
            redArmy = enemyArmy;
        } else {
            blueArmy = enemyArmy;
            redArmy = friendlyArmy;
        }
        state = new GameState(Color.BLUE, blueArmy, redArmy);
        actions = new MCSTNode(state);
        return friendlyArmy;
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
