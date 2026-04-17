/**
 * Board.java
 * ──────────────────────────────────────────────
 * Represents a 9×9 Sudoku grid.
 *
 * Responsibilities:
 *   - Store the grid values
 *   - Print the board to the console
 *   - Check whether placing a digit is legal
 *   - Validate that a starting board has no conflicts
 *   - Copy itself (so solvers don't share state)
 */
public class Board {

    // A cell that hasn't been filled yet is stored as 0
    public static final int EMPTY = 0;
    public static final int SIZE  = 9;   // 9 rows, 9 columns
    public static final int BOX   = 3;   // each sub-box is 3×3

    // The actual grid data
    private int[][] grid;

    // ── Constructors ─────────────────────────────────────────────────────

    /** Create a Board from an existing 9×9 array (deep-copied). */
    public Board(int[][] source) {
        this.grid = deepCopy(source);
    }

    /** Create an empty Board (all zeros). */
    public Board() {
        this.grid = new int[SIZE][SIZE];
    }

    // ── Grid access ──────────────────────────────────────────────────────

    public int  get(int row, int col)              { return grid[row][col]; }
    public void set(int row, int col, int digit)   { grid[row][col] = digit; }
    public void clear(int row, int col)            { grid[row][col] = EMPTY; }
    public boolean isEmpty(int row, int col)       { return grid[row][col] == EMPTY; }

    /** Returns a deep copy so solvers can work independently. */
    public Board copy() { return new Board(grid); }

    // ── Constraint checker ───────────────────────────────────────────────

    /**
     * Returns true if placing 'digit' at (row, col) breaks no Sudoku rule.
     *
     * Three checks:
     *   1. Row   – digit must not already appear in this row
     *   2. Column – digit must not already appear in this column
     *   3. Box   – digit must not already appear in the 3×3 sub-box
     */
    public boolean isValidPlacement(int row, int col, int digit) {

        // 1. Row check
        for (int c = 0; c < SIZE; c++)
            if (grid[row][c] == digit) return false;

        // 2. Column check
        for (int r = 0; r < SIZE; r++)
            if (grid[r][col] == digit) return false;

        // 3. Box check  (find the top-left corner of the 3×3 box)
        int boxStartRow = (row / BOX) * BOX;
        int boxStartCol = (col / BOX) * BOX;
        for (int r = boxStartRow; r < boxStartRow + BOX; r++)
            for (int c = boxStartCol; c < boxStartCol + BOX; c++)
                if (grid[r][c] == digit) return false;

        return true; // all three checks passed → placement is legal
    }

    /**
     * Counts how many digits (1–9) are currently legal at (row, col).
     * Used by the MRV heuristic to rank cells by how constrained they are.
     */
    public int countLegalDigits(int row, int col) {
        int count = 0;
        for (int d = 1; d <= SIZE; d++)
            if (isValidPlacement(row, col, d)) count++;
        return count;
    }

    // ── Static board validator ────────────────────────────────────────────

    /**
     * Checks a raw int[][] for obvious errors BEFORE solving:
     *   - Must be 9×9
     *   - Values must be 0–9
     *   - No row, column, or box may have duplicate non-zero digits
     */
    public static boolean isValidStartingBoard(int[][] b) {
        if (b == null || b.length != SIZE) return false;

        for (int r = 0; r < SIZE; r++) {
            if (b[r] == null || b[r].length != SIZE) return false;
            for (int c = 0; c < SIZE; c++) {
                if (b[r][c] < 0 || b[r][c] > SIZE) return false;
            }
        }

        // Temporarily blank each filled cell and re-check it for conflicts
        int[][] copy = deepCopy(b);
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (copy[r][c] != 0) {
                    int val = copy[r][c];
                    copy[r][c] = 0;                   // blank it out
                    Board tmp = new Board(copy);
                    if (!tmp.isValidPlacement(r, c, val)) return false;
                    copy[r][c] = val;                 // restore
                }
            }
        }
        return true;
    }

    // ── Printing ──────────────────────────────────────────────────────────

    /** Prints the board with box-separating lines. Empty cells show as '.'. */
    public void print() {
        System.out.println("┌───────┬───────┬───────┐");
        for (int r = 0; r < SIZE; r++) {
            if (r == 3 || r == 6)
                System.out.println("├───────┼───────┼───────┤");
            System.out.print("│");
            for (int c = 0; c < SIZE; c++) {
                System.out.print(" " + (grid[r][c] == EMPTY ? "." : grid[r][c]));
                if (c == 2 || c == 5) System.out.print(" │");
            }
            System.out.println(" │");
        }
        System.out.println("└───────┴───────┴───────┘");
    }

    // ── Utility ───────────────────────────────────────────────────────────

    private static int[][] deepCopy(int[][] src) {
        int[][] out = new int[src.length][];
        for (int i = 0; i < src.length; i++)
            out[i] = src[i].clone();
        return out;
    }
}
