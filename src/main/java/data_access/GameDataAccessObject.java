package data_access;

import entity.GameRecord;
import entity.GameState;
import use_case.EndGameDataAccess;

import com.google.gson.Gson;
import use_case.GameSummary;
import use_case.LeaderBoardGameDataAccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;
import use_case.ViewGameDataAccess;

import java.lang.reflect.Type;
import java.sql.ResultSet;

public class GameDataAccessObject implements EndGameDataAccess, LeaderBoardGameDataAccess,
        ViewGameDataAccess {

    private final String url = "jdbc:postgresql://aws-1-us-west-2.pooler.supabase.com:5432/postgres";
    private final String user = "postgres.mbkpcmikpoeoxcjdmbcs";
    private final String password = "CSC207H1Y2026";

    private final Gson gson = new Gson();

    public GameDataAccessObject() {}

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

            pstmt.setObject(1, game.getId());
            pstmt.setString(2, game.getTimeCreated());
            pstmt.setBoolean(3, game.isCompleted());
            pstmt.setString(4, game.getGameResult());
            String jsonHistory = gson.toJson(game.getHistory());
            pstmt.setString(5, jsonHistory);

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

    @Override
    public List<GameSummary> browse() {
        List<GameSummary> gameSummaries = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT id, time_created, is_completed, game_result " +
                "FROM games " +
                "WHERE is_completed = true " +
                "ORDER BY time_created DESC";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                LocalDateTime dateTime = rs.getObject("time_created", LocalDateTime.class);
                String timeCreated = (dateTime != null) ? dateTime.format(formatter) : "-";
                String gameResult = rs.getString("game_result");

                gameSummaries.add(new GameSummary(id, timeCreated, gameResult));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to browse games from the database", e);
        }

        return gameSummaries;
    }
}
