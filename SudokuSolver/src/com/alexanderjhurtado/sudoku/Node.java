/* Alex Hurtado, Aug. 2018
 * -----------------------
 * Class: Node
 * This class defines the Nodes that make-up the toroidal doubly-linked list in the DancingLinks
 * class. This class handles the linking and unlinking behavior of each Node to surrounding Nodes.
 */
package com.alexanderjhurtado.sudoku;

public class Node {
	
	// Instance Variables
	public ColumnHeader header; // Column that this Node belongs to
	public Node left, right, up, down; // Nodes to the left, right, above, below
	public int index; // Index of the row that this Node occupies in the Exact Cover matrix	
	
	/** Constructor: Node 
	 * ------------------
	 * Initializes instance variables
	 */
	protected Node() {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
	}
	
	/** Constructor: Node
	 * ------------------
	 * Initializes instance variables
	 * ------------------
	 * @param header is the HeaderNode which this Node is "under"
	 * @param coverBoardRowIndex is the index of the row in the Exact Cover matrix that this node occupies	 
	 */
	public Node(ColumnHeader header, int coverBoardRowIndex) {
		this();
		this.header = header;
		this.index = coverBoardRowIndex;
	}
	
	/** Method: Separate Left-Right
	 * ----------------------------
	 * This method removes this Node from the left-right double linked list. That is, the Nodes to
	 * the left and right of this Node will no longer point to this Node as their neighbor. Thus,
	 * this Node is not encountered when traversing the left-right double linked list.	 
	 */
	protected void separateLeftRight() {
		this.left.right = this.right;
		this.right.left = this.left;
	}
	
	/** Method: Link Left-Right
	 * ------------------------
	 * This method restores this Node in the left-right double linked list. That is, the Nodes to
	 * the left and right of this Node will now point to this Node as their neighbor. Thus, this
	 * Node will be encountered when traversing the left-right double linked list.
	 */
	protected void linkLeftRight() {		
		this.left.right = this;
		this.right.left = this;
	}
	
	/** Method: Separate Up-Down
	 * -------------------------
	 * This method removes this Node from the up-down double linked list. That is, the Nodes above
	 * and below this Node will no longer point to this Node as their neighbor. Thus, this Node is
	 * not encountered when traversing the up-down double linked list.
	 */
	protected void separateUpDown() {
		this.up.down = this.down;
		this.down.up = this.up;		
	}
	
	/** Method: Link Up-Down
	 * ---------------------
	 * This method restores this Node in the up-down double linked list. That is, the Nodes above
	 * and below this Node will now point to this Node as their neighbor. This, this Node will be
	 * encountered when traversing the up-down double linked list.
	 */
	protected void linkUpDown() {
		this.up.down = this;
		this.down.up = this;
	}
	
	/** Method: Add Node to Right
	 * --------------------------
	 * This method will insert the given Node to the right of `this` Node in the left-right double linked
	 * list. Thus, this newly added Node will be encountered when traversing the left-right list.
	 * --------------------------
	 * @param node is the Node you wish to insert to the right of `this` node
	 */
	public void addNodeRight(Node node) {	
		node.right = this.right;
		node.right.left = node;
		node.left = this;
		this.right = node;
	}
	
	/** Method: Add Node to Down
	 * -------------------------
	 * This method will insert the given Node below `this` Node in the up-down double linked list.
	 * Thus, this newly added Node will be encountered when traversing the up-down list.
	 * -------------------------
	 * @param node is the Node you wish to insert below `this` node
	 */
	public void addNodeDown(Node node) {	
		node.down = this.down;
		node.down.up = node;
		node.up = this;
		this.down = node;
	}
	
}
