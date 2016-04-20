import java.util.*;

public class GeneticAlgo implements Runnable {

	// static 
	
	// 挑選
	public static List<Sudoku> getRandomPick(List<Sudoku> list, int count) {
		
		// 排序
		Collections.sort(list);
		
		// 挑選機率
		List<Sudoku> result = new ArrayList<>();
		while (true) {
			for (Sudoku s : list) {
				if (Math.random() > 0.7) {
					result.add(s);
					count--;
					break;
				} else continue;
			}

			if (count == 0) break;
		}

		return result;
	}
	
	// class
	
	// Runnable
	@Override
	public void run() {
		

	}

}
