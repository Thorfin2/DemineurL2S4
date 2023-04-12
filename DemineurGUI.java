import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

public class DemineurGUI extends JFrame implements MouseListener {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DemineurGUI(3, 3, 15);
            }
        });
    }

    private Game game;
    private JButton[][] buttons;

    public DemineurGUI(int rows, int columns, int minePercentage) {
        game = new Game(rows, columns, minePercentage);
        buttons = new JButton[rows][columns];
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Démineur");

        Container container = getContentPane();
        container.setLayout(new GridLayout(rows, columns));
        setExtendedState(MAXIMIZED_BOTH);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.PLAIN, 12));
                button.addMouseListener(this);
                button.setFocusable(false);
                button.setName(i + ";" + j);
                button.setText(ANSIcodes.coveredCell);
                buttons[i][j] = button;
                container.add(button);
            }
        }
        pack();
        setVisible(true);
    }

    private void updateButton(JButton button, Cell cell) {
        if (cell.ismarked()) {
            button.setText(ANSIcodes.markedCell);
        } else if (cell.isUncovered()) {
            button.setEnabled(false);
            if (cell.hasMine()) {
                button.setText(ANSIcodes.mineCell);
            } else {
                int count = cell.getAdjacentMineCount();
                if (count > 0) {
                    button.setText(Integer.toString(count));
                } else {
                    button.setText(ANSIcodes.uncoveredCell);
                }
            }
        } else {
            button.setText(ANSIcodes.coveredCell);
        }
    }

    private void showGameoverMessage() {
        JOptionPane.showMessageDialog(this, "Game over!", "Démineur", JOptionPane.INFORMATION_MESSAGE);
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        String[] coord = source.getName().split(";");
        int i = Integer.parseInt(coord[0]);
        int j = Integer.parseInt(coord[1]);
        if (SwingUtilities.isLeftMouseButton(e)) {
            game.uncover(i, j);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            game.mark(i, j);
        }

        for (int y = 0; y < game.grid.getRows(); y++) {
            for (int x = 0; x < game.grid.getColumns(); x++) {
                updateButton(buttons[y][x], game.grid.getCell(y, x));
            }
        }

        if (game.isGameover()) {
            showGameoverMessage();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
