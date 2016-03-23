package ga.datamining.sutoku;

import java.awt.*;
import javax.swing.*;

public class Sutoku {

	static int size_width 	= 1400;
	static int size_height = 900;
	
	private static Sutoku sutoku = null;
	
	public static Sutoku get() {return sutoku==null?(sutoku = new Sutoku()):sutoku;}
	
	public static void main(String[] args) {
		// 主程式載入

		// 建立視窗
		Sutoku.get().createWindow();
	}
	
	JFrame window = new JFrame();
	BoardRenderer boardRenderer  = new BoardRenderer();	// JPanel
	
	public void createWindow() {
		
		window.setSize(size_width, size_height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 關閉後主程式會跟著關閉
		window.setLocationRelativeTo(null);						// 視窗開啟後會在螢幕中央
		
		// 加入元件
		window.add(boardRenderer);	// 棋盤
		//...	// 圖表
		
		// 顯示視窗
		window.setVisible(true);
	}

}