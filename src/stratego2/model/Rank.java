package stratego2.model;

/**
 *
 * @author roussew
 */
public enum Rank {
    MARSHAL(10), GENERAL(9), COLONEL(8), MAJOR(7), CAPTAIN(6), LIEUTENANT(5),
    SERGEANT(4), MINER(3), SCOUT(2), SPY(1), BOMB(11), FLAG(12);
    
    private final int value;
    
    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
