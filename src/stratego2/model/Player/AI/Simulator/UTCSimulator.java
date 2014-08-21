package stratego2.model.Player.AI.Simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import stratego2.model.Color;
import stratego2.model.FriendlyPiece;
import stratego2.model.Game;
import stratego2.model.GameState;
import stratego2.model.Move;
import stratego2.model.Rank;
import stratego2.model.Square;


/**
 *
 * @author roussew
 */
public class UTCSimulator implements Runnable {

    private static final int IMMOBILE_PIECE_INDEX
            = Game.ARMY_SIZE - Rank.BOMB.getCount() - Rank.FLAG.getCount();

    GameState startState;
    private int maxIterations;
    private SimPiece[][] board;
    private ArrayList<SimPiece> bluePieces, redPieces;
    private boolean gameOver;
    private Color winner;
    private Color toMove;
    private Color playerColor;
    private Random random;
    private int count;
    private double utility;
    

    public UTCSimulator(GameState state) {
        startState = state;
        maxIterations = Integer.MAX_VALUE;
        maxIterations = 100;
        board = new SimPiece[Game.NUM_ROWS][Game.NUM_COLUMNS];
        bluePieces = new ArrayList<>();
        redPieces = new ArrayList<>();
        toMove = state.getToMove();
        playerColor = state.getToMove();
        random = new Random();
    }

    @Override
    public void run() {
        initializeGame();
        count = 0;
        while (!gameOver && count++ < maxIterations) {
            getMove();
            toMove = (toMove == Color.BLUE)? Color.RED: Color.BLUE;                
        }
        Heuristic h = new Heuristic(board, playerColor, bluePieces, redPieces);
        utility = h.getUtility();
//        System.out.println(count);
       System.out.println(boardToString());
    }

    private void initializeGame() {
        for (Square square : startState) {
            if (square.isOccupied()) {
                FriendlyPiece fp = (FriendlyPiece) square.getOccupier();

                SimPiece sp = new SimPiece(
                        fp.getColor(), fp.getRank(), fp.getIsKnown(),
                        fp.getRow(), fp.getColumn()
                );

                if (sp.getColor() == Color.BLUE) {
                    bluePieces.add(sp);
                } else {
                    redPieces.add(sp);
                }
                board[fp.getRow()][fp.getColumn()] = sp;
            }
        }
    }

    private void getMove() {
        ArrayList<SimPiece> pieces = (toMove == Color.BLUE) ? bluePieces : redPieces;
        ArrayList<Move> possibleMoves = new ArrayList<>();

        for (SimPiece sp : pieces) {
            if (!addMoves(sp, possibleMoves)) {
                return;
            }

        }
        if (possibleMoves.isEmpty()) {
            winner = (toMove == Color.BLUE) ? Color.RED : Color.BLUE;
            gameOver = true;
        } else {
            int selection = random.nextInt(possibleMoves.size());
            makeMove(possibleMoves.get(selection));
        }
    }

