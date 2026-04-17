/**
 * SolverMRV.java
 * ──────────────────────────────────────────────
 * Solves a Sudoku board using BACKTRACKING + MRV HEURISTIC.
 *
 * What is MRV (Minimum Remaining Values)?
 * ─────────────────────────────────────────
 * Instead of always picking the next empty cell in reading order (left-to-right,
 * top-to-bottom), MRV picks the empty cell that currently has the FEWEST legal
 * digit options.
 *
 * Why does that help?
 *   • If a cell has only 1 legal digit, there's no choice to make — place it and move on.
 *   • If a cell has 0 legal digits, this branch is already dead → backtrack immediately
 *     without wasting time trying other cells.
 *   • By failing faster, we prune huge parts of the search tree early.
 *
 * In practice this cuts recursive calls by 70–99% on most puzzles compared to
 * plain backtracking.
 *
 * Complexity:
 *   Time  – Still O(9^k) worst case, but the constant factor is much smaller.
 *   Space – O(k) for the recursion stack + O(n²) to scan for the MRV cell each call.
 */
public class SolverMRV {

    private Board board;   // working copy of the puzzle
    private long  calls;   // recursive call counter for benchmarking

    // ── Constructor ───────────────────────────────────────────────────────

    /** @param puzzle the starting Board (copied internally — original unchanged) */
    public SolverMRV(Board puzzle) {
        this.board = puzzle.copy();
        this.calls = 0;
    }

    // ── Public API ────────────────────────────────────────────────────────

    /**
     * Run the MRV solver.
     * @return true if solved, false if unsolvable.
     */
    public boolean solve() {
        calls = 0;
        return backtrackMRV();
    }

    /** @return the solved board after calling solve(). */
    public Board getBoard() { return board.copy(); }

    /** @return total recursive calls made. */
    public long getCalls()  { return calls; }

    // ── Core algorithm ────────────────────────────────────────────────────

    private boolean backtrackMRV() {
        calls++;

        // Step 1: pick the MOST CONSTRAINED empty cell (MRV selection)
        int[] cell = findMRVCell();
        if (cell == null) return true;   // no empty cells → solved!

        int row = cell[0];
        int col = cell[1];

        // Step 2: try every digit 1–9 (same as plain backtracking)
        for (int digit = 1; digit <= Board.SIZE; digit++) {

            if (board.isValidPlacement(row, col, digit)) {

                board.set(row, col, digit);

                if (backtrackMRV()) return true;

                board.clear(row, col);   // backtrack
            }
        }

        return false;
    }

    // ── MRV cell selector ────────────────────────────────────────────────

    /**
     * Iterates ALL empty cells and returns the one with the fewest legal digits.
     *
     * Walk-through of the logic:
     *   - Start with minOptions = 10 (larger than any real option count of 0–9).
     *   - For each empty cell, count how many digits 1–9 are currently legal.
     *   - If this cell has fewer options than the current best, remember it.
     *   - Special case: if options == 1 we can't do better → return immediately.
     *   - Returns null only when the board is completely filled (solved).
     */
    private int[] findMRVCell() {
        int   minOptions = Board.SIZE + 1;  // sentinel: anything beats this
        int[] best       = null;

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {

                if (!board.isEmpty(r, c)) continue;  // skip filled cells

                int options = board.countLegalDigits(r, c);

                if (options < minOptions) {
                    minOptions = options;
                    best = new int[]{r, c};

                    if (minOptions == 1) return best; // forced move — can't do better
                    // (minOptions == 0 means dead branch, will return false immediately)
                }
            }
        }

        return best; // null only when board is full
    }
}
