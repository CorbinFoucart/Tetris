// Board.java
package tetris;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private int[] widths;
	private int[] heights;
	private int maxHeight;
	
	
	// BACKUP VARIABLES
	private boolean[][] xGrid;
	
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		this.widths = new int[height];
		this.heights = new int[width];
		
		// YOUR CODE HERE
	
		
	}
	
	/**
	 Saves all of the current values for the board to our 1 deep backup
	 variables. If undo is called, we can change the current values
	 to the backup variables' values.
	 */
	public void saveBackup() {
		xGrid = this.grid;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			
			// check heights[] is correct
			for (int i = 0; i < width; i++) {
				int chHeight = getColumnHeight(i);
				if (chHeight != heights[i]) {
					throw new RuntimeException("Heights[] incorrect");
				}
			}
			
			// check widths is correct
			for (int j = 0; j < height; j++) {
				int chWidth = getRowWidth(j);
				if (chWidth != widths[j]) {
					throw new RuntimeException("Widths[] incorrect");
				}
			}
			
			// check max height is correct
			int chMaxHeight = getMaxHeight();
			if (chMaxHeight != maxHeight) {
				throw new RuntimeException("MaxHeight incorrect");
			}
			
			
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		return 0; // YOUR CODE HERE
	}
		

	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		boolean sanityCheck = true;
		
		// get candidate squares for placement
		TPoint[] coordPts = getCoords(piece, x, y);
		
		// check if piece is in bounds, if square is currently obstructed 
		for (int i = 0; i < coordPts.length; i++) {
			int checkX = coordPts[i].x;
			int checkY = coordPts[i].y;
			
			if (!inBounds(checkX, checkY)) {
				int result = PLACE_OUT_BOUNDS;
				sanityCheck = false;
				return result;
			}
			
			if (grid[checkX][checkY] == true) {
				int result = PLACE_BAD;
				sanityCheck = false;
				return result;
			}
		}
		// place is at least OK by this point.
		int result = PLACE_OK;
		
		// save backup values in case of undo()
		saveBackup();
		
		// update widths[], heights[], maxHeight
		updateAfterPlace(coordPts);
		
		// CHECK FOR IF A ROW IS COMPLETED
		
		
		
		if (sanityCheck) sanityCheck();
//		committed = false;
		return result;
	}
			// Helper method to place() to get candiate coordinate points
			private TPoint[] getCoords(Piece piece, int x, int y) {
				TPoint[] piecePts = piece.getBody();
				TPoint[] coordPts = new TPoint[piece.getBody().length];
				for (int i = 0; i < piece.getBody().length; i++) {
					TPoint currTP = new TPoint(piecePts[i].x + x, piecePts[i].y + y);
					coordPts[i] = currTP;
				}
				return coordPts;
			}
			
			// Helper method to place() to update board information
			// updates widths[], heights[], maxHeight
			private void updateAfterPlace(TPoint [] coordPts) {
				for (int j = 0; j < coordPts.length; j++) {
					int checkX = coordPts[j].x;
					int checkY = coordPts[j].y;
					
					widths[checkY] += 1;
					if (heights[checkX] < checkY + 1) {
						heights[checkX] = checkY + 1;
					}
				}
				
				// update MaxHeight after piece has been added
				maxHeight = heights[0];
				for (int i = 0; i < width; i++) {
					int possMax = heights[i];
					if (possMax > maxHeight) maxHeight = possMax;
				}
				
				// update grid to include piece
				for (int i = 0; i < coordPts.length; i++) {
					grid[coordPts[i].x][coordPts[i].y] = true;
				}
			}
			
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		
		// YOUR CODE HERE
		
		sanityCheck();
		return rowsCleared;
	}
	
	/**
	 Checks whether a given x,y coordinate pair (grid[x][y]) are out
	 of bounds on our board. x,y must be 0 or higher, but must be one
	 less than the height or width due to 0 indexing.
	 */
	private boolean inBounds(int x, int y) {
		return ( ((x >= 0) && (x < width))
				&& ((y >= 0) && (y < height)) );
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
	}
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;	
	}
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		int rtnWidth = 0;
		for (int i = 0; i < width; i++) {
			boolean tmpBool = grid[i][y];
			if (tmpBool) rtnWidth += 1;
		}
		 return rtnWidth; 
	}
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		int rtnHeight = 0;
		for (int i = 0; i < height; i++) {
			boolean tmpBool = grid[x][i];
			if (tmpBool) rtnHeight = i + 1;
		}
		return rtnHeight; 
	}
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		int currMax = 0;
		for (int i = 0; i < width; i++) {
			int colHeight = getColumnHeight(i);
			if (colHeight > currMax) currMax = colHeight;
		}		
		return currMax; 
	}
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		return grid[x][y]; 
	}
	
	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
	
}


