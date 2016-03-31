import java.awt.Dimension;

import javax.swing.JPanel;

public class ResultRenderer extends JPanel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultRenderer(int width, int height, int left, int top, String type) {
		this.setSize(width, height);
		this.setPreferredSize(new Dimension(width, width));
		this.setLocation(left, top);
	}
	
}
