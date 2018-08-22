/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: DancingLinks
 * This class constructs a toroidal doubly-linked list using the Node and ColumnHeader classes.
 * This class is responsible for implementing and using the Dancing Links technique to solve the
 * given Sudoku puzzle.
 */
package com.alexanderjhurtado.sudoku;
import java.util.ArrayList;
import java.util.Arrays;
import static com.alexanderjhurtado.sudoku.Constants.*;

public class DancingLinks {
	
	// Instance Variables
	private boolean[][] coverMatrix; // Represents the Exact Cover matrix
	private int[][] sudokuBoard; // Represents the Sudoku Board	
	private ColumnHeader root; // First entry in columnList; the root of the entire structure
	
	/** Constructor: Dancing Links
	 * ---------------------------
	 * This constructor initializes the instance variables. Additionally, this constructor creates
	 * a boolean[][] representation of the Exact Cover matrix and utilizes that to create the toroidal
	 * doubly-linked list structure that defines the Dancing Links technique.
	 * ---------------------------
	 * @param sudokuBoard is the int[][] representation of the unsolved Sudoku board
	 */
	public DancingLinks(int[][] sudokuBoard) {
		this.sudokuBoard = sudokuBoard;
		this.coverMatrix = new boolean[BOARD_SIZE * BOARD_SIZE * NUM_VALUES][BOARD_SIZE * BOARD_SIZE * NUM_CONSTRAINTS];
		this.root = new ColumnHeader();
		initializeCoverMatrix();
		createDancingLinks();
	}
	
	/** Method: Initialize Cover Matrix
	 * --------------------------------
	 * This method creates the general matrix for a Sudoku Exact Cover problem and then utilizes
	 * the currently unsolved Sudoku board to update the Exact Cover matrix to perfectly model
	 * the exact unsolved Sudoku puzzle.
	 */
	private void initializeCoverMatrix() {
		applyConstraintsCover();
		updateCoverMatrix();
	}
	
	/** Method: Apply Constraints Cover Matrix
	 * ---------------------------------------
	 * This method will change the appropriate values in the Exact Cover matrix such that it
	 * models the 4 constraints of Sudoku: Cell, Row, Column, and Box.
	 * 
	 * WARNING: To be honest, I'm not sure if the methods called in this method scale with different
	 * dimensions of the Sudoku board. I made these methods by finding patterns in the 729x324
	 * Exact Cover Matrix for the normal 9x9 Sudoku board, so this method may not work for 16x16 Sudoku.
	 * 
	 * Full Exact Cover Matrix found here: https://www.stolaf.edu/people/hansonr/sudoku/exactcovermatrix.htm
	 */
	private void applyConstraintsCover() {
		applyCellConstraint();
		applyRowConstraint(BOARD_SIZE*BOARD_SIZE);
		applyColumnConstraint(BOARD_SIZE*BOARD_SIZE*2);
		applyBoxConstraint(BOARD_SIZE*BOARD_SIZE*3);
	}
	
	/** Method: Apply Cell Constraint
	 * ------------------------------
	 * This method modifies the appropriate values in the Exact Cover matrix to model the Cell
	 * constraint of Sudoku (i.e. each cell contains exactly one value). This constraint occupies
	 * the first quarter of the full Exact Cover matrix.
	 * 
	 *  81x81 -> 9x1
	 * =============
	 * |O...|    |1|
	 * |.+..| -> |+|
	 * |..+.|    |+|
	 * |...O|    |1|
	 * 
	 * ### Above is a matrix model of how the constraint appears in its 724x81 portion of the full 
	 * ### Exact Cover matrix. "." represents empty space, "+" functions as ellipsis, "1" represents
	 * ### the value `true`, and "O" represents a 'subgroup'. The "->" magnifies one 'subgroup' and 
	 * ### models what each subgroup looks like. The dimensions are in terms of subgroups.
	 */
	private void applyCellConstraint() {
		for (int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++) {
			for (int n = 0; n < NUM_VALUES; n++) {
				coverMatrix[i*NUM_VALUES + n][i] = true;
			}					
		}
	}
	
