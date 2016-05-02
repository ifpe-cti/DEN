package br.edu.ifpe.pdt.repositorios.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.edu.ifpe.pdt.util.LoggerPTD;

public class BackupBD implements Job{

	private static String dumpCommand;
	private static String redirectOutput;
	
	
	public static void configureBackupBD(String command, String redirectOut){
		dumpCommand = command;
		redirectOutput = redirectOut;
		
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		 final List<String> comandos = new ArrayList<String>();  
		 comandos.add("sudo");
		 comandos.add(dumpCommand); 
         ProcessBuilder pb = new ProcessBuilder(comandos);    
         
         try {      
        	 File f = new File(redirectOutput);
             f.createNewFile();
             pb.redirectError(f);
        	 LoggerPTD.getLoggerInstance().logInfo("Start " + dumpCommand);
             final Process process = pb.start();                
             int exitCode = process.waitFor();    
             process.destroy(); 
             LoggerPTD.getLoggerInstance().logInfo("Finish " + dumpCommand + ": Exit code: " + exitCode);
         } catch (IOException e) {      
        	 LoggerPTD.getLoggerInstance().logError(e.getMessage());  
         } catch (InterruptedException ie) {      
        	 LoggerPTD.getLoggerInstance().logError(ie.getMessage()); 
         }
	}
}
