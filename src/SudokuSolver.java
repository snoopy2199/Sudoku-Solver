import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SudokuSolver extends JFrame implements ActionListener{

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
	private ResultRenderer forceResultRenderer  = new ResultRenderer(450, 200, 500, 300, "Force");
	private JButton example1Btn = new JButton("範例1");
	private JButton example2Btn = new JButton("範例2");
	private JButton resetBtn = new JButton("清除");
	private JButton solveBtn = new JButton("解題");
	
	public SudokuSolver (String title) {
		super(title);
		createWindow();
	}
	
	public void createWindow() {

		this.setSize(size_width, size_height);
		this.setPreferredSize(new Dimension(size_width, size_height));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	   // 關閉視窗時離開主程式
		this.setLocationRelativeTo(null);                      // 開啟時顯示於螢幕正中央
		this.setAlwaysOnTop(true);                             // 顯示於最上方
		this.setResizable(false);                              // 不可被調整大小
			
		JPanel buttonPanel = new JPanel();
		buttonPanel.setSize(400,50);
		buttonPanel.setLocation(50, 470);
		buttonPanel.setBackground(null);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(example1Btn);
		buttonPanel.add(example2Btn);
		buttonPanel.add(resetBtn);
		buttonPanel.add(solveBtn);
		
		Container mainPanel = this.getContentPane();
		mainPanel.setBackground(null);
		mainPanel.setLayout(null);
		
		// 新增面板
		mainPanel.add(mainBoard);            // 棋盤 400*400
		mainPanel.add(gaResultRenderer);     // 基因演算法結果視窗 450*200
		mainPanel.add(forceResultRenderer);	 // 暴力解結果視窗 450*200
		mainPanel.add(buttonPanel);

		example1Btn.addActionListener(this);
		example2Btn.addActionListener(this);
		resetBtn.addActionListener(this);
		solveBtn.addActionListener(this);
		
		// 顯示視窗
		this.pack();
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == example1Btn) {
			int[][] example1 = {
					{0,0,1,0,0,0,0,8,0},
					{0,0,0,7,1,0,2,0,3},
					{3,0,5,0,0,0,0,0,0},
					{0,0,0,0,8,0,0,2,0},
					{6,0,0,5,0,7,0,0,1},
					{0,9,0,0,3,0,0,0,0},
					{0,0,0,0,0,0,1,0,9},
					{4,0,3,0,9,5,0,0,0},
					{0,1,0,0,0,0,4,0,0}
			};
			mainBoard.setContent(true, example1);
		}
		
		if (e.getSource() == example2Btn) {
			
		}
		
		if (e.getSource() == resetBtn) {
			mainBoard.clear();
		}
		
		if (e.getSource() == solveBtn) {
			
		}
	}
}