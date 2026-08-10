package archive;

import java.util.List;

public interface GameListDataAccess {

    /**
     * Browse all completed games.
     * @return a list of completed game summaries from latest to earliest
     */
    List<GameSummary> browse();
}
