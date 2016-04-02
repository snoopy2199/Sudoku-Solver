import java.awt.*;

public class Sudoku {
	
	private static Sudoku sudoku = null;
	
	public static Sudoku get() {
		return sudoku == null ? sudoku = new Sudoku() : sudoku;
	}
	
	private int [][] _data = new int [9][9];
	
	public Sudoku() {
		
	}
	
	// 設定值，若data為0，則表示那格沒有填入
	public boolean setData(int x, int y, int data) {
		if (x > 9 || x < 1 || y > 9 || y < 1 || data > 9 || data < 0) {
			return false;
		}
		_data [x][y] = data;
		return true;
	}
	
	public boolean setData(Point pos, int data) {
		return setData(pos.x, pos.y, data);
	}
	
	// 取得值
	public int getData(int x, int y) {
		if (x > 9 || x < 1 || y > 9 || y < 1) {
			return -1;
		}
		return _data[x][y];
	}
	
	public int getData(Point pos) {
		return getData(pos.x, pos.y);
	}
	
	public int [][] getData() {
		return _data;
	}
	
	// 重設
	public void clear() {
		_data = new int [9][9];
	}
	
	// 取得特定位置後的下一個空白位置
	public int getNextBlankPos(int x, int y) {
		// TODO
		return 0;
		
	}
	
	public int getNextBlankPos(Point p) {
		return getNextBlankPos(p.x, p.y);
	}
	
	// 算出共有幾個符合規則
	public int getPoint() {
		// TODO
		return 0;
	}
	
	public boolean isComplete() {
		if (getPoint() == 27) {
			return true;
		}
		else return false;
	}
}