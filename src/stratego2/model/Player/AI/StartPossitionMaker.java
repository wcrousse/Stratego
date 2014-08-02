package stratego2.model.Player.AI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import stratego2.model.Color;
import stratego2.model.Game;
import stratego2.model.Rank;

/**
 *
 * @author roussew
 */
public class StartPossitionMaker {

    Scanner scanner;
    private Color enemyColor;

    public StartPossitionMaker(Color enemyColor) {
        this.enemyColor = enemyColor;
    }

    private ArrayList<EnemyPiece> getBeliefState() {
        ArrayList<EnemyPiece> army = new ArrayList<>();
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("results.txt")));

            setScannerStart();
            int c = 0;
            for (int i = 0; i < Game.NUM_ROWS; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.println(i*10+j);
                    EnemyPiece p = createPiece(i, j);
                    army.add(p);
                    while (!scanner.hasNextDouble() && scanner.hasNext()) {
                        scanner.next();
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartPossitionMaker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            scanner.close();
        };

        return army;
    }

    private void setScannerStart() {
        String key = (enemyColor == Color.BLUE) ? "BLUE" : "RED";
        //advance to the propper player possition
        while (!scanner.next().equalsIgnoreCase(key));
        scanner.nextLine();
        //advance to the first probability;
        while (!scanner.hasNextDouble()) {
            scanner.next();
        }
    }

    private EnemyPiece createPiece(int row, int column) {
        ProbabilityDistribution distribution = new ProbabilityDistribution();
        for (Rank rank : Rank.values()) {
            distribution.setProb(rank, scanner.nextDouble());
        }

        return new EnemyPiece(distribution, enemyColor, row, column);
    }
    
    public static void main(String[] args) {
        StartPossitionMaker tester = new StartPossitionMaker(Color.BLUE);
        for (EnemyPiece e: tester.getBeliefState()) System.out.println(e);
    }
}
