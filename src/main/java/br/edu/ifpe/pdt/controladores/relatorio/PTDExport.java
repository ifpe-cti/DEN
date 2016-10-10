package br.edu.ifpe.pdt.controladores.relatorio;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.repositorios.util.PersistenceContext;
import br.edu.ifpe.pdt.util.LoggerPTD;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class PTDExport {

	public static File exportarPTD(PTD ptd) {

		File file = null;
		
		
		try {
			JasperReport report = JasperCompileManager.compileReport(AppContext.getRelatorioPath() + AppContext.getPtdReport());
		
			Map<String, Object> map = new HashMap<String, Object>();
			
			Professor prof = ptd.getProfessor();
			map.put("ano", ptd.getAno());
			map.put("semestre", ptd.getSemestre());
			map.put("siape", prof.getSiape());

			ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
			PersistenceContext pc = ac.getBean(PersistenceContext.class);
			JasperPrint print = JasperFillManager.fillReport(report, map, 
					pc.dataSource().getConnection());

			JasperExportManager.exportReportToPdfFile(print,
					AppContext.getRelatorioPath() + prof.getSiape()+"_ptd.pdf");
			
			file = new File(AppContext.getRelatorioPath() + prof.getSiape()+"_ptd.pdf");
		} catch (JRException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		} catch (SQLException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}
		
		return file;
	}
}
