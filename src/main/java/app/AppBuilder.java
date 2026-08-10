package app;

import javax.swing.*;
import java.awt.*;

import Analysis.AnalyzeController;
import Analysis.AnalyzeMoveInteractor;
import Analysis.AnalyzePresenter;
import Analysis.AnalyzeView;
import Analysis.AnalyzeViewModel;
import Analysis.ChessApiAdapter;
import MakeMove.*;
import archive.*;

/**
 * Assembles the application window, wiring each feature's Clean Architecture
 * stack and adding its view to a region of the frame.
 */
public class AppBuilder extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final String CENTER = "Center";
    private static final String EAST = "East";

    private final GameRecord gameRecord;

    private final GameDataAccessObject gameDataAccessObject =  new GameDataAccessObject();

    // Wrap views in a card and use view manager to switch
    CardLayout cardLayout = new CardLayout();
    JPanel views = new JPanel(cardLayout);
    JPanel playCard = new JPanel(new BorderLayout());
    JPanel gameDetailCard = new JPanel(new BorderLayout());
    JPanel gameListCard = new JPanel(new BorderLayout());
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    final ViewManager viewManager = new ViewManager(views, cardLayout, viewManagerModel);

    // MakeMove feature
    private MoveViewModel moveViewModel;
    private MoveView moveView;
    private MoveController moveController;
    private MakeMoveInteractor makeMoveInteractor;
    private MovePresenter movePresenter;

    // Analysis feature
    private AnalyzeViewModel analyzeViewModel;
    private AnalyzePresenter analyzePresenter;
    private AnalyzeMoveInteractor analyzeInteractor;
    private AnalyzeView analyzeView;
    private AnalyzeController analyzeController;
    private ChessApiAdapter chessApiAdaptor;

    // Game archive feature
    private GameDetailViewModel gameDetailViewModel;
    private GameDetailPresenter gameDetailPresenter;
    private GameDetailInteractor gameDetailInteractor;
    private GameDetailController gameDetailController;
    private GameDetailView gameDetailView;
    private SelectGameInteractor selectGameInteractor;
    private GameListViewModel gameListViewModel;
    private GameListPresenter gameListPresenter;
    private GameListInteractor gameListInteractor;
    private GameListController gameListController;
    private GameListView gameListView;
    // Game archive analysis
    private AnalyzeViewModel archiveAnalyzeViewModel;
    private AnalyzePresenter archiveAnalyzePresenter;
    private AnalyzeMoveInteractor archiveAnalyzeInteractor;
    private AnalyzeView archiveAnalyzeView;
    private AnalyzeController archiveAnalyzeController;

    /**
     * Configures the application frame around the given game state.
     * @param gameRecord the shared game state the features build on
     */
    public AppBuilder(GameRecord gameRecord) {
        this.gameRecord = gameRecord;
        changeListeningSetup();
        setTitle("Chess App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
    }

    private void changeListeningSetup() {
        analyzeViewModel = new AnalyzeViewModel();
        analyzePresenter = new AnalyzePresenter(analyzeViewModel);

        chessApiAdaptor = new ChessApiAdapter();
        analyzeInteractor = new AnalyzeMoveInteractor(chessApiAdaptor,
                analyzePresenter, gameRecord.getHistory().get(0));

        archiveAnalyzeViewModel = new AnalyzeViewModel();
        archiveAnalyzePresenter = new AnalyzePresenter(archiveAnalyzeViewModel);
        archiveAnalyzeInteractor = new AnalyzeMoveInteractor(chessApiAdaptor,
                archiveAnalyzePresenter, gameRecord.getHistory().get(0));
    }

    /**
     * Wires the make-move feature and adds its view to the center.
     * @return this builder, for chaining
     */
    public AppBuilder addMoveView() {
        moveViewModel = new MoveViewModel();
        final MoveValidator moveValidator = new MoveValidatorBuilder()
                .addNormalMoves()
                .addEnPassants()
                .addCastles()
                .duplicateAndMirrorMoves()
                .build();

        movePresenter = new MovePresenter(moveViewModel);
        makeMoveInteractor = new MakeMoveInteractor(moveValidator, gameDataAccessObject, gameRecord,
                gameRecord.getHistory().get(0), movePresenter);
        makeMoveInteractor.addPropertyChangeListener(analyzeInteractor);

        moveController = new MoveController(makeMoveInteractor);

        moveView = new MoveView(moveViewModel, moveController);
        playCard.add(moveView, CENTER);

        makeMoveInteractor.updateVisuals();
        return this;
    }

    /**
     * Wires the analysis feature and adds its view on the playCard.
     * @return this builder, for chaining
     */
    public AppBuilder addAnalysisView() {
        analyzeController = new AnalyzeController(analyzeInteractor);
        analyzeView = new AnalyzeView(analyzeViewModel, analyzeController);
        playCard.add(analyzeView, EAST);
        return this;
    }

    /**
     * Wires the analysis feature and adds its view on the gameDetailCard
     * @return this builder, for chaining
     */
    public AppBuilder addArchiveAnalysisView() {
        archiveAnalyzeController = new AnalyzeController(archiveAnalyzeInteractor);
        archiveAnalyzeView = new AnalyzeView(archiveAnalyzeViewModel, archiveAnalyzeController);
        gameDetailCard.add(archiveAnalyzeView, EAST);
        return this;
    }

    /**
     * Wires the game detail view for the archive feature.
     * @return this builder, for chaining
     */
    public AppBuilder addGameDetailView() {
        gameDetailViewModel = new GameDetailViewModel();
        gameDetailPresenter = new GameDetailPresenter(gameDetailViewModel, viewManagerModel);
        gameDetailInteractor = new GameDetailInteractor(gameDetailPresenter);
        gameDetailInteractor.addPropertyChangeListener(archiveAnalyzeInteractor);
        gameDetailController = new GameDetailController(gameDetailInteractor);
        gameDetailView = new GameDetailView(gameDetailController, gameDetailViewModel);
        gameDetailCard.add(gameDetailView, CENTER);
        return this;
    }

    /**
     * Wires the game list view for the archive feature.
     * @return this builder, for chaining
     */
    public AppBuilder addGameListView() {
        selectGameInteractor = new SelectGameInteractor(gameDataAccessObject, gameDetailPresenter);
        gameListViewModel = new GameListViewModel();
        gameListPresenter = new GameListPresenter(gameListViewModel, viewManagerModel);
        gameListInteractor = new GameListInteractor(gameDataAccessObject, gameListPresenter);
        gameListController = new GameListController(gameListInteractor, selectGameInteractor);
        gameListView = new GameListView(gameListController, gameListViewModel);
        gameListCard.add(gameListView, CENTER);
        return this;
    }

    /**
     * Sizes the frame to its contents and shows it.
     * @return the assembled application frame
     */
    public JFrame build() {
        playCard.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gameDetailCard.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gameListCard.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        views.add(playCard, "Play");
        views.add(gameDetailCard, GameDetailViewModel.VIEW_NAME);
        views.add(gameListCard, GameListViewModel.VIEW_NAME);
        add(views, BorderLayout.CENTER);
        pack();
        setVisible(true);
        analyzeInteractor.analyzeInitialPosition();
//        gameListController.getGameList();
        return this;
    }
}
