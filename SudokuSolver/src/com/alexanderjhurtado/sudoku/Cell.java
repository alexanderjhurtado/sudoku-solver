/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: Cell
 * This class is used in BruteForceSearch to represent positions in the Sudoku board. This class
 * also determines what the "next" Cell is for a given Cell.
 */
package com.alexanderjhurtado.sudoku;
import static com.alexanderjhurtado.sudoku.Constants.*;

public class Cell {
	
	// Instance Variables
	public int row, col;
	
	/** Constructor: Cell
	 * ------------------
	 * Initializes instance variables
	 * ------------------
	 * @param row is an int representing the row index of `this` Cell in the Sudoku board
	 * @param col is an int representing the column index of `this` Cell in the Sudoku board
	 */
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/** Method: Next Cell
	 * ------------------
	 * This method creates and returns a Cell that represents the appropriate position that follows
	 * `this` Cell. This method increments a Cell's `col` variable. If that `col` would be out-of-bounds,
	 * this method will then reset `col` and increment `row`. In the case that `row` would be 
	 * out-of-bounds, this method will return null, which indicates that you've reached the end of
	 * the board and no more Cells exist after `this` Cell.
	 * ------------------
	 * @return returns a Cell with the appropriate `row` and `col` variables
	 */
	public Cell nextCell() {
		int row = this.row;
		int col = this.col;
		col++;
		if (col > BOARD_SIZE-1) {
			row++;
			col = 0;
		}
		if (row > BOARD_SIZE-1) {
			return null;
		}
		return new Cell(row, col);
	}
	
}