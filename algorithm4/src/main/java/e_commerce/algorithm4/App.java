package e_commerce.algorithm4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import process.IProcessor;
import process.Start;
import e_commerce.algorithm4.FileOutput;
import e_commerce.algorithm4.stastic.ISequentialStastic;
import e_commerce.algorithm4.stastic.SequentialForSection;
import e_commerce.algorithm4.App;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static String Class3O = "process.CycleNegtive";
	public static String Class3X = "process.CyclePositive";
	
	public static int cycleStep=6;
	public static String class3X = "ecommerce.algorithm4.processor.CycleNegtive";
	public static String class3O = "ecommerce.algorithm4.processor.CyclePositive";

	public static void main(String[] args) throws IOException {

		if (args.length < 2) {
			logger.error("params:file path required!\r\n");
			return;
		}

		String filePath = args[0];
		String output = args[1];
		String fileName = null;
		{
			String[] array = filePath.split("/");
			fileName = array[array.length-1];
			array = fileName.split("\\.");
			fileName = String.format("%s%s_step%d.txt", output, array[0], cycleStep);
		}
		FileOutput.init(fileName);
		logger.info("----------------------------------------\r\n");
		logger.info("start scanning {} ...\r\n", filePath);
		logger.info("----------------------------------------\r\n");
		InputStreamReader read = new InputStreamReader(new FileInputStream(
				filePath), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		List<List<TrueAndFalse>> totalResult = new ArrayList<List<TrueAndFalse>>();
		int number = 1;
		List<Integer> maxSteps = new ArrayList<Integer>();
		List<Integer> countOfCycles = new ArrayList<Integer>();
		while ((lineTxt = bufferedReader.readLine()) != null) {

			String source = lineTxt.trim();
			if (source.length() == 0) {
				logger.info("skip row : {}\r\n", source);
				continue;
			}

			SourceRow sRow = new SourceRow(source);
			logger.debug("{}. ", number);
			FileOutput.write(String.format("%d. ", number++));
			sRow.print();
			IRow row = sRow.run();
			List<TrueAndFalse> rtn = row.run();
			for(TrueAndFalse taf : rtn){
				taf.print();
				taf.run();
				
				boolean[] result = new boolean[taf.getResult().size()];
				for(int i=0; i<taf.getResult().size(); i++)
					result[i] = taf.getResult().get(i);
				//IProcessor processor = Start.findProcessor(result);
				ecommerce.algorithm4.processor.IProcessor processor = 
						ecommerce.algorithm4.processor.Start.findProcessor(
								result, App.cycleStep, App.class3X, App.class3O);
				int maxStep=0;
				int countOfCycle=0;
				Result r = null;
				if(null != processor){
					boolean bStop = processor.execute();
					maxStep = processor.getMaxStep();
					countOfCycle = processor.getCountOfCycle();
					r = new Result(processor.getProcedure(), processor.getMaxStep(), bStop, processor.getCountOfCycle());
					FileOutput.writeline(r.getFormated());
				}
				maxSteps.add(maxStep);
				countOfCycles.add(countOfCycle);
			}
			totalResult.add(rtn);
		}
		bufferedReader.close();
		
		logger.info("--------------------------------------------------\r\n");
		logger.info("---------------------整个文件汇总-------------------\r\n");
		//for(int i=0; i<2; i++){//每一段的汇总
		int i=0;
			int max10=0, max15=0, max20=0;
			Map<Integer, Integer> mapCountOfMax10 = new TreeMap<Integer, Integer>();
			Map<Integer, Integer> mapCountOfMax15 = new TreeMap<Integer, Integer>();
			Map<Integer, Integer> mapCountOfMax20 = new TreeMap<Integer, Integer>();
			int sum = 0;
			int countTrue = 0, countFalse = 0;
			for(List<TrueAndFalse> list : totalResult)
				if(list.size() > i){
					
					TrueAndFalse taf = list.get(i);
					
					sum += taf.getSum();
					countTrue += taf.getCountTrue();
					countFalse += taf.getCountFalse();
					
					int tmpMax10 = taf.getMax().get(10);
					int tmpMax15 = taf.getMax().get(15);
					int tmpMax20 = taf.getMax().get(20);
					max10 = max10>tmpMax10 ? max10 : tmpMax10;
					if(mapCountOfMax10.get(tmpMax10) == null)
						mapCountOfMax10.put(tmpMax10, 1);
					else
						mapCountOfMax10.put(tmpMax10, mapCountOfMax10.get(tmpMax10)+1);
					
					max15 = max15>tmpMax15 ? max15 : tmpMax15;
					if(mapCountOfMax15.get(tmpMax15) == null)
						mapCountOfMax15.put(tmpMax15, 1);
					else
						mapCountOfMax15.put(tmpMax15, mapCountOfMax15.get(tmpMax15)+1);
					
					max20 = max20>tmpMax20 ? max20 : tmpMax20;
					if(mapCountOfMax20.get(tmpMax20) == null)
						mapCountOfMax20.put(tmpMax20, 1);
					else
						mapCountOfMax20.put(tmpMax20, mapCountOfMax20.get(tmpMax20)+1);
				}
			
			logger.info("第{}段 \r\n\t[ SUM:{}, MAX(10):{}, MAX(15):{}, MAX(20):{}, x:{}({}%), o:{}({}%) ]\r\n", i+1, sum, 
					max10, max15, max20,
					countFalse, (float)countFalse*100/(float)(countFalse+countTrue),
					countTrue, (float)countTrue*100/(float)(countFalse+countTrue));
			
			logger.info("\tSUM>=10[ ");
			for(Map.Entry<Integer, Integer> entry : mapCountOfMax10.entrySet()){
				logger.info("{}:{}, ", entry.getKey(), entry.getValue());
			}
			logger.info("]\r\n");
			
			logger.info("\tSUM>=15[ ");
			for(Map.Entry<Integer, Integer> entry : mapCountOfMax15.entrySet()){
				logger.info("{}:{}, ", entry.getKey(), entry.getValue());
			}
			logger.info("]\r\n");
			
			logger.info("\tSUM>=20[ ");
			for(Map.Entry<Integer, Integer> entry : mapCountOfMax20.entrySet()){
				logger.info("{}:{}, ", entry.getKey(), entry.getValue());
			}
			logger.info("]\r\n");
			
			//统计连续o/x的个数
			ISequentialStastic seqStastic = new SequentialForSection();
			seqStastic.run(totalResult, i);
			for(int seq=1; seq<=seqStastic.getMaxCountOfSeq(); seq++){
				logger.info("\tSEQ {} {x:{}, o:{}}\r\n", seq,
						seqStastic.getCountOfSeqX().get(seq)==null?0:seqStastic.getCountOfSeqX().get(seq),
						seqStastic.getCountOfSeqO().get(seq)==null?0:seqStastic.getCountOfSeqO().get(seq));
			}
		//}
		logger.info("--------------------------------------------------\r\n");
		
		int sumOfMax = maxSteps.size();
		HashMap<Integer, Integer> maxStepMap = new HashMap<Integer, Integer>();
		for(int maxStep : maxSteps){
			if(null == maxStepMap.get(maxStep))
				maxStepMap.put(maxStep, 1);
			else
				maxStepMap.put(maxStep, maxStepMap.get(maxStep)+1);
		}
		for(Map.Entry entry : maxStepMap.entrySet()){
			logger.debug("MAX {} : {} [{}%]\r\n", entry.getKey(), entry.getValue(), (float)((Integer)entry.getValue()*100)/(float)sumOfMax);
			FileOutput.write(String.format("MAX %d : %d [%f%%]\r\n", entry.getKey(), entry.getValue(), (float)((Integer)entry.getValue()*100)/(float)sumOfMax));
		}
		
		int sumOfCycle = countOfCycles.size();
		HashMap<Integer, Integer> cycleMap = new HashMap<Integer, Integer>();
		for(int countOfCycle : countOfCycles){
			if(null == cycleMap.get(countOfCycle))
				cycleMap.put(countOfCycle, 1);
			else
				cycleMap.put(countOfCycle, cycleMap.get(countOfCycle)+1);
		}
		for(Map.Entry entry : cycleMap.entrySet()){
			logger.debug("COUNT {} : {} [{}%]\r\n", entry.getKey(), entry.getValue(), (float)((Integer)entry.getValue()*100)/(float)sumOfCycle);
			FileOutput.write(String.format("COUNT %d : %d [%f%%]\r\n", entry.getKey(), entry.getValue(), (float)((Integer)entry.getValue()*100)/(float)sumOfCycle));
		}
		
		FileOutput.close();
	}
}
