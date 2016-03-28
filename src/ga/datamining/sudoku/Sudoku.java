package ga.datamining.sudoku;

import java.awt.*;

public class Sudoku {
	
	private static Sudoku sudoku = null;
	
	public static Sudoku get() {
		return sudoku == null ? sudoku = new Sudoku() : sudoku;
	}
	
	private int [][] data = new int [9][9];
	
	public Sudoku() {
		
	}
	
	public void setData(int x, int y, int data) {
		
	}
	
	public void setData(Point pos, int data) {
		
		setData(pos.x, pos.y, data);
	}
	
	public int getData(int x, int y) {
		
		return 0;
	}
	
	public int getData(Point pos) {
		
		return getData(pos.x, pos.y);
	}
	
	
	
}