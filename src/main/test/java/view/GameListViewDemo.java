package view;

import data_access.GameDataAccessObject;
import interface_adapter.GameListController;
import interface_adapter.GameListPresenter;
import interface_adapter.GameListViewModel;
import use_case.GameListInteractor;
import use_case.GameListOutputBoundary;

import javax.swing.*;

public class GameListViewDemo {

    public static void main(String[] args) {
        final JFrame application = new JFrame("Game List Test");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(300,600);

        final GameListViewModel gameListViewModel = new GameListViewModel();
        final GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
        final GameListOutputBoundary gameListOutputBoundary = new
                GameListPresenter(gameListViewModel);
        final GameListInteractor gameListInteractor= new GameListInteractor(gameDataAccessObject,
                gameListOutputBoundary);
        final GameListController gameListController = new GameListController(gameListInteractor);
        final GameListView gameListView = new GameListView(gameListController, gameListViewModel,
                null);

        application.add(gameListView);
        application.setVisible(true);
    }
}
