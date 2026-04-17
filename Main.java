import java.util.Scanner;

/**
 * Main.java
 * ──────────────────────────────────────────────
 * Entry point for the Sudoku Solver program.
 *
 * What this file does:
 *   1. Runs the easy puzzle  → Benchmark.run()
 *   2. Runs the hard puzzle  → Benchmark.run()
 *   3. Prompts the user to enter their own puzzle and solves it
 *
 * All algorithm logic lives in:
 *   Board.java      – grid storage + constraint checking
 *   Solver.java     – plain backtracking
 *   SolverMRV.java  – backtracking + MRV heuristic
 *   Benchmark.java  – side-by-side performance comparison
 *   Puzzles.java    – sample grids
 *
 * How to compile and run:
 *   javac *.java
 *   java Main
 */
public class Main {

    public static void main(String[] args) {

        // ── Run the two built-in examples ────────────────────────────────
        Benchmark.run(Puzzles.easy(), "Easy");
        Benchmark.run(Puzzles.hard(), "Hard");

        // ── Interactive: let the user enter their own puzzle ──────────────
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ENTER YOUR OWN PUZZLE");
        System.out.println("  Type 9 rows of 9 digits (0 = empty).");
        System.out.println("  Example row: 530070000");
        System.out.println("════════════════════════════════════════");

        int[][] custom = readPuzzleFromUser();

        if (custom != null) {
            if (!Board.isValidStartingBoard(custom)) {
                System.out.println("  ✗ Invalid board — duplicate digits detected.");
            } else {
                Benchmark.run(custom, "Custom");
            }
        }
    }

    // ── User input helper ─────────────────────────────────────────────────

    /**
     * Reads 9 lines from stdin, each containing exactly 9 digits.
     * Returns the parsed int[][] or null if the input is malformed.
     */
    private static int[][] readPuzzleFromUser() {
        Scanner sc  = new Scanner(System.in);
        int[][] out = new int[Board.SIZE][Board.SIZE];

        for (int r = 0; r < Board.SIZE; r++) {
            System.out.printf("  Row %d: ", r + 1);
            String line = sc.nextLine().trim().replaceAll("\\s+", "");

            // Validate: must be exactly 9 characters
            if (line.length() != Board.SIZE) {
                System.out.println("  ✗ Expected exactly 9 digits. Aborting.");
                return null;
            }

            // Parse each character as a digit 0–9
            for (int c = 0; c < Board.SIZE; c++) {
                char ch = line.charAt(c);
                if (ch < '0' || ch > '9') {
                    System.out.println("  ✗ Non-digit character '" + ch + "' detected. Aborting.");
                    return null;
                }
                out[r][c] = ch - '0';
            }
        }

        return out;
    }
}
