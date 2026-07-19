package view;

import interface_adapter.AnalyzeController;
import interface_adapter.AnalyzeViewModel;

public class AnalyzeView {
    AnalyzeViewModel viewModel =  new AnalyzeViewModel();
    AnalyzeController controller = new AnalyzeController();

    // Have action listeners wait for controller changes and run methods
    // that eventually update the view model
}
