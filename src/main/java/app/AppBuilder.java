package app;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import Analysis.AnalyzeController;
import Analysis.AnalyzeMoveInteractor;
import Analysis.AnalyzePresenter;
import Analysis.AnalyzeView;
import Analysis.AnalyzeViewModel;
import Analysis.ChessApiAdapter;
import entity.GameState;
import entity.MoveValidator;
import entity.MoveValidatorBuilder;
import interface_adapter.MoveController;
import interface_adapter.MovePresenter;
import interface_adapter.MoveViewModel;
import use_case.MakeMoveInteractor;
import view.MoveView;

/**
 * Assembles the application window, wiring each feature's Clean Architecture
 * stack and adding its view to a region of the frame.
 */
public class AppBuilder extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final String CENTER = "Center";
    private static final String EAST = "East";

    private final GameState gameState;

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

    /**
     * Configures the application frame around the given game state.
     * @param gameState the shared game state the features build on
     */
    public AppBuilder(GameState gameState) {
        changeListeningSetup();
        this.gameState = gameState;
        setTitle("Chess App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
    }

    private void changeListeningSetup() {
        analyzeViewModel = new AnalyzeViewModel();
        analyzePresenter = new AnalyzePresenter(analyzeViewModel);

        chessApiAdaptor = new ChessApiAdapter();
        analyzeInteractor = new AnalyzeMoveInteractor(chessApiAdaptor, analyzePresenter);
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
        makeMoveInteractor = new MakeMoveInteractor(moveValidator, gameState, movePresenter);
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

    /**
     * Sizes the frame to its contents and shows it.
     * @return the assembled application frame
     */
    public JFrame build() {
        pack();
        setVisible(true);
        return this;
    }
}
