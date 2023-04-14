import java.io.Serializable;
import java.io.Console;
import java.util.Random;


// Classe représentant la grille du plateau de jeu du démineur.

public class Grid implements Serializable {
    private Cell topLeft;
    private int rows;
    private int columns;
    private int minePercentage;

    public Grid(int rows, int columns, int minePercentage) {
        this.rows = rows;
        this.columns = columns;
        this.minePercentage = minePercentage;
        initGrid();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

 // Méthode pour initialiser la grille en créant les cellules et en les reliant entre elles.
    private void initGrid() {
        Cell currentRowStart = null;
        Cell previousRowStart = null;
        Cell currentCell = null;
        Cell previousCell = null;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                currentCell = new Cell();

                if (i == 0 && j == 0) {
                    topLeft = currentCell;
                }

                if (j > 0) {
                    currentCell.setNeighbor(Direction.LEFT, previousCell);
                    previousCell.setNeighbor(Direction.RIGHT, currentCell);
                }

                if (i > 0) {
                    Cell aboveCell = getRowCell(previousRowStart, j);
                    currentCell.setNeighbor(Direction.UP, aboveCell);
                    aboveCell.setNeighbor(Direction.DOWN, currentCell);

                    if (j > 0) {
                        currentCell.setNeighbor(Direction.UP_LEFT, aboveCell.getNeighbor(Direction.LEFT));
                        aboveCell.getNeighbor(Direction.LEFT).setNeighbor(Direction.DOWN_RIGHT, currentCell);
                    }

                    if (j < columns - 1) {
                        currentCell.setNeighbor(Direction.UP_RIGHT, aboveCell.getNeighbor(Direction.RIGHT));
                        aboveCell.getNeighbor(Direction.RIGHT).setNeighbor(Direction.DOWN_LEFT, currentCell);
                    }
                }

                if (j == 0) {
                    currentRowStart = currentCell;
                }

                previousCell = currentCell;
            }

            previousRowStart = currentRowStart;
        }

        int totalMines = rows * columns * minePercentage / 100;
        Random random = new Random();

        for (int i = 0; i < totalMines; i++) {
            int row, col;
            Cell cell;
            do {
                row = random.nextInt(rows);
                col = random.nextInt(columns);
                cell = getCell(row, col);
            } while (cell.hasMine());

            cell.setMine(true);
        }
    }
// Méthode pour récupérer la cellule d'une ligne à partir de la cellule de début de ligne et de l'index de colonne.
    private Cell getRowCell(Cell rowStart, int columnIndex) {
        Cell currentCell = rowStart;
        for (int i = 0; i < columnIndex; i++) {
            currentCell = currentCell.neighbors.get(Direction.RIGHT);
        }
        return currentCell;
    }
// Méthode pour récupérer une cellule de la grille à partir de ses coordonnées.
    public Cell getCell(int row, int col) {
        Cell currentCell = topLeft;
        for (int i = 0; i < row; i++) {
            currentCell = currentCell.neighbors.get(Direction.DOWN);
        }
        for (int j = 0; j < col; j++) {
            currentCell = currentCell.neighbors.get(Direction.RIGHT);
        }
        return currentCell;
    }

    public void mark(int row, int col) {
        getCell(row, col).mark();
    }
// Méthode pour découvrir une cellule de la grille à partir de ses coordonnées et retourner si elle contient une mine.
    public boolean uncover(int row, int col) {
        return getCell(row, col).uncover();
    }
// Méthode pour afficher la grille dans la console.

    public void printGrid() {
        Console console = System.console();
        Cell currentRowStart = topLeft;
        // Afficher les coordonnées horizontales
        console.writer().println();
        console.writer().print("   ");
        for (int x = 0; x < columns; x++) {
            console.writer().print("  " + x + "  ");
        }
        console.writer().println();

        for (int i = 0; i < rows; i++) {
            console.writer().print("   ");
            for (int x = 0; x < columns; x++) { // Afficher la ligne horizontale supérieure
                console.writer()
                        .print(ANSIcodes.upLeftCorner + ANSIcodes.horizontalLine + ANSIcodes.upRightCorner);
            }
            console.writer().println();
            console.writer().print("" + i + "  ");
            Cell currentCell = currentRowStart;
            for (int j = 0; j < columns; j++) {
                console.writer().print(ANSIcodes.verticalLine + currentCell + ANSIcodes.verticalLine);
                // System.out.print(" "+currentCell + " ");
                currentCell = currentCell.neighbors.get(Direction.RIGHT);
            }
            console.writer().println();
            // Afficher la ligne horizontale inférieure si ce n'est pas la dernière ligne
            console.writer().print("   ");
            for (int x = 0; x < columns; x++) {

                console.writer().print(ANSIcodes.downLeftCorner + ANSIcodes.horizontalLine + ANSIcodes.downRightCorner);
            }
            console.writer().println();
            currentRowStart = currentRowStart.neighbors.get(Direction.DOWN);

        }
    }
}
