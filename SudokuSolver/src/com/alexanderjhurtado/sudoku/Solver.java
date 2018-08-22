/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: Solver
 * This class is responsible for implementing the simple console user interface. This class prompts
 * the user for appropriate input in order to solve the user's given Sudoku puzzle
 */
package com.alexanderjhurtado.sudoku;
import java.util.Scanner;
import static com.alexanderjhurtado.sudoku.Constants.*;

public class Solver {
	
	/** The number of nanoseconds per millisecond **/
	private static final double NANO_TO_MILLI = 1000000.0;
	
	// Instance Variables
	private int[][] sudokuBoard; // Represents the Sudoku board
	private Scanner scanner; // Used for user input throughout the program

	public static void main(String[] args) {
		Solver s = new Solver();
		s.scanner = new Scanner(System.in);
		System.out.println("Welcome to Sudoku Solver");
		while (true) {
			s.promptAndSolveOnce();
			System.out.println("Enter anything to solve another Sudoku puzzle or \"0\" to exit: ");
			String input = s.scanner.nextLine();
			if (input.equals("0")) {
				System.out.println("Goodbye!");
				break;
			}
		}
	}
	
	/** Method: Prompt and Solve Once
	 * ------------------------------
	 * This method handles all the behavior necessary to prompt the user for a Sudoku puzzle and 
	 * solving that puzzle once.
	 */
	private void promptAndSolveOnce() {
		String boardData = promptBoardInput();
		setUpBoard(boardData);
		printBoard();
		String methodChoice = promptSolutionMethod();
		double runtime = solveSudoku(methodChoice);
		printBoard();
		System.out.println("Done! The algorithm took " + runtime + " milliseconds to solve.");
	}
	
	/** Method: Prompt Board Input
	 * ---------------------------
	 * This method will continually prompt the user for valid input to set-up the Sudoku board.
	 * If the user enters invalid input, the user is prompted until valid input is entered. 
	 * Valid input is then returned as a String.
	 * ---------------------------
	 * @return returns a String representing the user's valid input for the board
	 */
	private String promptBoardInput() {
		String validInput;		
		while (true) {
			System.out.println("Input board data: ");
			String input = scanner.nextLine();
			if (isValidBoardInput(input)) {
				validInput = input;
				break;
			}
		}		
		return validInput;
	}
	
	/** Method: Is Valid Board Input
	 * -----------------------------
	 * This method will check if the given String is valid input to use to set-up the Sudoku board.
	 * The input's length should equal the number of cells in the Sudoku board and should be numeric.
	 * 
	 * Valid board input is made by listing the values of each cell of an unsolved Sudoku board in
	 * order (left-to-right then top-to-bottom). Cells with no value yet should be represented by "0" (NO_VALUE).
	 * 
	 * For example, for the "board" given by (where "-" represents empty cells):
	 * |3 - 9|
	 * |- 4 8|
	 * |2 - -|
	 * the matching input would be: "309048200".
	 * -----------------------------
	 * @param input is the String that the user has input
	 * -----------------------------
	 * @return returns `true` if the user's input is valid for the Sudoku board and `false` if it is not
	 */
	private boolean isValidBoardInput(String input) {
		int inputLength = input.length();
		if (inputLength != BOARD_SIZE*BOARD_SIZE) {
			System.out.println("Invalid input length: " + inputLength + " char(s). Try again.");
			return false;
		}
		for (int i = 0; i < inputLength; i++) {
			if (!Character.isDigit(input.charAt(i))) {								
				String error = "Input must be numeric. Found invalid char at index " + i + ". Try again.";
				System.out.println(error);
				return false;
			}
		}
		return true;
	}
	
	/** Method: Set Up Board
	 * ---------------------
	 * This method will convert the given String containing the values of each cell of the Sudoku
	 * puzzle into a int[][] representation of the Sudoku board.
	 * ---------------------
	 * @param boardData is a String containing
	 */
	private void setUpBoard(String boardData) {
		this.sudokuBoard = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < boardData.length(); i++) {
			int r = i / BOARD_SIZE; // integer division
			int c = i % BOARD_SIZE;
			int newVal = (int) (boardData.charAt(i) - '0');
			sudokuBoard[r][c] = newVal;
		}
	}
	
	/** Method: Print Board
	 * --------------------
	 * This method will print out a stylized version of the current Sudoku board to the console.
	 */
	private void printBoard() {
		String str = "Sudoku Board: \n";
		for (int r = 0; r < BOARD_SIZE; r++) {
			if (r % BOX_SIZE == 0) str += '\n';
			for (int c = 0; c < BOARD_SIZE; c++) {
				if (c % BOX_SIZE == 0) str += " ";
				str += sudokuBoard[r][c];
				if (c == BOARD_SIZE - 1) str += '\n';
			}
		}
		System.out.println(str);		
	}
	
	/** Method: Prompt Solution Method
	 * -------------------------------
	 * This method continually prompts the user to input a valid choice in selecting which method
	 * to use to solve the Sudoku puzzle. If the input is invalid, the user is prompted again until
	 * valid input is entered. A valid choice is then returned as a String
	 * -------------------------------
	 * @return returns a String containing the user's valid input (choice)
	 */
	private String promptSolutionMethod() {
		String validInput;
		System.out.println("Select a method to solve the puzzle.");
		while (true) {
			System.out.println("Enter \"1\" for Brute-force search or \"2\" for Dancing Links algorithm: ");
			String input = scanner.nextLine();
			if (!input.equals("1") && !input.equals("2")) {
				System.out.println("\"" + input + "\" is not a valid choice. Try again.");
			} else {
				validInput = input;
				break;
			}			
		}
		return validInput;
	}
	
	/** Method: Solve Sudoku
	 * ---------------------
	 * This method will solve the Sudoku puzzle and return a double representing how fast the
	 * solution was found.
	 * ---------------------
	 * @param choice is a String that denotes the user's desired method of solving the puzzle
	 * ---------------------
	 * @return returns a double representing the number of milliseconds it took to solve the Sudoku puzzle
	 */
	private double solveSudoku(String choice) {
		System.out.println("Working...");
		long startTime = System.nanoTime();
		conductSolution(choice);
		long endTime = System.nanoTime();
		return (endTime - startTime) / NANO_TO_MILLI;
	}
	
	/** Method: Conduct Solution
	 * -------------------------
	 * This method will use the given String to solve the Sudoku puzzle by initializing and calling solve()
	 * on the appropriate method of solution.
	 * -------------------------
	 * @param choice is a String denoting which method the user wants to use to solve the Sudoku puzzle
	 */
	private void conductSolution(String choice) {
		if (choice.equals("1")) {
			BruteForceSearch naive = new BruteForceSearch(sudokuBoard);
			sudokuBoard = naive.solve();
		} else if (choice.equals("2")) {
			DancingLinks dlx = new DancingLinks(sudokuBoard);
			sudokuBoard = dlx.solve();
		}
	}
	
}
