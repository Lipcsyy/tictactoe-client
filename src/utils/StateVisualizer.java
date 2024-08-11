package utils;

public class StateVisualizer {
    public static void visualizeState(int state) {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                int cellState = (state >> ((i * 3 + j) * 2)) & 0b11;
                char symbol = getSymbol(cellState);
                System.out.print(symbol + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    private static char getSymbol(int cellState) {
        switch (cellState) {
            case 0b01:
                return 'X';
            case 0b10:
                return 'O';
            default:
                return ' ';
        }
    }
}
