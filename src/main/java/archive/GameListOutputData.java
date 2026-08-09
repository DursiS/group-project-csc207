package archive;

import java.util.List;

public class GameListOutputData {

    private final List<GameSummary> summaries;

    public GameListOutputData(List<GameSummary> summaries) {
        this.summaries = summaries;
    }

    public List<GameSummary> getSummaries() {
        return summaries;
    }
}
