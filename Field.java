package tictactoe;

import static tictactoe.Mark.*;

/**
 * Resembles an instance of the field (also called board).
 */
class Field {

    private final char[][] field = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};

    /**
     * Prints the field in its current state.
     */
    public void print() {
        System.out.println("---------");
        for (char[] row : field) {
            StringBuilder rowBuilder = new StringBuilder("| "); // Outer border.
            for (char cell : row) {
                rowBuilder.append(cell).append(BLANK); // Append the cell + whitespace.
            }
            System.out.println(rowBuilder.append('|')); // Outer border.
        }
        System.out.println("---------");
    }

    public void setMark(int row, int column, Mark m) {
        field[row][column] = m.getMark();
    }

    // Supposed to work with user input which is stored as int[].
    public void playerMove(int[] coordinates, char c) {
        field[coordinates[0] - 1][coordinates[1] - 1] = c;
        print();
    }

    public void playerMove(int[] coordinates, Mark m) {
        field[coordinates[0] - 1][coordinates[1] - 1] = m.getMark();
        print();
    }

    public Mark getMark(int row, int column) {
        return field[row][column] == X.getMark() ? X :
                field[row][column] == O.getMark() ? O : BLANK;
    }

    /**
     * Determines how many Xs and Os are present on the game field.
     *
     * @return number of Xs and Os.
     */
    public int numOfMarks() {
        int countX = 0, countO = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (getMark(i, j) == X) {
                    countX++;
                } else if (getMark(i, j) == O) {
                    countO++;
                }
            }
        }
        return countX + countO;
    }

    // Checks if the given cell is a valid move (must be blank).
    public boolean available(int i, int j) {
        return field[i][j] == BLANK.getMark();
    }
}

