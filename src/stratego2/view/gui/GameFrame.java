package stratego2.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import stratego2.model.Board;
import stratego2.model.FileParser;
import stratego2.model.FriendlyPiece;
import stratego2.model.Game;
import stratego2.model.Move;
import stratego2.model.Piece;
import stratego2.model.Square;
import stratego2.model.StartPossitions;
import stratego2.view.Display;

/**
 *
 * @author roussew
 *
 * This is the root container of the gui.
 */
public class GameFrame extends JFrame implements Display {

    GameSquare[][] squarePnls;
    /**
     * an integer matrix representation of a starting position.
     */
    private int[][] setup;
    JPanel board;
    /**
     * The color of pieces controlled by the owner of this view.*
     */
    stratego2.model.Color color;
    Move move;
    /**
     * true if the player who owns this view is currently to move. Check this in
     * the MouseListener method. Set this in the getMove() method.
     */
    private boolean isTurn;

    /**
     * true if a piece has been selected to move. should be set to true when the
     * player first clicks on a piece and false when a destination square is
     * clicked
     */
    private boolean isPieceSeclected;

    /**
     * true if a Move object has been fully initialized and is ready to be
     * returned should be set to true only by the MouseClicked listener once a
     * move has been completed. Should be set to false once a move has been
     * sent;
     */
    private boolean isMoveReady;

    /**
     * true if the player has finished setting up his/her pieces.
     */
    private boolean isSettupReady;

    private BoardListener boardListener;
    private boolean isInitailized;

    /**
     * creates a GameFrame. Most of the initialization happens in the
     * initComponents method.
     *
     * @param color the color of the pieces controlled by the owner of this
     * display.
     */
    public GameFrame(stratego2.model.Color color) {
        
        this.color = color;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        squarePnls = new GameSquare[Game.NUM_ROWS][Game.NUM_COLUMNS];
        boardListener = new BoardListener();
        //java.awt.EventQueue.invokeLater(() -> {
            initComponents();
        //});

    }

    public static void main(String[] args) {
        JFrame frame = new GameFrame(stratego2.model.Color.BLUE);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(1200, 1200);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * initializes the GUI components adds everything to the GameFrame.
     */
    private void initComponents() {
        JComponent contentpane = new JPanel(new BorderLayout());
        contentpane.setOpaque(true);
        this.setContentPane(contentpane);
        
        Dimension boardSize = new Dimension(500, 500);
        board = new JPanel();
        board.setPreferredSize(boardSize);
        board.setLayout(new GridLayout(10, 10));
        board.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < Game.NUM_ROWS; i++) {
            for (int j = 0; j < Game.NUM_COLUMNS; j++) {
                GameSquare gs = new GameSquare(true, i, j);
                squarePnls[i][j] = gs;
                board.add(gs);
                gs.setBackground(Color.YELLOW);
                gs.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                gs.setVisible(true);
                gs.addMouseListener(boardListener);
            }
        }
        contentpane.add(board, BorderLayout.CENTER);
        isInitailized = true;
        System.out.println("done initializing");
    }

