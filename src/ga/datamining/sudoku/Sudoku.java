package ga.datamining.sudoku;

import java.awt.*;
import javax.swing.*;

public class Sudoku {

	// Sutoku static ===============================================================
	
	static int size_width  = 1000;
	static int size_height = 730;
	
	private static Sudoku sutoku = null;
	
	public static Sudoku get() {return sutoku == null ? (sutoku = new Sudoku()) : sutoku;}
	
	public static void main(String[] args) {
		// 進入主程式
		Sudoku.get();
		
	}
	
	// Sutoku class  ===============================================================
	
	JFrame window = new JFrame("Sudoku Solver");
	BoardRenderer boardRenderer  = new BoardRenderer();
	
	public Sudoku() {
		this.createWindow(); // 建立視窗
	}
	
	public void createWindow() {

		window.setSize(size_width, size_height);
		window.setPreferredSize(new Dimension(size_width, size_height));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 關閉視窗時離開主程式
		window.setLocationRelativeTo(null);						// 開啟時顯示於螢幕正中央
		window.setAlwaysOnTop(true);							// 顯示於最上方
		window.setResizable(false);								// 不可被調整大小
		
		Container mainPanel = window.getContentPane();
		mainPanel.setBackground(Color.GRAY);
		mainPanel.setLayout(null);
		
		// 新增面板
		mainPanel.add(boardRenderer);	// 棋盤 600*600  位置 50*50
		//...	// 其他 250*600  位置 50*700
		
		// 顯示視窗
		window.pack();
		window.setVisible(true);
	}

}