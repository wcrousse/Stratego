
package stratego2.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import stratego2.model.Color;
import stratego2.model.Piece;

/**
 *
 * @author roussew
 */
public class GameSquare extends javax.swing.JPanel {

    /**
     * the size of the a JLabel representation of a game piece.*
     */
    private static final Dimension lblSize = new Dimension(40, 40);
    /**
     * true if the square is not dead, false otherwise*
     */
    boolean isActive;
    /**
     * indicates the index of the row containing the square.*
     */
    int row;
    /**
     * indicates the index of the column containing the square.*
     */
    int column;
    /**
     * A representation of a game piece set this to visible if the square is
     * occupied, return to false when the square is vacated.
     *
     */
    JLabel lblPiece;

    /**
     * Creates new form GameSquare
     */
    public GameSquare(boolean isActive) {
        this.isActive = isActive;
        initComponents();
        setColor();
    }

    /**
     * This is the constructor that should typically be used.
     *
     * @param isActive
     * @param row
     * @param column
     */
    public GameSquare(boolean isActive, int row, int column) {
        this.isActive = isActive;
        this.row = row;
        this.column = column;
        lblPiece = new JLabel();
        add(lblPiece);
        lblPiece.setSize(lblSize);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */                         
    private void initComponents() {
        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setPreferredSize(new java.awt.Dimension(50, 50));
        setRequestFocusEnabled(false);
        setSize(new java.awt.Dimension(50, 50));  
        this.setLayout(new GridBagLayout());
    }                  

    /**
     * sets color based on whether or not the square is dead.
     */
    private void setColor() {
        if (isActive) {
            this.setBackground(java.awt.Color.white);
        } else {
            this.setBackground(java.awt.Color.lightGray);
        }
    }

    /**
     * places an icon representing a game piece on the square.
     * @param occupier a model of the piece to be placed.
     */
    void setPiece(Piece occupier) {
        lblPiece.setText("<html><center>"+occupier.getValue() + "<br>----</html>");
        lblPiece.setSize(40, 40);
        lblPiece.setFont(new Font("Georgia", Font.BOLD, 24));
        lblPiece.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
        lblPiece.setOpaque(true);
        lblPiece.setForeground(java.awt.Color.white);
        if (occupier.getColor() == Color.BLUE) {
            lblPiece.setBackground(java.awt.Color.blue);
        } else {
            lblPiece.setBackground(java.awt.Color.red);

        }
        lblPiece.setVisible(true);
        revalidate();
        repaint();
    }
}
