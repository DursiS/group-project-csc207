package use_case;

public class AnalyzeMoveInteractor{
    private final EngineGateway engine;

    public AnalyzeMoveInteractor(EngineGateway engine) {
        this.engine = engine;
    }

    void analyze(String fen) throws Exception {
        double eval = this.engine.evaluate(fen);
        String bestMoveMessage = this.engine.bestMoveMessage(fen);
        String bestMove = this.engine.bestMove(fen);
        // Send this information to the presenter somehow
    }
}
