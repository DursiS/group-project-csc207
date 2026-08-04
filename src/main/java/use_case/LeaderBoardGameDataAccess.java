package use_case;

import entity.GameRecord;

import java.util.List;

public interface LeaderBoardGameDataAccess {

    /**
     * Browse all completed games.
     * @return a list of completed game summaries from latest to earliest
     */
    List<GameSummary> browse();
}
