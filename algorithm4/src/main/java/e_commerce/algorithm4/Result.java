package e_commerce.algorithm4;

import java.util.List;

public class Result {

	private List<Integer> source;
	private int sum;
	private int max;
	private boolean stop;
	private int countOfCycle;
	public Result(List<Integer> source, int sum, int max, boolean stop, int countOfCycle){
		this.source = source;
		this.sum = sum;
		this.max = max;
		this.stop = stop;
		this.countOfCycle = countOfCycle;
	}
	
	public List<Integer> getSource(){ return this.source; }
	public boolean getStop(){ return this.stop; }
	
	public int getMaxCycleStep(){ return this.max; }
	public int getCountOfCycle(){ return this.countOfCycle; }
	
	public Result(List<Integer>source, int max, boolean stop, int countOfCycle){
		
		this.source = source;
		this.max = max;
		this.stop = stop;
		this.countOfCycle = countOfCycle;
		for(int val : source)
			this.sum += val;
	}
	
	public String getFormated(){
		
		StringBuilder sb = new StringBuilder();
		for(int val : this.source)
			if(val>0)
				sb.append(String.format("+%d", val));
			else
				sb.append(String.format("%d", val));
		sb.append(String.format(" = %d  [MAX:%d, COUNT:%d]", sum, max, this.countOfCycle));
		return sb.toString();
	}
}
