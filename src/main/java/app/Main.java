package app;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;

import archive.GameDataAccessObject;
import archive.GameDetailController;
import archive.GameDetailInteractor;
import archive.GameDetailPresenter;
import archive.GameDetailView;
import archive.GameDetailViewModel;
import archive.GameListController;
import archive.GameListInteractor;
import archive.GameListPresenter;
import archive.GameListView;
import archive.GameListViewModel;
import archive.SelectGameInteractor;

/**
 * Application entry point. Builds a single window whose CardLayout swaps between
 * the main menu, the live game, and the saved-game browser (the archive feature).
 */
public class Main {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 640;
    private static final String GAME_VIEW = "Game";

    /**
     * Launches the app on the Swing event thread.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::buildAndShow);
    }

    /** Wires every card into one CardLayout window and shows the menu. */
    private static void buildAndShow() {
        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);

        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        addArchiveFeature(views, viewManagerModel, cardLayout);

        cardLayout.show(views, MainMenu.VIEW_NAME);
        showFrame(views);
    }

    /** Wires the archive (saved-game browser) stack and registers its cards. */
    private static void addArchiveFeature(JPanel views,
                                          ViewManagerModel viewManagerModel,
                                          CardLayout cardLayout) {
        final GameDataAccessObject dataAccess = new GameDataAccessObject();

        final GameListViewModel gameListViewModel = new GameListViewModel();
        final GameDetailViewModel gameDetailViewModel = new GameDetailViewModel();

        final GameListPresenter gameListPresenter =
                new GameListPresenter(gameListViewModel, viewManagerModel);
        final GameDetailPresenter gameDetailPresenter =
                new GameDetailPresenter(gameDetailViewModel, viewManagerModel);

        final GameListInteractor gameListInteractor =
                new GameListInteractor(dataAccess, gameListPresenter);
        final SelectGameInteractor selectGameInteractor =
                new SelectGameInteractor(dataAccess, gameDetailPresenter);
        final GameDetailInteractor gameDetailInteractor =
                new GameDetailInteractor(gameDetailPresenter);

        final GameListController gameListController =
                new GameListController(gameListInteractor, selectGameInteractor);
        final GameDetailController gameDetailController =
                new GameDetailController(gameDetailInteractor);

        final GameListView gameListView =
                new GameListView(gameListController, gameListViewModel);
        final GameDetailView gameDetailView =
                new GameDetailView(gameDetailController, gameDetailViewModel);

        addMainMenu(views, viewManagerModel, cardLayout, gameListController);
        views.add(gameListView, GameListViewModel.VIEW_NAME);
        views.add(gameDetailView, GameDetailViewModel.VIEW_NAME);
    }

    private static void addMainMenu(JPanel views, ViewManagerModel viewManagerModel, CardLayout cardLayout, GameListController gameListController) {
        final MainMenu mainMenu = new MainMenu(
                topology -> startGame(topology, views, cardLayout),
                () -> browseSavedGames(gameListController, viewManagerModel));

        views.add(mainMenu, MainMenu.VIEW_NAME);
    }

    /** Builds a fresh game for the chosen topology and shows it as a card. */
    private static void startGame(int topology, JPanel views, CardLayout cardLayout) {
        final Board board = new Board(topology, 0);
        final GameState gameState =
                new GameState(board, 0, 0, new BoardStateList(), "Main Board Result");
        final JPanel gamePanel = new GameBuilder(gameState)
                .addAnalysisView()
                .addMoveView()
                .build();
        views.add(gamePanel, GAME_VIEW);
        cardLayout.show(views, GAME_VIEW);
    }

    /** Loads the saved-game list and switches to the browser card. */
    private static void browseSavedGames(GameListController gameListController,
                                         ViewManagerModel viewManagerModel) {
        gameListController.getGameList();
        viewManagerModel.setCurrentView(GameListViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChanged();
    }

    /** Puts the card panel into a frame and shows it. */
    private static void showFrame(JPanel views) {
        final JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(views);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
