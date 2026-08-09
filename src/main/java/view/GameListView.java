package view;

import interface_adapter.GameListController;
import interface_adapter.GameListViewModel;
import use_case.SelectGameInputData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;

public class GameListView extends JPanel implements PropertyChangeListener {

    private final GameListController gameListController;
    private final GameListViewModel gameListViewModel;

    private JTable gameTable;

    public GameListView(GameListController gameListController,
                        GameListViewModel gameListViewModel) {
        this.gameListController = gameListController;
        this.gameListViewModel = gameListViewModel;
        this.gameListViewModel.addPropertyChangeListener(this);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Refresh the list
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.removeAll();

        String errorMessage = gameListViewModel.getErrorMessage();
        Object[][] data = gameListViewModel.getData();

        if (errorMessage != null) {
            showErrorMessage(errorMessage);
        }
        else if (data == null || data.length == 0) {
            showEmptyMessage();
        }
        else {
            showTable(data);
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Show the game list
     */
    private void showTable(Object[][] data) {
        JLabel titleLabel = new JLabel("Game List");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] columns = {"Time Created", "Game Result"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gameTable = new JTable(tableModel);

        // if user click the game, enter the game detail view
        gameTable.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row =  gameTable.getSelectedRow();
                if (row != -1) {
                    UUID id = gameListViewModel.getIds()[row];
                    SelectGameInputData selectGameInputData = new SelectGameInputData(id);
                    gameListController.selectGame(selectGameInputData);
                }
            }
        });

        // wrap table in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(gameTable);
        scrollPane.setPreferredSize(new Dimension(300, 100));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(titleLabel);
        this.add(scrollPane);
    }

    /**
     * Show the error message
     */
    private void showErrorMessage(String errorMessage) {
        this.setLayout(new GridBagLayout());
        JLabel errorLabel = new JLabel("Error: " + errorMessage);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.add(errorLabel);
    }

    /**
     * Show the empty message
     */
    private void showEmptyMessage() {
        this.setLayout(new GridBagLayout());
        JLabel emptyLabel = new JLabel("No past games found. Start playing to see your games!");
        emptyLabel.setForeground(Color.GRAY);
        this.add(emptyLabel);
    }
}
