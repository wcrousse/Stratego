package stratego2.model;

/**
 *
 * @author roussew
 */
public enum StartPossitions {
    FLAG_RIGHT1 ("rightCorner1.txt"),
    FLAG_LEFT1 ("leftCorner1.txt"),
    FLAG_MIDDLE ("middle1.txt"), 
    FLAG_RIGHT2 ("rightCorner2.txt"),
    FLAG_LEFT2 ("leftCorner2.txt");
    
    private final String fileName;
    
    StartPossitions(String fileName) {
        this.fileName = fileName;
    }
    
    public String fileName() {
        return fileName;
    }
}
