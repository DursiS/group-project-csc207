package app;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import archive.GameListController;
import archive.GameListViewModel;

/**
 * The landing screen (a CardLayout card): pick a board topology to start a new
 * game, or browse previously saved games.
 */
public class MainMenu extends JPanel {
    public static final String VIEW_NAME = "Main Menu";

    private static final int STANDARD_TOPOLOGY = 0;
    private static final int WRAP_TOPOLOGY = 1;
    static final String GAME_VIEW = "Game";

    private final JPanel views;
    private final ViewManagerModel viewManagerModel;
    private final GameListController gameListController;
    private GameBuilder currentGame;

    public MainMenu(JPanel views,
                    ViewManagerModel viewManagerModel,
                    GameListController gameListController) {
        this.views = views;
        this.viewManagerModel = viewManagerModel;
        this.gameListController = gameListController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addCentered(new JLabel("Pick a board topology:"));
        addCentered(playButton("Standard Chess (0)", STANDARD_TOPOLOGY));
        addCentered(playButton("Wrap-around (1)", WRAP_TOPOLOGY));

        final JButton savedGames = new JButton("Saved Games");
        savedGames.addActionListener(click -> browseSavedGames());
        addCentered(savedGames);
    }

    private JButton playButton(String label, int topology) {
        final JButton button = new JButton(label);
        button.addActionListener(click -> startGame(topology));
        return button;
    }

    /** Builds a fresh game for the chosen topology, adds it as a card, and shows it. */
    private void startGame(int topology) {
        final Board board = new Board(topology, 0);
        final GameState gameState =
                new GameState(board, 0, 0, new BoardStateList(), "Main Board Result");
        currentGame = new GameBuilder(gameState)
                .addAnalysisView()
                .addMoveView()
                .addSaveResumeView();
        currentGame.build();
        views.add(currentGame, GAME_VIEW);
        switchTo(GAME_VIEW);
    }

    /**
     * Gives the current game a chance to save before the application closes.
     * @param mainFrame the shared application frame
     */
    void exitCurrentGame(JFrame mainFrame) {
        if (currentGame == null) {
            mainFrame.dispose();
        }
        else {
            currentGame.exitGame(mainFrame);
        }
    }

    /** Loads the saved-game list and switches to the browser card. */
    private void browseSavedGames() {
        gameListController.getGameList();
        switchTo(GameListViewModel.VIEW_NAME);
    }

    private void switchTo(String viewName) {
        viewManagerModel.setCurrentView(viewName);
        viewManagerModel.firePropertyChanged();
    }

    private void addCentered(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(component);
    }
}
