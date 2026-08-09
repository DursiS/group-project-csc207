package archive;

import app.ViewManager;
import app.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

public class GameListViewDemo {

    public static void main(String[] args) {
        final JFrame application = new JFrame("Game List Test");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(600,600);
        CardLayout cardLayout = new CardLayout();
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        final ViewManager viewManager = new ViewManager(views, cardLayout, viewManagerModel);
        final GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
        final GameDetailViewModel gameDetailViewModel = new GameDetailViewModel();
        final GameDetailPresenter gameDetailPresenter = new
                GameDetailPresenter(gameDetailViewModel, viewManagerModel);
        final GameDetailInteractor gameDetailInteractor = new
                GameDetailInteractor(gameDetailPresenter);
        final GameDetailController gameDetailController=
                new GameDetailController(gameDetailInteractor);
        final GameDetailView gameDetailView = new
                GameDetailView(gameDetailController, gameDetailViewModel);

        final SelectGameInteractor selectGameInteractor = new
                SelectGameInteractor(gameDataAccessObject,  gameDetailPresenter);

        final GameListViewModel gameListViewModel = new GameListViewModel();
        final GameListOutputBoundary gameListOutputBoundary = new
                GameListPresenter(gameListViewModel, viewManagerModel);
        final GameListInteractor gameListInteractor= new GameListInteractor(gameDataAccessObject,
                gameListOutputBoundary);
        final GameListController gameListController = new
                GameListController(gameListInteractor, selectGameInteractor);
        final GameListView gameListView = new GameListView(gameListController, gameListViewModel);

        views.add(gameListView, GameListViewModel.VIEW_NAME);
        views.add(gameDetailView, GameDetailViewModel.VIEW_NAME);

        gameListController.getGameList();
        application.setVisible(true);
    }
}
