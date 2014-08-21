

package stratego2.model.Player.AI.Simulator;

import java.util.ArrayList;
import java.util.List;
import stratego2.model.Color;
import stratego2.model.Rank;

/**
 *
 * @author roussew
 */
public class Heuristic {
    private Color player;
    private Color enemy;
    private SimPiece fFlag, eFlag;
    private SimPiece[][] state;
    private ArrayList<SimPiece> friendlyArmy, enemyArmy;

    public Heuristic(SimPiece[][] state, Color player, ArrayList<SimPiece>blueArmy, 
            ArrayList<SimPiece>redArmy) 
    {
        this.state = state;
        if (player == Color.BLUE){
            this.enemyArmy = redArmy;
            this.friendlyArmy = blueArmy;
            enemy = Color.RED;
        } else {
            enemyArmy = blueArmy;
            friendlyArmy = redArmy;
            enemy= Color.BLUE;
        }  
        this.player = player;
    }
    
    public double getUtility() {
        double value = 0;
        
        int[] friendlyCounts = getCounts(friendlyArmy);
        int[] enemyCounts = getCounts(enemyArmy);
        value = calculateValue(friendlyCounts, enemyCounts);
        
        if(value > 100) return 1;
        if (value < -100) return -1;
        else return value/100;
    }
    
    private int[] getCounts(List<SimPiece>army) {
        int[] counts = {0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (SimPiece p: army) {
            Rank rank = p.getRank();
            
            int val = rank.getValue();
            if (val > counts[0]) counts[0] = val;
            switch (rank) {
                case BOMB:          counts[11]++; break;
                case FLAG:          extractFlag(p); break;
                case SPY:           counts[1]++; break;
                case SCOUT:         counts[2]++; break;
                case MINER:         counts[3]++; break;
                case SERGEANT:      counts[4]++; break;
                case LIEUTENANT:    counts[5]++; break;
                case CAPTAIN:       counts[6]++; break;
                case MAJOR:         counts[7]++; break;
                case COLONEL:       counts[8]++; break;
                case GENERAL:       counts[9]++; break;
                case MARSHAL:       counts[10]++; break;
                
            }             
        }
        return counts;
    }
    
    private void extractFlag(SimPiece p) {
        if (p.getColor() == player) {
            fFlag = p;
        } else {
            eFlag = p;
        }
    }
    
    private double calculateValue(int[] fCounts, int[] eCounts) {
        int lowHighest = Math.min(fCounts[0], eCounts[0]);
        double val = 0;
        for (int i=lowHighest; i<11; i++) {
            val += fCounts[i] * 20;
            val -= eCounts[i] * 20;
        }
        for (int i=1; i < lowHighest; i++) {
            val += fCounts[i]*i;
            val -= eCounts[i]*i;
        }
        val += spyVal(fCounts, eCounts);
        val += mineVal(fCounts, eCounts);
        
        return val;
    }
       
    private double mineVal(int[] fCounts, int[] eCounts) {
        double fMineVal, eMineVal;
        if (eCounts[3] == 0) {
            if (flagMined(player)) {
                fMineVal = 100;
            } else {
                fMineVal = 11;
            }
        } else {
            fMineVal = 12/eCounts[3];
        }
        
        if (fCounts[3] == 0) {
            if (flagMined(enemy)) {
                eMineVal = 100;
            } else {
                eMineVal = 11;
            }
        } else {
            eMineVal = 12/fCounts[3];
        }
        
        double val = 0;
        val += fCounts[11]*fMineVal;
        val -= eCounts[11]*eMineVal;
        return val;
    }
    
    private boolean flagMined(Color color) {
        boolean isMinedOne = true;
        boolean isMinedTwo = true;
        int row, column;  
        SimPiece flag = (color == player)? fFlag: eFlag;
        row = flag.getRow();
        column = flag.getColumn();
        for (int i=-2; i<2; i++) {
            for (int j=-2; j<2; j++) {
                if(i >= 0 && i < state.length && j >= 0 && j < state[i].length) {
                    if (Math.abs(i+j) == 1 
                            && (state[i][j] == null || state[i][j].getRank() != Rank.BOMB)) 
                    {
                        isMinedOne = false;
                    }
                    if (Math.abs(j) + Math.abs(i) == 2 
                        && state[i][j].getRank() != Rank.BOMB)
                    {
                        isMinedTwo = false;
                    }
                }
            }
        }
        return isMinedTwo || isMinedOne;
    }
    
    private int spyVal(int[] fCounts, int[] eCounts) {
        int val = 0;
        if (fCounts[1] > 0) {
            if (eCounts[10] > 0) val += 5;
            else val += 1;
        }
        if (eCounts[1] > 0) {
            if (fCounts[10] > 0) val -= 5;
            else val -= 1;
        }
        return val;
    }
    
    
}
