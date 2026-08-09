package archive;

import entity.Board;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameDetailView extends JPanel implements PropertyChangeListener {

    private final GameDetailController gameDetailController;
    private final GameDetailViewModel gameDetailViewModel;
    private final JButton[][] grid = new JButton[8][8];
    private final Color color1 = new Color(240, 217, 181);
    private final Color color2 = new Color(181, 136, 99);

    private JButton backwardButton;
    private JButton forwardButton;
    private JLabel sideLabel;

    public GameDetailView(GameDetailController gameDetailController,
                          GameDetailViewModel gameDetailViewModel) {
        this.gameDetailController = gameDetailController;
        this.gameDetailViewModel = gameDetailViewModel;
        this.gameDetailViewModel.addPropertyChangeListener(this);
        setup();
    }

    /**
     * Refresh the board, players' times and game result
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (gameDetailViewModel.getErrorMessage() != null) {
            showErrorMessage(gameDetailViewModel.getErrorMessage());
        }
        else {
            syncWithViewModel();
        }
    }

    private void setup() {
        this.setLayout(new BorderLayout(10,10));

        // Renders board
        JPanel boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.setLayout(new GridLayout(8,8));
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                grid[y][x] = new JButton();
                grid[y][x].setBackground((x + y) % 2 == 0 ? color1 : color2);
                grid[y][x].setBorderPainted(false);
                grid[y][x].setFocusPainted(false);
                grid[y][x].setContentAreaFilled(true);
                boardPanel.add(grid[y][x]);
            }
        }

        // Forward and back buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        backwardButton = new JButton("<");
        forwardButton = new JButton(">");
        backwardButton.setPreferredSize(new Dimension(60, 40));
        forwardButton.setPreferredSize(new Dimension(60, 40));
        backwardButton.addActionListener(e -> gameDetailController.back(
                gameDetailViewModel.getGameRecord(), gameDetailViewModel.getCurrentStateNumber()));
        forwardButton.addActionListener(e -> gameDetailController.forward(
                gameDetailViewModel.getGameRecord(), gameDetailViewModel.getCurrentStateNumber()));
        controlPanel.add(backwardButton);
        controlPanel.add(forwardButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(boardPanel, BorderLayout.CENTER);
        wrapper.add(controlPanel, BorderLayout.SOUTH);

        // analysis and result
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(100, 600));
        sideLabel = new JLabel(gameDetailViewModel.getGameResult());
        sidePanel.add(sideLabel);

        this.add(wrapper, BorderLayout.CENTER);
        this.add(sidePanel, BorderLayout.EAST);
        syncWithViewModel();
    }

    private void syncWithViewModel() {
        backwardButton.setEnabled(gameDetailViewModel.hasPrevious());
        forwardButton.setEnabled(gameDetailViewModel.hasNext());
        sideLabel.setText(gameDetailViewModel.getGameResult());

        Board board = gameDetailViewModel.getBoard();
        if (board != null) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    grid[y][x].setText(String.valueOf(board.getSquare(x, y)));
                }
            }
        }
    }

    /**
     * Show the error message
     */
    private void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Playback Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
