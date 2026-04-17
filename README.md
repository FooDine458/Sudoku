# Sudoku
# Sudoku Solver — COSC 251 DSA Project

A Sudoku solver written in Java that implements two solving strategies:
1. **Plain Backtracking** — scans cells left-to-right, top-to-bottom
2. **Backtracking + MRV** — uses the Minimum Remaining Values heuristic to pick the most constrained cell first

---

## Project Info

| Field       | Detail                                  |
|-------------|-----------------------------------------|
| Course      | COSC 251 – Data Structures & Algorithms |
| Topic       | NP-Complete / Constraint Satisfaction   |
| Language    | Java                                    |
| Due Date    | April 20, 2026                          |

---

## What is a Sudoku Solver?

Sudoku is a **Constraint Satisfaction Problem (CSP)**:
- **Variables** — the 81 cells of a 9×9 grid
- **Domains** — digits 1–9 for each empty cell
- **Constraints** — no digit may repeat in any row, column, or 3×3 box

The generalized N×N version is **NP-Complete** (Yato & Seta, 2003), meaning no known polynomial-time algorithm exists for all cases.

---

## Algorithms

### 1. Plain Backtracking — `Solver.java`

```
backtrack(board):
  cell = findNextEmpty(board)       // left-to-right, top-to-bottom
  if cell is None: return true      // board is full → solved!
  for digit in 1..9:
    if isValid(board, cell, digit):
      board[cell] = digit
      if backtrack(board): return true
      board[cell] = 0               // undo (backtrack)
  return false
```

- **Time:** O(9^k) worst case, where k = number of empty cells  
- **Space:** O(k) for the recursion stack

### 2. Backtracking + MRV — `SolverMRV.java`

Same as above but replaces `findNextEmpty()` with `findMRVCell()`:

> Instead of the next cell in reading order, always pick the cell with the **fewest legal digit options**.

- A cell with 1 option is a forced move — no branching needed.
- A cell with 0 options means this branch is already dead → backtrack immediately.
- By failing faster, the search tree is pruned much earlier.

---

## File Structure

```
sudoku/
├── Main.java          # Entry point — runs demos and interactive input
├── Board.java         # Grid storage, printing, constraint checking
├── Solver.java        # Plain backtracking
├── SolverMRV.java     # Backtracking + MRV heuristic
├── Benchmark.java     # Side-by-side performance comparison
├── Puzzles.java       # Sample easy and hard grids
└── README.md
```

---

## How to Run

### Compile
```bash
javac *.java
```

### Run
```bash
java Main
```

The program will:
1. Solve an **easy** puzzle with both strategies and compare results
2. Solve a **hard** puzzle (Arto Inkala's "World's Hardest Sudoku") with both strategies
3. Prompt you to **enter your own puzzle** (type 9 rows of 9 digits, use `0` for empty)

### Example Input (custom puzzle)
```
Row 1: 530070000
Row 2: 600195000
Row 3: 098000060
Row 4: 800060003
Row 5: 400803001
Row 6: 700020006
Row 7: 060000280
Row 8: 000419005
Row 9: 000080079
```

---

## Sample Output

```
════════════════════════════════════════
  Easy Puzzle
════════════════════════════════════════

  Initial board:
┌───────┬───────┬───────┐
│ 5 3 . │ . 7 . │ . . . │
│ 6 . . │ 1 9 5 │ . . . │
│ . 9 8 │ . . . │ . 6 . │
├───────┼───────┼───────┤
│ 8 . . │ . 6 . │ . . 3 │
│ 4 . . │ 8 . 3 │ . . 1 │
│ 7 . . │ . 2 . │ . . 6 │
├───────┼───────┼───────┤
│ . 6 . │ . . . │ 2 8 . │
│ . . . │ 4 1 9 │ . . 5 │
│ . . . │ . 8 . │ . 7 9 │
└───────┴───────┴───────┘

  ┌─ Plain Backtracking SOLVED ✓
  │  Recursive calls : 4,209
  └  Time            : 3.059 ms

  ┌─ Backtracking + MRV SOLVED ✓
  │  Recursive calls : 52
  └  Time            : 0.309 ms

  ► MRV saved 4,157 recursive calls (98.8% fewer)
```

---

## Benchmark Results

| Puzzle | Plain Backtracking | MRV        | Calls Saved |
|--------|--------------------|------------|-------------|
| Easy   | 4,209 calls        | 52 calls   | 98.8% fewer |
| Hard   | 49,559 calls       | 13,811 calls | 72.1% fewer |

---

## Complexity Analysis

| | Plain Backtracking | Backtracking + MRV |
|---|---|---|
| Time (worst case) | O(9^k) | O(9^k) — smaller constant |
| Space | O(k) stack | O(k) stack + O(n²) MRV scan |
| Cell selection | Fixed order | Most constrained first |

> k = number of empty cells, n = board dimension (9)

---

## References

- Yato, T. & Seta, T. (2003). *Complexity and Completeness of Finding Another Solution and Its Application to Puzzles*. IEICE Transactions on Fundamentals.
- Russell, S. & Norvig, P. *Artificial Intelligence: A Modern Approach* — Chapter 6: Constraint Satisfaction Problems.
