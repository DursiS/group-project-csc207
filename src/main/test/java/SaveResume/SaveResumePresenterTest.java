package SaveResume;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaveResumePresenterTest {

    @Test
    void saveSuccessPresenterTest() {
        SaveGameViewModel viewModel = new SaveGameViewModel();
        SaveGamePresenter presenter = new SaveGamePresenter(viewModel);

        presenter.prepareSuccessSaveView(new SaveGameOutputData("save1"));

        assertEquals("save1 Successfully Saved!", viewModel.getMessage());
        assertEquals("save1", viewModel.getSavedName());
    }

    @Test
    void saveFailPresenterTest() {
        SaveGameViewModel viewModel = new SaveGameViewModel();
        SaveGamePresenter presenter = new SaveGamePresenter(viewModel);

        presenter.prepareFailSaveView("Game state cannot be null!");

        assertEquals("Game state cannot be null!", viewModel.getError());
    }

    @Test
    void overwritePresenterTest() {
        SaveGameViewModel viewModel = new SaveGameViewModel();
        SaveGamePresenter presenter = new SaveGamePresenter(viewModel);

        presenter.prepareOverwriteView("Overwrite?");

        assertEquals("Overwrite?", viewModel.getOverwriteMessage());
    }

    @Test
    void resumePresenterTest() {
        ResumeGameViewModel viewModel = new ResumeGameViewModel();
        ResumeGamePresenter presenter = new ResumeGamePresenter(viewModel);

        presenter.prepareSuccessResumeView(new ResumeGameOutputData("save1"));

        assertEquals("save1 Successfully Resumed!", viewModel.getMessage());
    }

    @Test
    void resumeFailPresenterTest() {
        ResumeGameViewModel viewModel = new ResumeGameViewModel();
        ResumeGamePresenter presenter = new ResumeGamePresenter(viewModel);

        presenter.prepareFailResumeView("Save does not exist.");

        assertEquals("Error: Save does not exist.", viewModel.getErrorMessage());
    }
}
