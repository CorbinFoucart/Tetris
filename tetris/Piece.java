// Piece.java
package tetris;

import java.awt.Point;
import java.util.*;

/**
 An immutable representation of a tetris piece in a particular rotation.
 Each piece is defined by the blocks that make up its body.
 
 Typical client code looks like...
 <pre>
 Piece pyra = new Piece(PYRAMID_STR);		// Create piece from string
 int width = pyra.getWidth();			// 3
 Piece pyra2 = pyramid.computeNextRotation(); // get rotation, slow way
 
 Piece[] pieces = Piece.getPieces();	// the array of root pieces
 Piece stick = pieces[STICK];
 int width = stick.getWidth();		// get its width
 Piece stick2 = stick.fastRotation();	// get the next rotation, fast way
 </pre>
*/
public class Piece {
	// Starter code specs out a few basic things, leaving
	// the algorithms to be done.
	private TPoint[] body;
	private int[] skirt;
	private int width;
	private int height;
	private Piece next; // "next" rotation
	static private Piece[] pieces;	// singleton static array of first rotations

	/**
	 Defines a new piece given a TPoint[] array of its body.
	 Makes its own copy of the array and the TPoints inside it.
	*/
	public Piece(TPoint[] points) {
		this.body = points;
		this.width = computeWidth(points);
		this.height = computeHeight(points);
		this.skirt = computeSkirt(points, width);
	}
	
	/**
	 * Alternate constructor, takes a String with the x,y body points
	 * all separated by spaces, such as "0 0  1 0  2 0	1 1".
	 * (provided)
	 */
	public Piece(String points) {
		this(parsePoints(points));
	}

	/**
	 Returns the width of the piece measured in blocks.
	*/
	public int getWidth() {
		return width;
	}

	/**
	 Returns the height of the piece measured in blocks.
	*/
	public int getHeight() {
		return height;
	}

	/**
	 Returns a pointer to the piece's body. The caller
	 should not modify this array.
	*/
	public TPoint[] getBody() {
		return body;
	}

	/**
	 Returns a pointer to the piece's skirt. For each x value
	 across the piece, the skirt gives the lowest y value in the body.
	 This is useful for computing where the piece will land.
	 The caller should not modify this array.
	*/
	public int[] getSkirt() {
		return skirt;
	}

	
	/**
	 Returns a new piece that is 90 degrees counter-clockwise
	 rotated from the receiver.
	 */
	public Piece computeNextRotation() {
		// arraylist of TPoints to hold the rotated blocks
		TPoint[] rotatedPts = new TPoint[body.length];
		for (int i = 0; i < body.length; i++) {
			int x = body[i].x;
			int y = body[i].y;
			
			// apply CC rotation matrix to [x, y] vector: R = [0 -1; 1 0]
			int x_p = x * 0 + y * (-1);
			int y_p = x * 1 + y * 0;
			
			TPoint rotdNotCorrctd = new TPoint(x_p, y_p);
			rotatedPts[i] = rotdNotCorrctd;			
		}
		
		// we have now rotated the piece to quadrant II, so we must correct x values
		int xMin = 0;
		int yMin = 0;
		for (int i = 0; i < rotatedPts.length; i++) {
			int x = rotatedPts[i].x;
			int y = rotatedPts[i].y;
			
			if (x < xMin) xMin = x;
			if (y < yMin) yMin = y;
		}
		
		for (int i = 0; i < rotatedPts.length; i++) {
			rotatedPts[i].x += Math.abs(xMin);
			rotatedPts[i].y += Math.abs(yMin);
		} 
		
		// construct new piece from the arrayList
		Piece rotdPiece = new Piece(rotatedPts);
		return rotdPiece; 
	}

	/**
	 Returns a pre-computed piece that is 90 degrees counter-clockwise
	 rotated from the receiver.	 Fast because the piece is pre-computed.
	 This only works on pieces set up by makeFastRotations(), and otherwise
	 just returns null.
	*/	
	public Piece fastRotation() {
		if (next == null) {
			makeFastRotations(this);
		}
		return next;
	}
	

	/**
	 Given the "first" root rotation of a piece, computes all
	 the other rotations and links them all together
	 in a circular list. The list loops back to the root as soon
	 as possible. Returns the root piece. fastRotation() relies on the
	 pointer structure setup here.
	*/
	/*
	 Implementation: uses computeNextRotation()
	 and Piece.equals() to detect when the rotations have gotten us back
	 to the first piece.
	*/
	private static Piece makeFastRotations(Piece root) {		
		Piece nextRotation = root.computeNextRotation();
		root.next = nextRotation;
		while(!nextRotation.computeNextRotation().equals(root)) {
			nextRotation.next = nextRotation.computeNextRotation();
			nextRotation = nextRotation.next;
		}
		nextRotation.next = root;
		return root;
	}

