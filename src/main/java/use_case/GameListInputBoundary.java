package use_case;

import java.sql.SQLException;

public interface GameListInputBoundary {

    /**
     * Get game summaries for the game list
     */
    void getGameList();
}
