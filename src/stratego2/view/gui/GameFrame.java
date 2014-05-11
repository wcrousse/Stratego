

package stratego2.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import stratego2.model.Board;
import stratego2.model.Game;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.view.Display;

/**
 *
 * @author roussew
 */
public class GameFrame extends JFrame implements Display {
    JPanel[][] squarePnls;
    JPanel board;
    
    public GameFrame() {
        squarePnls = new GameSquare[Game.NUM_ROWS][Game.NUM_COLUMNS];
        initComponents();
        
        
    }
    
    public static void main(String[] args) {
        JFrame frame = new GameFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(1200, 1200);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initComponents() {
        Dimension boardSize = new Dimension(1000, 1000); 
        board = new JPanel();
        board.setPreferredSize(boardSize);
        board.setLayout(new GridLayout(10, 10));
        board.setBounds(0, 0, boardSize.width, boardSize.height);
        
        for(int i=0; i<Game.NUM_ROWS; i++) {
            for(int j=0; j<Game.NUM_COLUMNS; j++) {
                GameSquare gs = new GameSquare(true, i, j);
                squarePnls[i][j] = gs;
                board.add(gs);
                gs.setBackground(Color.YELLOW);
                gs.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                gs.setVisible(true);
            }
        }
        add(board);
    }

    @Override
    public int[][] getSetup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Move getMove(Board board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayBoard(Board board) {
        
    }

    @Override
    public void reportIllegalMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void revealSquare(Piece piece) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
