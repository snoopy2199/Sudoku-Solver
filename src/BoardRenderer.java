import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardRenderer extends JPanel implements MouseMotionListener, MouseListener{
	
	private static final long serialVersionUID = -2233346051421465237L;

	public enum BoardType {
		MAIN_BOARD,
		SMALL_BOARD
	}
	
	// == 畫面設計 == //
	
	private int SIZE_WIDTH      = 600;      // 背景寬度
	private int SIZE_HEIGHT     = 600;      // 背景高度
	private int LOC_TOP         = 50;	// 棋盤距離上方
	private int LOC_LEFT        = 50;       // 棋盤距離左方
	private int GRID_SIZE       = 60;       // 棋格大小
	private int GRID_MIN        = 30;       // 棋盤最小偏移(距離邊界)
	private int GRID_MAX        = 570;      // 棋盤最大偏移(距離邊界)
	private float NORMAL_LINE   = 1.0f;     // 普通線寬度
	private float SPLIT_LINE    = 2.0f;     // 特殊線寬度
	private int MOUSE_POS_RAW_X 	= 0;    // 滑鼠X座標
	private int MOUSE_POS_RAW_Y 	= 0;    // 滑鼠Y座標
	private int MOUSE_POS_X     = -1;       // 滑鼠棋盤X位置
	private int MOUSE_POS_Y     = -1;       // 滑鼠棋盤Y位置
	private int BIG_FONT_SIZE   = 50;       // 棋盤數字大小
	private int SMALL_FONT_SIZE 	= 12;   // 一般文字大小
	private int BIG_FONT_OFFSET 	= 35;   // 棋盤數字偏移矯正
	private boolean HOVER_EFFECT       = true;  // 滑過是否高亮
	private boolean ENABLE_REMBER_LAST = true;  // 是否紀錄最後按下的方塊
	
	// 取得畫筆物件
	private BasicStroke NORMAL_STROKE = new BasicStroke(NORMAL_LINE);
	private BasicStroke SPLIT_STROKE  = new BasicStroke(SPLIT_LINE);
	private Font BIG_FONT 	= new Font("TimesRoman", Font.PLAIN, BIG_FONT_SIZE);
	private Font SMALL_FONT = new Font("TimesRoman", Font.PLAIN, SMALL_FONT_SIZE);
	
	// == 畫面資料 == //
	
	// contentQ為題目
	// contentA為解答
	// 值為0時為空
	private int[][] contentQ = new int[9][9];
	private int[][] contentA = new int[9][9];
	
	private boolean mouseDown = false;   // 滑鼠是否按下中
	private Point lastClick   = null;    // 最後按下的位置
	
	public BoardRenderer() {
		resetBoard();
	}
	
	/*
	 * 建構子
	 * @參數  type: MAIN_BOARD-輸入題目用, SMALL_BOARD-呈現解答用
	 */
	public BoardRenderer(BoardType type) {
		if (type == BoardType.MAIN_BOARD) {
			SIZE_WIDTH  = 400;
			SIZE_HEIGHT = 400;
			BIG_FONT_SIZE = 30;
			BIG_FONT_OFFSET = 20;
			setBoardGrid(40, 20);
		} else if (type == BoardType.SMALL_BOARD) {
			SIZE_WIDTH  = 150;
			SIZE_HEIGHT = 150;
			BIG_FONT_SIZE = 12;
			BIG_FONT_OFFSET = 9;
			LOC_TOP   = 33;
			LOC_LEFT  = 10;
			setBoardGrid(15, 10);
			HOVER_EFFECT = false;
			ENABLE_REMBER_LAST = false;
		}
		resetBoard();
	}
	
	/*
	 * 初始化棋盤
	 */
	protected void resetBoard() {
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
	
	/*
	 * 繪製
	 */
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
		
		// 繪製數字
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
	} 
	
	/*
	 * 設定格子
	 */
	public void setBoardGrid(int size, int min) {
		GRID_SIZE = size;
		GRID_MIN = min;
		GRID_MAX = min + 9 * size;
		
		this.repaint();
	}
	
	/*
	 * MouseMotionListener
	 */
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		MOUSE_POS_RAW_X = e.getX();
		MOUSE_POS_RAW_Y = e.getY();
		
		if ((MOUSE_POS_RAW_X < GRID_MIN) || (MOUSE_POS_RAW_X > GRID_MAX - 1)) {
			MOUSE_POS_X = -1;    // 超出格子範圍
		} else {
			MOUSE_POS_X = (MOUSE_POS_RAW_X - GRID_MIN) / GRID_SIZE;
		}
		
		if ((MOUSE_POS_RAW_Y < GRID_MIN) || (MOUSE_POS_RAW_Y > GRID_MAX - 1)) {
			MOUSE_POS_Y = -1;    // 超出格子範圍
		} else {
			MOUSE_POS_Y = (MOUSE_POS_RAW_Y - GRID_MIN) / GRID_SIZE;
		}
		
		this.repaint();
	}

	/*
	 * MouseListener
	 */
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
		
		// 記錄上次點選
		if (ENABLE_REMBER_LAST) {
			if (((lastClick != null) && (lastClick.x == MOUSE_POS_X) && (lastClick.y == MOUSE_POS_Y))
					|| (MOUSE_POS_X == -1) || (MOUSE_POS_Y == -1)) {
			} else {
				lastClick = new Point(MOUSE_POS_X, MOUSE_POS_Y);	
			}
		}
		
		// 為記錄上次點選模式 且 按下右鍵
		// 顯示數字鍵盤
		if (ENABLE_REMBER_LAST && SwingUtilities.isRightMouseButton(arg0)) {
			doPop(arg0);
		}
		
		this.repaint();
	}
	
	/*
	 * 數字鍵盤
	 */
	class NumberKeyboard extends JPopupMenu implements ActionListener {

		private static final long serialVersionUID = 1L;

		/*
		 * 建構子
		 */
		public NumberKeyboard(){
			JMenuItem item;
			GridBagConstraints constraints;
			
			// 數字鍵 1-9
			this.setLayout(new GridBagLayout());
			for (int i = 0; i < 3; i++) {
				for (int j = 1; j <= 3; j++) {
					item = new JMenuItem(String.valueOf(i*3+j));
					constraints = new GridBagConstraints();
					constraints.gridx = j-1;
					constraints.gridy = i;
					constraints.gridwidth = 1;
					constraints.gridheight = 1;
					item.addActionListener(this);
					this.add(item, constraints);
				}
			}
			
			// 清除鍵 (佔3格)
			item = new JMenuItem("清除");
			constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 3;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.BOTH;
	        item.addActionListener(this);
			this.add(item, constraints);
		}

		/*
		 * 事件
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getActionCommand().equals("清除")){
				// 清除
				contentQ[lastClick.y][lastClick.x] = 0; 
			} else {
				// 數字鍵
				contentQ[lastClick.y][lastClick.x] = Integer.parseInt(e.getActionCommand()); 
			}

			lastClick = null;
			BoardRenderer.this.repaint();
		}
	}
	
	/*
	 * 顯示數字鍵盤
	 */
	 private void doPop(MouseEvent e){
		 NumberKeyboard numberKeyboard = new NumberKeyboard();
		 numberKeyboard.show(e.getComponent(), e.getX(), e.getY());
	}
	
	/*
	 * 填入資料
	 * @參數  isQuestion: true-填入資料作為題目, false-填入資料作為答案
	 *      content: 資料
	 * @回傳 true-成功, false-失敗
	 */
	public boolean setContent(boolean isQuestion, int[][] content) {
		int[][] targetContent = isQuestion ? contentQ : contentA;
		
		// 檢查是否為9行
		if (content.length != 9) {
			return false;
		}
		
		// 檢查每行是否各為9列
		for (int i = 0; i < 9; i++) {
			
			if (content[i].length != 9) {
				return false;
			}
		}
		
		// 填入值
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				targetContent[i][j] = content[i][j];
			}
		}
		
		this.repaint();
		return true;
	}
	
	/*
	 * 取得題目
	 * @回傳 題目二維陣列
	 */
	public int[][] getQuestion() {
		return contentQ;
	}
	
	/*
	 * 清除資料 
	 */
	public void clear() {
		contentQ = new int[9][9];
		contentA = new int[9][9];
		this.repaint();
	}
}
