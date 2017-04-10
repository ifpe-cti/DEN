package br.edu.ifpe.pdt.controladores.relatorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.tools.zip.ZipEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.Disciplina;
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
			JasperReport report = JasperCompileManager
					.compileReport(AppContext.getRelatorioPath() + AppContext.getPtdReport());

			Map<String, Object> map = new HashMap<String, Object>();

			Professor prof = ptd.getProfessor();
			map.put("ano", ptd.getAno());
			map.put("semestre", ptd.getSemestre());
			map.put("siape", prof.getSiape());

			ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
			PersistenceContext pc = ac.getBean(PersistenceContext.class);
			JasperPrint print = JasperFillManager.fillReport(report, map, pc.dataSource().getConnection());

			JasperExportManager.exportReportToPdfFile(print,
					AppContext.getRelatorioPath() + prof.getSiape() + "_ptd.pdf");

			file = new File(AppContext.getRelatorioPath() + prof.getSiape() + "_ptd.pdf");
		} catch (JRException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		} catch (SQLException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}

		return file;
	}

	public static File exportarPlanejamento(Disciplina d) {

		File file = null;

		try {
			JasperReport report = JasperCompileManager
					.compileReport(AppContext.getRelatorioPath() + AppContext.getPlanejamentoReport());

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("psID", d.getPlanejamentoSemestral().getCodigo());

			ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
			PersistenceContext pc = ac.getBean(PersistenceContext.class);
			JasperPrint print = JasperFillManager.fillReport(report, map, pc.dataSource().getConnection());

			JasperExportManager.exportReportToPdfFile(print,
					AppContext.getRelatorioPath() + d.getNome() + "_planejamento.pdf");

			file = new File(AppContext.getRelatorioPath() + d.getNome() + "_planejamento.pdf");
		} catch (JRException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		} catch (SQLException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}

		return file;
	}

	public static void setPDFResponse(File f) {
		if (f != null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			ec.responseReset();
			ec.setResponseContentType("application/pdf");
			ec.setResponseContentLength((int) f.length());
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
			try {
				OutputStream output = ec.getResponseOutputStream();
				FileInputStream fis = new FileInputStream(f);
				while (fis.available() > -1) {
					output.write(fis.read());
				}

				fis.close();
				output.flush();
				output.close();

			} catch (IOException e) {
				LoggerPTD.getLoggerInstance().logError(e.getMessage());
			} catch (Exception e) {
				LoggerPTD.getLoggerInstance().logError(e.getMessage());
			}
		}
	}

	public static File exportarPTDs(List<PTD> ptds) {
		File zipFile = null;
		
		try {
			zipFile = new File(AppContext.getRelatorioPath() + "ptds.zip");
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
			
			for (PTD ptd : ptds) {
				JasperReport report = JasperCompileManager
						.compileReport(AppContext.getRelatorioPath() + AppContext.getPtdReport());

				Map<String, Object> map = new HashMap<String, Object>();

				Professor prof = ptd.getProfessor();
				map.put("ano", ptd.getAno());
				map.put("semestre", ptd.getSemestre());
				map.put("siape", prof.getSiape());

				ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
				PersistenceContext pc = ac.getBean(PersistenceContext.class);
				JasperPrint print = JasperFillManager.fillReport(report, map, pc.dataSource().getConnection());

				JasperExportManager.exportReportToPdfFile(print,
						AppContext.getRelatorioPath() + prof.getSiape() + "_ptd.pdf");

				ZipEntry entry = new ZipEntry(AppContext.getRelatorioPath() + prof.getSiape() + "_ptd.pdf");
				out.putNextEntry(entry);
				FileInputStream is = new FileInputStream(new File(AppContext.getRelatorioPath() + prof.getSiape() + "_ptd.pdf"));
				int b = 0;
				while ((b = is.read()) != -1) {
					out.write(b);
				}
				is.close();
				out.closeEntry();
			}
			out.close();
		} catch (JRException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		} catch (SQLException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		} catch (IOException e) {
			LoggerPTD.getLoggerInstance().logError(e.getMessage());
		}

		return zipFile;
	}

	public static void setZipResponse(File f) {
		if (f != null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			ec.responseReset();
			ec.setResponseContentType("application/zip");
			ec.setResponseContentLength((int) f.length());
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
			try {
				OutputStream output = ec.getResponseOutputStream();
				FileInputStream fis = new FileInputStream(f);
				while (fis.available() > -1) {
					output.write(fis.read());
				}

				fis.close();
				output.flush();
				output.close();

			} catch (IOException e) {
				LoggerPTD.getLoggerInstance().logError(e.getMessage());
			} catch (Exception e) {
				LoggerPTD.getLoggerInstance().logError(e.getMessage());
			}
		}
	}
}
