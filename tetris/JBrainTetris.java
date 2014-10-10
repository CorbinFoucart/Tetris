package tetris;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

import tetris.Brain.Move;

public class JBrainTetris extends JTetris {
	
	private DefaultBrain brain;
	private Move idealLocation;
	private int saveCount;
	JCheckBox brainMode;
	JSlider adversary;
	JLabel ok;

	JBrainTetris(int pixels) {
		super(pixels);
		this.brain = new DefaultBrain();
	}
	
	/**
	 Creates a frame with a JTetris.
	*/
	public static void main(String[] args) {
		// Set GUI Look And Feel Boilerplate.
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);
	}
	
	/**
	 * Tick(DOWN) Override, allows the DefaultBrain to
	 * control the piece before as it falls. 
	 */
	@Override()
	public void tick(int verb) {
		super.tick(verb);
		if (verb == DOWN && brainMode.isSelected()) {
			// if it's a fresh piece, call the brain to give us 
			// some ideal real estate for it
			if (count != saveCount){
				board.undo();
				idealLocation = brain.bestMove(board, currentPiece, HEIGHT, null);
				board.commit();
				saveCount = count;
			}
			
			if (currentPiece == null) stopGame();
			if (idealLocation == null) stopGame();
			
			// figure out whether to rotate and do so
			boolean needsRotation = brainRotate();
			
			// figure out which way to move our piece L/R
			brainMove(needsRotation);
			board.undo();
			int result = board.place(currentPiece, currentX, currentY - 1);
			
			boolean failed = (result >= Board.PLACE_OUT_BOUNDS);
			if (failed) super.tick(DOWN);
			
		}
		
		
	}
	
	// rotate our piece if not agreeing with brain's instructions
	public boolean brainRotate() {
		if (!currentPiece.equals(idealLocation.piece)) {
//			if (currentPiece == null) stopGame();
//			if (idealLocation == null) stopGame();
			
			if (board.getMaxHeight() != HEIGHT){
				tick(ROTATE);
			}
			return true;
		}else return false;
	}
	
	// moves our piece one horizontal step closer to its ideal position
	// boolean is so we don't get hasty and drop a bad orientation
	// into a good slot.
	public void brainMove(boolean stillNeedsRotation) {
		int idealX = idealLocation.x;
		int dX = currentX - idealX;
		if (dX < 0) tick(RIGHT);
		if (dX > 0) tick(LEFT);
		if (dX == 0 && !stillNeedsRotation) tick(DROP);
	}
	
	// Picks next piece according to adversary
	@Override
	public Piece pickNextPiece() {
		Piece piece = super.pickNextPiece();
		int rNum = (int) (100*random.nextDouble());
		if (rNum < adversary.getValue()) {
			// find worst piece it could possibly be
			piece = findWorstPiece();
			ok.setText("*ok*");
		}else ok.setText("ok");
		return piece;
	}
	
	
		private Piece findWorstPiece() {
			double largestScore = 0;
			Piece returnPiece = pieces[0];
			for (int i = 0; i < pieces.length; i++) {
				Piece candidate = pieces[i];
				Move moveCandidate = brain.bestMove(board, candidate, HEIGHT, null);
				if (moveCandidate.score > largestScore) {
					returnPiece = pieces[i];
					largestScore = moveCandidate.score;
				}
			}
			
			return returnPiece;			
		}
	
	/**
	 * createControlPanel() override
	 */
	@Override()
	public JComponent createControlPanel() {
		JPanel panel =  (JPanel) super.createControlPanel();
		
		panel.add(new JLabel("Brain"));
		brainMode = new JCheckBox("Brain active");
		panel.add(brainMode);
		
		
		saveCount = 0;
		
		// add Adversary paneling
		JPanel little = new JPanel();
		little.add(new JLabel("Adversary"));
		adversary = new JSlider(0, 100, 0);
		adversary.setPreferredSize(new Dimension(120, 30));
		little.add(adversary);
		panel.add(little);
		ok = new JLabel("ok");
		panel.add(ok);

		
		
		
		
		
		return panel;
	}
	

}
