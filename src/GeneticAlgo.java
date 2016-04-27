import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class GeneticAlgo implements Runnable {

	//挑選子代數量
	private final int numberOfSelection = 20; 
	//配對後的染色體數量
	private final int numberOfPopulations = numberOfSelection * (numberOfSelection-1);
	//演化跳脫次數
	private final int endOfSelection = 1000;
	//fitness分數連續相同最高次數
	private final int maxSamePointTimes = 30;
	//突變染色體數量
	private final int numOfMutationGene = 10;
	//突變基因數量
	private final int numOfChromosome = 5;
	
	//空白格位置紀錄
	private ArrayList<Point> questionLocation = new ArrayList<Point>();
	
	private int lengthOfGene;                  //基因長度
	private Population[] populations;          //目前染色體陣列
	private Population[] SelectedPopulations;  //挑選出的子代陣列
	private int samePointTimes = 0;            //fitness分數連續相同次數
	
	private double costTime;                   //總執行時間
	private int times = 0;                     //世代數量
	
	//每代最高分數
	private ArrayList<Integer> fitnessValues = new ArrayList<Integer>();
	
	/*
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		initialise();
		
		//紀錄開始時間
		Date startTime = new Date();
		
		//執行基因演算法
		doGA();

		//計算總執行時間
		Date endTime = new Date();
		costTime = (endTime.getTime()-startTime.getTime())*0.001;
	}
	
	/*
	 * 初始化
	 */
	private void initialise() {
		Sudoku.get().clearAnswer();
		times = 0;
		samePointTimes = 0;
		fitnessValues = new ArrayList<Integer>();
		questionLocation = new ArrayList<Point>();
	}
	
	/*
	 * 執行基因演算法
	 */
	private void doGA() {
		defineChromosome();         //計算染色體長度
		initialisePopulation();     //隨機產生初代
		
		for(int i = 0; i < endOfSelection; i++) {
			doSort();         //排序
			doSelection();    //挑選
			         
			//若分數達到27，則完成基因演算法
			if (populations[0].point == 27) {
				populations[0].fillAnswer();
				break;
			}
			
			//fitness分數是否持續相同
			if (i != 0) {
				int lastFitnessValue = fitnessValues.get(fitnessValues.size()-2);
				int nowFitnessValue = fitnessValues.get(fitnessValues.size()-1);
				if (lastFitnessValue == nowFitnessValue){
					samePointTimes++;
				}
			} else {
				samePointTimes = 0;
			}
			
			//若fitness分數持續相同到maxSamePointTimes，則突變
			if (samePointTimes == maxSamePointTimes) {
				mutate();
				samePointTimes = 0;
			}
			
			//配對
			crossover();
			
			//若執行代數為endOfSelection，則結束計算
			//即使尚未找到最佳解
			if (i == endOfSelection - 1){
				Sudoku.get().clearAnswer();
			}
		}
	}

	/*
	 * 決定染色體長度
	 * 並且記錄空白位置
	 */
	private void defineChromosome() {
		Point Point = Sudoku.get().getFirstBlank();
		questionLocation.add(Point);
		while (true) {
			Point = Sudoku.get().getNextBlank(Point);
			if (Point == null) {
				break; //找不到空白格則跳出
			}
			questionLocation.add(Point);
		}
		lengthOfGene = questionLocation.size();
	}
	
	/*
	 * 隨機產生初代基因
	 */
	private void initialisePopulation() {
		populations = new Population[numberOfPopulations];
		for (int i = 0; i < numberOfPopulations; i++) {
			int[] tempGene = new int[lengthOfGene]; 
			for (int j = 0; j < lengthOfGene; j++) {
				tempGene[j] = (int)(Math.random()*9) + 1;
			}
			populations[i] = new Population(tempGene);
		}
	}
	
	/*
	 * 依得分排序基因
	 */
	private void doSort() {
		Arrays.sort(populations);
		times++;
		fitnessValues.add(populations[0].point);
	}
	
	/*
	 * 挑選基因(輪盤法)
	 */
	private void doSelection() {	
		int sum = 0;
		//加總所有點數作為random範圍
		for (Population population : populations) {
			sum += population.point;
		}
		
		SelectedPopulations = new Population[numberOfSelection];
		for (int i = 0; i < numberOfSelection; i++) {
			int randomNum = (int)(Math.random()*sum);
			
			int count = 0;
			for (Population population : populations) {
				count += population.point;
				if (count >= randomNum) {
					SelectedPopulations[i] = population;
					break;
				}
			}
		}
	}
	
	/*
	 * 交換染色體
	 */
	private void crossover() {
		int count = 0;
		
		//隨機挑選染色體切割點
		int cutOfGene = (int)(Math.random()*(lengthOfGene-2))+1;
		
		for (int i = 0; i < numberOfSelection; i++) {
			for (int j = i+1; j < numberOfSelection; j++) {
				
				int[] tempGene1 = new int[lengthOfGene]; 
				int[] tempGene2 = new int[lengthOfGene]; 
				
				for (int k = 0; k < cutOfGene; k++){
					tempGene1[k] = SelectedPopulations[i].gene[k];
					tempGene2[k] = SelectedPopulations[j].gene[k];
				}
				
				for (int k = cutOfGene; k < lengthOfGene; k++){
					tempGene1[k] = SelectedPopulations[j].gene[k];
					tempGene2[k] = SelectedPopulations[i].gene[k];
				}

				populations[count] = new Population(tempGene1);
				populations[count+1] = new Population(tempGene2);
				
				count += 2;
			}
		}
	}
	
	/*
	 * 突變
	 */
	private void mutate() {
		for (int i = 0; i < numOfMutationGene; i++) {
			for (int j = 0; j < numOfChromosome; j++) {
				Population targetPopulation = SelectedPopulations[(int)(Math.random()*numberOfSelection)];
				targetPopulation.gene[(int)(Math.random()*(lengthOfGene-1))] = (int)(Math.random()*9) + 1;
			}
		}
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
	 * 取得最高分數
	 * @回傳  最高分數
	 */
	public int getMaxPoint(){
		int maxPoint = Collections.max(fitnessValues);
		return maxPoint;
	}
	
	/*
	 * Population類別
	 */
	class Population implements Comparable<Population>{
		int[] gene;
		int point;   //fitnessValues
		
		/*
		 * 建構子
		 */
		public Population() {}

		public Population(int[] gene) {
			this.gene = gene;
			fillAnswer();
			point = Sudoku.get().getFitnessValue();
		}
		
		/*
		 * 將基因填入數獨
		 */
		private void fillAnswer() {
			for (int i = 0; i < lengthOfGene; i++) {
				Sudoku.get().setOneAnswer(questionLocation.get(i), gene[i]);
			}
		}
		
		/*
		 * 用point作為比較依據
		 */
		@Override
		public int compareTo(Population o) {
			return point - o.point;
		}
	}
}
