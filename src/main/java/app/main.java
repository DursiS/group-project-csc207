package app;

import data_access.InMemoryGameDataAccessObject;
import entity.Board;
import entity.GameState;

import interface_adapter.ResumeGameController;
import interface_adapter.ResumeGamePresenter;
import interface_adapter.ResumeGameViewModel;
import interface_adapter.SaveGameController;
import interface_adapter.SaveGamePresenter;
import interface_adapter.SaveGameViewModel;

import use_case.GameDataAccess;
import use_case.ResumeGameInputBoundary;
import use_case.ResumeGameInteractor;
import use_case.ResumeGameOutputBoundary;
import use_case.SaveGameInputBoundary;
import use_case.SaveGameInteractor;
import use_case.SaveGameOutputBoundary;

import view.SaveResumeView;

public class main {
    public static void main(String[] args) {

        GameDataAccess gameDataAccess =
                new InMemoryGameDataAccessObject();

        SaveGameViewModel saveViewModel =
                new SaveGameViewModel();

        SaveGameOutputBoundary savePresenter =
                new SaveGamePresenter(saveViewModel);

        SaveGameInputBoundary saveInteractor =
                new SaveGameInteractor(
                        gameDataAccess,
                        savePresenter
                );

        SaveGameController saveController =
                new SaveGameController(saveInteractor);


        ResumeGameViewModel resumeViewModel =
                new ResumeGameViewModel();

        ResumeGameOutputBoundary resumePresenter =
                new ResumeGamePresenter(resumeViewModel);

        ResumeGameInputBoundary resumeInteractor =
                new ResumeGameInteractor(
                        gameDataAccess,
                        resumePresenter
                );

        ResumeGameController resumeController =
                new ResumeGameController(resumeInteractor);


        Board board = new Board();

        GameState gameState =
                new GameState(
                        board,
                        300000,
                        300000,
                        "IN_PROCESS"
                );


        new SaveResumeView(
                saveController,
                saveViewModel,
                resumeController,
                resumeViewModel,
                gameState
        );
    }
}