package ga.datamining.sutoku;

import java.awt.*;
import javax.swing.*;

public class BoardRenderer extends JPanel{
	
	public BoardRenderer() {
		setBackground(Color.LIGHT_GRAY);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawLine(100,100,150,150);
	} 
}
