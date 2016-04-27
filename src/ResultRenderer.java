import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	private int CHART_WIDTH  = 160;                 //折線圖寬度
	private int CHART_HEIGHT = 140;                 //折線圖高度
	private int CHART_TOP    = 40;                  //折線圖上方距離
	private int CHART_LEFT   = 180;                 //折線圖左方距離
	private int ARROW_SIZE   = 10;                  //箭頭大小
	private int POINT_SIZE   = CHART_HEIGHT / -27 ; //資料傳換圖表刻度
	
	//文字資訊位置設定
	private int INFO_LEFT = 360;
	
	//建立棋盤
	private BoardRenderer smallBoard = new BoardRenderer(BoardRenderer.BoardType.SMALL_BOARD);
	
	// == 畫面資料 == //
	
	private Integer[] data = {};     //折線圖資料
	private String second = "0";     //執行時間
	private int endPoint = 0;        //最終點數
	private int generationCount = 0; //世代數量
	
	/*
	 * 建構子
	 * @參數  width: 寬
	 *      height: 高
	 *      left: 與視窗最左端之距離
	 *      top: 與視窗最上端之距離
	 *      type: 面板類別 (GA-基因演算法, Force-暴力破解法)
	 */
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
	
	/*
	 * 繪製
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//底色
		g.setColor(Color.WHITE);
		
		//-2為預留空間
		//40是圓弧程度，沒有用成變數，因為覺得應該不會去改
		g.fillRoundRect(0, 0, SIZE_WIDTH-2, SIZE_HEIGHT-2, 40, 40);

		//繪製顏色
		g.setColor(Color.black);
		
		//標題
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		if (TYPE.equals("GA")) {
			g.drawString("基因演算法", 15, 30);
		} else {
			g.drawString("暴力破解法", 15, 30);
		}
		
		//底框
		g.drawRoundRect(0, 0, SIZE_WIDTH-2, SIZE_HEIGHT-2, 40, 40);
		
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		
		//折線圖底圖
		g.drawLine(CHART_LEFT, CHART_TOP, CHART_LEFT, CHART_TOP + CHART_HEIGHT);
		g.drawLine(CHART_LEFT, CHART_TOP + CHART_HEIGHT, CHART_LEFT + CHART_WIDTH,  CHART_TOP + CHART_HEIGHT);
		g.drawLine(CHART_LEFT, CHART_TOP, CHART_LEFT - ARROW_SIZE, CHART_TOP + ARROW_SIZE);
		g.drawLine(CHART_LEFT, CHART_TOP, CHART_LEFT + ARROW_SIZE, CHART_TOP + ARROW_SIZE);
		g.drawLine(CHART_LEFT + CHART_WIDTH,  CHART_TOP + CHART_HEIGHT, CHART_LEFT + CHART_WIDTH - ARROW_SIZE,  CHART_TOP + CHART_HEIGHT - ARROW_SIZE);
		g.drawLine(CHART_LEFT + CHART_WIDTH,  CHART_TOP + CHART_HEIGHT, CHART_LEFT + CHART_WIDTH - ARROW_SIZE,  CHART_TOP + CHART_HEIGHT + ARROW_SIZE);
		
		g.drawString("point", CHART_LEFT - 15, CHART_TOP - 10);
		g.drawString("times", CHART_LEFT + CHART_WIDTH + 10, CHART_TOP + CHART_HEIGHT);
		
		//秒數
		//距離上方的高度固定為60
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
		g.drawString("執行時間：" + second, INFO_LEFT, 60);
		g.drawString("最終點數：" + endPoint, INFO_LEFT, 85);
		
		//根據圖表不同給予不同標題
		if (TYPE.equals("GA")) {
			g.drawString("世代數量：" + generationCount, INFO_LEFT, 110);
		} else {
			g.drawString("嘗試次數：" + generationCount, INFO_LEFT, 110);
		}
		
		//圖表繪製顏色 紅色
		g.setColor(Color.red);
		
		//有資料的時候執行
		if (data.length > 0) {
			//依照資料數量，分配橫軸刻度大小
			double interval = (double)(CHART_WIDTH) / data.length;
			
			//圖表資料繪製
			for (int i = 0; i < data.length - 1; i++) {
				int startLeft = (int)(CHART_LEFT + interval * i);
				int startTop = (int)(CHART_TOP + (data[i] - 27) * POINT_SIZE);
				int endLeft = (int)(CHART_LEFT + interval * (i + 1));
				int endTop = (int)(CHART_TOP + (data[i+1] - 27) * POINT_SIZE);
				g.drawLine(startLeft, startTop, endLeft, endTop);
			}
		}
		
	}	
	
	/*
	 * 設定結束時間
	 * @參數  second: 結束時間(秒)
	 */
	public void setResultTime(double second) {
		this.second = String.format("%.04f", second);
		this.repaint();
	}
	
	/*
	 * 設定折線圖資料
	 * @參數  data: 繪圖資料 
	 */
	public void setData(Integer[] data){
		this.data = data;	
		this.repaint();
	}
	
	/*
	 * 設定最終點數
	 * @參數 point: 最終點數
	 */
	public void setEndPoint(int point) {
		endPoint = point;
		this.repaint();
	}
	
	/*
	 * 設定世代(執行)數量
	 * @參數  generationCount 執行次數
	 */
	public void setGenerationCount(int generationCount) {
		this.generationCount = generationCount;
		this.repaint();
	}
	
	/*
	 * 繪製棋盤
	 * @參數  question: 題目陣列
	 *      answer: 答案陣列
	 */
	public void showSudoku(int[][] question, int[][] answer) {
		smallBoard.setContent(true, question);
		smallBoard.setContent(false, answer);
	}
}