import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BruteForce implements Runnable {

	private double costTime;
	private int times = 0;
	private ArrayList<Integer> fitnessValues = new ArrayList<Integer>();
	
	/*
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 初始化
		Sudoku.get().clearAnswer();
		Date startTime = new Date();
		times = 0;
		fitnessValues = new ArrayList<Integer>();
		
		fillAnswer(Sudoku.get().getFirstBlank());

		Date endTime = new Date();
		costTime = (endTime.getTime()-startTime.getTime())*0.001;
	}
	
	/*
	 * 取得執行時間
	 * @回傳  執行時間
	 */
	public double getCostTime(){
		return costTime;
	}
	
	/*
	 * 取得執行次數
	 * @回傳  執行次數
	 */
	public int getTimes(){
		return times;
	}
	
	/*
	 * 取得分數陣列
	 * @回傳  分數陣列
	 */
	public Integer[] getFitnessValues(){
		return fitnessValues.toArray(new Integer[0]);
	}
	
	/*
	 * 取得最終分數
	 * @回傳  最終分數
	 */
	public int getEndPoint(){
		return fitnessValues.get(fitnessValues.size()-1);
	}
	
	/*
	 * 填入答案
	 * @參數  格子位子
	 */
	private boolean fillAnswer(Point nowLocation) {
		// 1-9試驗
		for (int i = 1; i <= 9; i++) {
			// 若填入不造成衝突
			if (checkValid(nowLocation, i)) {
				Sudoku.get().setOneAnswer(nowLocation, i);
				fitnessValues.add(Sudoku.get().getFitnessValue());
				times++;
				
				Point newLocation = Sudoku.get().getNextBlank(nowLocation);	
				// 結束
				if (newLocation == null || fillAnswer(newLocation)) {
					return true;
				}
				
				// 回溯
				Sudoku.get().clearOneAnswer(nowLocation);
			}
		}
		return false;
	}
	
	/*
	 * 確認填入答案是否合法
	 * @參數  nowLocation: 欲填入位子
	 *      answer: 欲填入值
	 * @回傳  true-填入不會造成衝突(合法), false-填入會造成衝突(不合法)
	 */
	private boolean checkValid(Point nowLocation, int answer) {
		// 檢查 九宮格、直線、橫線 是否合法
		if (checkJiugonggeValid(nowLocation, answer) &&
			checkVerticalValid(nowLocation, answer) &&
			checkParallelValid(nowLocation, answer)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 確認填入答案後該九宮格是否合法
	 * @參數  nowLocation: 欲填入位子
	 *      answer: 欲填入值
	 * @回傳  true-填入不會造成衝突(合法), false-填入會造成衝突(不合法)
	 */
	private boolean checkJiugonggeValid(Point nowLocation, int answer) {
		Set<Integer> numbers = new HashSet<Integer>();
		// 九宮格最左上之格子座標
		int startX = ((int)(nowLocation.x/3)) * 3;
		int startY = ((int)(nowLocation.y/3)) * 3;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int thisNumber = Sudoku.get().getNumber(startX + i, startY + j);
				// 有值 且 與目前讀到數字重複(加入暫存數字集合失敗)
				if (thisNumber != 0 && !numbers.add(thisNumber)){
					return false;
				}
			}
		}
		
		// 與目前讀到數字重複(加入暫存數字集合失敗)
		if (!numbers.add(answer)) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * 確認填入答案後該直線是否合法
	 * @參數  nowLocation: 欲填入位子
	 *      answer: 欲填入值
	 * @回傳  true-填入不會造成衝突(合法), false-填入會造成衝突(不合法)
	 */
	private boolean checkVerticalValid(Point nowLocation, int answer) {
		Set<Integer> numbers = new HashSet<Integer>();
		
		for (int i = 0; i < 9; i++) {
			int thisNumber = Sudoku.get().getNumber(i, nowLocation.y);
			// 有值 且 與目前讀到數字重複(加入暫存數字集合失敗)
			if (thisNumber != 0 && !numbers.add(thisNumber)){
				return false;
			}
		}
		
		// 與目前讀到數字重複(加入暫存數字集合失敗)
		if (!numbers.add(answer)) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * 確認填入答案後該橫線是否合法
	 * @參數  nowLocation: 欲填入位子
	 *      answer: 欲填入值
	 * @回傳  true-填入不會造成衝突(合法), false-填入會造成衝突(不合法)
	 */
	private boolean checkParallelValid(Point nowLocation, int answer) {
		Set<Integer> numbers = new HashSet<Integer>();
		for (int i = 0; i < 9; i++) {
			int thisNumber = Sudoku.get().getNumber(nowLocation.x, i);
			// 有值 且 與目前讀到數字重複(加入暫存數字集合失敗)
			if (thisNumber != 0 && !numbers.add(thisNumber)){
				return false;
			}
		}
		
		// 與目前讀到數字重複(加入暫存數字集合失敗)
		if (!numbers.add(answer)) {
			return false;
		}
		
		return true;
	}
}
