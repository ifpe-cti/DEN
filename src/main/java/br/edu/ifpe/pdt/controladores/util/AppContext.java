package br.edu.ifpe.pdt.controladores.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.ContextLoader;

import br.edu.ifpe.pdt.repositorios.util.PropertyPlaceholderConfig;

@Configuration
@Import(PropertyPlaceholderConfig.class)
@ComponentScan({ "br.edu.ifpe.pdt" })
public class AppContext {
	
	@Value("${app.relatoriopath}")
	private String relatorioPath;
	
	@Value("${app.emailAuth}")
	private String emailAuth;
	
	@Value("${app.passAuth}")
	private String passAuth;
	
	@Value("${app.emailSubject}")
	private String emailSubject;
	
	@Value("${app.emailCturSubject}")
	private String emailCturSubject;
	
	@Value("${app.emailCturMessage}")
	private String emailCturMessage;
	
	@Value("${app.ptdReport}")
	private String ptdReport;
	
	@Value("${app.ptdReportPdf}")
	private String ptdReportPdf;
	
	@Value("${app.emailPlanejamentoSubject}")
	private String emailPlanejamentoSubject;

	public static String getRelatorioPath(){
		AppContext appc = getAppContextBean();
		return appc.getRelatorioPathInternal();
	}

	private String getRelatorioPathInternal() {
		return this.relatorioPath;
	}
	
	public static String getEmailAuth(){
		AppContext appc = getAppContextBean();
		return appc.getEmailAuthInternal();
	}
	
	private String getEmailAuthInternal() {
		return this.emailAuth;
	}

	public static String getPassAuth(){
		AppContext appc = getAppContextBean();
		return appc.getPassAuthInternal();
	}

	private String getPassAuthInternal() {
		return this.passAuth;
	}

	private static AppContext getAppContextBean() {
		ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
		AppContext appc = ac.getBean(AppContext.class);
		return appc;
	}

	public static String getEmailSubject() {
		AppContext appc = getAppContextBean();
		return appc.getEmailSubjectInternal();
	}

	private String getEmailSubjectInternal() {
		return this.emailSubject;
	}

	public static String getEmailCturMessage() {
		AppContext appc = getAppContextBean();
		return appc.getEmailCturMessageInternal();
	}
	
	private String getEmailCturMessageInternal() {
		return this.emailCturMessage;
	}
	
	public static String getEmailCturSubject() {
		AppContext appc = getAppContextBean();
		return appc.getEmailCturSubjectInternal();
	}

	private String getEmailCturSubjectInternal() {
		return this.emailCturSubject;
	}
	
	public static String getPtdReport() {
		AppContext appc = getAppContextBean();
		return appc.getPtdReportInternal();
	}

	private String getPtdReportInternal() {
		return this.ptdReport;
	}
	
	public static String getPtdReportPdf() {
		AppContext appc = getAppContextBean();
		return appc.getPtdReportPdfInternal();
	}

	private String getPtdReportPdfInternal() {
		return this.ptdReportPdf;
	}
	
	public static String getEmailPlanejamentoSubject() {
		AppContext appc = getAppContextBean();
		return appc.getEmailPlanejamentoSubjectInternal();
	}

	private String getEmailPlanejamentoSubjectInternal() {
		return this.emailSubject;
	}
}
