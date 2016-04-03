public class Sudoku {
	
	private static Sudoku sudoku = null;
	
	public static Sudoku get() {
		return sudoku == null ? sudoku = new Sudoku() : sudoku;
	}
	
	private int[][] question = new int[9][9];
	
	public Sudoku() {}
	
	public void setQuestion(int[][] data) {
		question = data;
	}
	
	public int getQuestion(int x, int y) {
		if (x >= 9 || x < 0 || y >= 9 || y < 0) {
			return -1;
		}
		return question[x][y];
	}

	public void clear() {
		question = new int[9][9];
	}
}