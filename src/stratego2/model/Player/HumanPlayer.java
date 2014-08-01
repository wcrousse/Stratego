package stratego2.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import stratego2.model.Board;
import stratego2.model.Color;
import stratego2.model.ControllerInterface;
import stratego2.model.FriendlyPiece;
import stratego2.model.Game;
import stratego2.model.GameState;
import stratego2.model.HumanEnemyPiece;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Player.AI.EnemyPiece;
import stratego2.model.Player.Player;
import stratego2.model.Rank;
import stratego2.model.Square;
import stratego2.view.Display;
import stratego2.view.gui.GameFrame;

/**
 *
 * @author roussew
 */
public class HumanPlayer implements Player {

    private List<FriendlyPiece> army;
    private List<HumanEnemyPiece> enemyArmy;
    private ControllerInterface controller;
    private Color color;
    private Display view;
    private GameState state;

    public HumanPlayer(Color color, Display view) {
        this.color = color;
        this.view = view;
        army = new ArrayList<>();
        enemyArmy = new ArrayList<>();
    }

    /**
     * gets a data structure representing a Stratego move. The Move object
     * returned contains no Piece object, only coordinates of the piece. The
     * method is ensured to return a start square that contains a piece. No
     * further guarantees are made as to the legality of the proposed move
     * however. These checks are the responsibility of the calling class.
     *
     * @param board a representation of the game state just prior to the move to
     * be made
     * @return a Move object, a simple data structure which has fields and
     * accessors startRow, StartColumn, destinationRow, and destination column,
     * indicating, the location of the piece intended to be moved before and
     * after the move is made.
     *
     */
    @Override
    public Move getMove() {
        Move move = null;
        view.displayBoard(state);
        //if start square does not contain a piece
        do {
            move = view.getMove(state);
        } while (!state.getSquare(move.getStartRow(), move.getStartColumn()).isOccupied());
        return move;
    }

    /**
     * retrieves an integer array representation of initial army positions. uses
     * it to generate army.
     *
     * @return a list of Pieces having
     */
    public List<FriendlyPiece> getSetup() {
        int[][] simpleLayout = view.getSetup();
        for (int i = 0; i < simpleLayout.length; i++) {
            for (int j = 0; j < simpleLayout[i].length; j++) {
                int value = simpleLayout[i][j];
                Rank rank = Rank.getRank(value);
                FriendlyPiece p;
                if (this.color == Color.BLUE) {
                    p = new FriendlyPiece(color, i, j, rank);
                } else {
                    p = new FriendlyPiece(color, 9 - i, 9 - j, rank);
                }

                army.add(p);
            }
        }

        initializeState();
        return army;
    }

    private void initializeState() {
        List<? extends Piece> blueArmy, redArmy;
        if (this.color == Color.BLUE) {
            blueArmy = army;
            redArmy = enemyArmy;
        }else {
            redArmy = army;
            blueArmy = enemyArmy;
        }
        
        for (int i = 0; i < Game.NUM_ROWS; i++) {
            for (int j = 0; j < Game.NUM_COLUMNS; j++) {
                if (this.color == Color.BLUE) {
                    enemyArmy.add(new HumanEnemyPiece(Color.RED, i, j));
                } else {
                    enemyArmy.add(new HumanEnemyPiece(Color.BLUE, 9 - i, 9 - j));
                }
            }
        }
        state = new GameState(Color.RED, redArmy, blueArmy);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void reportIllegalMove() {
        view.reportIllegalMove();
    }

    @Override
    public void revealSquare(Square square) {
        if (square.isOccupied() && square.getOccupier().getColor() != color) {
            view.revealSquare(square.getOccupier());
        }
    }

    @Override
    public void displayBoard() {
        view.displayBoard(state);
    }

    void setDisplay(GameFrame gameFrame) {
        this.view = gameFrame;
    }

    @Override
    public void reportResult(Color color) {
        view.displayResults(color);
    }

    @Override
    public void reportMove(Move move) {
         state = state.makeMove(move);
    }

}