	/** Method: Apply Row Constraint
	 * -----------------------------
	 * This method modifies the appropriate values in the Exact Cover matrix to model the Row
	 * constraint of Sudoku (i.e. each row contains each value exactly once). This constraint
	 * occupies the second quarter of the full Exact Cover matrix.
	 * 
	 *    9x9 -> 9x1 -> 9x9
	 * =======================
	 * |O...|    |O|    |1...|
	 * |.+..| -> |+| -> |.+..|
	 * |..+.|    |+|    |..+.|
	 * |...O|    |O|    |...1|
	 * 
	 * ### Above is a matrix model of how the constraint appears in its 724x81 portion of the full 
	 * ### Exact Cover matrix. "." represents empty space, "+" functions as ellipsis, "1" represents
	 * ### the value `true`, and "O" represents a 'subgroup'. The "->" magnifies one 'subgroup' and 
	 * ### models what each subgroup looks like. The dimensions are in terms of subgroups.
	 * -----------------------------
	 * @param horizOffset is an int representing the starting column index for this constraint's section
	 */
	private void applyRowConstraint(int horizOffset) {
		for (int i = 0; i < BOARD_SIZE; i++) { // matrix 1
			for (int n = 0; n < BOARD_SIZE; n++) { // matrix 2
				downRightDiagonal(i*NUM_VALUES*BOARD_SIZE + n*NUM_VALUES, horizOffset + i*BOARD_SIZE); // matrix 3				
			}
		}
	}
	
	/** Method: Down Right Diagonal
	 * ----------------------------
	 * This method takes a starting position and creates a diagonal of `true` going directly down
	 * and to the right on the Exact Cover matrix. The 'size' of the diagonal is NUM_VALUES.
	 * 
	 * For example, if NUM_VALUES = 5:
	 * |1....|
	 * |.1...|
	 * |..1..|
	 * |...1.|	
	 * |....1|
	 * (where 1 represents the value `true`)
	 * ----------------------------
	 * @param startR is an int representing the starting row index of the diagonal
	 * @param startC is an int representing the starting column index of the diagonal
	 */
	private void downRightDiagonal(int startR, int startC) {
		for (int i = 0; i < NUM_VALUES; i++) {
			coverMatrix[startR+i][startC+i] = true;
		}
	}
	
	/** Method: Apply Column Constraint
	 * --------------------------------
	 * This method modifies the appropriate values in the Exact Cover matrix to model the Column
	 * constraint of Sudoku (i.e. each column contains each value exactly once). This constraint
	 * occupies the third quarter of the full Exact Cover matrix.
	 * 
	 * 9x1 ->  9x9   ->  9x9
	 * =======================
	 * |O|    |O...|    |1...|
	 * |+| -> |.+..| -> |.+..|
	 * |+|    |..+.|    |..+.|
	 * |O|    |...O|    |...1|
	 * 
	 * ### Above is a matrix model of how the constraint appears in its 724x81 portion of the full 
	 * ### Exact Cover matrix. "." represents empty space, "+" functions as ellipsis, "1" represents
	 * ### the value `true`, and "O" represents a 'subgroup'. The "->" magnifies one 'subgroup' and 
	 * ### models what each subgroup looks like. The dimensions are in terms of subgroups.
	 * --------------------------------
	 * @param horizOffset is an int representing the starting column index for this constraint
	 */
	private void applyColumnConstraint(int horizOffset) {
		for (int i = 0; i < BOARD_SIZE; i++) { // matrix 1
			for (int n = 0; n < BOARD_SIZE; n++) { // matrix 2
				downRightDiagonal(i*NUM_VALUES*BOARD_SIZE + n*NUM_VALUES, horizOffset + n*NUM_VALUES); // matrix 3
			}
		}
	}
	
	/** Method: Apply Box Constraint
	 * -----------------------------
	 * This method modifies the appropriate values in the Exact Cover matrix to model the Box
	 * constraint of Sudoku (i.e. each box contains each value exactly once). This constraint
	 * occupies the fourth quarter of the full Exact Cover matrix.
	 * 
	 *  3x3  -> 3x1 ->  3x3  -> 3x1 ->  9x9
	 * =====================================
	 * |O..|    |O|    |O..|    |O|    |1..|
	 * |.+.| -> |+| -> |.+.| -> |+| -> |.+.|
	 * |..O|    |O|    |..O|    |O|    |..1|
	 * 
	 * ### Above is a matrix model of how the constraint appears in its 724x81 portion of the full 
	 * ### Exact Cover matrix. "." represents empty space, "+" functions as ellipsis, "1" represents
	 * ### the value `true`, and "O" represents a 'subgroup'. The "->" magnifies one 'subgroup' and 
	 * ### models what each subgroup looks like. The dimensions are in terms of subgroups.
	 * -----------------------------
	 * @param horizOffset is an int representing the starting column index for this constraint
	 */
	private void applyBoxConstraint(int horizOffset) {
		for (int i = 0; i < BOX_SIZE; i++) { // matrix 1
			for (int n = 0; n < BOX_SIZE; n++) { // matrix 2
				for (int k = 0; k < BOX_SIZE; k++) { // matrix 3
					stackedDiagonals(i*NUM_VALUES*BOARD_SIZE*BOX_SIZE + n*NUM_VALUES*BOARD_SIZE + k*NUM_VALUES*BOX_SIZE,
							horizOffset + i*NUM_VALUES*BOX_SIZE + k*NUM_VALUES); // matrix 4+5
				}
			}
		}
	}
	
