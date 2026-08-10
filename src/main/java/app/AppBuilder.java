package app;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import Analysis.AnalyzeController;
import Analysis.AnalyzeMoveInteractor;
import Analysis.AnalyzePresenter;
import Analysis.AnalyzeView;
import Analysis.AnalyzeViewModel;
import Analysis.ChessApiAdapter;
import MakeMove.GameState;
import MakeMove.MoveValidator;
import MakeMove.MoveValidatorBuilder;
import MakeMove.MoveController;
import MakeMove.MovePresenter;
import MakeMove.MoveViewModel;
import MakeMove.MakeMoveInteractor;
import MakeMove.MoveView;

import SaveResume.GameDataAccess;
import SaveResume.FileGameDataAccessObject;
import SaveResume.ResumeGameController;
import SaveResume.ResumeGameInputBoundary;
import SaveResume.ResumeGameInteractor;
import SaveResume.ResumeGamePresenter;
import SaveResume.ResumeGameViewModel;
import SaveResume.SaveGameController;
import SaveResume.SaveGameInputBoundary;
import SaveResume.SaveGameInteractor;
import SaveResume.SaveGamePresenter;
import SaveResume.SaveGameViewModel;
import SaveResume.SaveResumeView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Assembles the application window, wiring each feature's Clean Architecture
 * stack and adding its view to a region of the frame.
 */
public class AppBuilder extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final String CENTER = "Center";
    private static final String EAST = "East";
    private static final String WEST = "West";

    private GameState gameState;

    // MakeMove feature
    private MoveViewModel moveViewModel;
    private MoveView moveView;
    private MoveController moveController;
    private MakeMoveInteractor makeMoveInteractor;
    private MovePresenter movePresenter;

    //Save feature
    private GameDataAccess gameDataAccess;
    private SaveGameViewModel saveGameViewModel;
    private SaveGamePresenter saveGamePresenter;
    private SaveGameInputBoundary saveGameInteractor;
    private SaveGameController saveGameController;

    private ResumeGameViewModel resumeGameViewModel;
    private ResumeGamePresenter resumeGamePresenter;
    private ResumeGameInputBoundary resumeGameInteractor;
    private ResumeGameController resumeGameController;

    private SaveResumeView saveResumeView;

    // Analysis feature
    private AnalyzeViewModel analyzeViewModel;
    private AnalyzePresenter analyzePresenter;
    private AnalyzeMoveInteractor analyzeInteractor;
    private AnalyzeView analyzeView;
    private AnalyzeController analyzeController;
    private ChessApiAdapter chessApiAdaptor;

    /**
     * Configures the application frame around the given game state.
     * @param gameState the shared game state the features build on
     */
    public AppBuilder(GameState gameState) {
        this.gameState = gameState;
        saveSetup();
        changeListeningSetup();
        setTitle("Chess App");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
    }

    private void changeListeningSetup() {
        analyzeViewModel = new AnalyzeViewModel();
        analyzePresenter = new AnalyzePresenter(analyzeViewModel);

        chessApiAdaptor = new ChessApiAdapter();
        analyzeInteractor = new AnalyzeMoveInteractor(chessApiAdaptor, analyzePresenter, gameState);
    }

    private void saveSetup() {

        gameDataAccess = new FileGameDataAccessObject();
        saveGameViewModel = new SaveGameViewModel();
        saveGamePresenter = new SaveGamePresenter(saveGameViewModel);
        saveGameInteractor = new SaveGameInteractor(gameDataAccess, saveGamePresenter);
    }

    private void replaceGameState(GameState newGameState) {

        this.gameState = newGameState;

        makeMoveInteractor.setGameState(newGameState);
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
        makeMoveInteractor = new MakeMoveInteractor(moveValidator,
                gameState,
                movePresenter,
                saveGameInteractor);
        makeMoveInteractor.addPropertyChangeListener(analyzeInteractor);

        moveController = new MoveController(makeMoveInteractor);

        moveView = new MoveView(moveViewModel, moveController);
        add(moveView, CENTER);

        makeMoveInteractor.updateVisuals();
        return this;
    }

    /**
     * Wires the analysis feature and adds its view to the east.
     * @return this builder, for chaining
     */
    public AppBuilder addAnalysisView() {
        analyzeController = new AnalyzeController(analyzeInteractor);
        analyzeView = new AnalyzeView(analyzeViewModel, analyzeController);
        add(analyzeView, EAST);
        return this;
    }

    public AppBuilder addSaveResumeView() {
        saveGameController = new SaveGameController(saveGameInteractor);
        resumeGameViewModel = new ResumeGameViewModel();
        resumeGamePresenter = new ResumeGamePresenter(resumeGameViewModel);

        resumeGameInteractor = new ResumeGameInteractor(
                        gameDataAccess,
                        resumeGamePresenter,
                        saveGameInteractor);
        resumeGameController =
                new ResumeGameController(
                        resumeGameInteractor
                );
        saveResumeView = new SaveResumeView(
                        saveGameController,
                        saveGameViewModel,
                        resumeGameController,
                        resumeGameViewModel,
                        gameState,
                        this::replaceGameState);
        add(saveResumeView, WEST);
        closeButtonListener();
        return this;
    }

    private void closeButtonListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveResumeView.exitGame(AppBuilder.this);
            }
        });
    }

    /**
     * Sizes the frame to its contents and shows it.
     * @return the assembled application frame
     */
    public JFrame build() {
        pack();
        setVisible(true);
        analyzeInteractor.analyzeInitialPosition();
        return this;
    }
}
