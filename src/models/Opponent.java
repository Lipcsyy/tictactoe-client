package models;

import constants.Constants;
import utils.StateVisualizer;

import java.util.ArrayList;
import java.util.List;


public class Opponent {

    // 0b01 is X and 0b10 is O
    //X is maxing and O is minimizing

    Model model;

    public Opponent(Model _model) {
        model = _model;
    }

    public int Minimax( int state ){

        //First we need to check whether it's the end of the game or not --> If it's the end of the game we can just evaluate that position
        if ( IsStateTerminal(state) ) {
            return EvaluateState(state);
        }

        List<Integer> possibleStates = PossibleStates(state);

        //Now we need to check how is coming, if X is coming we are maximizing
        if (IsXPlayersTurn(state)) {
            System.out.println("X is maximizing");
            int value = Integer.MIN_VALUE;
            for (int possibleState : possibleStates) {
                value = Math.max(value, Minimax(possibleState));
            }
            return value;
        } else {
            System.out.println("O is minimizing");
            int value = Integer.MAX_VALUE;
            for (int possibleState : possibleStates) {
                value = Math.min(value, Minimax(possibleState));
            }
            return value;
        }

    }

    //Somebody won or it's draw
    public boolean IsStateTerminal(int state) {

        int[] playerBits = {0b01, 0b10};

        for ( int playerBit : playerBits ) {
            for (int winningState : Constants.WINNING_STATES) {

                if ((winningState & model.GetPlayerState(state,playerBit)) == winningState) {
                    return true;
                }
            }
        }

        return PossibleStates(state).isEmpty();

    }

    public int EvaluateState(int state) {

        //X is maxing so if X wins we'll get 1 and O is minimizing so if O wins we'll get -1

        int[] playerBits = {0b01, 0b10};

        for ( int playerBit : playerBits ) {
            for (int winningState : Constants.WINNING_STATES) {
                if ((winningState & model.GetPlayerState(state,playerBit)) == winningState) {
                    return playerBit == 1 ? 1 : -1;
                }
            }
        }

        System.out.println("Draw");

        return 0;

    }

    public boolean IsXPlayersTurn ( int state ) {

        //System.out.println("Checking turn");

        //Only based on the board state we can determine whose turn it is, not using the model

        //The 19th bit is the player bit

        int XState = model.GetPlayerState(state, 0b01);
        int OState = model.GetPlayerState(state, 0b10);

        //System.out.println("Xstate:" + Integer.toBinaryString(XState));
        //System.out.println("Ostate:" + Integer.toBinaryString(OState));

        int stateTogether = XState | OState;

        //System.out.println(Integer.toBinaryString(stateTogether));

        return Integer.bitCount(stateTogether) % 2 == 0;

    }

    private List<Integer> PossibleStates(int state) {
        List<Integer> possibleStates = new ArrayList<>();
        int playerBit = IsXPlayersTurn(state) ? 0b01 : 0b10;

        for (int i = 0; i < 9; i++) {
            if ((state >> (i * 2) & 0b11) == 0) {
                int newState = state | (playerBit << (i * 2));
                possibleStates.add(newState);
            }
        }

        return possibleStates;
    }

    public int[] FindBestMove(int state) {

        List<Integer> possibleStates = PossibleStates(state);

        System.out.println("Possible states: " + possibleStates.size());

        int bestValue = IsXPlayersTurn(state) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestMove = -1;

        for (int possibleState : possibleStates) {

            int value = Minimax(possibleState);

            System.out.println("Value of possible state: " + value);
            StateVisualizer.visualizeState(possibleState);

            if (IsXPlayersTurn(state) && value > bestValue) {
                bestValue = value;
                bestMove = possibleState;
            } else if (!IsXPlayersTurn(state) && value < bestValue) {
                bestValue = value;
                bestMove = possibleState;
            }
        }

        // Convert the best move state back to row and column
        for (int i = 0; i < 9; i++) {
            if (((state >> (i * 2)) & 0b11) == 0 && ((bestMove >> (i * 2)) & 0b11) != 0) {
                return new int[]{i / 3, i % 3};
            }
        }
        return new int[]{-1, -1};  // Invalid move

    }

}
