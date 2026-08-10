package app;

import javax.swing.*;
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
import SaveResume.FileGameDataAccessObject;
import SaveResume.GameDataAccess;
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

/**
 * Assembles the application window, wiring each feature's Clean Architecture
 * stack and adding its view to a region of the frame.
 */
public class GameBuilder extends JPanel {
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

    // Save/Resume feature
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
    public GameBuilder(GameState gameState) {
        this.gameState = gameState;
        saveSetup();
        changeListeningSetup();
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
    }

    private void saveSetup() {
        gameDataAccess = new FileGameDataAccessObject();
        saveGameViewModel = new SaveGameViewModel();
        saveGamePresenter = new SaveGamePresenter(saveGameViewModel);
        saveGameInteractor = new SaveGameInteractor(gameDataAccess, saveGamePresenter);
    }

    private void replaceGameState(GameState newGameState) {
        gameState = newGameState;
        makeMoveInteractor.setGameState(newGameState);
    }

    private void changeListeningSetup() {
        analyzeViewModel = new AnalyzeViewModel();
        analyzePresenter = new AnalyzePresenter(analyzeViewModel);

        chessApiAdaptor = new ChessApiAdapter();
        analyzeInteractor = new AnalyzeMoveInteractor(chessApiAdaptor, analyzePresenter, gameState);
    }

    /**
     * Wires the make-move feature and adds its view to the center.
     * @return this builder, for chaining
     */
    public GameBuilder addMoveView() {
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
    public GameBuilder addAnalysisView() {
        analyzeController = new AnalyzeController(analyzeInteractor);
        analyzeView = new AnalyzeView(analyzeViewModel, analyzeController);
        add(analyzeView, EAST);
        return this;
    }

    /**
     * Wires the save/resume feature and adds its view to the west.
     * @return this builder, for chaining
     */
    public GameBuilder addSaveResumeView() {
        saveGameController = new SaveGameController(saveGameInteractor);
        resumeGameViewModel = new ResumeGameViewModel();
        resumeGamePresenter = new ResumeGamePresenter(resumeGameViewModel);
        resumeGameInteractor = new ResumeGameInteractor(
                gameDataAccess,
                resumeGamePresenter,
                saveGameInteractor);
        resumeGameController = new ResumeGameController(resumeGameInteractor);
        saveResumeView = new SaveResumeView(
                saveGameController,
                saveGameViewModel,
                resumeGameController,
                resumeGameViewModel,
                gameState,
                this::replaceGameState);
        add(saveResumeView, WEST);
        return this;
    }

    /**
     * Runs the save prompt before closing the shared application frame.
     * @param mainFrame the application frame to close
     */
    public void exitGame(JFrame mainFrame) {
        saveResumeView.exitGame(mainFrame);
    }

    /**
     * Sizes the frame to its contents and shows it.
     * @return the assembled application frame
     */
    public JPanel build() {
        setVisible(true);
        analyzeInteractor.analyzeInitialPosition();
        return this;
    }
}
