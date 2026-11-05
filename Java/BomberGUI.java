import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BomberGUI extends JFrame {
    private Board board;
    private JButton[][] buttons;
    private boolean gameOver;
    private boolean boom;
    private JLabel statusLabel;
    private static final int SIZE = 9;

    private final MouseAdapter cellListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            handleCellClick(e);
        }
    };

    public BomberGUI() {
        board = new Board();
        buttons = new JButton[SIZE][SIZE];

        setTitle("Сапёр");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Ходите.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].addMouseListener(cellListener);
                gridPanel.add(buttons[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Начать заново");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.addActionListener(e -> reset_game());
        add(resetButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void handleCellClick(MouseEvent e) {
        if (gameOver)
            return;
        JButton clicked = (JButton) e.getSource();

        int row = -1, col = -1;
        char rl = 'Z';
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (buttons[i][j] == clicked) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            rl = 'F';
        } else {
            rl = 'O';
        }

        if (row != -1 && col != -1 && board.make_move(row, col, rl)) {
            if ((board.get_cell(row, col).get_value() == -1) && (rl=='O')){
                gameOver = true;
                boom = true;
            }
            if ((board.get_cell(row, col).get_value() == 0) && (rl=='O')){
                open_nulls(row, col,true);
            }
            if (board.is_full()) {
                if (board.is_winner()) {
                    statusLabel.setText("Это победа");
                    gameOver = true;
                    disable_all();
                }else{
                statusLabel.setText("Лишний флаг");
                }
            }
            if (boom) {
                statusLabel.setText("Попався шаранойд.");
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        board.get_cell(i, j).open();
                    }
                }
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board.get_cell(i, j).get_is_flag()) {
                        buttons[i][j].setText("F");
                        continue;
                    }
                    if (board.get_cell(i, j).get_check()) {
                        buttons[i][j].setEnabled(false);
                        if (board.get_cell(i, j).get_value() == -1) {
                            buttons[i][j].setText("B");
                            continue;
                        }
                        if (board.get_cell(i, j).get_value() > 0) {
                            buttons[i][j].setText(String.valueOf(board.get_cell(i, j).get_value()));
                            continue;
                        }
                        if (board.get_cell(i, j).get_value() == 0) {
                            buttons[i][j].setText("0");
                            continue;
                        }
                    }
                    buttons[i][j].setText("");
                }
            }
        }
    }

    void open_nulls(int row_n, int col_n, boolean start) {
        if (row_n < 0 || row_n >= SIZE || col_n < 0 || col_n >= SIZE) {
            return;
        }
        if (board.get_cell(row_n, col_n).get_check() && !start){
            return;
        }
        if ((board.get_cell(row_n, col_n).get_value() > -1) && !(board.get_cell(row_n,col_n).get_is_flag())){
            board.get_cell(row_n, col_n).open();
            if (board.get_cell(row_n, col_n).get_value() == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (!((i == 0) && (j == 0))) {
                            open_nulls((row_n + i), (col_n + j),false);
                        }
                    }
                }
            }
        }
    }

    public void disable_all() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void reset_game() {
        board.reset();
        board.fill();
        gameOver = false;
        boom = false;
        statusLabel.setText("New game.");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
    }
}