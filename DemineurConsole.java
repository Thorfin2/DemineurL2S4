import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class DemineurConsole {
    
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
