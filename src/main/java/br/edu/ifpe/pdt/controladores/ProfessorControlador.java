package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.entidades.Falta;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.Professor.AUTORIZACAO;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;

@Component
@ManagedBean(name = "professorControlador", eager = true)
@SessionScoped
public class ProfessorControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	public ProfessorControlador() {
	}

	public String redirectCadastro() {
		return "/professor/cadastro.xhtml";
	}

	public String adicionaProfessor(String siape, String senha, String coordenacao, String nome, String email) {
		Professor p = new Professor();
		p.setSiape(siape);
		p.setSenha(senha);
		p.setCoordenacao(coordenacao);
		p.setNome(nome);
		p.setEmail(email);
		p.setAutorizacao(AUTORIZACAO.PROFESSOR);
		this.setProfessorLogado(professorRepositorio.saveAndFlush(p));

		return "/login.xhtml";
	}

	public String buscarProfessorPorSIAPE(String siape) {
		String ret = "";

		initProfessores();

		if (siape != null) {
			Professor prof = professorRepositorio.findBySiape(siape);
			if ((prof != null) && (prof.getAutorizacao() != AUTORIZACAO.SUPER)) {
				if (this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.DIVEN
						|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.DEN
						|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.SUPER
						|| prof.getCoordenacao().equals(this.getProfessorLogado().getAutorizacao().getNome())) {
					List<Professor> profs = this.getProfessores();
					profs.add(prof);
					this.setProfessores(profs);
				}
			}
			ret = "/restrito/professor/ensino/buscar.xhtml";
		}

		return ret;
	}

	public String buscarProfessorPorCoordenacao(String coordenacao) {

		initProfessores();

		if (this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.DIVEN
				|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.DEN
				|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.SUPER
				|| this.getProfessorLogado().getAutorizacao().getNome().equals(coordenacao)) {
			this.setProfessores(professorRepositorio.findByCoordenacao(coordenacao));
		}

		return "/restrito/professor/ensino/buscar.xhtml";
	}

	public String buscarProfessores() {

		initProfessores();

		if (this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.DIVEN
				|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.DEN
				|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.SUPER
				|| this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.CRAT) {
			this.setProfessores(professorRepositorio.findAll());
			Professor pRemoveSuper = null;
			Professor pRemoveCrat = null;
			for (Professor prof : this.getProfessores()) {
				if (prof.getAutorizacao() == AUTORIZACAO.SUPER) {
					pRemoveSuper = prof;
				}
				if (prof.getAutorizacao() == AUTORIZACAO.CRAT) {
					pRemoveCrat = prof;
				}
			}
			List<Professor> profs = this.getProfessores();
			profs.remove(pRemoveSuper);
			profs.remove(pRemoveCrat);
			this.setProfessores(profs);
		} else {
			this.setProfessores(professorRepositorio.findByCoordenacao(this.getProfessorLogado().getCoordenacao()));
		}

		return "/restrito/professor/ensino/buscar.xhtml";
	}

	private void initProfessores() {
		if (this.getProfessores() == null) {
			this.setProfessores(new ArrayList<Professor>());
		} else {
			List<Professor> profs = this.getProfessores();
			profs.clear();
			this.setProfessores(profs);
		}
	}

	public String realizarLogin(String siape, String senha) {
		String ret = "";
		Professor prof = null;

		prof = this.professorRepositorio.findBySiape(siape);

		if (prof != null) {
			if (prof.getSenha().equals(senha)) {
				this.setProfessorLogado(prof);
				ret = "/restrito/index.xhtml";
			} else {
				prof = null;
			}
		}

		return ret;
	}

	public String realizarLogout() {

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/login.xhtml";
	}

	public String mostrarProfessor(String siape) {
		String ret = "";

		if (siape != null) {
			this.setProfessorDetalhado(this.professorRepositorio.findBySiape(siape));
			ret = "/restrito/professor/ensino/mostrar.xhtml";
		}

		return ret;
	}

	public String alterarAutorizacao(Integer autorizacao) {
		String ret = "";

		if (autorizacao != null) {
			Professor profDetalhado = this.getProfessorDetalhado();
			profDetalhado.setAutorizacao(AUTORIZACAO.getAutorizacao(autorizacao));
			this.setProfessorDetalhado(professorRepositorio.saveAndFlush(profDetalhado));
			for (Professor prof : this.getProfessores()) {
				if (prof.getSiape().equals(profDetalhado.getSiape())) {
					prof.setAutorizacao(AUTORIZACAO.getAutorizacao(autorizacao));
				}
			}
			ret = "/restrito/professor/ensino/buscar.xhtml";
		}

		return ret;
	}

	public String mostrarFaltas() {
		return "";
	}

	public String cadastrarFalta() {
		return "/restrito/falta/professor/buscar.xhtml";
	}

	public String adicionarFalta(String siape, Integer ano, Integer semestre) {
		String ret = "";
		
		if (siape != null && ano != null && semestre != null) {

			this.setProfessorDetalhado(this.professorRepositorio.findBySiape(siape));

			for (PTD ptd : this.getProfessorDetalhado().getPtds()) {
				if (ptd.getAno().equals(ano) && ptd.getSemestre().equals(semestre)) {
					this.setPtdFalta(ptd);
					ret = "/restrito/falta/registrar.xhtml";
					break;
				}
			}
		}

		return ret;
	}

	public String salvarFalta(Integer numFaltas, String disciplina, String data) {
		Falta f = new Falta();

		return "";
	}

	public String atualizarFalta(String siape) {

		this.setProfessorDetalhado(this.professorRepositorio.findBySiape(siape));

		return "/restrito/falta/atualizar.xhtml";
	}

	public String buscarProfessorPorSIAPEFalta(String siape) {
		String ret = "";

		initProfessores();

		if (siape != null) {
			Professor prof = professorRepositorio.findBySiape(siape);
			if ((prof != null) && (prof.getAutorizacao() != AUTORIZACAO.SUPER)
					&& (this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.CRAT)) {
				List<Professor> profs = this.getProfessores();
				profs.add(prof);
				this.setProfessores(profs);
			}
		}

		return ret;
	}

	public String buscarProfessorPorCoordenacaoFalta(String coordenacao) {

		initProfessores();

		if (this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.CRAT) {
			List<Professor> profs = professorRepositorio.findByCoordenacao(coordenacao);
			this.setProfessores(profs);
		}

		return "";
	}

	public String buscarProfessoresFalta() {

		initProfessores();

		if (this.getProfessorLogado().getAutorizacao() == AUTORIZACAO.CRAT) {
			this.setProfessores(professorRepositorio.findAll());
			Professor pRemoveSuper = null;
			Professor pRemoveCrat = null;
			for (Professor prof : this.getProfessores()) {
				if (prof.getAutorizacao() == AUTORIZACAO.SUPER) {
					pRemoveSuper = prof;
				}
				if (prof.getAutorizacao() == AUTORIZACAO.CRAT) {
					pRemoveCrat = prof;
				}
			}
			List<Professor> profs = this.getProfessores();
			profs.remove(pRemoveSuper);
			profs.remove(pRemoveCrat);
			this.setProfessores(profs);
		}

		return "";
	}

	// Getters from bean
	@SuppressWarnings("unchecked")
	public List<Professor> getProfessores() {
		return (List<Professor>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("professores");
	}

	public void setProfessores(List<Professor> professores) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professores", professores);
	}

	public Professor getProfessorLogado() {
		return (Professor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("professorLogado");
	}

	public void setProfessorLogado(Professor prof) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorLogado", prof);
	}

	public Professor getProfessorDetalhado() {
		return (Professor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("professorDetalhado");
	}

	private void setProfessorDetalhado(Professor prof) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorDetalhado", prof);
	}

	private void setPtdFalta(PTD ptd) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ptdFalta", ptd);
	}

	public PTD getPtdFalta() {
		return (PTD) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ptdFalta");
	}
}
