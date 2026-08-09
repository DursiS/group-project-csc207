package interface_adapter;

import entity.GameState;
import use_case.GameDetailOutputBoundary;
import use_case.GameDetailOutputData;
import use_case.SelectGameOutputBoundary;
import use_case.SelectGameOutputData;

public class GameDetailPresenter implements SelectGameOutputBoundary, GameDetailOutputBoundary {

    private final GameDetailViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public GameDetailPresenter(GameDetailViewModel viewModel, ViewManagerModel viewManagerModel)
    {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void initializeGameDetailView(SelectGameOutputData selectGameOutputData) {
        GameState gameState = selectGameOutputData.gameState();

        viewModel.setGameRecord(selectGameOutputData.gameRecord());
        viewModel.setCurrentStateNumber(0);
        viewModel.setBoard(gameState.getBoard());
        viewModel.setBlackMilliSec(gameState.getBlackMilliSec());
        viewModel.setWhiteMilliSec(gameState.getWhiteMilliSec());
        viewModel.setHasPrevious(false);
        viewModel.setHasNext(selectGameOutputData.hasNext());
        viewModel.setGameResult(selectGameOutputData.gameResult());
        viewModel.setErrorMessage(null);
        viewModel.firePropertyChanged();

        viewManagerModel.setCurrentView(GameDetailViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareGameDetailView(GameDetailOutputData gameDetailOutputData) {
        int current = gameDetailOutputData.currentStateNumber();
        GameState gameState = gameDetailOutputData.gameState();

        viewModel.setCurrentStateNumber(current);
        viewModel.setBoard(gameState.getBoard());
        viewModel.setBlackMilliSec(gameState.getBlackMilliSec());
        viewModel.setWhiteMilliSec(gameState.getWhiteMilliSec());
        viewModel.setHasPrevious(gameDetailOutputData.hasPrevious());
        viewModel.setHasNext(gameDetailOutputData.hasNext());
        viewModel.setErrorMessage(null);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareErrorView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.firePropertyChanged();

        viewManagerModel.setCurrentView(GameListViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChanged();
    }
}
