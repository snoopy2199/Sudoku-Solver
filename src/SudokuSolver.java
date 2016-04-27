import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SudokuSolver extends JFrame implements ActionListener{

	private static final long serialVersionUID = 5121011014101155908L;
	
	static int size_width  = 1050;
	static int size_height = 600;
		
	public static void main(String[] args) {
		new SudokuSolver("SudokuSolver");
	}
	
	// SudokuSolver class  ===============================================================
	
	private BoardRenderer mainBoard  = new BoardRenderer(BoardRenderer.BoardType.MAIN_BOARD);
	private ResultRenderer gaResultRenderer  = new ResultRenderer(500, 200, 500, 50, "GA");
	private ResultRenderer forceResultRenderer  = new ResultRenderer(500, 200, 500, 300, "Force");
	private JButton example1Btn = new JButton("範例1");
	private JButton example2Btn = new JButton("範例2");
	private JButton resetBtn = new JButton("清除");
	private JButton solveBtn = new JButton("解題");
	private BruteForce bruteForce = new BruteForce();
	private GeneticAlgo geneticAlgo = new GeneticAlgo();
	
	
	/*
	 * 建構子
	 * @參數  title:視窗標題 
	 */
	public SudokuSolver (String title) {
		super(title);
		createWindow();
	}
	
	/*
	 * 建立視窗內容
	 */
	private void createWindow() {

		// 視窗設定
		this.setSize(size_width, size_height);
		this.setPreferredSize(new Dimension(size_width, size_height));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // 關閉視窗時離開主程式
		this.setLocationRelativeTo(null);                      // 開啟時顯示於螢幕正中央
		this.setAlwaysOnTop(true);                             // 顯示於最上方
		this.setResizable(false);                              // 不可被調整大小
			
		// 按鈕列
		JPanel buttonPanel = new JPanel();
		buttonPanel.setSize(400,50);
		buttonPanel.setLocation(50, 470);
		buttonPanel.setBackground(null);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(example1Btn);
		buttonPanel.add(example2Btn);
		buttonPanel.add(resetBtn);
		buttonPanel.add(solveBtn);
		
		// 監聽按鈕事件
		example1Btn.addActionListener(this);
		example2Btn.addActionListener(this);
		resetBtn.addActionListener(this);
		solveBtn.addActionListener(this);
		
		// 面板
		Container mainPanel = this.getContentPane();
		mainPanel.setBackground(null);
		mainPanel.setLayout(null);

		mainPanel.add(mainBoard);            // 棋盤 400*400
		mainPanel.add(gaResultRenderer);     // 基因演算法結果視窗 500*200
		mainPanel.add(forceResultRenderer);	 // 暴力解結果視窗 500*200
		mainPanel.add(buttonPanel);
		
		// 顯示視窗
		this.pack();
		this.setVisible(true);
	}
	
	/*
	 * 按鈕事件
	 */
	public void actionPerformed(ActionEvent e) {

		// 按鈕"範例1"
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
		
		// 按鈕"範例2"
		if (e.getSource() == example2Btn) {
			int[][] example2 = {
					{5,3,0,0,7,0,0,0,0},
					{6,0,0,1,9,5,0,0,0},
					{0,9,8,0,0,0,0,6,0},
					{8,0,0,0,6,0,0,0,3},
					{4,0,0,8,0,3,0,0,1},
					{7,0,0,0,2,0,0,0,6},
					{0,6,0,0,0,0,2,8,0},
					{0,0,0,4,1,9,0,0,5},
					{0,0,0,0,8,0,0,7,9}
			};
			mainBoard.setContent(true, example2);
		}
		
		// 按鈕"清除"
		if (e.getSource() == resetBtn) {
			mainBoard.clear();
		}
		
		// 按鈕"解題"
		if (e.getSource() == solveBtn) {
			// 暴力破解法
			Sudoku.get().setQuestion(mainBoard.getQuestion());
			bruteForce.run();
			forceResultRenderer.setResultTime(bruteForce.getCostTime());
			forceResultRenderer.setEndPoint(bruteForce.getEndPoint());
			forceResultRenderer.setGenerationCount(bruteForce.getTimes());
			forceResultRenderer.setData(bruteForce.getFitnessValues());
			forceResultRenderer.showSudoku(Sudoku.get().getQuestion(), Sudoku.get().getAnswer());
			
			// 基因演算法
			// TODO
		}
	}
}
