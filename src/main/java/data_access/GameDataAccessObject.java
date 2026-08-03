package data_access;

import entity.GameRecord;
import entity.GameState;
import use_case.EndGameDataAccess;

import com.google.gson.Gson;
import use_case.LeaderBoardGameDataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.ResultSet;

public class GameDataAccessObject implements EndGameDataAccess, LeaderBoardGameDataAccess {

    private final String url = "jdbc:postgresql://aws-1-us-west-2.pooler.supabase.com:5432/postgres";
    private final String user = "postgres.mbkpcmikpoeoxcjdmbcs";
    private final String password = "CSC207H1Y2026";

    private final Gson gson = new Gson();

    @Override
    public void save(GameRecord game) {
        String sql = "INSERT INTO games (id, time_created, is_completed, game_result, history) " +
                "VALUES (?, ?::TIMESTAMPTZ, ?, ?, ?::jsonb) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "is_completed = EXCLUDED.is_completed, " +
                "game_result = EXCLUDED.game_result, " +
                "history = EXCLUDED.history";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, game.getTimeCreated());
            pstmt.setBoolean(2, game.isCompleted());
            pstmt.setString(3, game.getGameResult());
            String jsonHistory = gson.toJson(game.getHistory());
            pstmt.setString(4, jsonHistory);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save game to the database", e);
        }
    }

    @Override
    public GameRecord load(UUID id) {
        String sql = "SELECT * FROM games WHERE id = ?";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String timeCreated = rs.getString("time_created");
                    boolean isCompleted = rs.getBoolean("is_completed");
                    String gameResult = rs.getString("game_result");

                    String historyJson = rs.getString("history");
                    Type listType = new TypeToken<ArrayList<GameState>>(){}.getType();
                    ArrayList<GameState> history = gson.fromJson(historyJson, listType);

                    return new GameRecord(id, history, timeCreated, isCompleted, gameResult);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load game from the database", e);
        }
        return null; // game not found
    }
}
