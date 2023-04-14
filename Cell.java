import java.io.Serializable;
import java.util.EnumMap;

// Classe représentant une cellule individuelle sur le plateau de jeu du démineur.

public class Cell implements Serializable {

    public  EnumMap<Direction, Cell> neighbors;
    private boolean                  mine;
    private boolean                  marked;
    private boolean                  uncovered;

    public Cell() {

        // EnumMap stockant les voisins de cette cellule dans chaque direction.

        neighbors = new EnumMap<>(Direction.class);
        mine = false;
        marked = false;
        uncovered = false;
    }
    
    // Méthodes pour définir et récupérer les voisins de la cellule.
    
    public void setNeighbor(Direction direction, Cell neighbor) {
        neighbors.put(direction, neighbor);
    }

    public Cell getNeighbor(Direction direction) {
        return neighbors.get(direction);
    }

    public boolean hasMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void mark() {
        if (!uncovered) {
            marked = !marked;
        }
    }

    public boolean ismarked() {

        return marked;
    }

    public boolean isUncovered() {
        return uncovered;
    }
    
    // Méthode pour découvrir la cellule et propager la découverte aux cellules voisines si nécessaire.
    
    public boolean uncover() {
        if (!marked && !uncovered) {
            uncovered = true;

            if (!mine && getAdjacentMineCount() == 0) {
                for (Cell neighbor : neighbors.values()) {
                    if (neighbor != null) {
                        neighbor.uncover();
                    }
                }
            }
            return !mine;
        }
        return true;
    }

    public int getAdjacentMineCount() {
        int count = 0;
        for (Cell neighbor : neighbors.values()) {
            if (neighbor != null && neighbor.hasMine()) {
                count++;
            }
        }
        return count;
    }

    // Méthode pour retourner une représentation de chaîne de caractères de la cellule en fonction de son état.
    
    @Override
    public String toString() {
        if (marked) {
            return " " + ANSIcodes.markedCell + " ";
        } else if (!uncovered) {
            return " " + ANSIcodes.coveredCell + " ";
        } else if (mine) {
            return ANSIcodes.redColor + " " + ANSIcodes.mineCell + " " + ANSIcodes.resetColor;
        } else {
            int count = getAdjacentMineCount();
            return count > 0 ? ANSIcodes.greenColor + " " + String.valueOf(count) + " " + ANSIcodes.resetColor
                    : " " + ANSIcodes.uncoveredCell + " ";
        }
    }
}
