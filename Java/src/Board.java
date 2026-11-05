package src;
import java.util.Random;

public class Board {
    Random random = new Random();
    private static final int SIZE = 9;
    private Cell[][] grid;
    private static final int BOMBS = 10;

    public Board() {
        grid = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell();
            }
        }
        reset();
        fill();
    }

    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j].clear_all();
            }
        }
    }

    public void fill() {
        int bombs_on_board = 0;
        while (bombs_on_board < BOMBS) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (grid[row][col].get_value() != -1) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if ((row + i >= 0) && (row + i < SIZE) && (col + j >= 0) && (col + j < SIZE)) {
                            grid[row + i][col + j].increase();
                        }
                    }
                }
                grid[row][col].bomb();
                bombs_on_board = bombs_on_board + 1;
            }
        }
    }

    public boolean make_move(int row, int col, char code) {
        if ((row < 0 || row >= SIZE) || (col < 0 || col >= SIZE)) {
            return false;
        }
        if (grid[row][col].get_check()) {
            return false;
        }
        if (code == 'F') {
            grid[row][col].flag();
            return true;
        }
        grid[row][col].open();
        return true;
    }

    public boolean is_winner() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((grid[i][j].get_is_flag()) && (grid[i][j].get_value() > -1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean is_full() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!grid[i][j].get_check() && !grid[i][j].get_is_flag()){
                    return false;
                }
            }
        }
        return true;
    }

    public Cell get_cell(int row, int col) {
        return grid[row][col];
    }
}