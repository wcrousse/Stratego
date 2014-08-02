package stratego2.model;

/**
 *
 * @author roussew
 */
public enum Rank {
    MARSHAL(10, 1), GENERAL(9, 1), COLONEL(8, 2), MAJOR(7, 3), CAPTAIN(6, 4), 
    LIEUTENANT(5, 4),  SERGEANT(4, 4), MINER(3, 5), SCOUT(2, 8), SPY(1, 1), 
    BOMB(11, 6), FLAG(12, 1);
    
    private final int value;
    private final int count;
    
    Rank(int value, int count) {
        this.value = value;
        this.count = count;
    }

    public int getValue() {
        return value;
    }
    
    public int getCount() {
        return count;
    }
    
    /**
     * gets a corresponding rank from and integer value.
     * @param value the integer value
     * @return a piece rank
     */
    public static Rank getRank(int value) {
        Rank rank = null;
        switch (value) {
            case 1: rank = SPY;
                    break;
            case 2: rank = SCOUT;
                    break;
            case 3: rank = MINER;
                    break;
            case 4: rank = SERGEANT;
                    break;
            case 5: rank = LIEUTENANT;
                    break;
            case 6: rank = CAPTAIN;
                    break;
            case 7: rank = MAJOR;
                    break;
            case 8: rank = COLONEL;
                    break;
            case 9: rank = GENERAL;
                    break;
            case 10: rank = MARSHAL;
                    break;
            case 11: rank = BOMB;
                    break;
            case 12: rank = FLAG;
                    break;
        }
        
        return rank;
    }
    
}
