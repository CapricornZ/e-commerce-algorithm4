package ecommerce.algorithm4.processor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 固定的使用同一种期待(同Processor3X)
 * @author martin
 *
 */
public class Processor3O implements IProcessor {

	private static final Logger logger = LoggerFactory.getLogger(Processor3O.class);
	
	private int offset;
	private boolean[] source;
	private int cycleStep;
	private String class3O;
	public Processor3O(boolean[] source, int offset, int cycleStep, String class3O){
		this.offset = offset;
		this.source = source;
		this.cycleStep = cycleStep;
		this.class3O = class3O;
	}
	
	private int maxStep;
	@Override
	public int getMaxStep() { return this.maxStep; }
	
	private int countOfCycle;
	public int getCountOfCycle(){ return this.countOfCycle; }
	
	private List<Integer> procedure;
	@Override
	public List<Integer> getProcedure(){ return this.procedure; }
	
	@Override
	public boolean execute() {

		boolean finished = false;
		this.procedure = new ArrayList<Integer>();
		Constructor<ICycle> constructor = null;
		try {
			@SuppressWarnings("unchecked")
			Class<ICycle> Cycle = (Class<ICycle>) Class.forName(this.class3O);
			constructor = Cycle.getConstructor(int.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int cycleStep = this.cycleStep;
		logger.debug("3O FOUND");
		//FileOutput.write("3O FOUND");
		int totalSum = 0;
		List<Integer> steps = new ArrayList<Integer>();
		for(int i=0; i+offset<this.source.length; i+=cycleStep){
			ICycle cycle = null;
			if(steps.size() == 0)
				try {
					cycle = (ICycle) constructor.newInstance(1);
					this.countOfCycle++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			else{
				int step = 2;
				for(int var : steps)
					step += Math.abs(var);
				try {
					cycle = (ICycle) constructor.newInstance(step);
					this.countOfCycle++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			int length = this.offset+i+(cycleStep-1)<=this.source.length ? cycleStep : this.source.length-(this.offset+i);
			cycle.execute(this.source, this.offset+i, length);
			int sum = cycle.getSum();
			steps.add(sum);
			
			boolean bStop = false;
			for(int index=0; !bStop && index<cycle.getProcess().size(); index++){
				int val = cycle.getProcess().get(index);
				totalSum += val;
				if(this.maxStep < Math.abs(val))
					this.maxStep = Math.abs(val);
				logger.debug(String.format("%s%d", val<0?"":"+", val));
				this.procedure.add(val);
				//FileOutput.write(String.format("%s%d", val<0?"":"+", val));
				if(totalSum >= 2){
					bStop = true;
					finished = true;
				}
			}
			if(bStop)
				break;
			if(i==0 && sum == 0){
				finished = true;
				break;
			}
		}

		logger.debug(String.format("=%d {MAX:%d}\r\n", totalSum, this.maxStep));
		//FileOutput.write(String.format("=%d {MAX:%d}\r\n", totalSum, this.maxStep));
		
		/*if(this.offset+this.procedure.size()<this.source.length)
			return true;
		else
			return false;*/
		return finished;
	}

}
