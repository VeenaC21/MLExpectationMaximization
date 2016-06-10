import java.util.ArrayList;


public class Row {
	public int rowId;
	public ArrayList<Integer> missingcol = new ArrayList<Integer>();
	public ArrayList<Integer> obsCol = new ArrayList<Integer>();
	
	public ArrayList<Integer> list = new ArrayList<Integer>();
	public double weight;
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public ArrayList<Integer> getMissingcol() {
		return missingcol;
	}
	public void setMissingcol(ArrayList<Integer> missingcol) {
		this.missingcol = missingcol;
	}
	public ArrayList<Integer> getObsCol() {
		return obsCol;
	}
	public void setObsCol(ArrayList<Integer> obsCol) {
		this.obsCol = obsCol;
	}
	public ArrayList<Integer> getList() {
		return list;
	}
	public void setList(ArrayList<Integer> list) {
		this.list = list;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Row(int rowId, ArrayList<Integer> missingcol,
			ArrayList<Integer> obsCol, ArrayList<Integer> list, double weight) {
		super();
		this.rowId = rowId;
		this.missingcol = missingcol;
		this.obsCol = obsCol;
		this.list = list;
		this.weight = weight;
	}
	
	
	
	
	
	
	
	
	
}
