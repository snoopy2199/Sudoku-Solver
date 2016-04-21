import java.util.*;

public class GeneticAlgo implements Runnable {

	// static ==========
	
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
	
	// class ===========
	
	private ISudokuCallback callback = null;
	private Sudoku inputObject = null;
	
	//建構子
	public GeneticAlgo (ISudokuCallback callback,Sudoku inputObject) {
		this.callback = callback;
		this.inputObject = inputObject;
	}
	
	// Runnable ========
	@Override
	public void run() {
		
		ISudokuCallback.SolveData solveData = 
				new ISudokuCallback.SolveData(
						ISudokuCallback.SolveData.SolverType.Genetic, inputObject);
		// 中途更新時回調
		//if (callback != null) callback.onSolverUpdate(sloveDate);(solveData);
		
		// 中途新增每部記錄 
		//solveData.addRecord(0);
		
		if (callback != null) callback.onSolverFinish(solveData);
	}

}
