package ga.datamining.sutoku;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BoardRenderer extends JPanel implements MouseMotionListener, MouseListener{
	
	private static final long serialVersionUID = -2233346051421465237L;

	public static int SIZE_WIDTH  	= 600;	// 背景寬度
	public static int SIZE_HEIGHT 	= 600;	// 背景高度
	public static int LOC_TOP 		= 50;	// 棋盤距離上方
	public static int LOC_LEFT 		= 50;	// 棋盤距離左方
	public static int GRID_SIZE 	= 60;	// 棋格大小
	public static int GRID_MIN 		= 30;	// 棋盤最小偏移(距離邊界)
	public static int GRID_MAX 		= 570;	// 棋盤最大偏移(距離邊界)
	public static float NORMAL_LINE = 1.0f;	// 普通線寬度
	public static float SPLIT_LINE 	= 2.0f;	// 特殊線寬度
	public static int MOUSE_POS_RAW_X = 0;	//滑鼠座標
	public static int MOUSE_POS_RAW_Y = 0;	//滑鼠座標
	public static int MOUSE_POS_X = 0;		//滑鼠棋盤位置
	public static int MOUSE_POS_Y = 0;		//滑鼠棋盤位置
	public static boolean HOVER_EFFECT = true;			// 滑過是否高亮
	public static boolean ENABLE_REMBER_LAST = true; 	// 是否紀錄最後按下的方塊
	
	// 取得畫筆物件
	private static BasicStroke NORMAL_STROKE = new BasicStroke(NORMAL_LINE);
	private static BasicStroke SPLIT_STROKE	 = new BasicStroke(SPLIT_LINE);
	private static Font BIG_FONT 	= new Font("TimesRoman", Font.PLAIN, 50);
	private static Font SMALL_FONT 	= new Font("TimesRoman", Font.PLAIN, 12);
	
	// 紀錄數值 0為空 1-9為顯示數字
	private int defaultContent [] = new int [81];
	private int content [] = new int [81];
	
	private boolean mouseDown = false;	// 滑鼠是否按下中
	private Point lastClick = null;		// 最後按下的位置
	
	public BoardRenderer() {
		this.setBackground(Color.WHITE);
		this.setSize(SIZE_WIDTH, SIZE_HEIGHT);
		this.setPreferredSize(new Dimension(SIZE_WIDTH, SIZE_HEIGHT));
		this.setLocation(LOC_TOP, LOC_LEFT);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;	// 開始2D繪圖設置
		
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
		
		defaultContent[10] = 3;
		content[66] = 8;
		// 數字
		g.setFont(BIG_FONT);
		for(int i = 0; i < 81; i++) {
			
			if(defaultContent[i] != 0) {
				g.setColor(Color.BLACK);
				g2.drawString(String.valueOf(defaultContent[i]),
						i % 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE), 
						i / 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE) + 35);
			} else if (content[i] != 0) {
				g.setColor(Color.BLUE);
				g2.drawString(String.valueOf(content[i]),
						i % 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE), 
						i / 9 * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE) + 35);
			}
		}
		g.setFont(SMALL_FONT);
		g.setColor(Color.BLACK);
		
		
		g2.drawString(String.format("mouse pos:%d:%d    mouse loc:%d:%d"
				+ "    last pos:%d:%d", 
				MOUSE_POS_RAW_X, MOUSE_POS_RAW_Y, MOUSE_POS_X, MOUSE_POS_Y,
				lastClick==null?0:lastClick.x, lastClick==null?0:lastClick.y), 30, 20
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
	
	// 滑過是否有特效
	public void setHoverEffect(boolean enable) {
		HOVER_EFFECT = enable;
		this.repaint();
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