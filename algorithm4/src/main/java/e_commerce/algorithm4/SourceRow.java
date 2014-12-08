package e_commerce.algorithm4;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import e_commerce.algorithm4.perterns.Pattern1;


public class SourceRow {
	
	private static Logger logger = LoggerFactory.getLogger(SourceRow.class);
	
	private String source;
	public SourceRow(String source){
		this.source = source;
	}
	
	public void print(){
		logger.info("{}\r\n", this.source);
	}
	
	/*public Row run(){
		char[] array = this.source.toCharArray();
		int length = this.source.length()/2;
		char[] arrRow0 = new char[length+this.source.length() % 2];
		char[] arrRow1 = new char[length];
		
		//一行数据，根据数组中index坐标，分成奇偶两列
		for(int i=0; i<length; i++){
			arrRow0[i] = array[i*2];
			arrRow1[i] = array[i*2 + 1];
		}
		if(this.source.length() % 2 > 0)
			arrRow0[length] = array[array.length-1];
		
		//通过Pattern1，从两行A/B数组，转换成3/2单行数组
		String row0 = String.valueOf(arrRow0);
		String row1 = String.valueOf(arrRow1);
		
		StringBuilder sbResult0 = new StringBuilder("---"), sbResult1 = new StringBuilder("---");
		List<Integer> result0 = new ArrayList<Integer>();
		List<Integer> result1 = new ArrayList<Integer>();
		for(int i=3; i<length; i++){
			String sub = row0.substring(i-3, i+1);
			int value = Pattern1.execute(sub);
			result0.add(value); sbResult0.append(value);
			
			sub = row1.substring(i-3, i+1);
			value = Pattern1.execute(sub);
			result1.add(value); sbResult1.append(value);
		}
		if(this.source.length() % 2 > 0){
			String sub = row0.substring(length-3, length+1);
			int value = Pattern1.execute(sub);
			result0.add(value);
			sbResult0.append(value);
		}
		
		{// for logger.debug
			logger.debug("{}\r\n", row0);
			logger.debug("{}\r\n", row1);
			logger.debug("{}\r\n", sbResult0.toString());
			logger.debug("{}\r\n", sbResult1.toString());
		}
		
		return new Row(result0, result1);
	}*/
	
	public IRow run(){
		char[] array = this.source.toCharArray();
		int length = this.source.length()/2;
		char[] arrRow0 = new char[length+this.source.length() % 2];
		char[] arrRow1 = new char[length];
		
		//一行数据，根据数组中index坐标，分成奇偶两列
		for(int i=0; i<length; i++){
			arrRow0[i] = array[i*2];
			arrRow1[i] = array[i*2 + 1];
		}
		if(this.source.length() % 2 > 0)
			arrRow0[length] = array[array.length-1];
		
		//通过Pattern1，从两行A/B数组，转换成3/2单行数组
		String row0 = String.valueOf(arrRow0);
		String row1 = String.valueOf(arrRow1);
		
		StringBuilder sbResult0 = new StringBuilder("---"), sbResult1 = new StringBuilder("---");
		List<Integer> result = new ArrayList<Integer>();
		for(int i=3; i<length; i++){
			String sub = row0.substring(i-3, i+1);
			int value = Pattern1.execute(sub);
			result.add(value); sbResult0.append(value);
			
			sub = row1.substring(i-3, i+1);
			value = Pattern1.execute(sub);
			result.add(value); sbResult1.append(value);
		}
		if(this.source.length() % 2 > 0){
			String sub = row0.substring(length-3, length+1);
			int value = Pattern1.execute(sub);
			result.add(value);
			sbResult0.append(value);
		}
		
		{// for logger.debug
			logger.debug("{}\r\n", row0);
			logger.debug("{}\r\n", row1);
			logger.debug("{}\r\n", sbResult0.toString());
			logger.debug("{}\r\n", sbResult1.toString());
		}
		
		//return new Row(result0, result1);
		return new Row0(result);
	}
}
