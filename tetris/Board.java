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
		

	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		
		// make array of rows that are filled, falsify such rows
		boolean[] filledRows = new boolean[height];
		for (int y = 0; y < maxHeight; y++) {
			if (isFilled(y)) {
				filledRows[y] = true;
				falsifyRow(y);
				rowsCleared += 1;
			}
		}
		
		for (int i = filledRows.length - 1; i >= 0; i--) {
			if (filledRows[i]) {
				shiftRowsDown(i);
			}
		}		

		
		lowerHeights(rowsCleared);
		sanityCheck();		
		return rowsCleared;
		
	}
	
		// takes every row and shifts it down by 1, adds all falses to top
		private void shiftRowsDown(int start) {
			for (int rowNum = start; rowNum < maxHeight - 1; rowNum++) {
				if (start == height - 1) {
					widths[start] = 0;
				}else {
					copyRow(rowNum, rowNum + 1);
				}
			}
			falsifyRow(maxHeight - 1);
			widths[maxHeight - 1] = 0;
		}
		
		private boolean allFilledRemoved(boolean[] filledRows) {
			for (boolean row : filledRows) if (row) return false;
			return true;
		}
	
		// Helper meoth for clearRows() that checks if a row is filled
		private boolean isFilled(int row) {
			return (widths[row] == width);
		}
		
		/* Helper method for clearRows() that
		 * makes every value in a given row false
		 * in the grid.  */
		private void falsifyRow(int row) {
			for (int x = 0; x < width; x++) {
				grid[x][row] = false;
			}
		}
		
		// Helper method to clearRows() copies the 'fromRow'
		// to the 'toRow' on the grid
		private void copyRow(int toRow, int fromRow) {
			for (int x = 0; x < width; x++) {
				grid[x][toRow] = grid[x][fromRow];
			}
			widths[toRow] = widths[fromRow];
		}
		
		// Helper method for clearRows(), corrects heights[], maxHeight
		// after row removal
		private void lowerHeights(int rowsCleared) {
			for (int i = 0; i < heights.length; i++) {
				heights[i] = checkColumnHeight(i);
			}
			maxHeight -= rowsCleared;
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
	
	// ------------------- .place() and helper methods -------------------- //
	
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
		
		// update widths[], heights[], maxHeight, and
		// method returns whether a row was filled
		if(updateAfterPlace(coordPts)) result = PLACE_ROW_FILLED;	
		
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
			private boolean updateAfterPlace(TPoint [] coordPts) {
				boolean rowFilled = false;
				for (int j = 0; j < coordPts.length; j++) {
					int checkX = coordPts[j].x;
					int checkY = coordPts[j].y;
					
					widths[checkY] += 1;
					if (widths[checkY] == width) rowFilled = true;
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
				return rowFilled;
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


	
	// ------------------- Sanity Check, Debugging Methods ------------------ //
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			
			// check heights[] is correct
			for (int i = 0; i < width; i++) {
				int chHeight = checkColumnHeight(i);
				if (chHeight != heights[i]) {
					throw new RuntimeException("Heights[] incorrect");
				}
			}
			
			// check widths is correct
			for (int j = 0; j < height; j++) {
				int chWidth = checkRowWidth(j);
				if (chWidth != widths[j]) {
					throw new RuntimeException("Widths[] incorrect");
				}
			}
			
			// check max height is correct
			int chMaxHeight = checkMaxHeight();
			if (chMaxHeight != maxHeight) {
				throw new RuntimeException("MaxHeight incorrect");
			}
			
			
		}
	}
	
				/**
			 Returns the number of filled blocks in
			 the given row.
			*/
			private int checkRowWidth(int y) {
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
			private int checkColumnHeight(int x) {
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
			private int checkMaxHeight() {	 
					int currMax = 0;
					for (int i = 0; i < width; i++) {
						int colHeight = checkColumnHeight(i);
						if (colHeight > currMax) currMax = colHeight;
					}		
					return currMax; 
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
		buff.append("\n");
		buff.append("\n");
		return(buff.toString());
	}
	
	// ---------------------------- Getters ------------------------------ //
	
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
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return widths[y];
	}
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return this.maxHeight;
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
//	//toRow is number of row being checked
//	int toRow = 0;
//	int fromRow = 1;
//	while (fromRow < maxHeight) {
//		if (!isFilled(toRow)){
//			toRow += 1;
//			fromRow +=1;
//		}else {
//			// CASE: ToRow is at the top, i.e. we don't
//			// need to check above or copy. We are done!
//			if (toRow == maxHeight - 1) {
//				widths[toRow] = 0;
//				falsifyRow(toRow);
//				rowsCleared += 1;
//				lowerHeights(rowsCleared);
//				sanityCheck();
//				return rowsCleared;
//				
//			}else {
//				while(isFilled(fromRow)) {
//					if(fromRow + 1 == maxHeight) {
//						
//						// CASE: top row is filled, T somewhere below
//						// with only filled rows in between
//						// must falsify everything from T -> F
//						for (int k = toRow; k <= fromRow; k++) {
//							widths[k] =0;
//							falsifyRow(k);
//							rowsCleared += 1;
//						}
//						
//						// We are done!
//						
//						lowerHeights(rowsCleared);
//						sanityCheck();
//						return rowsCleared;
//					} else {
//						
//						// ready to check next row, clear fromRow
//						rowsCleared += 1;
//						falsifyRow(fromRow);
//						fromRow += 1;
//					}					
//				}
//				
//				// CASE: we have found a from row that is incomplete
//				// we have to copy the values in F to T
//				// advance T row to next row up; ready to repeat
//				//not done
//				copyRow(toRow, fromRow);
//				falsifyRow(fromRow);
//				widths[fromRow] = 0;
//				toRow += 1;
//				rowsCleared += 1;
//				
//			}
//			
//			
//		}
//	}
//	
//	lowerHeights(rowsCleared);
//	sanityCheck(); 
* 
*/

	
}


