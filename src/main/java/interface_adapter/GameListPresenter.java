package interface_adapter;

import use_case.GameListOutputBoundary;
import use_case.GameListOutputData;
import use_case.GameSummary;

import java.util.List;
import java.util.UUID;

public class GameListPresenter implements GameListOutputBoundary {

    private final GameListViewModel viewModel;

    public GameListPresenter(GameListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareGameListView(GameListOutputData gameListOutputData) {
        final List<GameSummary> summaries = gameListOutputData.getSummaries();
        final Object[][] data = new Object[summaries.size()][2];
        final UUID[] ids = new UUID[summaries.size()];

        for (int i  = 0; i < summaries.size(); i++) {
            data[i][0] = summaries.get(i).timeCreated();
            data[i][1] = summaries.get(i).gameResult();
            ids[i] = summaries.get(i).id();
        }

        viewModel.setData(data);
        viewModel.setIds(ids);
        viewModel.setErrorMessage(null);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareErrorView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
        viewModel.firePropertyChanged();
    }
}
