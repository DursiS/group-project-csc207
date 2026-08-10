package app;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import entity.Board;
import entity.BoardStateList;
import entity.GameState;

/**
 * Application entry point: shows a topology menu, then launches the game.
 */
public class Main {
    private static final int STANDARD_TOPOLOGY = 0;
    private static final int WRAP_TOPOLOGY = 1;
    private static final int ROWS = 3;
    private static final int COLS = 1;

    /**
     * Prompt the user for their choice of topology and run the app.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        showTopologyMenu();
    }

    /** Shows the topology selection window. */
    private static void showTopologyMenu() {
        final JFrame menu = new JFrame("Choose Board Topology");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLayout(new GridLayout(ROWS, COLS));
        menu.add(new JLabel("Pick a board topology:", SwingConstants.CENTER));
        menu.add(topologyButton("Standard Chess (0)", STANDARD_TOPOLOGY, menu));
        menu.add(topologyButton("Wrap-around (1)", WRAP_TOPOLOGY, menu));
        menu.pack();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }

    /**
     * Builds a button that launches the game with the given topology.
     * @param label the button text
     * @param topology the topology value to start with
     * @param menu the menu window to close on click
     * @return the configured button
     */
    private static JButton topologyButton(String label, int topology, JFrame menu) {
        final JButton button = new JButton(label);
        button.addActionListener(click -> {
            menu.dispose();
            startGame(topology);
        });
        return button;
    }

    /**
     * Builds the game state and app window for the chosen topology.
     * @param topology the board topology to play with
     */
    private static void startGame(int topology) {
        final GameState gameState = getGameState(topology);
        new AppBuilder(gameState)
                .addAnalysisView()
                .addMoveView()
                .addSaveResumeView()
                .build();
    }

    /**
     * Creates a fresh game state whose board uses the given topology.
     * @param topology the vertical-edge topology for the board
     * @return the new game state
     */
    private static GameState getGameState(int topology) {
        final Board board = new Board(topology, 0);
        return new GameState(board, 0, 0, new BoardStateList(), "Main Board Result");
    }
}
