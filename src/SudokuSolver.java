import java.awt.*;
import javax.swing.*;

public class SudokuSolver extends JFrame {

	// Sutoku static ===============================================================

	private static final long serialVersionUID = 5121011014101155908L;
	
	static int size_width  = 1000;
	static int size_height = 600;
		
	public static void main(String[] args) {
		// 進入主程式
		new SudokuSolver("SudokuSolver");
	}
	
	// SudokuSolver class  ===============================================================
	
	private BoardRenderer mainBoard  = new BoardRenderer(BoardRenderer.BoardType.MAIN_BOARD);
	private ResultRenderer gaResultRenderer  = new ResultRenderer(450, 200, 500, 50, "GA");
	
	public SudokuSolver (String title) {
		super(title);
		createWindow();
	}
	
	public void createWindow() {

		this.setSize(size_width, size_height);
		this.setPreferredSize(new Dimension(size_width, size_height));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 關閉視窗時離開主程式
		this.setLocationRelativeTo(null);						// 開啟時顯示於螢幕正中央
		this.setAlwaysOnTop(true);							// 顯示於最上方
		this.setResizable(false);								// 不可被調整大小
		
		Container mainPanel = this.getContentPane();
		mainPanel.setBackground(Color.GRAY);
		mainPanel.setLayout(null);
		
		// 新增面板
		mainPanel.add(mainBoard);
		mainPanel.add(gaResultRenderer);	

		
		// 棋盤 600*600  位置 50*50
		//...	// 其他 250*600  位置 50*700
		
		// 顯示視窗
		this.pack();
		this.setVisible(true);
	}
	
}
