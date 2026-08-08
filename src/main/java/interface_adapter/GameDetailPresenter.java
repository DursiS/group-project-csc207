package interface_adapter;

import entity.GameState;
import use_case.GameDetailOutputBoundary;
import use_case.GameDetailOutputData;

import java.util.UUID;

public class GameDetailPresenter implements GameDetailOutputBoundary {

    private final GameDetailViewModel viewModel;

    public GameDetailPresenter(GameDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareGameDetailView(GameDetailOutputData gameDetailOutputData) {
        UUID gameId = gameDetailOutputData.gameId();
        int current = gameDetailOutputData.currentStateNumber();
        GameState gameState = gameDetailOutputData.gameState();
        String gameResult = gameDetailOutputData.gameResult();

        viewModel.setGameId(gameId);
        viewModel.setCurrentStateNumber(current);
        viewModel.setBoard(gameState.getBoard());
        viewModel.setGameResult(gameResult);
        viewModel.setBlackMilliSec(gameState.getBlackMilliSec());
        viewModel.setWhiteMilliSec(gameState.getWhiteMilliSec());
        viewModel.setErrorMessage(null);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareErrorView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.firePropertyChanged();
    }
}
