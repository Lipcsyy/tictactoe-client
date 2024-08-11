package controllers;
import views.Board;
import models.*;

public class Controller {

    private final Board board;
    private final Model model;

    private final Opponent opponent;

    public Controller() {

        //Set up the game
        model = new Model();
        board = new Board(this);
        opponent = new Opponent(model);

        board.setVisible(true);
        UpdateBoardBasedOnState();
    }

    public void handleButtonClick(int row, int col) {

        //make the move
        model.MakeMove(row, col);
        UpdateBoardAndCheckWin(row,col);

        model.SwitchPlayer();

        System.out.println("FROM CONTROLLER: " + (model.IsXTurn() ? "X turn" : "O turn") );

        //Now let the ai move
        MakeAIMove();

    }

    private void MakeAIMove() {

        int [] aiMoveRowCol = opponent.FindBestMove(model.GetState());

        System.out.println("AI move row: " + aiMoveRowCol[0] + " col: " + aiMoveRowCol[1]);

        if ( aiMoveRowCol[0] != -1 && aiMoveRowCol[1] != -1 ) {

            model.MakeMove(aiMoveRowCol[0], aiMoveRowCol[1]);

            UpdateBoardAndCheckWin(aiMoveRowCol[0], aiMoveRowCol[1]);

            model.SwitchPlayer();

        }

    }

    private void UpdateBoardAndCheckWin (int row, int col) {

        board.UpdateButton(row, col, model.IsXTurn() ? "X" : "O");

        if ( model.CheckWin() ) {
            board.UpdateStatus("Player" + ( model.IsXTurn() ? "X" : "O") + "wins" );
            board.DisableAllButtons();
        }
        else if ( model.IsBoardFull() ) {
            board.UpdateStatus("Draw");
            board.DisableAllButtons();
        }
    }

    private void UpdateBoardBasedOnState() {

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    int playerBits = model.GetPlayerState(model.GetState(), 0b01);

                    if ( (playerBits & (1 << (i * 3 + j))) != 0 ) {
                        board.UpdateButton(i, j, "X");
                    }
                    else {
                        playerBits = model.GetPlayerState(model.GetState(), 0b10);
                        if ( (playerBits & (1 << (i * 3 + j))) != 0 ) {
                            board.UpdateButton(i, j, "O");
                        }
                    }
                }
            }
    }

}
