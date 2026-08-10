package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Analysis.*;
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

    /**
     * Launches the app on the Swing event thread.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> buildAndShow());
    }

    /** Wires every card into one CardLayout window and shows the menu. */
    private static void buildAndShow() {
        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);

        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        final MainMenu mainMenu = addArchiveFeature(views, viewManagerModel);

        viewManagerModel.setCurrentView(MainMenu.VIEW_NAME);
        viewManagerModel.firePropertyChanged();
        showFrame(views, viewManagerModel, mainMenu);
    }

    /** Wires the archive (saved-game browser) stack and registers its cards. */
    private static MainMenu addArchiveFeature(JPanel views,
                                              ViewManagerModel viewManagerModel) {
        final GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();

        JPanel gameReplayView = new JPanel(new BorderLayout());

        ChessApiAdapter chessApiAdaptor = new ChessApiAdapter();
        AnalyzeViewModel archiveAnalyzeViewModel = new AnalyzeViewModel();
        AnalyzePresenter archiveAnalyzePresenter = new AnalyzePresenter(archiveAnalyzeViewModel);
        AnalyzeMoveInteractor archiveAnalyzeInteractor = new AnalyzeMoveInteractor(chessApiAdaptor,
                archiveAnalyzePresenter, new GameState(new Board(), 0, 0,
                new BoardStateList(), "In progress"));
        AnalyzeController archiveAnalyzeController = new
                AnalyzeController(archiveAnalyzeInteractor);
        AnalyzeView archiveAnalyzeView = new AnalyzeView(archiveAnalyzeViewModel,
                archiveAnalyzeController);
        gameReplayView.add(archiveAnalyzeView, BorderLayout.EAST);

        GameDetailViewModel gameDetailViewModel = new GameDetailViewModel();
        GameDetailPresenter gameDetailPresenter = new
                GameDetailPresenter(gameDetailViewModel, viewManagerModel);
        GameDetailInteractor gameDetailInteractor = new GameDetailInteractor(gameDetailPresenter);
        gameDetailInteractor.addPropertyChangeListener(archiveAnalyzeInteractor);
        GameDetailController gameDetailController = new GameDetailController(gameDetailInteractor);
        GameDetailView gameDetailView = new
                GameDetailView(gameDetailController, gameDetailViewModel);
        gameReplayView.add(gameDetailView, BorderLayout.CENTER);


        SelectGameInteractor selectGameInteractor = new
                SelectGameInteractor(gameDataAccessObject, gameDetailPresenter);
        selectGameInteractor.addPropertyChangeListener(archiveAnalyzeInteractor);
        GameListViewModel gameListViewModel = new GameListViewModel();
        GameListPresenter gameListPresenter = new
                GameListPresenter(gameListViewModel, viewManagerModel);
        GameListInteractor gameListInteractor = new
                GameListInteractor(gameDataAccessObject, gameListPresenter);
        GameListController gameListController = new
                GameListController(gameListInteractor, selectGameInteractor);
        GameListView gameListView = new GameListView(gameListController, gameListViewModel);


        final MainMenu mainMenu =
                new MainMenu(views, viewManagerModel, gameListController);

        views.add(mainMenu, MainMenu.VIEW_NAME);
        views.add(gameListView, GameListViewModel.VIEW_NAME);
        views.add(gameReplayView, GameDetailViewModel.VIEW_NAME);
        return mainMenu;
    }

    /** Puts the card panel into a frame and shows it. */
    private static void showFrame(JPanel views,
                                  ViewManagerModel viewManagerModel,
                                  MainMenu mainMenu) {
        final JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (MainMenu.GAME_VIEW.equals(viewManagerModel.getCurrentView())) {
                    mainMenu.exitCurrentGame(frame);
                }
                else {
                    frame.dispose();
                }
            }
        });
        frame.setContentPane(views);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
