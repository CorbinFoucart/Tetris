package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, stick1;
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(4, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		stick1 = new Piece(Piece.STICK_STR);
		
	}
	
	//Check Board.place() results
	@Test
	public void PlaceCheck() {
		// Out of Bounds Testing
		assertEquals(PLACE_OUT_BOUNDS, b.place(pyr1, -2, -2));
		assertEquals(PLACE_OUT_BOUNDS, b.place(pyr1, -2, 0));
		assertEquals(PLACE_OUT_BOUNDS, b.place(pyr1, 0, -2));
		assertEquals(PLACE_OUT_BOUNDS, b.place(pyr1, 0, 5));
		assertEquals(PLACE_OUT_BOUNDS, b.place(pyr1, 2, 0));
		
		// Piece Collision Testing
		b.place(pyr1, 0, 0);
		assertEquals(PLACE_BAD, b.place(pyr1, 0, 0));
		assertEquals(PLACE_BAD, b.place(pyr1, 0, 1));
		assertEquals(PLACE_BAD, b.place(pyr2, 0, 0));
		assertEquals(PLACE_OK, b.place(pyr2, 1, 1));		
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void geometrySample1() {
		Board GS1 = new Board(3, 6);
		GS1.place(pyr1, 0, 0);
		assertEquals(1, GS1.getColumnHeight(0));
		assertEquals(2, GS1.getColumnHeight(1));
		assertEquals(2, GS1.getMaxHeight());
		assertEquals(3, GS1.getRowWidth(0));
		assertEquals(1, GS1.getRowWidth(1));
		assertEquals(0, GS1.getRowWidth(2));	
	}

	
	// Check the basic width/height/max after the one placement
	@Test
	public void geometrySample2() {
		Board GS2 = new Board(3, 7);
		GS2.place(pyr1, 0, 0);
		GS2.place(pyr2, 1, 1);
		
		// after second placement
		assertEquals(1, GS2.getColumnHeight(0));
		assertEquals(3, GS2.getColumnHeight(1));
		assertEquals(4, GS2.getMaxHeight());
		assertEquals(3, GS2.getRowWidth(0));
		assertEquals(2, GS2.getRowWidth(1));
		assertEquals(2, GS2.getRowWidth(2));
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void geometrySample3() {
		Board GS3 = new Board(4, 6);
		GS3.place(pyr1, 0, 0);
		GS3.place(pyr2, 1, 1);
		GS3.place(pyr4, 0, 2);
		
		// after third placement
		assertEquals(5, GS3.getColumnHeight(0));
		assertEquals(4, GS3.getColumnHeight(1));
		assertEquals(5, GS3.getMaxHeight());
		assertEquals(3, GS3.getRowWidth(0));
		assertEquals(2, GS3.getRowWidth(1));
		assertEquals(3, GS3.getRowWidth(2));
//		System.out.print(GS3.toString());
		
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void geometrySample4() {
		Board GS4 = new Board(3, 6);
		GS4.place(pyr1, 0, 0);
		int result = GS4.place(sRotated, 1, 1);
		
		// REMOVE ANNOTATION AFTER CLEARROWS 
		// this test passes because PLACE_ROW_FILLED 
		// would have been the result if it had been
		// run 1 step earlier. As far as the computer
		// knows, the row from pyr has been cleared
		assertEquals(Board.PLACE_OK, result);
//		System.out.print(GS4.toString());
		assertEquals(1, GS4.getColumnHeight(0));
		assertEquals(4, GS4.getColumnHeight(1));
		assertEquals(3, GS4.getColumnHeight(2));
		assertEquals(4, GS4.getMaxHeight());
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void geometrySample5() {
		Board GS5 = new Board(3, 6);
		GS5.place(pyr1, 0, 0);
		GS5.place(stick1, 0, 1);
		int result = GS5.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);
//		System.out.print(GS5.toString());
		assertEquals(5, GS5.getColumnHeight(0));
		assertEquals(4, GS5.getColumnHeight(1));
		assertEquals(3, GS5.getColumnHeight(2));
		assertEquals(5, GS5.getMaxHeight());
	}

	// Single row clear test
	@Test
	public void clearRows1() {
		Board CR1 = new Board(3,6);
		CR1.place(pyr1, 0, 0);
		CR1.clearRows();
//		System.out.print(CR1.toString());
	}
	
	@Test
	public void clearRows2() {
		Board CR2 = new Board(3,6);
		CR2.place(pyr1, 0, 0);
		CR2.place(pyr2, 1, 1);
		CR2.place(stick1, 0, 1);
//		System.out.print(CR2.toString());
		CR2.clearRows();
//		System.out.print(CR2.toString());
	}
	

	@Test
	public void clearRows3() {
		Board CR3 = new Board(4,4);
		CR3.place(stick1, 0, 0);
		CR3.place(stick1, 1, 0);
		CR3.place(stick1, 2, 0);
		CR3.place(stick1, 3, 0);
//		System.out.print(CR3.toString());
		CR3.clearRows();
//		System.out.print(CR3.toString());
	}
	
	@Test
	public void clearRows4() {
		Board CR4 = new Board(4,6);
		CR4.place(pyr1, 0, 0);
		CR4.place(stick1.computeNextRotation(), 0, 2);
		CR4.place(pyr1, 1, 3);
		CR4.place(stick1.fastRotation(), 0, 5);
//		System.out.print(CR4.toString());
		CR4.clearRows();
//		System.out.print(CR4.toString());
	}
	
	@Test
	public void dropheight1() {
		Board DH1 = new Board(4,6);
		DH1.place(pyr1, 0, 0);
		assertEquals(1, DH1.dropHeight(pyr2, 1));
//		System.out.print(DH1.toString());
		DH1.place(pyr2, 1, DH1.dropHeight(pyr2, 1));
//		System.out.print(DH1.toString());
	}
	
	@Test
	public void dropheight2() {
		Board DH2 = new Board(5,7);
		DH2.place(stick1, 1, 0);
		System.out.print(DH2.toString());
		assertEquals(3, DH2.dropHeight(sRotated, 1));
		int test = DH2.dropHeight(sRotated, 1);
		DH2.place(sRotated, 1, test);
		System.out.print(DH2.toString());
	}
	
	@Test
	public void dropheight3() {
		Board DH2 = new Board(5,7);
		DH2.place(stick1, 1, 0);
		System.out.print(DH2.toString());
		assertEquals(3, DH2.dropHeight(sRotated, 1));
		int test = DH2.dropHeight(sRotated, 1);
		DH2.place(sRotated, 1, test);
		System.out.print(DH2.toString());
	}
	
	
	
	
	
}
