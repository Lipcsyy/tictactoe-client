package views;

import controllers.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JFrame {

    private JButton[][] buttons = new JButton[3][3];
    private JLabel statusLabel;

    Controller controller = null;

    public Board(Controller _controller) {

        controller = _controller;

        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                boardPanel.add(buttons[i][j]);
            }
        }

        setupButtonListeners(controller);

        add(boardPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Player X's turn");
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);

    }

    private static class ButtonClickListener implements ActionListener {
        private final int row;
        private final int col;
        private final Controller controller;

        public ButtonClickListener(int row, int col, Controller controller) {
            this.row = row;
            this.col = col;
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.handleButtonClick(row, col);
        }
    }

    private void setupButtonListeners(Controller controller) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].addActionListener(new ButtonClickListener(i, j, controller));
            }
        }
    }

    public void UpdateButton(int row, int col, String text) {
        buttons[row][col].setText(text);
    }

    public void UpdateStatus(String status) {
        statusLabel.setText(status);
    }

    public void DisableAllButtons() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setEnabled(false);
            }
        }
    }

}
