/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: Constants
 * This final class is responsible for storing constants related to Sudoku puzzles. In order to
 * reference these constants, each class uses a static import of this class.
 */
package com.alexanderjhurtado.sudoku;

public final class Constants {
	
	private Constants() {/*empty*/}
	
	/** The size of each side of the Sudoku board **/
	public static final int BOARD_SIZE = 9;
	/** The number of values in the Sudoku puzzle, closely related to the size of the board **/
	public static final int NUM_VALUES = 9;
	/** The size of each side of the box subsections of the Sudoku board **/
	public static final int BOX_SIZE = 3;
	/** The number of constraints in the Sudoku puzzle (Cell, Row, Column, & Box) **/
	public static final int NUM_CONSTRAINTS = 4;
	/** The value of each cell in the Sudoku puzzle if there is no initial value between MIN_VALUE and MAX_VALUE **/
	public static final int NO_VALUE = 0;	
	/** The minimum value that can exist in the Sudoku puzzle **/
	public static final int MIN_VALUE = 1;
	/** The maximum value that can exist in the Sudoku puzzle **/
	public static final int MAX_VALUE = 9;

}
