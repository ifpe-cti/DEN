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

	public static String getRelatorioPath(){
		AppContext appc = getAppContextBean();
		return appc.getRelatorioPathInternal();
	}

	private static AppContext getAppContextBean() {
		ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
		AppContext appc = ac.getBean(AppContext.class);
		return appc;
	}

	private String getRelatorioPathInternal() {
		return this.relatorioPath;
	}
}
