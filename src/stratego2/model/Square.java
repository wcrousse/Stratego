package stratego2.model;

import java.util.Objects;

/**
 *
 * @author roussew
 */
public class Square {

    private Piece occupier;
    private final boolean isActive;


    public Square(boolean isActive) {
        this.isActive = isActive;
        occupier = null;
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
            return new Square(occupier.copy());
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

    /**
     * returns a deep copy of the piece occupying the square
     * @return a copy of the occupying piece
     */
    public Piece getOccupier() {
        return occupier.copy();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setOccupier(Piece piece) {
        occupier = piece;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.occupier);
        hash = 17 * hash + (this.isActive ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Square other = (Square) obj;
        
        if (this.isActive != other.isActive) {
            return false;
        }
        
        if (!Objects.equals(this.occupier, other.occupier)) {
            return false;
        }
        
        return true;
    }
    
    
}
