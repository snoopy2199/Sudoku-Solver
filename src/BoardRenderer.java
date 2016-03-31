import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BoardRenderer extends JPanel implements MouseMotionListener, MouseListener{
	
	private static final long serialVersionUID = -2233346051421465237L;

	public enum BoardType {
		MAIN_BOARD,
		SMALL_BOARD;
	}
	
	// 請不要直接改動預設值，若有需要修改請繼承此類別，並於子類別建構子中使用 super.SIZE_WIDTH 等重設參數
	
	private int SIZE_WIDTH  		= 600;	// 背景寬度
	private int SIZE_HEIGHT 		= 600;	// 背景高度
	private int LOC_TOP 			= 50;	// 棋盤距離上方
	private int LOC_LEFT 		= 50;	// 棋盤距離左方
	private int GRID_SIZE 		= 60;	// 棋格大小
	private int GRID_MIN 		= 30;	// 棋盤最小偏移(距離邊界)
	private int GRID_MAX 		= 570;	// 棋盤最大偏移(距離邊界)
	private float NORMAL_LINE 	= 1.0f;	// 普通線寬度
	private float SPLIT_LINE 	= 2.0f;	// 特殊線寬度
	private int MOUSE_POS_RAW_X 	= 0;	//滑鼠座標
	private int MOUSE_POS_RAW_Y 	= 0;	//滑鼠座標
	private int MOUSE_POS_X 		= 0;	//滑鼠棋盤位置
	private int MOUSE_POS_Y 		= 0;	//滑鼠棋盤位置
	private boolean HOVER_EFFECT = true;		// 滑過是否高亮
	private boolean ENABLE_REMBER_LAST = true; 	// 是否紀錄最後按下的方塊
	private int BIG_FONT_SIZE 		= 50;	// 棋盤數字大小
	private int SMALL_FONT_SIZE 	= 12;	// 一般文字大小
	private int BIG_FONT_OFFSET 	= 35;	// 棋盤數字偏移矯正
	private boolean SHOW_INFO 		= true; // 是否顯示棋盤定位資訊
	
	// 取得畫筆物件
	private BasicStroke NORMAL_STROKE 	= new BasicStroke(NORMAL_LINE);
	private BasicStroke SPLIT_STROKE	= new BasicStroke(SPLIT_LINE);
	private Font BIG_FONT 	= new Font("TimesRoman", Font.PLAIN, BIG_FONT_SIZE);
	private Font SMALL_FONT = new Font("TimesRoman", Font.PLAIN, SMALL_FONT_SIZE);
	
	// 紀錄數值 0為空 1-9為顯示數字
	private int defaultContent [] = new int [81];
	private int content [] = new int [81];
	private boolean mouseDown = false;	// 滑鼠是否按下中
	private Point lastClick = null;		// 最後按下的位置
	
	public BoardRenderer() {
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		resetBoard();
	}
	
	public BoardRenderer(int width, int height, int left, int top, boolean isHighLightAndHover, int fontSize, int fontOffset) {
		this();
		setBoardSize(width, height);
		setBoardLoc(left, top);
		setHoverEffect(isHighLightAndHover);
		setRememberLast(isHighLightAndHover);
		setBigFont(fontSize, fontOffset);
	}

	public BoardRenderer(BoardType type) {
		this();

		if(type == BoardType.MAIN_BOARD) {
			// 預設值
		} else if(type == BoardType.SMALL_BOARD) {
			// 小棋盤設定
			
			
		}
		
	}
	
	protected void resetBoard() {
		this.setBackground(Color.WHITE);
		this.setSize(SIZE_WIDTH, SIZE_HEIGHT);
		this.setPreferredSize(new Dimension(SIZE_WIDTH, SIZE_HEIGHT));
		this.setLocation(LOC_LEFT, LOC_TOP);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;	// 開始2D繪圖設置

		// 高亮最後按下的位置
		if(ENABLE_REMBER_LAST && lastClick != null && !isDefaultContent(lastClick.x, lastClick.y)) {
			g.setColor(Color.decode("#ef9a9a"));
			
			g.fillRect((lastClick.x-1)*GRID_SIZE+1+GRID_MIN,
					(lastClick.y-1)*GRID_SIZE+1+GRID_MIN, 
					GRID_SIZE-1, GRID_SIZE-1);
		}
		
		// 高亮
		if (HOVER_EFFECT && MOUSE_POS_X != 0 && MOUSE_POS_Y != 0) {
			// 預設部分不會被高亮
			if(!isDefaultContent(MOUSE_POS_X, MOUSE_POS_Y)) {
				if(mouseDown) {
					// 滑鼠按下中
					if(ENABLE_REMBER_LAST && lastClick != null 
							&& lastClick.x == MOUSE_POS_X 
							&& lastClick.y == MOUSE_POS_Y ) {
						g.setColor(Color.decode("#b71c1c"));
					} else {
						g.setColor(Color.decode("#64b5f6"));
					}
					
				} else {
					// 滑鼠未按下中
					if(ENABLE_REMBER_LAST && lastClick != null 
							&& lastClick.x == MOUSE_POS_X 
							&& lastClick.y == MOUSE_POS_Y) {
						g.setColor(Color.decode("#ef5350"));	
					} else {
						g.setColor(Color.decode("#bbdefb"));
					}
				}
				g.fillRect((MOUSE_POS_X-1)*GRID_SIZE+1+GRID_MIN,
						(MOUSE_POS_Y-1)*GRID_SIZE+1+GRID_MIN, 
						GRID_SIZE-1, GRID_SIZE-1);
			}
		}
		
		// 數字
		g.setFont(BIG_FONT);
		for(int i = 0; i < 81; i++) {
			
			if(defaultContent[i] != 0) {
				g.setColor(Color.BLACK);
				g2.drawString(String.valueOf(defaultContent[i]),
						i % 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE), 
						i / 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE) + BIG_FONT_OFFSET);
			} else if (content[i] != 0) {
				g.setColor(Color.BLUE);
				g2.drawString(String.valueOf(content[i]),
						i % 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE), 
						i / 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE) + BIG_FONT_OFFSET);
			}
		}
		g.setFont(SMALL_FONT);
		g.setColor(Color.BLACK);
		
		// 外框
		g2.setStroke(new BasicStroke(SPLIT_LINE));
		g.drawLine(GRID_MIN,GRID_MIN,GRID_MAX,GRID_MIN);
		g.drawLine(GRID_MAX,GRID_MIN,GRID_MAX,GRID_MAX);
		g.drawLine(GRID_MAX,GRID_MAX,GRID_MIN,GRID_MAX);
		g.drawLine(GRID_MIN,GRID_MAX,GRID_MIN,GRID_MIN);
				
		// 內框
		g2.setStroke(NORMAL_STROKE);
		for(int i = GRID_MIN + GRID_SIZE, count = 0; i < GRID_MAX; i += GRID_SIZE, count++) {
			if (count == 2 || count == 5) g2.setStroke(SPLIT_STROKE);
					
			g.drawLine(i ,GRID_MIN,i ,GRID_MAX);
			g.drawLine(GRID_MIN, i, GRID_MAX, i);
			g2.setStroke(NORMAL_STROKE);
		}
		
		// 資訊
		if(SHOW_INFO)
			g2.drawString(String.format("mouse pos:%d:%d    mouse loc:%d:%d"
				+ "    last pos:%d:%d", 
				MOUSE_POS_RAW_X, MOUSE_POS_RAW_Y, MOUSE_POS_X, MOUSE_POS_Y,
				lastClick==null?0:lastClick.x, lastClick==null?0:lastClick.y), GRID_MIN, GRID_MIN / 3 * 2
				);
		
		
	} 
	
	// 刪除所有內容(重設)
	public void clear() {
		defaultContent = new int[81];
		content = new int[81];
		this.repaint();
	}
	
	// 刪除除了Default的內容
	public void clearFilled() {
		content = new int[81];
		this.repaint();
	}
	
	public void setToDefault() {
		defaultContent = content;
		this.repaint();
		
	}
	
	// 設置預設數字(非必要=0)，數字顏色也會不同
	// 傳入長度81陣列，可用clearFilled 清除設置預設數字以外的數字
	// PS 設置後this.content會被清除 (可活動部分數字)
	public boolean setDefaultContent(int content []) {
		if (content.length != 81) return false;
		
		// 檢查是否有非法字元
		for(int i : content)
			if(i < 0 || i > 9) return false;
		
		defaultContent = content;
		this.content = new int [81];
		this.repaint();
		return true;
	}
	
	public boolean setDefaultContent(int content [][]) {
		if (content.length != 9) return false;
		int newContent [] = new int [81];
		for(int i = 0; i < 9; i++) {
			if (content[i].length != 9) return false;
			for(int j = 0; j < 9; j++) 
				newContent[j*9 + i] = content [i][j];
		}
		
		return setDefaultContent(newContent);
	}
	
	// 重設棋盤內容
	// 傳入長度81陣列    留白或0 = 不更動,    -1 = 刪除該格內容(若為Default則略過),
	// 其他數字則為欲顯示的數字 (若該格為Default則略過) 若含非法字原則傳回false
	public boolean refreshContent(int content []) {
		if (content.length != 81) return false;
		
		// 檢查是否有非法字元
		for(int i : content)
			if(i < -1 || i > 9) return false;
		
		for(int i = 0, nowValue = content[i]; i < content.length; nowValue = content[++i])
			if(nowValue == 0) continue;	// 不變動
			else if(defaultContent[i] != 0) continue;	// 該格式default，無法變動
			else if(nowValue == -1) this.content[i] = 0;
			else this.content[i] = nowValue;
		
		this.repaint();
		return true;
	}
	
	public boolean refreshContent(int content [][]) {
		if (content.length != 9) return false;
		int newContent [] = new int [81];
		for(int i = 0; i < 9; i++) {
			if (content[i].length != 9) return false;
			for(int j = 0; j < 9; j++) 
				newContent[j*9 + i] = content [i][j];
		}
		
		return refreshContent(newContent);
	}
	
	// 滑過是否有特效
	public BoardRenderer setHoverEffect(boolean enable) {
		HOVER_EFFECT = enable;
		this.repaint();
		return this;
	}
	
	// 是否記錄最後按下的位置
	public BoardRenderer setRememberLast(boolean enable) {
		ENABLE_REMBER_LAST = enable;
		this.repaint();
		return this;
	}	
	
	// 設定大小
	public BoardRenderer setBoardSize(int width, int height) {
		SIZE_WIDTH = width;
		SIZE_HEIGHT = height;
		
		this.setSize(SIZE_WIDTH, SIZE_HEIGHT);
		this.setPreferredSize(new Dimension(SIZE_WIDTH, SIZE_HEIGHT));
		
		this.repaint();
		return this;
	}
	
	// 設定位置
	public BoardRenderer setBoardLoc(int left, int top) {
		LOC_TOP = top;
		LOC_LEFT = left;
		
		this.setLocation(LOC_LEFT, LOC_TOP);
		
		this.repaint();
		return this;
	}
	
	// 設定格子
	public BoardRenderer setBoardGrid(int size, int min, int max) {
		GRID_SIZE = size;
		GRID_MIN = min;
		GRID_MAX = max;
		this.repaint();
		return this;
	}
	
	// 設定框厚度
	public BoardRenderer setBoardGrid(int normal, int split) {
		NORMAL_LINE = normal;
		SPLIT_LINE = split;
		
		NORMAL_STROKE 	= new BasicStroke(NORMAL_LINE);
		SPLIT_STROKE	= new BasicStroke(SPLIT_LINE);
		
		this.repaint();
		return this;
	}	

	// 設定數字顯示大小及位移
	public BoardRenderer setBigFont(int size, int offset) {
		BIG_FONT_SIZE = size;
		BIG_FONT_OFFSET = offset;
		
		BIG_FONT 	= new Font("TimesRoman", Font.PLAIN, BIG_FONT_SIZE);
		
		this.repaint();
		return this;
	}		
	
	// 設定資訊文字顯示大小
	public BoardRenderer setSmallFont(int size) {
		SMALL_FONT_SIZE = size;
		
		SMALL_FONT = new Font("TimesRoman", Font.PLAIN, SMALL_FONT_SIZE);
		
		this.repaint();
		return this;
	}		

	// 設定是否顯示資訊文字
	public BoardRenderer setShowInfo(boolean enable) {
		SHOW_INFO = enable;
		this.repaint();
		return this;
	}
	
	// 取得滑鼠停在的格子 1-9 (0為不在格子上)
	public Point getMousePosition() {
		return new Point(MOUSE_POS_X, MOUSE_POS_Y);
	}

	// 取得最後位置
	public Point getLastClick() {
		return lastClick;
	}
	
	// 取得是否預設內容
	public boolean isDefaultContent(int x, int y) {
		if(x == 0 || y == 0) return false;
		if(defaultContent[(y-1)*9 + (x-1)] == 0)
			return false;
		else return true;
	}
	
	// MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		MOUSE_POS_RAW_X = e.getX();
		MOUSE_POS_RAW_Y = e.getY();
		
		if (MOUSE_POS_RAW_X < GRID_MIN || MOUSE_POS_RAW_X > GRID_MAX - 1) MOUSE_POS_X = 0;
		else MOUSE_POS_X = (MOUSE_POS_RAW_X - GRID_MIN) / GRID_SIZE + 1;
		if (MOUSE_POS_RAW_Y < GRID_MIN || MOUSE_POS_RAW_Y > GRID_MAX - 1) MOUSE_POS_Y = 0;
		else MOUSE_POS_Y = (MOUSE_POS_RAW_Y - GRID_MIN) / GRID_SIZE + 1;
		
		if(MOUSE_POS_X == 0 || MOUSE_POS_Y == 0) MOUSE_POS_X = MOUSE_POS_Y = 0;
		
		this.repaint();
	}

	// MouseListener
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseDown = true;
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseDown = false;
		
		if (ENABLE_REMBER_LAST && !isDefaultContent(MOUSE_POS_X, MOUSE_POS_Y)) {
			if( (lastClick != null && lastClick.x == MOUSE_POS_X && lastClick.y == MOUSE_POS_Y)
					|| MOUSE_POS_X == 0 || MOUSE_POS_Y == 0)
				lastClick = null;
			else lastClick = new Point(MOUSE_POS_X, MOUSE_POS_Y);
		}
		
		this.repaint();
	}
	
	
}




/**
*	BoardRenderer 
*	包含棋盤顯示設定等功能
*	Gama
*/