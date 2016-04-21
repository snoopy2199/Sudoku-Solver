import java.util.*;

// 這個介面是讓
public interface ISudokuCallback {

	public static class SolveData {
		public enum SolverType {
			Genetic,
			BruteForce
		}
		
		private SolverType solverType;
		private Sudoku inputObject;
		private Sudoku outputObject;
		
		// 記錄每一步的fitness
		private List<Integer> records = new ArrayList<>();
		
		public SolveData (SolverType t, Sudoku iObject, Sudoku oObject) {
			this(t, iObject);
			outputObject = oObject;
		}
		
		public SolveData (SolverType t, Sudoku iObject) {
			solverType = t;
			inputObject = iObject;
		}
		
		public SolverType getType() {return solverType;}
		public Sudoku getInputObject() {return  inputObject;}
		public Sudoku getOututObject() {return  outputObject;}
		
		public SolveData setOututObject(Sudoku oObject) {
			outputObject = oObject;
			return this;
		}
		
		public void addRecord(int value) {
			records.add(value);
		}
		
		public int [] getRecord() {
			int [] result = new int [records.size()];
			for (int i = 0; i < records.size(); i++) result[i] = records.get(i);
			return result;
		}
	}
	
	public void onSolverUpdate(SolveData sloveDate);
	public void onSolverFinish(SolveData sloveDate);
	
}