    /**
     * adds the moves available to the provided piece to the list of possible
     * moves. if a move results in a battle and the defending piece is known,
     * the move will be added to the list if the battle is a draw, will be added
     * only if the move list was otherwise empty if the attacker looses. If the
     * attacker wins, the move will immediately be played and false will be
     * returned
     *
     * @param piece the piece whose available moves are to be found
     * @param moves the list of moves available to the player at the current
     * ply.
     * @return true if a move was not played, false otherwise.
     */
    private boolean addMoves(SimPiece piece, List<Move> moves) {
        if (!piece.isMobile()) {
            return true;
        }
        if (piece.getRank() == Rank.SCOUT) {
            return addScoutMoves(piece, moves);
        } else {
            int sRow = piece.getRow();
            int sCol = piece.getColumn();
            Move[] mvArray = {new Move(sRow, sCol, sRow - 1, sCol),
                new Move(sRow, sCol, sRow, sCol - 1), new Move(sRow, sCol, sRow + 1, sCol),
                new Move(sRow, sCol, sRow, sCol + 1)};

            for (Move m : mvArray) {
                if (m.getDestinationRow() >= 0
                        && m.getDestinationColumn() >= 0
                        && m.getDestinationRow() < board.length
                        && m.getDestinationColumn() < board.length) {
                    if (!addHelper(piece, m, moves)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean addHelper(SimPiece piece, Move move, List<Move> moves) {
        SimPiece p = board[move.getDestinationRow()][move.getDestinationColumn()];
        if (p == null) {
            moves.add(move);

        } else if (p.getColor() != Color.GREY && p.getColor() != toMove) {
            if (p.getRank() == Rank.FLAG) {
                gameOver = true;
                winner = toMove;
                utility = (winner == playerColor)? 1: -1;
                return false;
            }
            if (p.isKnown() && piece.compareTo(p) > 0) {
                makeMove(move);
                return false;
            } else if (piece.compareTo(p) == 0) {
                moves.add(move);
            } else if (moves.isEmpty()) {
                moves.add(move);
            }
        }
        return true;
    }

    private boolean addScoutMoves(SimPiece piece, List<Move> moves) {
        int destRow = piece.getRow() + 1;
        int destCol = piece.getColumn();
        int startRow = piece.getRow();
        int startCol = piece.getColumn();
        SimPiece next;
        while (destRow < board.length && board[destRow][destCol] == null) {
            moves.add(new Move(piece.getRow(), piece.getColumn(), destRow++, destCol));
        }
        Move move;
        if (destRow < board.length) {
            move = new Move(startRow, startCol, destRow, destCol);
            if (board[destRow][destCol].getColor() != toMove) {
                if (!addHelper(piece, move, moves)) {
                    return false;
                }
            }
        }
        destCol = startCol + 1;
        destRow = startRow;
        while (destCol < board.length && board[destRow][destCol] == null) {
            moves.add(new Move(piece.getRow(), piece.getColumn(), destRow, destCol++));
        }
        if (destCol < board.length) {
            move = new Move(startRow, startCol, destRow, destCol);
            if (board[destRow][destCol].getColor() != toMove) {
                if (!addHelper(piece, move, moves)) {
                    return false;
                }
            }
        }

        destCol = startCol;
        destRow = startRow - 1;
        while (destRow >= 0 && board[destRow][destCol] == null) {
            moves.add(new Move(piece.getRow(), piece.getColumn(), destRow--, destCol));
        }
        if (destRow >= 0) {
            move = new Move(startRow, startCol, destRow, destCol);
            if (board[destRow][destCol].getColor() != toMove) {
                if (!addHelper(piece, move, moves)) {
                    return false;
                }
            }
        }

        destCol = startCol - 1;
        destRow = startRow;
        while (destCol >= 0 && board[destRow][destCol] == null) {
            moves.add(new Move(piece.getRow(), piece.getColumn(), destRow, destCol--));
        }
        if (destCol >= 0) {
            move = new Move(startRow, startCol, destRow, destCol);
            if (board[destRow][destCol].getColor() != toMove) {
                if (!addHelper(piece, move, moves)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void makeMove(Move move) {
        int startRow, startColumn, endRow, endColumn;
        startRow = move.getStartRow();
        endRow = move.getDestinationRow();
        startColumn = move.getStartColumn();
        endColumn = move.getDestinationColumn();
        SimPiece piece = board[startRow][startColumn];
        
        
        if (board[endRow][endColumn] == null) {
            piece.setRow(endRow);
            piece.setColumn(endColumn);
            board[endRow][endColumn] = piece;
        } else {
            
            if(board[endRow][endColumn] == null)
                System.out.println();
            int comp = piece.compareTo(board[endRow][endColumn]);
            if (comp < 0) {
                if (piece.getColor() == Color.BLUE) {
                    bluePieces.remove(piece);
                }
            }
            if (comp == 0) {
                SimPiece killed = board[endRow][endColumn];
                if (killed.getColor() == Color.BLUE) {
                    bluePieces.remove(killed);
                    redPieces.remove(piece);
                } else {
                    redPieces.remove(killed);
                    bluePieces.remove(piece);
                }

            } else {
                SimPiece killed = board[endRow][endColumn];
                if (killed.getColor() == Color.BLUE) {
                    bluePieces.remove(killed);
                } else {
                    redPieces.remove(killed);
                }
                board[endRow][endColumn] = piece;
                piece.setRow(endRow);
                piece.setColumn(endColumn);
            }
        }
        board[startRow][startColumn] = null;

    }

    public Color getWinner() {
        return winner;
    }

    public int getDepth() {
        return count;
    }
    
    public double getUtility() {
        return utility;
    }
    
    public String boardToString() {
        StringBuilder builder = new StringBuilder();
        for(SimPiece[] array: board) {
            for(SimPiece s: array) {
                if(s == null) {
                    builder.append("|       |");
                }else {
                    builder.append("| "+s.toString()+ " |");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
}
