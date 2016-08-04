package br.edu.ifpe.pdt.controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.relatorio.XLSExport;
import br.edu.ifpe.pdt.controladores.relatorio.bean.EntregaPTDProfessor;
import br.edu.ifpe.pdt.entidades.Disciplina;
import br.edu.ifpe.pdt.entidades.Falta;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.Professor.AUTORIZACAO;
import br.edu.ifpe.pdt.repositorios.PTDRepositorio;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;
import br.edu.ifpe.pdt.util.LoggerPTD;

@Component
@ManagedBean(name = "relatorioControlador", eager = true)
@SessionScoped
public class RelatorioControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int LISTAR_CARGA_HORARIA_SEMESTRE = 1;
	public static final int LISTAR_FALTA_PROFESSORES = 2;
	public static final int RELATORIO_PTD = 3;
	public static final int RELATORIO_PLANEJAMENTO = 4;

	@Autowired
	private PTDRepositorio ptdRepositorio;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	public String emitirRelatorio(Integer relatorioID) {
		String ret = "";

		switch (relatorioID) {
		case LISTAR_CARGA_HORARIA_SEMESTRE:
			ret = "/restrito/relatorio/professorCargaHoraria.xhtml";
			break;
		case LISTAR_FALTA_PROFESSORES:
			ret = "/restrito/relatorio/professoresFalta.xhtml";
			break;
		case RELATORIO_PTD:
			ret = "/restrito/relatorio/relatorioPTD.xhtml";
			break;			
		case RELATORIO_PLANEJAMENTO:
			ret = "/restrito/relatorio/relatorioPlanejamento.xhtml";
			break;
		default:
			break;
		}

		return ret;
	}

	public String listarCargaHorariaSemestre(Integer ano, Integer semestre) {

		List<PTD> ptds = this.ptdRepositorio.findByAnoAndSemestre(ano, semestre);

		for (PTD ptd : ptds) {
			Integer cargaHoraria = 0;

			for (Disciplina disciplina : ptd.getDisciplinas()) {
				cargaHoraria += disciplina.getCargaHoraria();
			}

			ptd.setCargaHoraria(cargaHoraria);
		}

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPtds", ptds);

		return "";
	}

	@SuppressWarnings("unchecked")
	public String exportarCargaHorariaSemestre() {
		List<PTD> ptds = (List<PTD>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("selectedPtds");

		File f = XLSExport.exportarCargaHorariaSemestre(ptds);
		setXLSResponse(f);

		return "/restrito/relatorio/listar.xhtml";
	}

	private void setXLSResponse(File f) {
		if (f != null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			ec.responseReset();
			ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			ec.setResponseContentLength((int) f.length());
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
			try {
				OutputStream output = ec.getResponseOutputStream();

				FileInputStream fis = new FileInputStream(f);
				while (fis.available() > -1) {
					output.write(fis.read());
				}

				output.flush();
				output.close();
				fis.close();
				ec.redirect("/restrito/relatorio/listar.xhtml");
				fc.responseComplete();

			} catch (IOException e) {
				LoggerPTD.getLoggerInstance().logError(e.getMessage());
			}
		}
	}

	public String listarProfessoresFalta(String coordenacao) {

		List<Professor> professores = professorRepositorio.findByCoordenacao(coordenacao);
		List<Falta> faltas = new ArrayList<Falta>();

		for (Professor prof : professores) {
			faltas.addAll(prof.getFaltas());
		}

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("faltas", faltas);

		return "";
	}

	public String listarProfessoresFalta() {

		List<Professor> professores = professorRepositorio.findAll();
		List<Falta> faltas = new ArrayList<Falta>();

		for (Professor prof : professores) {
			faltas.addAll(prof.getFaltas());
		}

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("faltas", faltas);

		return "";
	}

	@SuppressWarnings("unchecked")
	public String exportarFaltasProfessores() {
		List<Falta> faltas = (List<Falta>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("faltas");

		File f = XLSExport.exportarFaltaProfessores(faltas);

		setXLSResponse(f);

		return "/restrito/relatorio/listar.xhtml";
	}

	public String listarPTDProfessor(Integer ano, Integer semestre) {

		List<Professor> professores = this.professorRepositorio.findAll();

		List<EntregaPTDProfessor> entregas = new ArrayList<EntregaPTDProfessor>();

		for (Professor p : professores) {
			if (!(p.getAutorizacao().equals(AUTORIZACAO.SUPER) 
					|| p.getAutorizacao().equals(AUTORIZACAO.CRAT)
					|| p.getSiape().equals("aspe"))) {
				boolean found = false;
				for (PTD ptd : p.getPtds()) {
					if (ptd.getAno().equals(ano) && ptd.getSemestre().equals(semestre)) {
						entregas.add(new EntregaPTDProfessor(p.getSiape(), p.getNome(), ptd.getStatus().name()));
						found = true;
						break;
					}
				}
				if (!found) {
					entregas.add(new EntregaPTDProfessor(p.getSiape(), p.getNome(), "NAO_ENTREGUE"));
				}
			}
		}
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ptdProfessores", entregas);

		return "";
	}
	
	public String listarPlanejamentoProfessor(Integer ano, Integer semestre) {

		List<Professor> professores = this.professorRepositorio.findAll();

		List<EntregaPTDProfessor> entregas = new ArrayList<EntregaPTDProfessor>();

		for (Professor p : professores) {
			if (!(p.getAutorizacao().equals(AUTORIZACAO.SUPER) 
					|| p.getAutorizacao().equals(AUTORIZACAO.CRAT)
					|| p.getSiape().equals("aspe"))) {
				boolean found = false;
				for (PTD ptd : p.getPtds()) {
					if (ptd.getAno().equals(ano) && ptd.getSemestre().equals(semestre)) {
						
						int numPlaPreenchidos = 0;
						for (Disciplina d: ptd.getDisciplinas()){
							if (d.getPlanejamentoSemestral() != null) {
								numPlaPreenchidos++;
							}
						}
						if (numPlaPreenchidos == 0) {
							entregas.add(new EntregaPTDProfessor(p.getSiape(), p.getNome(), "NAO_ENTREGUE"));														
						} else if (numPlaPreenchidos < ptd.getDisciplinas().size()) {
							entregas.add(new EntregaPTDProfessor(p.getSiape(), p.getNome(), "ENTREGUE_PARCIALMENTE"));
						} else if (numPlaPreenchidos == ptd.getDisciplinas().size()){
							entregas.add(new EntregaPTDProfessor(p.getSiape(), p.getNome(), "ENTREGUE"));
						}						
						found = true;
						break;
					}
				}
				if (!found) {
					entregas.add(new EntregaPTDProfessor(p.getSiape(), p.getNome(), "NAO_ENTREGUE"));
				}
			}
		}

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ptdProfessores", entregas);

		return "";
	}

}
