package br.edu.ifpe.pdt.repositorios.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.edu.ifpe.pdt.util.LoggerPTD;

public class BackupBD implements Job{

	private static String backupFolder;
	private static String backupFile;
	private static String dumpCommand;
	private static String user;
	private static String password;
	private static String name;
	
	
	public static void configureBackupBD(String folder, String file, String command,
			String u, String p, String n){
		backupFolder = folder;
		backupFile = file;
		dumpCommand = command;
		user = u;
		password = p;
		name = n;
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		 final List<String> comandos = new ArrayList<String>();      
         comandos.add(dumpCommand); 
         comandos.add("-i");     
         comandos.add("-U");      
         comandos.add(user);      
         comandos.add("-F");      
         comandos.add("c");      
         comandos.add("-b");      
         comandos.add("-v");      
         comandos.add("-f");      
         comandos.add(backupFolder+backupFile);     
         comandos.add(name);      
         ProcessBuilder pb = new ProcessBuilder(comandos);      
         pb.environment().put("PGPASSWORD", password);           
         try {      
             final Process process = pb.start();                
             process.waitFor();    
             process.destroy(); 
         } catch (IOException e) {      
        	 LoggerPTD.getLoggerInstance().logError(e.getMessage());  
         } catch (InterruptedException ie) {      
        	 LoggerPTD.getLoggerInstance().logError(ie.getMessage()); 
         }
	}
}
