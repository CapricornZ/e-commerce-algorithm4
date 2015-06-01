package ecommerce.algorithm4.processor;

import java.util.List;

public interface IProcessor {
	
	boolean execute();
	int getMaxStep();
	int getCountOfCycle();
	List<Integer> getProcedure();
}
