import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// Classe représentant une partie de démineur.

public class Game implements Serializable {
    public Grid grid;
    private boolean gameover = false;

    public Game() {
    }

    public Game(int rows, int columns, int minePercentage) {
        grid = new Grid(rows, columns, minePercentage);
    }

    public void mark(int row, int col) {
        grid.mark(row, col);
    }

// Méthode pour découvrir une cellule de la grille à partir de ses coordonnées et mettre à jour l'état de la partie.
    public void uncover(int row, int col) {
        gameover = !grid.uncover(row, col);
        if (gameover) {
            System.out.println("Game over!");
            grid.printGrid();
        }

    }

    public boolean isGameover() {
        return gameover;
    }
    // Méthode pour sauvegarder la partie en cours.
    public void save() throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("DemineurSauvegard"));
        out.writeObject(grid);
        out.flush();
        out.close();

    }
    
    // Méthode pour charger une partie sauvegardée et retourner si le chargement a réussi.
    public boolean load() {
        try {
            FileInputStream fileIn = new FileInputStream("DemineurSauvegard");
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            this.grid = (Grid) ois.readObject();
            // System.out.println(grid);
            ois.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    // Méthode pour vérifier si la partie n'est pas encore terminée.
    public boolean isNotFinished() {
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getColumns(); col++) {
                Cell cell = grid.getCell(row, col);
                if (!cell.isUncovered() && !cell.hasMine())
                    return true;
            }
        }
        return false;
    }
}
