package stratego2.view;

/**
 * @author roussew
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import stratego2.model.Game;
import stratego2.model.Move;
import stratego2.model.Square;
import stratego2.model.StartPossitions;
import stratego2.model.StrategoBoard;

public class TextDisplay implements Display {

    @Override
    public void displayBoard(int[][] board) {
        System.out.println("------------------------------");
        for (int[] row : board) {
            for (int val : row) {
                if (val < 0 || val > 9) System.out.print(val + " | ");
                else System.out.print(val + "  | ");                       
            }
            System.out.println("------------------------------");
        }
    }

    @Override
    public Move getMove(int[][] board) {
        displayBoard(board);
        Scanner input = new Scanner(System.in);
        System.out.print("Enter piece to move in format: <row> <column>");
        int startRow = input.nextInt();
        int startColumn = input.nextInt();
        System.out.println("Enter destination: <row> <column>");
        int destRow = input.nextInt();
        int destColumn = input.nextInt();
        Move move = new Move(startRow, startColumn, destRow, destColumn);
        return move;
    }

    @Override
    public int[][] getSetup() {
        Scanner input = new Scanner(System.in);
        boolean gotInput = false;
        String fileName = null;
        while (!gotInput) {
            System.out.println("Please enter an integer in the range (0, 5), indicating\n"
                    + "which default start possition you would like to have");
            int positionNum = input.nextInt();

            switch (positionNum) {
                case 0:
                    fileName = StartPossitions.FLAG_LEFT1.fileName();
                    gotInput = true;
                    break;
                case 1:
                    fileName = StartPossitions.FLAG_RIGHT1.fileName();
                    gotInput = true;
                    break;
                case 2:
                    fileName = StartPossitions.FLAG_MIDDLE.fileName();
                    gotInput = true;
                    break;
                case 3:
                    fileName = StartPossitions.FLAG_LEFT2.fileName();
                    gotInput = true;
                    break;
                case 4:
                    fileName = StartPossitions.FLAG_RIGHT2.fileName();
                    gotInput = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
        int[][] startPossition = readPositionFile(fileName);
        return startPossition;
    }
    /**
     * TODO: Currently the path is a string literal. Fix this!
     * reads an input text file representing a player's starting position.
     * 
     * @param fileName the name of a textfile representing a player's start
     * position
     * @return a 2D integer representation of the player's starting position.
     */
    private int[][] readPositionFile(String fileName) {
        String path = "/Users/roussew/NetBeansProjects/Stratego2/StartPossitions";
        System.out.println(fileName);
        FileReader file = null;
        int numRows = Game.ARMY_SIZE / Game.NUM_COLUMNS;
        int[][] startPossition = new int[numRows][Game.NUM_COLUMNS];
        
        try {
            Scanner sc = new Scanner(new File(path + "/" + fileName));
            for(int i=0; i<numRows; i++) {
                for(int j=0; j<Game.NUM_COLUMNS; j++) {
                    if (sc.hasNext())
                        startPossition[i][j] = sc.nextInt();
                }
                System.out.println("let's look:");
                for (int[] row: startPossition) {
                    System.out.println(Arrays.toString(row));
                }
            }
            
        } catch(FileNotFoundException e) {
            System.err.print("File lost or corrupted");
            System.exit(-1);
        }
        return startPossition;
    }
    private void displayBoard(){
        
    }

    @Override
    public void reportIllegalMove() {
        System.out.println("Illegal move");
    }

    @Override
    public void revealSquare() {

    }
}

