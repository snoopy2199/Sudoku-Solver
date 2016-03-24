package ga.datamining.sutoku;

import java.awt.*;
import javax.swing.*;

public class BoardRenderer extends JPanel{
	
	private static final long serialVersionUID = -2233346051421465237L;

	static int size_width  = 600;
	static int size_height = 600;
	
	public BoardRenderer() {
		this.setBackground(Color.WHITE);
		this.setSize(size_width, size_height);
		this.setPreferredSize(new Dimension(size_width, size_height));
		this.setLocation(50, 50);
		
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 外框
		g.drawLine(30 ,30 ,570,30);
		g.drawLine(570,30 ,570,570);
		g.drawLine(570,570,30 ,570);
		g.drawLine(30 ,570,30 ,30);
		
		// 內框
		for(int i = 90; i < 570; i += 60 ) {
			g.drawLine(i ,30,i ,570);
			g.drawLine(30, i, 570, i);
		}
		
		// 數字
		
		
		
		
	} 
	
	public void refreshContent(int nums []) {
		
		
	}
	
}
