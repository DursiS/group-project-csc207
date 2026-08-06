package app;
import entity.Board;
import entity.GameState;
import entity.BoardStateList;

import data_access.InMemoryGameDataAccessObject;

import use_case.GameDataAccess;
import use_case.ResumeGameInputBoundary;
import use_case.ResumeGameInteractor;
import use_case.SaveGameInputBoundary;
import use_case.SaveGameInteractor;

import java.util.ArrayList;

public class saveresumeTest {
    public static void main(String[] args) {

        // Create the DAO.
        GameDataAccess gameDataAccess =
                new InMemoryGameDataAccessObject();

        // Connect the DAO to the interactors.
        SaveGameInputBoundary saveInteractor =
                new SaveGameInteractor(gameDataAccess);

        ResumeGameInputBoundary resumeInteractor =
                new ResumeGameInteractor(gameDataAccess);

        // Create a test board.
        Board board = new Board();

        // Store the board in the history.
        BoardStateList boardStateList =
                new BoardStateList();

        boardStateList.addBoardCopy(board);

        // Create a test GameState.
        GameState gameState =
                new GameState(
                        board,
                        300000,
                        295000,
                        boardStateList,
                        "IN_PROGRESS"
                );

        // Test manual save.
        saveInteractor.execute(
                "testGame",
                gameState
        );

        System.out.println("Game saved.");

        // Test manual resume.
        GameState loadedGame =
                resumeInteractor.execute(
                        "testGame"
                );

        System.out.println("Game loaded.");

        System.out.println(
                "White time: "
                        + loadedGame.getWhiteMilliSec()
        );

        System.out.println(
                "Black time: "
                        + loadedGame.getBlackMilliSec()
        );

        System.out.println(
                "History size: "
                        + loadedGame
                        .getBoardStateListCopy()
                        .size()
        );

        // Test autosave.
        saveInteractor.autosave(gameState);

        GameState autosavedGame =
                resumeInteractor.recoverAutosave();

        System.out.println(
                "Autosave recovered: "
                        + (autosavedGame != null)
        );

        // Test listing all save names.
        ArrayList<String> saveNames =
                gameDataAccess.getSaveNames();

        System.out.println("All saves:");

        for (int i = 0; i < saveNames.size(); i++) {
            System.out.println(saveNames.get(i));
        }
    }

}
