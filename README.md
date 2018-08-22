# Overview
This program implements a simple console interface that allows the user to enter and solve 9x9 Sudoku puzzles. The user is given the option to solve the puzzle through a [brute-force search](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms#Backtracking) or through a [Dancing Links](https://en.wikipedia.org/wiki/Dancing_Links) implementation. 

# Why Dancing Links (Toroidal Doubly-Linked Lists)?
By generalizing a 9x9 Sudoku puzzle as an [Exact Cover problem](https://en.wikipedia.org/wiki/Exact_cover#Sudoku), we can solve Sudoku puzzles incredibly efficiently by implementing the Dancing Links approach to Donald Knuth's [Algorithm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X).

The Dancing Links technique relies on the behavior of ***recursive backtracking*** and ***toroidal doubly-linked lists***, particularly in their ability to efficiently remove and restore nodes to their original positions in their respective lists. I would highly suggest reading [Knuth's paper](https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/0011047.pdf) on the approach.

# Description of .java files
This program is made up of 7 distinct .java files:  
1. **Solver:**  
This class is responsible for the simple console interface that the user interacts with.  
2. **Constants:**  
This final class is responsible for storing constants related to Sudoku puzzles. In order to use the values, each class uses a static import of **Constants**.  
3. **DancingLinks:**  
This class is responsible for creating and using the Dancing Links structure to solve the given Sudoku puzzle.  
4. **ColumnHeader:**  
This class is used to keep track of each column of Nodes in **DancingLinks**.  
5. **Node:**  
This class is used to construct the toroidal doubly-linked list in **DancingLinks**.  
6. **BruteForceSearch:**  
This class is responsible for implementing the depth-first, brute-force search to solve the given Sudoku puzzle.  
7. **Cell:**  
This class is used in **BruteForceSearch** to represent positions in the Sudoku board.  

# User Input
The console interface requires that the user inputs the unsolved Sudoku board's data in a particular way. Valid board input is structured by listing each cell's value in order (left-to-right then top-to-bottom). Cells with no value yet should be represented by "0".

For example, given the board represented below:  
![alt text](https://upload.wikimedia.org/wikipedia/commons/e/e0/Sudoku_Puzzle_by_L2G-20050714_standardized_layout.svg)  
The matching input would be:  
> 530070000600195000098000060800060003400803001700020006060000280000419005000080079  

# Example Inputs
Board #1:  
> 530070000600195000098000060800060003400803001700020006060000280000419005000080079  

Board #2:  
> 003004500080000300900000007306081000040205010000960803800000002007000050001600900  

Board #3:  
> 000034000004100709205000600009400201000301000301002900002000405903008100000940000  

The input below is unique in that it is constructed to work against the brute-force search algorithm i.e. the solving time is incredibly slow compared to other methods, such as Dancing Links.  
Board #4:  
> 000000000000003085001020000000507000004000100090000000500000073002010000000040009  
