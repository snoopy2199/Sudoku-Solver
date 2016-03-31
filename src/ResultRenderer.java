import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ResultRenderer extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private int SIZE_WIDTH;
	private int SIZE_HEIGHT;
	private int LOC_TOP;
	private int LOC_LEFT;
	private String TYPE;	//辨別結果視窗
	private BoardRenderer smallBoard = new BoardRenderer(BoardRenderer.BoardType.SMALL_BOARD);

	public ResultRenderer(int width, int height, int left, int top, String type) {
		SIZE_HEIGHT = height;
		SIZE_WIDTH = width;
		LOC_TOP = top;
		LOC_LEFT = left;
		TYPE = type;
		
		this.setSize(SIZE_WIDTH, SIZE_HEIGHT);
		this.setPreferredSize(new Dimension(SIZE_WIDTH, SIZE_HEIGHT));
		this.setLocation(LOC_LEFT, LOC_TOP);
		this.setBackground(null);
		
		//加入棋盤物件
		this.setLayout(null);
		this.add(smallBoard);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;	// 開始2D繪圖設置
		
		//底色
		g.setColor(Color.WHITE);
		g.fillRoundRect(0, 0, SIZE_WIDTH-2, SIZE_HEIGHT-2, 40, 40);

		//底框
		g.setColor(Color.black);
		g.drawRoundRect(0, 0, SIZE_WIDTH-2, SIZE_HEIGHT-2, 40, 40);
		
	}
	
}
