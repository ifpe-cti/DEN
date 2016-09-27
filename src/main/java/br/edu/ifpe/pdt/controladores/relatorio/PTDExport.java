package br.edu.ifpe.pdt.controladores.relatorio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.util.LoggerPTD;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PTDExport {

	public static File exportarPTD(PTD ptd) {

		File file = null;
		
		
		try {
			JasperReport report = JasperCompileManager.compileReport(AppContext.getRelatorioPath() + AppContext.getPtdReport());
			JasperReport subReportDisc = JasperCompileManager.compileReport(AppContext.getRelatorioPath() + "disciplinas.jrxml");
		
			Map<String, Object> map = new HashMap<String, Object>();
			
			Professor prof = ptd.getProfessor();
			map.put("nome", prof.getNome());
			map.put("email", prof.getEmail());
			map.put("siape", prof.getSiape());
			map.put("coordenacao", prof.getCoordenacao());
			map.put("ano", ptd.getAno() + "." + ptd.getSemestre());
			map.put("image", AppContext.getRelatorioPath()+"logo.png");
			
			
			JRBeanCollectionDataSource diciplinas = new JRBeanCollectionDataSource(ptd.getDisciplinas());
			map.put("subreportParameter", subReportDisc);
			map.put("SUBREPORT_DIR", AppContext.getRelatorioPath());
			/*JRBeanCollectionDataSource aaes = new JRBeanCollectionDataSource(ptd.getAaes());
			JRBeanCollectionDataSource pesquisas = new JRBeanCollectionDataSource(ptd.getPesquisas());
			JRBeanCollectionDataSource extensoes = new JRBeanCollectionDataSource(ptd.getExtensoes());
			JRBeanCollectionDataSource aaps = new JRBeanCollectionDataSource(ptd.getAaps());
			
			map.put("disciplinas", diciplinas);
			map.put("aaes", aaes);
			map.put("pesquisas", pesquisas);
			map.put("extensoes", extensoes);
			map.put("aaps", aaps);*/

			JasperPrint print = JasperFillManager.fillReport(report, map, diciplinas);

			JasperExportManager.exportReportToPdfFile(print,
					AppContext.getRelatorioPath() + AppContext.getPtdReportPdf());
			
			file = new File(AppContext.getRelatorioPath() + AppContext.getPtdReportPdf());
		} catch (JRException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}
		
		return file;
	}
}
