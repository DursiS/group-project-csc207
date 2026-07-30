package interface_adapter;

import use_case.MoveOutputBoundary;

public class MovePresenter implements MoveOutputBoundary {
    private MoveViewModel moveViewModel;

    public MovePresenter(MoveViewModel moveViewModel) {
        this.moveViewModel = moveViewModel;
    }
}
