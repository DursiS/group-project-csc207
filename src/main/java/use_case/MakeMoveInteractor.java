//this class is responsible for receiving the user's button press through some interface,
// checking whether it is a valid move, and if so,
//GameState owns the board


//clean architecture components required for move:
//view,
// view model, controller, presenter
// input boundary, output boundary (both interfaces) + input data, output data(both data objects)

//no data access interface required. maybe pass something to gamestate for it to do that purpose...
//





package use_case;

import entity.MoveValidator;

public class MakeMoveInteractor {
    private MoveValidator validator;



}
