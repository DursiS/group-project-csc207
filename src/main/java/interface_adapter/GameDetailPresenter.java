package interface_adapter;

import entity.GameState;
import use_case.GameDetailOutputBoundary;
import use_case.GameDetailOutputData;

import java.util.UUID;

public class GameDetailPresenter implements GameDetailOutputBoundary {

    private final GameDetailViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public GameDetailPresenter(GameDetailViewModel viewModel, ViewManagerModel viewManagerModel)
    {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
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
        viewModel.setHasPrevious(gameDetailOutputData.hasPrevious());
        viewModel.setHasNext(gameDetailOutputData.hasNext());
        viewModel.setErrorMessage(null);
        viewModel.firePropertyChanged();

        viewManagerModel.setCurrentView(GameDetailViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareErrorView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.firePropertyChanged();

        viewManagerModel.setCurrentView(GameListViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChanged();
    }
}
