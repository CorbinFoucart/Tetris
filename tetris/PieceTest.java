package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4, pyr5, pyr1test,
				  stick1, stick2, stick3, stick4, stick5, stick1test, 
				  L11, L12, L13, L14, L15, L1test,
				  L21, L22, L23, L24, L25, L2test,
				  S11, S12, S13, S14, S15, S1test,
				  S21, S22, S23, S24, S25, S2test,
				  sqr1, sqr2, sqr3, sqr4, sqr5, sqr1test;
	private Piece s, sRotated;

	@Before
	public void setUp() throws Exception {
		
		// ALL piece instantiation
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr1test = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		pyr5 = pyr4.computeNextRotation();
		
		stick1 = new Piece(Piece.STICK_STR);
		stick1test = new Piece(Piece.STICK_STR);
		stick2 = stick1.computeNextRotation();
		stick3 = stick2.computeNextRotation();
		stick4 = stick3.computeNextRotation();
		stick5 = stick4.computeNextRotation();
		
		L11 = new Piece(Piece.L1_STR);
		L1test = new Piece(Piece.L1_STR);
		L21 = new Piece(Piece.L2_STR);
		L2test = new Piece(Piece.L2_STR);
		S11 = new Piece(Piece.S1_STR);
		S1test = new Piece(Piece.S1_STR);
		S21 = new Piece(Piece.S2_STR);
		S2test = new Piece(Piece.S2_STR);
		sqr1 = new Piece(Piece.SQUARE_STR);
		sqr1test = new Piece(Piece.SQUARE_STR);
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	
	@Test
	// test the first set of rotations
	public void testRotation2() {
		// Check size of pyr piece
		int[] pyrSkirt = {1, 0};
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		assertArrayEquals(pyrSkirt, pyr2.getSkirt());
		
		//check size of stick
		int[] stickSkirt = {0, 0, 0, 0};
		assertEquals(4, stick2.getWidth());
		assertEquals(1, stick2.getHeight());
		assertArrayEquals(stickSkirt, stick2.getSkirt());
		
		
		
	}
	
//	@Test
//	// Test our overridden .equals() method
//	public void testEquals(){
//		assertEquals(true, pyr1.equals(pyr1test));
//		assertEquals(true, stick1.equals(stick1test));
//		assertEquals(true, L11.equals(L1test));
//		assertEquals(true, L21.equals(L2test));
//		assertEquals(true, S11.equals(S1test));
//		assertEquals(true, S21.equals(S2test));
//		assertEquals(true, sqr1.equals(sqr1test));
//	}
//	
//	@Test
//	// test the second set of rotations
//	public void testRotation3() {
//		// Check size of pyr piece
//		int[] pyrSkirt = {1, 0, 1};
//		assertEquals(3, pyr3.getWidth());
//		assertEquals(2, pyr3.getHeight());
//		assertArrayEquals(pyrSkirt, pyr3.getSkirt());
//		
//	}
//	
//	@Test
//	// test the third set of rotations
//	public void testRotation4() {
//		// Check size of pyr piece
//		int[] pyrSkirt = {0, 1};
//		assertEquals(2, pyr4.getWidth());
//		assertEquals(3, pyr4.getHeight());
//		assertArrayEquals(pyrSkirt, pyr4.getSkirt());
//		
//	}
//	
//	@Test
//	// test the 4th set of rotations, equal to first
//	public void testRotation5() {
//		// Check size of pyr piece
//		int[] pyrSkirt = {0, 0, 0};
//		assertEquals(3, pyr5.getWidth());
//		assertEquals(2, pyr5.getHeight());
//		assertArrayEquals(pyrSkirt, pyr5.getSkirt());
//		
//	}
//	
//	
//	
//	
//	@Test
//	// Test the beginning attributes of each of the pieces without rotation
//	public void testSampleSize() {
//		// Check size of pyr piece
//		int[] pyrSkirt = {0, 0, 0};
//		assertEquals(3, pyr1.getWidth());
//		assertEquals(2, pyr1.getHeight());
//		assertEquals(pyrSkirt[0], pyr1.getSkirt()[0]);
//		assertEquals(pyrSkirt[1], pyr1.getSkirt()[1]);
//		assertEquals(pyrSkirt[2], pyr1.getSkirt()[2]);
//		assertArrayEquals(pyrSkirt, pyr1.getSkirt());
//		
//		
//		// Now try after rotation
//		// Effectively we're testing size and rotation code here
//		assertEquals(2, pyr2.getWidth());
//		assertEquals(3, pyr2.getHeight());
////		
////		// Now try with some other piece, made a different way
////		Piece l = new Piece(Piece.STICK_STR);
////		assertEquals(1, l.getWidth());
////		assertEquals(4, l.getHeight());
//		
//		//check size of stick
//		int[] stickSkirt = {0};
//		assertEquals(1, stick1.getWidth());
//		assertEquals(4, stick1.getHeight());
//		assertArrayEquals(stickSkirt, stick1.getSkirt());
//		
//		//check size of L1
//		int[] L1Skirt = {0,0};
//		assertEquals(2, L11.getWidth());
//		assertEquals(3, L11.getHeight());
//		assertArrayEquals(L1Skirt, L11.getSkirt());
//		
//		//check size of L2
//		int[] L2Skirt = {0,0};
//		assertEquals(2, L21.getWidth());
//		assertEquals(3, L21.getHeight());
//		assertArrayEquals(L2Skirt, L21.getSkirt());
//		
//		//check size of S1
//		int[] S1Skirt = {0,0,1};
//		assertEquals(3, S11.getWidth());
//		assertEquals(2, S11.getHeight());
//		assertArrayEquals(S1Skirt, S11.getSkirt());
//		
//		//check size of S2
//		int[] S2Skirt = {1,0,0};
//		assertEquals(3, S21.getWidth());
//		assertEquals(2, S21.getHeight());
//		assertArrayEquals(S2Skirt, S21.getSkirt());
//		
//		
//		//check size of sqr
//		int[] sqrSkirt = {0,0};
//		assertEquals(2, sqr1.getWidth());
//		assertEquals(2, sqr1.getHeight());
//		assertArrayEquals(sqrSkirt, sqr1.getSkirt());
//				
//	}
//	
//	
//	// Test the skirt returned by a few pieces
//	@Test
//	public void testSampleSkirt() {
//		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
//		// right for arrays.
//		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
//		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
//		
//		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
//		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
//	}
	
	
}
