package br.edu.ifpe.pdt.util;

import org.apache.log4j.Logger;

public class LoggerPTD {
	
	private static LoggerPTD instance;
	/* Get actual class name to be printed on */
	private static Logger log = Logger.getLogger(LoggerPTD.class.getName());
	
	private LoggerPTD() {
		
	}
	
	public static LoggerPTD getLoggerInstance(){
		if (instance == null) {
			instance = new LoggerPTD();
		}
		
		return instance;
	}
	
	public void logError(String error) {
		log.error(error);
	}
	
	public void logInfo(String info) {
		log.info(info);
	}

}
