import java.awt.Point;
import java.util.Arrays;

public class Sudoku {
	
	private static Sudoku sudoku = null;
	
	private int[][] question = new int[9][9];
	private int[][] answer 	 = new int[9][9];
	
	public static Sudoku get() {
		return sudoku == null ? sudoku = new Sudoku() : sudoku;
	}
	
	/*
	 * 建構子
	 */
	public Sudoku() {}
	
	/*
	 * 設定題目
	 * @參數   data 題目二維陣列
	 */
	public void setQuestion(int[][] data) {
		question = data;
	}
	
	/*
	 * 取得題目
	 * @回傳  題目二維陣列
	 */
	public int[][] getQuestion() {
		return question;
	}
	
	/*
	 * 取得答案
	 * @回傳  答案二維陣列
	 */
	public int[][] getAnswer() {
		return answer;
	}
	
	/*
	 * 設定一格答案
	 * @參數   nowLocation: 格子
	 *      number: 值
	 */
	public void setOneAnswer(Point nowLocation, int number) {
		answer[nowLocation.x][nowLocation.y] = number;
	}
	
	/*
	 * 清除一格答案
	 * @參數   nowLocation: 格子
	 */
	public void clearOneAnswer(Point nowLocation) {
		answer[nowLocation.x][nowLocation.y] = 0;
	}

	/*
	 * 清除所有答案
	 */
	public void clearAnswer() {
		answer = new int[9][9];
	}
	
	/*
	 * 取得目前得分 (滿分27)
	 * @回傳  得分
	 */
	public int getFitnessValue() {
		int sum = 0;
		
		sum += countParallelPoint();
		sum += countVerticalPoint();
		sum += countJiugonggePoint();
		
		return sum;
	}
	
	/*
	 * 取得目前九宮格得分 (滿分9)
	 * @回傳  九宮格部分得分
	 */
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
	
	/*
	 * 取得目前直線得分 (滿分9)
	 * @回傳  垂直線部分得分
	 */
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
	
	/*
	 * 取得目前橫線得分 (滿分9)
	 * @回傳  水平線部分得分
	 */
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
	
	/*
	 * 取得某格之值(題目或是答案)
	 * @參數  x: 格子x座標, y: 格子y座標
	 * @回傳  值
	 */
	public int getNumber(int x, int y) {
		if (question[x][y] != 0) {
			return question[x][y];
		}		
		return answer[x][y];
	}

	/*
	 * 判斷陣列中是否剛好為1-9
	 * @參數  valueList: 數字陣列
	 * @回傳  true-符合, false-不符合
	 */
	private boolean hasOneToNine(int[] valueList) {
		Arrays.sort(valueList);
		for (int i = 0; i < 9; i++) {
			if (valueList[i] != (i + 1)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * 取得第一個需填答位子
	 * @回傳  第一個需填答位子
	 */
	public Point getFirstBlank() {
		if (question[0][0] == 0) {
			return new Point(0, 0);
		} else {
			return getNextBlank(new Point(0, 0));
		}
	}
	
	/*
	 * 取得該點的下一個需填答位子
	 * @參數  nowLocation: 現在位子
	 * @回傳  下一個需填答位子，若無則回傳null
	 */
	public Point getNextBlank(Point nowLocation) {
		
		// 檢查該行剩下的位子
		for (int i = nowLocation.y + 1; i < 9; i++){
			if (question[nowLocation.x][i] == 0) {
				return new Point(nowLocation.x, i);
			}
		}
		
		// 檢查剩下的行
		for (int i = nowLocation.x + 1; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (question[i][j] == 0) {
					return new Point(i, j);
				}
			}
		}
		
		return null;
	}
}