	/**
	 Returns true if two pieces are the same --
	 their bodies contain the same points.
	 Interestingly, this is not the same as having exactly the
	 same body arrays, since the points may not be
	 in the same order in the bodies. Used internally to detect
	 if two rotations are effectively the same.
	*/
	@Override
	public boolean equals(Object obj) {
		// standard equals() technique 1
		if (obj == this) return true;
		
		// standard equals() technique 2
		// (null will be false)
		// any other class object will be caught as false
		if (!(obj instanceof Piece)) return false;
		Piece other = (Piece)obj;
		
		
		// above lines guarantee that the object is a Piece
		// look inside the other piece and determine whether
		// the properties are the same
		TPoint[] objBody = other.getBody();
		TPoint[] ourBody = body;
		
		// check for same size (so other piece can't contain more points)
		if (objBody.length != ourBody.length) return false;
		
		boolean[] matchingCoords = new boolean[ourBody.length];
		for (int i = 0; i < ourBody.length; i++) {
			int ourX = ourBody[i].x;
			int ourY = ourBody[i].y;
			for (int j = 0; j < objBody.length; j++){
				int objX = objBody[j].x;
				int objY = objBody[j].y;
				
				if (ourX == objX && ourY == objY) {
					matchingCoords[i] = true;
				}
			}
		}
		
		// check that all coordinates matched
		// credit to stackOverflow for this alternative to normal
		// for loop checking each element: Question 18631837
		for (boolean match : matchingCoords){
			if (!match) return false;
		}
		
		return true;
	}


	// String constants for the standard 7 tetris pieces
	public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
	public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
	public static final String L2_STR		= "0 0	1 0 1 1	 1 2";
	public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
	public static final String S2_STR		= "0 1	1 1  1 0  2 0";
	public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
	public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";
	
	// Indexes for the standard 7 pieces in the pieces array
	public static final int STICK = 0;
	public static final int L1	  = 1;
	public static final int L2	  = 2;
	public static final int S1	  = 3;
	public static final int S2	  = 4;
	public static final int SQUARE	= 5;
	public static final int PYRAMID = 6;
	
	/**
	 Returns an array containing the first rotation of
	 each of the 7 standard tetris pieces in the order
	 STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
	 The next (counterclockwise) rotation can be obtained
	 from each piece with the {@link #fastRotation()} message.
	 In this way, the client can iterate through all the rotations
	 until eventually getting back to the first rotation.
	 (provided code)
	*/
	public static Piece[] getPieces() {
		// lazy evaluation -- create static array if needed
		if (Piece.pieces==null) {
			// use makeFastRotations() to compute all the rotations for each piece
			Piece.pieces = new Piece[] {
				makeFastRotations(new Piece(STICK_STR)),
				makeFastRotations(new Piece(L1_STR)),
				makeFastRotations(new Piece(L2_STR)),
				makeFastRotations(new Piece(S1_STR)),
				makeFastRotations(new Piece(S2_STR)),
				makeFastRotations(new Piece(SQUARE_STR)),
				makeFastRotations(new Piece(PYRAMID_STR)),
			};
		}
		
		
		return Piece.pieces;
	}
	
	

	/**
	 Given a string of x,y pairs ("0 0	0 1 0 2 1 0"), parses
	 the points into a TPoint[] array.
	 (Provided code)
	*/
	private static TPoint[] parsePoints(String string) {
		List<TPoint> points = new ArrayList<TPoint>();
		StringTokenizer tok = new StringTokenizer(string);
		try {
			while(tok.hasMoreTokens()) {
				int x = Integer.parseInt(tok.nextToken());
				int y = Integer.parseInt(tok.nextToken());
				
				points.add(new TPoint(x, y));
			}
		}
		catch (NumberFormatException e) {
			throw new RuntimeException("Could not parse x,y string:" + string);
		}
		
		// Make an array out of the collection
		TPoint[] array = points.toArray(new TPoint[0]);
		return array;
	}
	
	// -------------------------- My working helper Methods ----------------------------- //
	
	//Given a width, and TPoints, returns the skirt int[] for a given piece
	private int[] computeSkirt(TPoint[] points, int width) {
		int[] skirt = new int[width];
		for (int i = 0; i < width; i++) {
			//impossibly high value 
			int minY = 5;
			for (int j = 0; j < points.length; j++) {
				TPoint currentPoint = points[j];
				// if point is in right column
				if (currentPoint.x == i) {
					if (currentPoint.y < minY) minY = currentPoint.y;
				}
			}
			skirt[i] = minY;
		}		
		return skirt;
	}
	
	// Compute the width of a tetris piece from its points
	private int computeWidth(TPoint[] points) {
		int maxX = points[0].x;  
		int minX = points[0].x;
		for (int i = 1; i < points.length; i++) {
			TPoint currentPoint = points[i];
			if (currentPoint.x > maxX) maxX = currentPoint.x;
			if (currentPoint.x < minX) minX = currentPoint.x;			
		}
		// + 1 is to account for differences in coordinates and actual width
		int width = maxX - minX + 1;
		return width;
	}
	
	// Compute the height of a tetris piece from its points
	private int computeHeight(TPoint[] points) {
		int maxY = points[0].y;  
		int minY = points[0].y;
		for (int i = 1; i < points.length; i++) {
			TPoint currentPoint = points[i];
			if (currentPoint.y > maxY) maxY = currentPoint.y;
			if (currentPoint.y < minY) minY = currentPoint.y;			
		}
		// + 1 is to account for differences in coordinates and actual width
		int width = maxY - minY + 1;
		return width;
	}

	


}
