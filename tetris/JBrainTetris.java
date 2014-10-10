package tetris;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class JBrainTetris extends JTetris {
	
	private DefaultBrain brain;

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
	 * createControlPanel() override
	 */
	@Override()
	public JComponent createControlPanel() {
		JPanel panel =  (JPanel) super.createControlPanel();
		
		panel.add(new JLabel("Brain"));
		JCheckBox brainMode = new JCheckBox("Brain active");
		panel.add(brainMode);
		return panel;
	}
	

}