	/** Method: Stacked Diagonal
	 * -------------------------
	 * This method takes a starting position and creates a column of NUM_VALUES diagonals
	 * going down and to the right in the Exact Cover matrix.
	 * 
	 * For example, if NUM_VALUES = 3:
	 * |1..|
	 * |.1.|
	 * |..1|
	 * |1..|
	 * |.1.|
	 * |..1|
	 * |1..|
	 * |.1.|
	 * |..1|
	 * (where 1 represents the value `true`)
	 * -------------------------
	 * @param startR is an int representing the starting row index of the stack
	 * @param startC is an int representing the starting column index of the stack
	 */
	private void stackedDiagonals(int startR, int startC) {
		for (int i = 0; i < BOX_SIZE; i++) {
			downRightDiagonal(startR + i*NUM_VALUES, startC);
		}
	}
	
	/** Method: Update Cover Matrix
	 * ----------------------------
	 * This method iterates through the currently unsolved Sudoku board and updates the general 
	 * Exact Cover matrix to match this specific Sudoku board.
	 */
	private void updateCoverMatrix() {
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				int value = sudokuBoard[r][c];
				if (value != NO_VALUE) updateCoverRows(r, c, value);
			}
		}
	}
	
	/** Method: Update Cover Rows
	 * --------------------------
	 * This method utilizes the given Sudoku row index, column index, and value to update the 
	 * appropriate rows of the Exact Cover matrix.
	 * --------------------------
	 * @param row is an int that represents a row index in the Sudoku board
	 * @param col is an int that represents a column index in the Sudoku board
	 * @param value is an int representing the value at (row, col) in the Sudoku board 
	 */
	private void updateCoverRows(int row, int col, int value) {
		for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
			if (i != value) {
				int coverRowIndex = getCoverRowIndex(row, col, i);
				Arrays.fill(coverMatrix[coverRowIndex], false);
			}
		}
	}
	
	/** Method: Get Cover Row Index
	 * ----------------------------
	 * This method calculates the index of the appropriate row in the Exact Cover matrix given
	 * a row and column index of the Sudoku board and the value at that position.
	 * ----------------------------
	 * @param row is an int that represents a row index in the Sudoku board
	 * @param col is an int that represents a column index in the Sudoku board
	 * @param value is an int representing the value at (row, col) in the Sudoku board
	 * ----------------------------
	 * @return returns an int representing the appropriate row in the Exact Cover matrix for the
	 * given information
	 */
	private int getCoverRowIndex(int row, int col, int value) {
		return ((row * BOARD_SIZE * BOARD_SIZE) + (col * BOARD_SIZE) + (value - 1));
	}
	
	/** Method: Create Dancing Links
	 * -----------------------------
	 * This method utilizes the updated Exact Cover matrix to create the appropriate toroidal
	 * doubly-linked list structure that defines the Dancing Links technique.
	 */
	private void createDancingLinks() {
		ArrayList<ColumnHeader> columnList = createHeaderRow();
		populateStructure(columnList);
	}
	
	/** Method: Create Header Row
	 * --------------------------
	 * This method creates and links the appropriate amount of ColumnHeader nodes to the root node.
	 * In addition, this method creates and returns an ArrayList of those ColumnHeaders.
	 * --------------------------
	 * @return returns an ArrayList<ColumnHeader> that represents all the ColumnHeaders attached to the root
	 */
	private ArrayList<ColumnHeader> createHeaderRow() {
		ArrayList<ColumnHeader> columnList = new ArrayList<ColumnHeader>();
		ColumnHeader currHeader = root;
		int numCols = coverMatrix[0].length;
		for (int c = 0; c < numCols; c++) {
			ColumnHeader newHeader = new ColumnHeader();
			columnList.add(newHeader);
			currHeader.addNodeRight(newHeader);
			currHeader = newHeader;
		}
		root.size = numCols;
		return columnList;
	}
	
	/** Method: Populate Structure
	 * ---------------------------
	 * This method utilizes the given ArrayList<ColumnHeader> and Exact Cover matrix to construct
	 * the Dancing Links structure. In particular, this method will appropriately create and link
	 * new Nodes to the ColumnHeaders.
	 * ---------------------------
	 * @param columnList is the ArrayList<ColumnHeader> that contains all the ColumnHeaders of the structure
	 */
	private void populateStructure(ArrayList<ColumnHeader> columnList) {
		int numCols = coverMatrix[0].length;
		int numRows = coverMatrix.length;
		for (int r = 0; r < numRows; r++) {
			Node prevNode = null;
			for (int c = 0; c < numCols; c++) {
				if (coverMatrix[r][c] == true) {
					ColumnHeader header = columnList.get(c);
					Node newNode = new Node(header, r);
					
					header.up.addNodeDown(newNode); // Adds to the "bottom" of the column
					header.size++;
					
					if (prevNode == null) prevNode = newNode;
					prevNode.addNodeRight(newNode); // Connects to right of the previous Node in the same row
					prevNode = newNode;
				}
			}
		}		
	}
	
	/** Method: Solve
	 * --------------
	 * This method utilizes the Dancing Links structure to solve and return the Sudoku board.
	 * --------------
	 * @return returns an int[][] representing the solved Sudoku board
	 */
	public int[][] solve() {
		ArrayList<Node> solutionNodes = new ArrayList<Node>();
		solve(solutionNodes);
		applySolution(solutionNodes);
		return sudokuBoard;
	}
	
	/** Method: Solve
	 * --------------
	 * This method attempts to find the solution to the Sudoku board by recursive backtracking.
	 * This method will employ the "S" heuristic to select a column. It will then iterate
	 * through the available rows in the column to choose a row and recursively explore possible
	 * solutions containing that row. If no solution is found, this method will unchoose that row
	 * and continue to iterate through the remaining rows and recursively attempt to find a solution.
	 * This method explores and eliminates solutions by covering and uncovering columns of the Dancing
	 * Links structure.
	 * --------------
	 * @param solutionNodes is an ArrayList<Node> containing the currently attempted solution
	 * --------------
	 * @return returns a boolean describing whether the attempted solution has solved the Sudoku board
	 */
	private boolean solve(ArrayList<Node> solutionNodes) {
		if (root.right == root) return true;
		// determine and cover column
		ColumnHeader columnHeader = determineHeuristic();
		columnHeader.cover();
		// iterate down column
		Node rowHead = columnHeader.down;
		while (rowHead != columnHeader) {
			// cover and add to potential solution
			coverRow(rowHead);
			solutionNodes.add(rowHead);
			// recurse
			if (solve(solutionNodes)) return true;	
			// uncover and remove from potential solution
			solutionNodes.remove(solutionNodes.size() - 1);
			uncoverRow(rowHead);
			// increment down
			rowHead = rowHead.down;
		}
		// uncover column
		columnHeader.uncover();
		return false;	
	}
	
	/** Method: Cover Row
	 * ------------------
	 * This method iterates right through all the Nodes on the same row (i.e. left-right linked list) as
	 * the given Node and covers all their respective columns.
	 * ------------------
	 * @param rowHead is the Node representing a "beginning" of a row
	 */
	private void coverRow(Node rowHead) {
		Node currRowNode = rowHead.right;
		while (currRowNode != rowHead) {
			currRowNode.header.cover();
			currRowNode = currRowNode.right;
		}
	}
	
	/** Method: Uncover Row
	 * --------------------
	 * This method iterates left through all the Nodes on the same row as the given Node and 
	 * uncovers all their respective columns.
	 * --------------------
	 * @param rowHead is the Node representing a "beginning" of a row
	 */
	private void uncoverRow(Node rowHead) {
		Node currRowNode = rowHead.left;
		while (currRowNode != rowHead) {
			currRowNode.header.uncover();
			currRowNode = currRowNode.left;
		}
	}
	
	/** Method: Determine Heuristic
	 * ----------------------------
	 * This method will employs the "S" heuristic to determine which column to explore. In essence,
	 * this method will iterate through all the ColumnHeaders connected to the `root` and return the
	 * ColumnHeader with the smallest `size` (i.e. number of connected Nodes).
	 * ----------------------------
	 * @return returns the ColumnHeader representing the column with the smallest number of connected Nodes
	 */
	private ColumnHeader determineHeuristic() {
		ColumnHeader minHeader = null;
		int minSize = Integer.MAX_VALUE;
		ColumnHeader currHeader = (ColumnHeader) root.right;
		while (currHeader != root) {
			if (currHeader.size < minSize) {
				minSize = currHeader.size;
				minHeader = currHeader;
			}
			currHeader = (ColumnHeader) currHeader.right;
		}
		return minHeader;
	}

	/** Method: Apply Solution
	 * -----------------------
	 * This method takes an ArrayList<Node> containing the Nodes representing the Sudoku board
	 * solution and converts this information such that the solution can be applied to the
	 * int[][] representing the Sudoku board.
	 * -----------------------
	 * @param solutionNodes is an ArrayList<Node> containing the Nodes representing the solution
	 */
	private void applySolution(ArrayList<Node> solutionNodes) {
		for (Node node : solutionNodes) {
			int index = node.index;
			int row = index / (BOARD_SIZE * BOARD_SIZE); // integer division
			int col = (index - (row * BOARD_SIZE * BOARD_SIZE)) / BOARD_SIZE; // integer division
			int val = (index % BOARD_SIZE) + 1;
			sudokuBoard[row][col] = val;
		}
	}

}
