import models.Model;
import models.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.StateVisualizer;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    int state;
    Model model;
    Opponent opponent;

    @BeforeEach
    void setup() {
        model = new Model();
        opponent = new Opponent(model);

        //Let's simulate an endgame where O comes and the next move is winning for him
        /*
         * --------
         * |x o x|
         * |x o  |
         * |     |
         * --------
         * */

        state = 0b0000000001001011001;
    }

    @Test
    public void testIsXPlayerTurn() {
        System.out.println("State");
        System.out.println(Integer.toBinaryString(state));

        assertFalse(opponent.IsXPlayersTurn(state));
    }

    @Test
    public void testIsTerminalState() {
        assertFalse(opponent.IsStateTerminal(state));
    }

    @Test
    public void testEvaluateState() {
        assertEquals(0, opponent.EvaluateState(state));
    }

    @Test
    public void testXWins() {
        state = 0b0000001001001011001;
        assertEquals(1, opponent.EvaluateState(state));
    }

    @Test
    public void testOWins() {
        state = 0b0001000001001011001;
        assertEquals(-1, opponent.EvaluateState(state));
    }

    @Test
    public void testOWinningMinimaxValue() {
        assertEquals(-1, opponent.Minimax(state));
    }

    @Test
    public void testFindBestMove1() {

        System.out.println("State");
        StateVisualizer.visualizeState(state);
        System.out.println("---------------");

        int[] bestMove = opponent.FindBestMove(state);
        System.out.println("Best move row: " + bestMove[0] + " col: " + bestMove[1]);
        assertArrayEquals(new int[]{2, 1}, bestMove);
    }

    @Test
    public void testFindBestMove2() {
        state = 0b0000000101001011001;
        assertTrue(opponent.IsXPlayersTurn(state));
        int[] bestMove = opponent.FindBestMove(state);
        assertArrayEquals(new int[]{2, 0}, bestMove);
    }
}