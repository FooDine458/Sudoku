/**
 * Benchmark.java
 * ──────────────────────────────────────────────
 * Runs BOTH solvers (plain backtracking and MRV) on the same puzzle
 * and prints a side-by-side performance comparison.
 *
 * This is purely a utility class — it contains no algorithm logic.
 * It just wires together Board, Solver, and SolverMRV and formats the output.
 */
public class Benchmark {

    /**
     * Run the benchmark for a given puzzle.
     *
     * @param rawGrid  a 9×9 int[][] (0 = empty)
     * @param label    a human-readable name shown in the output (e.g. "Easy")
     */
    public static void run(int[][] rawGrid, String label) {

        System.out.println("\n════════════════════════════════════════");
        System.out.println("  " + label + " Puzzle");
        System.out.println("════════════════════════════════════════");

        Board puzzle = new Board(rawGrid);

        System.out.println("\n  Initial board:");
        puzzle.print();

        // ── Plain Backtracking ───────────────────────────────────────────

        Solver plain = new Solver(puzzle);

        long startPlain = System.nanoTime();
        boolean solvedPlain = plain.solve();
        long timePlain = System.nanoTime() - startPlain;

        System.out.println("\n  ┌─ Plain Backtracking " + (solvedPlain ? "SOLVED ✓" : "No solution ✗"));
        if (solvedPlain) plain.getBoard().print();
        System.out.printf("  │  Recursive calls : %,d%n", plain.getCalls());
        System.out.printf("  └  Time            : %.3f ms%n", timePlain / 1_000_000.0);

        // ── Backtracking + MRV ───────────────────────────────────────────

        SolverMRV mrv = new SolverMRV(puzzle);

        long startMRV = System.nanoTime();
        boolean solvedMRV = mrv.solve();
        long timeMRV = System.nanoTime() - startMRV;

        System.out.println("\n  ┌─ Backtracking + MRV " + (solvedMRV ? "SOLVED ✓" : "No solution ✗"));
        if (solvedMRV) mrv.getBoard().print();
        System.out.printf("  │  Recursive calls : %,d%n", mrv.getCalls());
        System.out.printf("  └  Time            : %.3f ms%n", timeMRV / 1_000_000.0);

        // ── Comparison summary ───────────────────────────────────────────

        if (solvedPlain && solvedMRV) {
            long savedCalls = plain.getCalls() - mrv.getCalls();
            double pct      = 100.0 * savedCalls / plain.getCalls();
            System.out.printf("%n  ► MRV saved %,d recursive calls (%.1f%% fewer)%n",
                    savedCalls, pct);
        }
    }
}
