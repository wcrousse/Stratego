package stratego2.model;

/**
 *
 * @author roussew
 */
public class Square {

    private Piece occupier;
    private boolean isActive;

    public Square() {
    }

    public Square(boolean isActive) {
        this.isActive = isActive;
    }

    public Square(Piece piece) {
        this.isActive = true;
        this.occupier = piece;
    }

    public String toString() {
        if (!isActive) {
            return " - ";
        } else if (occupier != null) {
            return occupier.toString();
        } else {
            return "[ ]";
        }
    }

    /**
     * provides a deep copy of the square in question.
     *
     * @return a copy of the square whose copy method is called
     */
    public Square copy() {
        if (isOccupied()) {
            return new Square(new Piece(occupier));
        }
        return new Square(isActive);
    }

    /**
     * returns true if the square is currently occupied by a piece and false
     * otherwise
     *
     * @return a boolean indicating whether the square is occupied or not.
     */
    public boolean isOccupied() {
        return !(occupier == null);
    }

    public Piece getOccupier() {
        return occupier;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setOccupier(Piece piece) {
        this.occupier = piece;
    }
}
