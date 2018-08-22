/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: BruteForceSearch
 * This class is responsible for implementing the depth-first, brute-force search to solve the 
 * given Sudoku puzzle. This class utilizes the Cell class to represent board positions.
 */
package com.alexanderjhurtado.sudoku;
import static com.alexanderjhurtado.sudoku.Constants.*;

public class BruteForceSearch {
	
	// Instance Variables
	private int[][] sudokuBoard;	// Represents the Sudoku board
	
	/** Constructor: Brute-force Search
	 * --------------------------------
	 * Initializes instance variable
	 * --------------------------------	 
	 * @param sudokuBoard is the int[][] representation of the unsolved Sudoku board
	 */
	public BruteForceSearch(int[][] sudokuBoard) {
		this.sudokuBoard = sudokuBoard;		
	}
	
	/** Method: Solve
	 * --------------
	 * This method utilizes brute-force search to solve and return the Sudoku board
	 * --------------
	 * @return returns an int[][] representation of the solved Sudoku board
	 */
	public int[][] solve() {
		Cell origin = new Cell(0, 0);
		solve(origin);
		return sudokuBoard;
	}
	
	/** Method: Solve
	 * --------------
	 * This method attempts to find a solution to the Sudoku board through recursive backtracking.
	 * This method will set and check the given position on the board (represented by the given Cell)
	 * to each possible value. If the value is potentially valid, this method will move onto the next
	 * Cell and repeat the process. The method will continue to choose a value, explore possible 
	 * solutions, and unchoose the value until the given Cell is `null`, which indicates that you've
	 * iterated through all the Cells in the board and concludes the search.
	 * --------------
	 * @param curr is a Cell representing the current position on the Sudoku board being explored
	 * --------------
	 * @return returns a boolean describing whether the attempted solution has solved the Sudoku board
	 */
	private boolean solve(Cell curr) {
		if (curr == null) return true;
		if (sudokuBoard[curr.row][curr.col] != NO_VALUE) {
			return solve(curr.nextCell());
		} else {
			for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
				sudokuBoard[curr.row][curr.col] = i;
				if (isValidValue(curr) && solve(curr.nextCell())) return true;
				sudokuBoard[curr.row][curr.col] = NO_VALUE;
			}
		}
		return false;
	}
	
	/** Method: Is Valid Value
	 * -----------------------
	 * This method will check if the given Cell is potentially part of a valid solution.
	 * Essentially, this method will return `true` if the value of the given Cell is allowable
	 * (i.e. doesn't match another value in the same row, column, and box).
	 * -----------------------
	 * @param curr is a Cell representing the position on the Sudoku board being checked
	 * -----------------------
	 * @return returns a boolean describing if the given Cell is potentially a valid solution
	 */
	private boolean isValidValue(Cell curr) {
		int r = curr.row;
		int c = curr.col;
		return (isRowValid(r,c) && isColumnValid(r,c) && isBoxValid(r,c));
	}
	
	/** Method: Is Row Valid
	 * ---------------------
	 * This method will check if the value at the given position is allowed in regard to the 
	 * Sudoku row constraint. This method will return `true` if the value at the given position
	 * does not match another value in the same row.
	 * --------------------- 
	 * @param r is an int representing the row index of a position on the Sudoku board
	 * @param c is an int representing the column index of a position on the Sudoku board
	 * ---------------------
	 * @return returns a boolean describing if the value at the given position is valid row-wise
	 */
	private boolean isRowValid(int r, int c) {
		int currVal = sudokuBoard[r][c];
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (i != c && sudokuBoard[r][i] == currVal) return false;
		}
		return true;
	}
	
	/** Method: Is Column Valid
	 * ------------------------
	 * This method will check if the value at the given position is allowed in regard to the Sudoku
	 * column constraint. This method will return `true` if the value at the given position does
	 * not match another value in the same column.
	 * ------------------------
	 * @param r is an int representing the row index of a position on the Sudoku board
	 * @param c is an int representing the column index of a position on the Sudoku board
	 * ------------------------
	 * @return returns a boolean describing if the value at the given position is valid column-wise
	 */
	private boolean isColumnValid(int r, int c) {
		int currVal = sudokuBoard[r][c];
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (i != r && sudokuBoard[i][c] == currVal) return false;
		}
		return true;
	}
	
	/** Method: Is Box Valid
	 * ---------------------
	 * This method will check if the value at the given position is allowed in regard to the Sudoku
	 * box constraint. This method will return `true` if the value at the given position does not
	 * match another value in the same box.
	 * ---------------------
	 * @param r is an int representing the row index of a position on the Sudoku board
	 * @param c is an int representing the column index of a position on the Sudoku board
	 * ---------------------
	 * @return returns a boolean describing if the value at the given position is valid box-wise
	 */
	private boolean isBoxValid(int r, int c) {
		int currVal = sudokuBoard[r][c];
		int boxR = (r / BOX_SIZE) * BOX_SIZE; // integer division
		int boxC = (c / BOX_SIZE) * BOX_SIZE; // integer division
		for (int newR = boxR; newR < boxR+BOX_SIZE; newR++) {
			for (int newC = boxC; newC < boxC+BOX_SIZE; newC++) {
				if ((newR != r && newC != c) && sudokuBoard[newR][newC] == currVal) return false;
			}
		}
		return true;
	}
	
}
