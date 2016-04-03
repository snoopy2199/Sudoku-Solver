import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardRenderer extends JPanel implements MouseMotionListener, MouseListener{
	
	private static final long serialVersionUID = -2233346051421465237L;

	public enum BoardType {
		MAIN_BOARD,
		SMALL_BOARD
	}
	
	private int SIZE_WIDTH      = 600;   // 背景寬度
	private int SIZE_HEIGHT     = 600;   // 背景高度
	private int LOC_TOP         = 50;	 // 棋盤距離上方
	private int LOC_LEFT        = 50;    // 棋盤距離左方
	private int GRID_SIZE       = 60;    // 棋格大小
	private int GRID_MIN        = 30;    // 棋盤最小偏移(距離邊界)
	private int GRID_MAX        = 570;   // 棋盤最大偏移(距離邊界)
	private float NORMAL_LINE   = 1.0f;  // 普通線寬度
	private float SPLIT_LINE    = 2.0f;  // 特殊線寬度
	private int MOUSE_POS_RAW_X 	= 0;     // 滑鼠X座標
	private int MOUSE_POS_RAW_Y 	= 0;     // 滑鼠Y座標
	private int MOUSE_POS_X     = -1;     // 滑鼠棋盤X位置
	private int MOUSE_POS_Y     = -1;     // 滑鼠棋盤Y位置
	private int BIG_FONT_SIZE   = 50;    // 棋盤數字大小
	private int SMALL_FONT_SIZE 	= 12;    // 一般文字大小
	private int BIG_FONT_OFFSET 	= 35;    // 棋盤數字偏移矯正
	private boolean HOVER_EFFECT       = true;  // 滑過是否高亮
	private boolean ENABLE_REMBER_LAST = true;  // 是否紀錄最後按下的方塊
	private boolean SHOW_INFO          = true;  // 是否顯示棋盤定位資訊
	
	// 取得畫筆物件
	private BasicStroke NORMAL_STROKE = new BasicStroke(NORMAL_LINE);
	private BasicStroke SPLIT_STROKE	  = new BasicStroke(SPLIT_LINE);
	private Font BIG_FONT 	= new Font("TimesRoman", Font.PLAIN, BIG_FONT_SIZE);
	private Font SMALL_FONT = new Font("TimesRoman", Font.PLAIN, SMALL_FONT_SIZE);
	
	// 紀錄數值 0為空 1-9為顯示數字
	private int[][] contentQ = new int[9][9];
	private int[][] contentA = new int[9][9];
	
	private boolean mouseDown = false;   // 滑鼠是否按下中
	private Point lastClick   = null;    // 最後按下的位置
	
	public BoardRenderer() {
		resetBoard();
	}

	public BoardRenderer(BoardType type) {
		if (type == BoardType.MAIN_BOARD) {
			SIZE_WIDTH  = 400;
			SIZE_HEIGHT = 400;
			BIG_FONT_SIZE = 30;
			BIG_FONT_OFFSET = 20;
			setBoardGrid(40, 20);
			SHOW_INFO = false;
		} else if (type == BoardType.SMALL_BOARD) {
			SIZE_WIDTH  = 150;
			SIZE_HEIGHT = 150;
			LOC_TOP   = 33;
			LOC_LEFT  = 10;
			setBoardGrid(15, 10);
			SHOW_INFO = false;
			HOVER_EFFECT = false;
			ENABLE_REMBER_LAST = false;
		}
		resetBoard();
	}
	
	protected void resetBoard() {
		this.removeMouseListener(this);
		this.removeMouseMotionListener(this);
		this.setBackground(Color.WHITE);
		this.setSize(SIZE_WIDTH, SIZE_HEIGHT);
		this.setPreferredSize(new Dimension(SIZE_WIDTH, SIZE_HEIGHT));
		this.setLocation(LOC_LEFT, LOC_TOP);
		
		NORMAL_STROKE = new BasicStroke(NORMAL_LINE);
		SPLIT_STROKE = new BasicStroke(SPLIT_LINE);
		BIG_FONT = new Font("TimesRoman", Font.PLAIN, BIG_FONT_SIZE);
		SMALL_FONT = new Font("TimesRoman", Font.PLAIN, SMALL_FONT_SIZE);
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;	// 開始2D繪圖設置

		// 高亮最後按下的位置
		if (ENABLE_REMBER_LAST && lastClick != null) {
			g.setColor(Color.decode("#ef9a9a"));
			
			g.fillRect((lastClick.x)*GRID_SIZE+GRID_MIN+1,
					(lastClick.y)*GRID_SIZE+GRID_MIN+1, 
					GRID_SIZE-1, GRID_SIZE-1);
		}
		
		// 滑過高亮
		if (HOVER_EFFECT && MOUSE_POS_X != -1 && MOUSE_POS_Y != -1) {
			
			if (mouseDown) {
			// 滑鼠按下中
				if (ENABLE_REMBER_LAST && lastClick != null 
					&& lastClick.x == MOUSE_POS_X 
					&& lastClick.y == MOUSE_POS_Y ) {
					g.setColor(Color.decode("#b71c1c"));
				} else {
					g.setColor(Color.decode("#64b5f6"));
				}
			} else {
			// 滑鼠未按下中
				if (ENABLE_REMBER_LAST && lastClick != null 
					&& lastClick.x == MOUSE_POS_X 
					&& lastClick.y == MOUSE_POS_Y) {
					g.setColor(Color.decode("#ef5350"));	
				} else {
					g.setColor(Color.decode("#bbdefb"));
				}
			}
			g.fillRect((MOUSE_POS_X)*GRID_SIZE+GRID_MIN+1,
					(MOUSE_POS_Y)*GRID_SIZE+GRID_MIN+1, 
					GRID_SIZE-1, GRID_SIZE-1);
		}
		
		// 數字
		g.setFont(BIG_FONT);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (contentQ[i][j] != 0) {
					g.setColor(Color.BLACK);
					g2.drawString(String.valueOf(contentQ[i][j]),
							j * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE),
							i * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE) + BIG_FONT_OFFSET);
				}
				else if (contentA[i][j] != 0) {
					g.setColor(Color.BLUE);
					g2.drawString(String.valueOf(contentA[i][j]),
							j * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE),
							i * GRID_SIZE + GRID_MIN + (int)(0.25 * GRID_SIZE) + BIG_FONT_OFFSET);
				}
			}
		}
		
		g.setFont(SMALL_FONT);
		g.setColor(Color.BLACK);
		
		// 外框
		g2.setStroke(new BasicStroke(SPLIT_LINE));
		g.drawLine(GRID_MIN, GRID_MIN, GRID_MAX, GRID_MIN);
		g.drawLine(GRID_MAX, GRID_MIN, GRID_MAX, GRID_MAX);
		g.drawLine(GRID_MAX, GRID_MAX, GRID_MIN, GRID_MAX);
		g.drawLine(GRID_MIN, GRID_MAX, GRID_MIN, GRID_MIN);
				
		// 內框
		g2.setStroke(NORMAL_STROKE);
		for (int i = GRID_MIN + GRID_SIZE, count = 0; i < GRID_MAX; i += GRID_SIZE, count++) {
			if ((count == 2) || (count == 5)) {
				g2.setStroke(SPLIT_STROKE);
			}	
			g.drawLine(i, GRID_MIN, i, GRID_MAX);
			g.drawLine(GRID_MIN, i, GRID_MAX, i);
			g2.setStroke(NORMAL_STROKE);
		}
		
		// 資訊
		if (SHOW_INFO) {
			g2.drawString(String.format("mouse pos:%d:%d    mouse loc:%d:%d"
				+ "    last pos:%d:%d", 
				MOUSE_POS_RAW_X, MOUSE_POS_RAW_Y, MOUSE_POS_X, MOUSE_POS_Y,
				lastClick==null?0:lastClick.x, lastClick==null?0:lastClick.y), GRID_MIN, GRID_MIN / 3 * 2
				);
		}				
	} 
	
	// 重設棋盤內容
	public void clear() {
		contentQ = new int[9][9];
		contentA = new int[9][9];
		this.repaint();
	}
	
	public boolean setContent(boolean isQuestion, int[][] content) {
		int[][] targetContent = isQuestion ? contentQ : contentA;
		
		if (content.length != 9) {
			return false;
		}
		
		for (int i = 0; i < 9; i++) {
			if (content[i].length != 9) {
				return false;
			}
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				targetContent[i][j] = content[i][j];
			}
		}
		
		this.repaint();
		return true;
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
	public BoardRenderer setBoardGrid(int size, int min) {
		GRID_SIZE = size;
		GRID_MIN = min;
		GRID_MAX = min + 9 * size;
		
		this.repaint();
		return this;
	}
	
	// 設定框厚度
	public BoardRenderer setBoardGridLine(int normal, int split) {
		NORMAL_LINE = normal;
		SPLIT_LINE = split;
		
		NORMAL_STROKE = new BasicStroke(NORMAL_LINE);
		SPLIT_STROKE = new BasicStroke(SPLIT_LINE);
		
		this.repaint();
		return this;
	}	

	// 設定數字顯示大小及位移
	public BoardRenderer setBigFont(int size, int offset) {
		BIG_FONT_SIZE = size;
		BIG_FONT_OFFSET = offset;
		
		BIG_FONT = new Font("TimesRoman", Font.PLAIN, BIG_FONT_SIZE);
		
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
	
	// MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		MOUSE_POS_RAW_X = e.getX();
		MOUSE_POS_RAW_Y = e.getY();
		
		if ((MOUSE_POS_RAW_X < GRID_MIN) || (MOUSE_POS_RAW_X > GRID_MAX - 1)) {
			MOUSE_POS_X = -1;
		} else {
			MOUSE_POS_X = (MOUSE_POS_RAW_X - GRID_MIN) / GRID_SIZE;
		}
		
		if ((MOUSE_POS_RAW_Y < GRID_MIN) || (MOUSE_POS_RAW_Y > GRID_MAX - 1)) {
			MOUSE_POS_Y = -1;
		} else {
			MOUSE_POS_Y = (MOUSE_POS_RAW_Y - GRID_MIN) / GRID_SIZE;
		}
		
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
		
		if (ENABLE_REMBER_LAST) {
			if (((lastClick != null) && (lastClick.x == MOUSE_POS_X) && (lastClick.y == MOUSE_POS_Y))
					|| (MOUSE_POS_X == -1) || (MOUSE_POS_Y == -1)) {
				lastClick = null;
			} else {
				lastClick = new Point(MOUSE_POS_X, MOUSE_POS_Y);
			}
		}
		
		this.repaint();
	}
}