package ecommerce.algorithm4.processor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 从第三个周期开始，使用相反的期待
 * @author martin
 *
 */
public class Processor3XChange implements IProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(Processor3XChange.class);

	private int offset;
	private boolean[] source;
	private int cycleStep;
	private String class3X;
	private String class3O;
	public Processor3XChange(boolean[] source, int offset, int cycleStep, String class3X, String class3O){
		this.offset = offset;
		this.source = source;
		this.cycleStep = cycleStep;
		this.class3X = class3X;
		this.class3O = class3O;
	}
	
	private int maxStep;
	@Override
	public int getMaxStep() { return maxStep; }
	
	private int countOfCycle;
	public int getCountOfCycle(){ return this.countOfCycle; }
	
	private List<Integer> procedure;
	@Override
	public List<Integer> getProcedure(){ return this.procedure; }
	
	@Override
	public boolean execute() {

		boolean finished = false;
		this.procedure = new ArrayList<Integer>();
		Constructor<ICycle> constructor3X = null;
		Constructor<ICycle> constructor3O = null;
		try {
			@SuppressWarnings("unchecked")
			Class<ICycle> Cycle3X = (Class<ICycle>) Class.forName(this.class3X);
			constructor3X = Cycle3X.getConstructor(int.class);
			
			@SuppressWarnings("unchecked")
			Class<ICycle> Cycle3O = (Class<ICycle>) Class.forName(this.class3O);
			constructor3O = Cycle3O.getConstructor(int.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int cycleStep = this.cycleStep;
		logger.debug("3X FOUND-CHANGE");

		int totalSum = 0;
		List<Integer> steps = new ArrayList<Integer>();
		for(int i=0; i+offset<this.source.length; i+=cycleStep){

			ICycle cycle = null;
			if(steps.size() == 0){
				try {
					cycle = (ICycle) constructor3X.newInstance(1);
					this.countOfCycle++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				int step = 2;
				for(int var : steps)
					step += Math.abs(var);
				try {
					if(steps.size()<2)//周期数小于等于2
						cycle = (ICycle) constructor3X.newInstance(step);
					else//周期数大于2
						cycle = (ICycle) constructor3O.newInstance(step);
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
		return finished;
	}
}
