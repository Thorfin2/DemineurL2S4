import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class Game implements Serializable {
    private Grid grid;
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
            // TODO Auto-generated catch block
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

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            // Vous pouvez choisir la taille de la grille et le pourcentage de mines ici
            System.out.println("Bienvenue dans le jeu DEMINEUR !");
            Game game;
            System.out.print("1- Nouvelle Partie\n2- Charger une partie existante\n ");
            int option = scanner.nextInt();
            if (option == 1) {
                System.out.print("Entrer la largeur de la Grille (cols) : ");

                int cols = scanner.nextInt();

                System.out.print("Entrer la Hauteur de la Grille (rows) : ");
                int rows = scanner.nextInt();

                System.out.print("Entrer le % de mines : ");
                int percentageMines = scanner.nextInt();
                game = new Game(rows, cols, percentageMines);
            } else {
                boolean exists = false;
                do {
                    /*
                     * System.out.print("Entrer le nom fichier: ");
                     * String file = scanner.next();
                     */
                    game = new Game();
                    exists = game.load();
                } while (!exists);
            }

            boolean isSave = false;
            while (!game.isGameover() && game.isNotFinished()) {
                game.grid.printGrid();
                System.out.print(
                        "Entrer une commande (s pour save, m pour marquer une case , u pour en decouvrir une ): ");
                String action = scanner.next();
                while (action.charAt(0) != 'u' && action.charAt(0) != 'm' && action.charAt(0) != 's') {
                    System.out.print(
                            "Entrer une commande (s pour save, m pour marquer une case , u pour en decouvrir une ): ");
                    action = scanner.next();
                }
                if (!"s".equals(action)) {

                    System.out.print("Entrer les coordonnes row : ");
                    int row = scanner.nextInt();
                    System.out.print("Entrer les coordonnes col : ");
                    int col = scanner.nextInt();
                    if ((row <= game.grid.getRows() - 1) || (col <= game.grid.getColumns() - 1)) {
                        if ("m".equals(action)) {
                            game.mark(row, col);
                        } else if ("u".equals(action)) {
                            game.uncover(row, col);
                        } else {
                            System.out.println("commande Invalide . veuillez réessayer.");
                        }
                    } else {
                        System.out.println("commande Invalide . veuillez réessayer.");
                    }
                } else {
                    game.save();
                    isSave = true;
                    break;
                }
            }
            if (!game.isGameover() && isSave == false) {
                game.grid.printGrid();
                System.out.println("Bravo, Vous avez gagner !");
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
