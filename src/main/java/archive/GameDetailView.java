package archive;

import MakeMove.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class GameDetailView extends JPanel implements PropertyChangeListener {

    private final GameDetailController gameDetailController;
    private final GameDetailViewModel gameDetailViewModel;
    private final JButton[][] grid = new JButton[8][8];
    public static final int INNER_MARGIN = 5;
    public static final Color color1 = new Color(240, 217, 181);
    public static final Color color2 = new Color(181, 136, 99);
    public static final Map<Integer, String> pieceStrings = new HashMap<>(Map.ofEntries(
            Map.entry(0,""),
            Map.entry(1,"♙"),
            Map.entry(2,"♙"),
            Map.entry(3,"♙"),
            Map.entry(4,"♖"),
            Map.entry(5,"♖"),
            Map.entry(6,"♘"),
            Map.entry(7,"♗"),
            Map.entry(8,"♕"),
            Map.entry(9,"♔"),
            Map.entry(10,"♔"),
            Map.entry(-1,"♟"),
            Map.entry(-2,"♟"),
            Map.entry(-3,"♟"),
            Map.entry(-4,"♜"),
            Map.entry(-5,"♜"),
            Map.entry(-6,"♞"),
            Map.entry(-7,"♝"),
            Map.entry(-8,"♛"),
            Map.entry(-9,"♚"),
            Map.entry(-10,"♚")
    ));

    private JButton backwardButton;
    private JButton forwardButton;
    private JLabel resultLabel;

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

        // Game result
        resultLabel = new JLabel("Game Result Label", SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        // Renders board
        JPanel boardPanel = setupBoardPanel();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                grid[y][x] = new JButton();
                grid[y][x].setMargin(new Insets(0,0,0,0));
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

        this.add(resultLabel, BorderLayout.NORTH);
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        syncWithViewModel();
    }

    private JPanel setupBoardPanel() {
        JPanel boardPanel = new JPanel(null);
        boardPanel.setPreferredSize(new Dimension(400, 400));
        boardPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int size = Math.min(boardPanel.getWidth(), boardPanel.getHeight());
                int sideLength = (size - 7 * INNER_MARGIN) / 8;
                int totalLength = 7 * INNER_MARGIN + 8 * sideLength;
                int x_0 = (boardPanel.getWidth() - totalLength) / 2 ;
                int y_0 = (boardPanel.getHeight() - totalLength) /2 ;

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        grid[y][x].setBounds(x_0 + (sideLength + INNER_MARGIN) * x,
                                y_0 + (sideLength + INNER_MARGIN) * y, sideLength, sideLength);
                        grid[y][x].setFont(new Font(Font.DIALOG, Font.PLAIN,
                                (int) (sideLength * 0.75)));
                    }
                }

                // Dynamically resize result label too
                resultLabel.setFont(new Font(Font.DIALOG, Font.BOLD,
                        Math.max(12, (int) (sideLength * 0.35))));
            }
        });
        return boardPanel;
    }

    private void syncWithViewModel() {
        backwardButton.setEnabled(gameDetailViewModel.hasPrevious());
        forwardButton.setEnabled(gameDetailViewModel.hasNext());
        resultLabel.setText(gameDetailViewModel.getGameResult());

        Board board = gameDetailViewModel.getBoard();
        if (board != null) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    grid[y][x].setText(pieceStrings.get(board.getSquare(x, y)));
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
