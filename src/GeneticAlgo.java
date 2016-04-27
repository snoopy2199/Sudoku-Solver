import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class GeneticAlgo implements Runnable {

	private final int numberOfSelection = 20;
	private final int numberOfPopulations = numberOfSelection * (numberOfSelection-1);
	
	private ArrayList<Point> questionLocation = new ArrayList<Point>();
	private int lengthOfGene;
	private Population[] populations;
	private Population[] SelectedPopulations;
	private int samePointTimes = 0;
	
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
		samePointTimes = 0;
		fitnessValues = new ArrayList<Integer>();
		questionLocation = new ArrayList<Point>();
		
		defineChromosome();
		initialisePopulation();
		for(int i = 0; i < 1000; i++) {
			doSelection();
			doSort();
			if (populations[0].point == 27) {
				populations[0].fillAnswer();
				break;
			}
			
			if (i != 0 && fitnessValues.get(fitnessValues.size()-1) == fitnessValues.get(fitnessValues.size()-2)) {
				samePointTimes++;
			} else {
				samePointTimes = 0;
			}
			
			if (samePointTimes == 20) {
				mutate();
			}
			
			crossover();
			
			if (i == 999){
				Sudoku.get().clearAnswer();
			}
		}

		Date endTime = new Date();
		costTime = (endTime.getTime()-startTime.getTime())*0.001;
	}

	/*
	 * 決定染色體
	 */
	private void defineChromosome() {
		Point Point = Sudoku.get().getFirstBlank();
		questionLocation.add(Point);
		while (true) {
			Point = Sudoku.get().getNextBlank(Point);
			if (Point == null) {
				break;
			}
			questionLocation.add(Point);
		}
		lengthOfGene = questionLocation.size();
	}
	
	/*
	 * 初始化基因
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
	 * 挑選基因
	 */
	private void doSelection() {	
		int sum = 0;
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
		int cutOfGene = (int)(Math.random()*lengthOfGene);
		
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
		for (int numOfMutationGene = 0; numOfMutationGene < 10; numOfMutationGene++) {
			for (int numOfChromosome = 0; numOfChromosome < 5; numOfChromosome++) {
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
	 * 
	 */
	class Population implements Comparable<Population>{
		int[] gene;
		int point;
		
		public Population() {}
		
		public Population(int[] gene) {
			this.gene = gene;
			fillAnswer();
			point = Sudoku.get().getFitnessValue();
		}
		
		private void fillAnswer() {
			for (int i = 0; i < lengthOfGene; i++) {
				Sudoku.get().setOneAnswer(questionLocation.get(i), gene[i]);
			}
		}
		
		@Override
		public int compareTo(Population o) {
			return point - o.point;
		}
	}
}
