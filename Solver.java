/**
 * Solver.java
 * ──────────────────────────────────────────────
 * Solves a Sudoku board using PLAIN BACKTRACKING.
 *
 * How it works (step by step):
 *   1. Scan the grid left-to-right, top-to-bottom for the first empty cell.
 *   2. Try placing digits 1–9 one at a time.
 *   3. If a digit is legal (passes row/col/box checks), place it and recurse.
 *   4. If the recursion eventually hits a dead end, UNDO the digit (backtrack)
 *      and try the next one.
 *   5. If no digit works, return false so the caller can backtrack further.
 *   6. If there are no empty cells left, the board is solved → return true.
 *
 * Complexity:
 *   Time  – O(9^k) worst case, where k = number of empty cells
 *   Space – O(k)   for the recursion call stack
 */
public class Solver {

    private Board  board;   // working copy of the puzzle
    private long   calls;   // how many times backtrack() was called

    // ── Constructor ───────────────────────────────────────────────────────

    /** @param puzzle the starting Board (copied internally — original unchanged) */
    public Solver(Board puzzle) {
        this.board = puzzle.copy();  // never modify the caller's board
        this.calls = 0;
    }

    // ── Public API ────────────────────────────────────────────────────────

    /**
     * Run the solver.
     * @return true if a solution was found, false if the puzzle is unsolvable.
     */
    public boolean solve() {
        calls = 0;
        return backtrack();
    }

    /** @return the solved (or partially solved) board after calling solve(). */
    public Board getBoard() { return board.copy(); }

    /** @return total recursive calls made — useful for benchmarking. */
    public long getCalls()  { return calls; }

    // ── Core algorithm ────────────────────────────────────────────────────

    private boolean backtrack() {
        calls++;

        // Step 1: find the next empty cell
        int[] cell = findNextEmpty();
        if (cell == null) return true;  // no empty cells → puzzle is solved!

        int row = cell[0];
        int col = cell[1];

        // Step 2: try every digit 1–9
        for (int digit = 1; digit <= Board.SIZE; digit++) {

            if (board.isValidPlacement(row, col, digit)) {

                board.set(row, col, digit);    // tentatively place the digit

                if (backtrack()) return true;  // recurse — did it lead to a solution?

                board.clear(row, col);         // NO → undo (this is the "backtrack")
            }
        }

        // No digit worked for this cell → tell the caller to backtrack
        return false;
    }

    // ── Helper ────────────────────────────────────────────────────────────

    /**
     * Scans left-to-right, top-to-bottom and returns the first empty cell as
     * [row, col], or null if the board is completely filled.
     */
    private int[] findNextEmpty() {
        for (int r = 0; r < Board.SIZE; r++)
            for (int c = 0; c < Board.SIZE; c++)
                if (board.isEmpty(r, c))
                    return new int[]{r, c};
        return null;
    }
}
