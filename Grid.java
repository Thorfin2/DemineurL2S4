import java.io.Serializable;
import java.util.Random;

public class Grid  implements Serializable{
    private Cell topLeft;
    private int rows;
    private int columns;
    private int minePercentage;

    public Grid(int rows, int columns,  int minePercentage) {
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
                    currentCell.setNeighbor(Direction.W, previousCell);
                    previousCell.setNeighbor(Direction.E, currentCell);
                }

                if (i > 0) {
                    Cell aboveCell = getRowCell(previousRowStart, j);
                    currentCell.setNeighbor(Direction.N, aboveCell);
                    aboveCell.setNeighbor(Direction.S, currentCell);

                    if (j > 0) {
                        currentCell.setNeighbor(Direction.NW, aboveCell.getNeighbor(Direction.W));
                        aboveCell.getNeighbor(Direction.W).setNeighbor(Direction.SE, currentCell);
                    }

                    if (j < columns - 1) {
                        currentCell.setNeighbor(Direction.NE, aboveCell.getNeighbor(Direction.E));
                        aboveCell.getNeighbor(Direction.E).setNeighbor(Direction.SW, currentCell);
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

    private Cell getRowCell(Cell rowStart, int columnIndex) {
        Cell currentCell = rowStart;
        for (int i = 0; i < columnIndex; i++) {
            currentCell = currentCell.neighbors.get(Direction.E);
        }
        return currentCell;
    }

    public Cell getCell(int row, int col) {
        Cell currentCell = topLeft;
        for (int i = 0; i < row; i++) {
            currentCell = currentCell.neighbors.get(Direction.S);
        }
        for (int j = 0; j < col; j++) {
            currentCell = currentCell.neighbors.get(Direction.E);
        }
        return currentCell;
    }

    public void mark(int row, int col) {
        getCell(row, col).mark();
    }

    public boolean uncover(int row, int col) {
        return getCell(row, col).uncover();
    }

    public void printGrid() {
        Cell currentRowStart = topLeft;
        for (int i = 0; i < rows; i++) {
            Cell currentCell = currentRowStart;
            for (int j = 0; j < columns; j++) {
                System.out.print(currentCell + " ");
                currentCell = currentCell.neighbors.get(Direction.E);
            }
            System.out.println();
            currentRowStart = currentRowStart.neighbors.get(Direction.S);
        }
    }
}

    // Autres méthodes de la classe Grid restent inchangées, mais vous devrez les adapter pour travailler avec la structure mise à jour

