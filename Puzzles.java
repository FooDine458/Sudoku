/**
 * Puzzles.java
 * ──────────────────────────────────────────────
 * A library of sample Sudoku grids.
 *
 * Each method returns a raw int[][] where:
 *   0  = empty cell (to be solved)
 *   1–9 = pre-filled digit (given clue)
 *
 * Having puzzles here keeps Main.java clean and makes it easy to add
 * more test cases later.
 */
public class Puzzles {

    /**
     * Easy puzzle — relatively few empty cells, minimal backtracking required.
     * (Classic example used in most Sudoku tutorials.)
     */
    public static int[][] easy() {
        return new int[][] {
            {5, 3, 0,  0, 7, 0,  0, 0, 0},
            {6, 0, 0,  1, 9, 5,  0, 0, 0},
            {0, 9, 8,  0, 0, 0,  0, 6, 0},

            {8, 0, 0,  0, 6, 0,  0, 0, 3},
            {4, 0, 0,  8, 0, 3,  0, 0, 1},
            {7, 0, 0,  0, 2, 0,  0, 0, 6},

            {0, 6, 0,  0, 0, 0,  2, 8, 0},
            {0, 0, 0,  4, 1, 9,  0, 0, 5},
            {0, 0, 0,  0, 8, 0,  0, 7, 9}
        };
    }

    /**
     * Hard puzzle — very few clues, forces heavy backtracking.
     * (Known as "The World's Hardest Sudoku" — Arto Inkala, 2010.)
     * Plain backtracking needs ~50k calls; MRV cuts it to ~14k.
     */
    public static int[][] hard() {
        return new int[][] {
            {8, 0, 0,  0, 0, 0,  0, 0, 0},
            {0, 0, 3,  6, 0, 0,  0, 0, 0},
            {0, 7, 0,  0, 9, 0,  2, 0, 0},

            {0, 5, 0,  0, 0, 7,  0, 0, 0},
            {0, 0, 0,  0, 4, 5,  7, 0, 0},
            {0, 0, 0,  1, 0, 0,  0, 3, 0},

            {0, 0, 1,  0, 0, 0,  0, 6, 8},
            {0, 0, 8,  5, 0, 0,  0, 1, 0},
            {0, 9, 0,  0, 0, 0,  4, 0, 0}
        };
    }
}
