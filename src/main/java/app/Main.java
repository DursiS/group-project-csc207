package app;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        final MainMenu mainMenu =
                new MainMenu(views, viewManagerModel, gameListController);

        views.add(mainMenu, MainMenu.VIEW_NAME);
        views.add(gameListView, GameListViewModel.VIEW_NAME);
        views.add(gameDetailView, GameDetailViewModel.VIEW_NAME);
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
