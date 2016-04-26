import java.util.Arrays;

public class Sudoku implements Comparable<Sudoku> {
	
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
		
		sum += countParallelPoint();
		sum += countVerticalPoint();
		sum += countJiugonggePoint();
		
		return sum;
	}

	
	private int countJiugonggePoint() {
		int sum = 0;
		//選取九宮格
		int[] dx = {0,0,0,1,1,1,2,2,2};
		int[] dy = {0,1,2,0,1,2,0,1,2};
		
		for (int i = 0; i < 9; i+=3) {
			for (int j = 0; j < 9; j+=3) {
				//取出該線
				int[] temList = new int [9];
				
				for (int index = 0; index < 9; index++) {
					temList[index] = getNumber((dx[index] + i), (dy[index] + j));
				}
				
				if (hasOneToNine(temList)) {
					sum++;
				}
			}
		}
		return sum;
	}
	
	private int countVerticalPoint() {
		int sum = 0;
		//選取垂直線
		for (int j = 0; j < 9; j++) {
			//取出該線
			int[] temList = new int [9];
			
			for (int i = 0; i < 9; i++){
				temList[i] = getNumber(i, j);
			}
			
			if (hasOneToNine(temList)) {
				sum++;
			}
		}
		return sum;
	}
	
	private int countParallelPoint() {
		int sum = 0;
		//選取水平線
		for (int i = 0; i < 9; i++) {
			//取出該線
			int[] temList = new int [9];
			
			for (int j = 0; j < 9; j++){
				temList[j] = getNumber(i, j);
			}
			
			if (hasOneToNine(temList)) {
				sum++;
			}
			
		}
		return sum;
	}
	
	private int getNumber(int x, int y) {
		if (question[x][y] != 0) {
			return question[x][y];
		}		
		return answer[x][y];
	}


	private boolean hasOneToNine(int[] valueList) {
		Arrays.sort(valueList);
		for (int i = 0; i < 9; i++) {
			if (valueList[i] != (i + 1)) {
				return false;
			}
		}
		return true;
	}

	// Comparable
	@Override
	public int compareTo(Sudoku a) {
		return a.getFitnessValue() - this.getFitnessValue();
	}
}