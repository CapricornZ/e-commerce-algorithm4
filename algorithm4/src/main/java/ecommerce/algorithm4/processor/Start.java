package ecommerce.algorithm4.processor;

public class Start{

	static public IProcessor findProcessor(boolean[] source, int cycleStep, 
			String class3X, String class3O) {
		
		IProcessor processor = null;
		boolean bFound = false;
		for(int offset=2; !bFound && offset<source.length; offset++){
			
			if(offset>1 && source[offset] && source[offset-1] && source[offset-2]){//3*o
				//processor = new Processor3O(source, offset+1, cycleStep, class3O);
				processor = new Processor3XChange(source, offset+1, cycleStep, class3X, class3O);
			}
			
			if(offset>1 && !source[offset] && !source[offset-1] && !source[offset-2]){//3*x
				//processor = new Processor3X(source, offset+1, cycleStep, class3X);
				processor = new Processor3OChange(source, offset+1, cycleStep, class3O, class3X);
			}
			
			bFound = processor != null;
		}
		
		return processor;
	}

}
