package models;

import constants.Constants;

public class Model {

    // 00 is empty, 01 is X, 10 is O --> playerbit be
    private int state;
    private static final int PLAYER_BIT_MASK = 1 << 18;

    public Model() {
        state = 0;
    }

    public boolean IsXTurn() {

        return (state & PLAYER_BIT_MASK) == 0;
    }

    public void MakeMove(int row, int column) {

        int position = row * 3 + column;

        int playerBits = IsXTurn() ? 1 : 2;

        state = state | (playerBits << position * 2); // We are OR-ing the state with the players bits shifted in it's place

    }

    public boolean IsCellEmpty(int row, int column) {
        return (state >> (row * 3 + column)) == 0;
    }

    public int GetPlayerState(int _state,int playerBits) {

        //System.out.println("Checking player state");

        //playerBits are here either 0b01/1 (X) or 0b10/2 (Y)

        //Playerstate : 110011001
        int playerState = 0;

        for (int i = 0; i < 9; i++) {

            //Check if the current cell (represented by 2 bits) is occupied by X or O;

            if (((_state >> (i * 2) & 0b11) == playerBits)) {
                //System.out.println("Found matching playerbit");
                playerState = playerState | (1 << i);
            }
        }

        return playerState;

    }

    public boolean CheckWin() {

        int playerBits = IsXTurn() ? 0b01 : 0b10;

        for (int winningState : Constants.WINNING_STATES) {

            if ((winningState & GetPlayerState(this.state,playerBits)) == winningState) {
                return true;
            }
        }

        return false;

    }

    public void SwitchPlayer() {
        state ^= PLAYER_BIT_MASK;
    }

    public boolean IsBoardFull() {
        final int mask = 0b11;

        for (int i = 0; i < 9; i++) {
            if ((state >> (i * 2) & mask) == 0) {
                return false;  // Found an empty cell
            }
        }
        return true;  // All cells are filled
    }

    public int GetState() {
        return state;
    }

}
