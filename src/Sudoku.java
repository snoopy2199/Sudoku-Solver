public class Sudoku {
	
	//static
	private static Sudoku sudoku = null;
	
	public static Sudoku get() {
		return sudoku == null ? sudoku = new Sudoku() : sudoku;
	}
	
	public static int getFitnessValue(Sudoku s) {
		return s.getFitnessValue();
	}
	
	// class
	private int[][] question = new int[9][9];
	private int[][] answer 	  = new int[9][9];
	
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

	public void setAnswer(int[][] data) {
		answer = data;
	}
	
	public int getAnswer(int x, int y) {
		if (x >= 9 || x < 0 || y >= 9 || y < 0) {
			return -1;
		}
		return answer[x][y];
	}
	
	public void clear() {
		question = new int[9][9];
	}
	
	public int getFitnessValue() {
		int sum = 0;
		// 檢查直線
		for (int i = 0; i < 9; i++) {
			boolean [] vChecker = new boolean [9];
			boolean [] hChecker = new boolean [9];
			for (int j = 0; j < 9; j++) {
				// 水平
				if (question [i][j] == 0) {
					if (answer[i][j] != 0) {
						hChecker[answer[i][j] - 1] = true;
					}
				} else hChecker[question[i][j] - 1] = true;
				
				// 垂直
				if (question [j][i] == 0) {
					if (answer[j][i] != 0) {
						vChecker[answer[j][i] - 1] = true;
					}
				} else vChecker[question[j][i] - 1] = true;
				
			}
			// 檢查是否 1-9 都有
			boolean isVMeet = true;
			boolean isHMeet = true;
			for (int x = 0; x < 9; x++)
				if (vChecker[x] == false) {
					isVMeet = false;
					break;
				}
			for (int x = 0; x < 9; x++)
				if (hChecker[x] == false) {
					isHMeet = false;
					break;
				}
					
			if (isVMeet) sum++;
			if (isHMeet) sum++;
		}
		// 檢查九宮格
		for (int i = 0; i <= 6; i += 3) {
			for (int j = 0; j <= 6; j += 3) {
				// [i][j] = 左上角起始點
				boolean [] nChecker = new boolean [9];
				for (int x = i; x < i+3; x++) {
					for (int y = j; y < j+3; y++) {
						if (question [x][y] == 0) { 
							if (answer[x][y] != 0) {
								nChecker[answer[x][y] - 1] = true;
							}
						} else {
							nChecker[question[x][y] - 1] = true;
						}
					}
				}
				
				// 檢查是否 1-9 都有
				boolean isMeet = true;
				for (int x = 0; x < 9; x++)
					if (nChecker[x] == false) {
						isMeet = false;
						break;
					}
				
				if (isMeet) sum++;
			}
		}
		return sum;
	}
}