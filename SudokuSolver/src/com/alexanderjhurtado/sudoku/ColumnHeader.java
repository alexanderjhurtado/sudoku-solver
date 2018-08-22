/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: ColumnHeader
 * This class is used to keep track of each column of Nodes in the DancingLinks class. In
 * particular, this class handles the "covering" and "uncovering" of columns in the DancingLinks
 * structure. 
 */
package com.alexanderjhurtado.sudoku;

public class ColumnHeader extends Node {
	
	// Instance Variables
	public int size; // Number of nodes belonging to this column
	
	/** Constructor: Column Header
	 * ---------------------------
	 * Initializes instance variables
	 */
	public ColumnHeader() {
		super();
		this.size = 0;
		this.header = this;
		this.index = -1;
	}
	
	/** Method: Cover
	 * --------------
	 * This method "covers" the column represented by `this` ColumnHeader. That is, this method
	 * removes `this` ColumnHeader from the left-right linked list of ColumnHeaders. Additionally,
	 * this method removes all Nodes from their respective up-down linked lists if they exist on
	 * the same row (i.e. left-right list) as the Nodes of the given column.
	 */
	public void cover() {
		this.separateLeftRight();
		Node currColNode = this.down;
		while (currColNode != this) { // Traverses down the column
			Node currRowNode = currColNode.right;
			while (currRowNode != currColNode) { // Traverses right across the row
				currRowNode.separateUpDown();
				currRowNode.header.size--;
				currRowNode = currRowNode.right; // increment right
			}
			currColNode = currColNode.down; // increment down
		}
	}
	
	/** Method: Uncover
	 * ----------------
	 * This method "uncovers" the column represented by `this` ColumnHeader; it essentially undoes
	 * the Cover method. Specifically, this method will restore all Nodes that were removed in the
	 * Cover method to their respective up-down linked lists. Additionally, this method will restore
	 * `this` ColumnHeader to the left-right linked list of ColumnHeaders.
	 */
	public void uncover() {
		Node currColNode = this.up;
		while (currColNode != this) { // Traverses up the column
			Node currRowNode = currColNode.left;
			while (currRowNode != currColNode) { // Traverses left across the row
				currRowNode.linkUpDown();
				currRowNode.header.size++;
				currRowNode = currRowNode.left; // increment left
			}
			currColNode = currColNode.up; // increment right
		}
		this.linkLeftRight();
	}

}
