import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

    public void save() throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("DemineurSauvegard"));
        out.writeObject(grid);
        out.flush();
        out.close();

    }

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
