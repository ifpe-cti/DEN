package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.relatorio.beans.ProfessorCargaHoraria;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.Professor.AUTORIZACAO;
import br.edu.ifpe.pdt.repositorios.PTDRepositorio;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;

@Component
@ManagedBean(name = "relatorioControlador", eager = true)
@SessionScoped
public class RelatorioControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int LISTAR_CARGA_HORARIA_PROFESSOR = 1;

	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
	@Autowired
	private PTDRepositorio ptdRepositorio;

	public String emitirRelatorio(Integer relatorioID) {
		String ret = "";

		switch (relatorioID) {
		case LISTAR_CARGA_HORARIA_PROFESSOR:
			ret = listarCargaHorariaProfessor();
			break;
		default:
			break;
		}

		return ret;
	}

	private String listarCargaHorariaProfessor() {
		List<Professor> professores = this.professorRepositorio.findAll();
		List<ProfessorCargaHoraria> profsCH = new ArrayList<ProfessorCargaHoraria>();
		for (Professor prof : professores) {
			if (prof.getAutorizacao() != AUTORIZACAO.SUPER) {
				ProfessorCargaHoraria profCH = new ProfessorCargaHoraria(prof);
				
				
				
				profsCH.add(profCH);
			}
		}

		return "/restrito/relatorio/professorCargaHoraria.xhtml";
	}
}