    private synchronized void waitforSetup() {
        try {
            this.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int[][] getSetup() {
        SetupPanel setupPnl = new SetupPanel();
        add(setupPnl, BorderLayout.EAST);
        setupPnl.setVisible(true);
        pack();
        waitforSetup();
        remove(setupPnl);
        validate();
        repaint();
        return setup;
    }

    @Override
    public Move getMove(Board board) {
        move = null;
        isTurn = true;
        Thread moveGetter = new Thread() {
            @Override
            public void run() {
                waitForMove();
            }
        };
        moveGetter.start();
        try {
            moveGetter.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("returning move " + move);
        return move;
    }

    /**
     * to be called from the getMove() method. Waits until a Move has been fully
     * initialized and is ready to be sent back to the Player object.
     */
    private synchronized void waitForMove() {
        try {
            this.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void displayBoard(Board board) {
        //for testing only. replace with more sophisticated method.
        System.out.println("done initailizing? " + isInitailized);

        System.out.println("Initialization complete");
        for (int i = 0; i < Game.NUM_ROWS; i++) {
            for (int j = 0; j < Game.NUM_COLUMNS; j++) {
                Square squareModel = board.getSquare(i, j);
                GameSquare squareView = squarePnls[i][j];
                if (!squareModel.isActive()) {
                    squareView.isActive = false;
                    squareView.setBackground(Color.lightGray);

                } else if (squareModel.isOccupied()) {
                    Piece piece = squareModel.getOccupier();
                    if (piece.getColor() == color) {
                        squareView.setColor(this.color);
                    }
                    squareView.setPiece(squareModel.getOccupier());
                } else {
                    squareView.lblPiece.setVisible(false);
                }
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void reportIllegalMove() {
        System.out.println("illegal move pick again");
    }

    @Override
    public void revealSquare(Piece piece) {
        if (piece.getColor() != this.color) {
            int row = piece.getRow();
            int column = piece.getColumn();
            int value = ((FriendlyPiece)piece).getValue();
            GameSquare gs = squarePnls[row][column];
            new Thread() {
                @Override
                public void run() {
                    String originalText = gs.lblPiece.getText();
                    for (int i = 0; i < 5; i++) {
                        try {
                            gs.lblPiece.setText("<html><center>" + value
                                    + "<br>----</html>");
                            validate();
                            repaint();
                            TimeUnit.MILLISECONDS.sleep(200);
                            System.out.println("showit " + value);
                            gs.lblPiece.setText("<html><center>-----"
                                    + "<br>----</html>");
                            validate();
                            repaint();
                            TimeUnit.MILLISECONDS.sleep(200);
                            System.out.println(originalText);

                        } catch (InterruptedException ex) {
                            Logger.getLogger(
                                    GameFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void displayResults(stratego2.model.Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * A listener for mouse clicked events. To be used with the GameSquares.
     */
    class BoardListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            GameSquare gs = (GameSquare) e.getSource();
            System.out.println("mouse clicked in square"
                    + gs.column + ", " + gs.row);

            generateMove(gs);

        }
    }

    private void generateMove(GameSquare gs) {
        if (isTurn) {
            if (!isPieceSeclected) {
                move = new Move();
                move.setStartRow(gs.row);
                move.setStartColumn(gs.column);
                isPieceSeclected = true;
                System.out.println("move started " + move.getStartRow()
                        + move.getStartColumn());

            } else {
                synchronized (this) {
                    move.setDestinationRow(gs.row);
                    move.setDestinationColumn(gs.column);
                    isTurn = false;
                    isPieceSeclected = false;
                    System.out.println("move finished " + move.getDestinationColumn()
                            + move.getDestinationRow());
                    this.notifyAll();
                }
            }
        }
    }

    private synchronized void setSetup(int[][] setup) {
        this.setup = setup;
        this.notify();
    }

    class SetupPanel extends JPanel implements ActionListener {

        ButtonGroup group;

        SetupPanel() {
            super(new BorderLayout());
            group = new ButtonGroup();
            JPanel radioPanel = new JPanel(new GridLayout(0, 1));
            for (int i = 0; i < StartPossitions.numStartPossitions; i++) {
                JRadioButton rbutton = new JRadioButton(i + "");
                rbutton.setActionCommand(i + "");
                group.add(rbutton);
                radioPanel.add(rbutton);
            }
            add(radioPanel, BorderLayout.LINE_START);
            radioPanel.setVisible(true);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JButton btnSubmit = new JButton("submit");
            btnSubmit.addActionListener(this);
            add(btnSubmit, BorderLayout.LINE_END);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            FileParser parser = new FileParser();
            String fileName;
            ButtonModel btnmodel = group.getSelection();
            int setUpNum = Integer.parseInt(btnmodel.getActionCommand());
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

            int[][] setup = parser.readPositionFile(fileName);
            setSetup(setup);
        }
    }
}
