package archive;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import app.ViewManagerModel;
import static org.junit.jupiter.api.Assertions.*;

public class GameListInteractorTest {

    @Test
    void successTest() {
        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), "some time",
                "White wins (Checkmate)");
        List<GameSummary> gameSummaries = new ArrayList<>();
        gameSummaries.add(gameSummary);
        GameDataAccessObject testGameDataAccessObject = new GameDataAccessObject() {
            @Override
            public List<GameSummary> browse() {
                return gameSummaries;
            }
        };
        GameListPresenter testGameListPresenter = new GameListPresenter(new GameListViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareGameListView(GameListOutputData gameListOutputData) {
                assertEquals(1, gameListOutputData.getSummaries().size());
                assertEquals(gameSummary, gameListOutputData.getSummaries().get(0));
            }
        };

        GameListInteractor gameListInteractor = new GameListInteractor(testGameDataAccessObject,
                testGameListPresenter);
        gameListInteractor.getGameList();
    }

    @Test
    void errorTest() {
        GameDataAccessObject testGameDataAccessObject = new GameDataAccessObject() {
            @Override
            public List<GameSummary> browse() {
                throw new RuntimeException("E");
            }
        };
        GameListPresenter testGameListPresenter = new GameListPresenter(new GameListViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareErrorView(String errorMessage) {
                assertEquals("Could not load games: E", errorMessage);
            }
        };
        GameListInteractor gameListInteractor = new GameListInteractor(testGameDataAccessObject,
                testGameListPresenter);
        gameListInteractor.getGameList();
    }
}
