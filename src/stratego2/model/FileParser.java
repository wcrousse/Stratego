

package stratego2.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author roussew
 */
public class FileParser {

    public int[][] readPositionFile(String fileName) {
        String path = "/Users/roussew/NetBeansProjects/Stratego2/StartPossitions";
        System.out.println(fileName);
        FileReader file = null;
        int numRows = Game.ARMY_SIZE / Game.NUM_COLUMNS;
        int[][] startPossition = new int[numRows][Game.NUM_COLUMNS];

        try {
            Scanner sc = new Scanner(new File(path + "/" + fileName));
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < Game.NUM_COLUMNS; j++) {
                    if (sc.hasNext()) {
                        startPossition[i][j] = sc.nextInt();
                    }
                }
                System.out.println("let's look:");
                for (int[] row : startPossition) {
                    System.out.println(Arrays.toString(row));
                }
            }

        } catch (FileNotFoundException e) {
            System.err.print("File lost or corrupted");
            System.exit(-1);
        }
        return startPossition;
    }
}
