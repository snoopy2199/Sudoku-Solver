import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ResultRenderer extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private String TYPE;	//辨別結果視窗
	
	// == 畫面設計 == //
	
	//整體大小距離設定
	private int SIZE_WIDTH;
	private int SIZE_HEIGHT;
	private int LOC_TOP;
	private int LOC_LEFT;
	
	//折線圖設定
	private int CHART_WIDTH  = 160;     //折線圖寬度
	private int CHART_HEIGHT = 140;     //折線圖高度
	private int CHART_TOP    = 25;      //折線圖上方距離
	private int CHART_LEFT   = 180;     //折線圖左方距離
	private int ARROW_SIZE   = 10;      //箭頭大小
	
	//秒數位置設定
	private int TIME_LEFT = 340;
	
	//建立棋盤
	private BoardRenderer smallBoard = new BoardRenderer(BoardRenderer.BoardType.SMALL_BOARD);
	
	// == 畫面資料 == //
	
	private int[] data = {};      //折線圖資料
	private String second = "0";  //執行時間
	
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
		
		//底色
		g.setColor(Color.WHITE);
		
		//-2為預留空間
		//40是圓弧程度，沒有用成變數，因為覺得應該不會去改
		g.fillRoundRect(0, 0, SIZE_WIDTH-2, SIZE_HEIGHT-2, 40, 40);

		//底框
		g.setColor(Color.black);
		g.drawRoundRect(0, 0, SIZE_WIDTH-2, SIZE_HEIGHT-2, 40, 40);
		
		//折線圖底圖
		g.drawLine(CHART_LEFT, CHART_TOP, CHART_LEFT, CHART_TOP + CHART_HEIGHT);
		g.drawLine(CHART_LEFT, CHART_TOP + CHART_HEIGHT, CHART_LEFT + CHART_WIDTH,  CHART_TOP + CHART_HEIGHT);
		g.drawLine(CHART_LEFT, CHART_TOP, CHART_LEFT - ARROW_SIZE, CHART_TOP + ARROW_SIZE);
		g.drawLine(CHART_LEFT, CHART_TOP, CHART_LEFT + ARROW_SIZE, CHART_TOP + ARROW_SIZE);
		g.drawLine(CHART_LEFT + CHART_WIDTH,  CHART_TOP + CHART_HEIGHT, CHART_LEFT + CHART_WIDTH - ARROW_SIZE,  CHART_TOP + CHART_HEIGHT - ARROW_SIZE);
		g.drawLine(CHART_LEFT + CHART_WIDTH,  CHART_TOP + CHART_HEIGHT, CHART_LEFT + CHART_WIDTH - ARROW_SIZE,  CHART_TOP + CHART_HEIGHT + ARROW_SIZE);
		
		//TODO:畫資料

		//秒數
		//距離上方的高度固定為50
		g.drawString("執行時間：" + second, TIME_LEFT, 50);
	}	
	
	//設定結束時間
	public void setResultTime(int second) {
		this.second = String.valueOf(second);
		this.repaint();
	}
	
	//設定折線圖資料
	public void setData(int[] data){
		this.data = data;
		this.repaint();
	}
}