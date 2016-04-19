package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.Professor.AUTORIZACAO;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;

@Component
@ManagedBean(name = "professorControlador", eager = true)
@SessionScoped
public class ProfessorControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Professor> professores = new ArrayList<Professor>();

	private Professor professorLogado;

	private Professor professorDetalhado;

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
		this.professorLogado = professorRepositorio.saveAndFlush(p);

		return "/login.xhtml";
	}

	public String buscarProfessorPorSIAPE(String siape) {
		String ret = "";

		if (this.professores == null) {
			this.professores = new ArrayList<Professor>();
		} else {
			this.professores.clear();
		}
		if (siape != null) {
			Professor prof = professorRepositorio.findBySiape(siape);
			if ((prof != null) && (prof.getAutorizacao() != AUTORIZACAO.SUPER)) {
				if (this.professorLogado.getAutorizacao() == AUTORIZACAO.DIVEN
						|| this.professorLogado.getAutorizacao() == AUTORIZACAO.DEN
						|| this.professorLogado.getAutorizacao() == AUTORIZACAO.SUPER
						|| prof.getCoordenacao().equals(this.professorLogado.getAutorizacao().getNome())) {
					this.professores.add(prof);
				}
			}
			ret = "/restrito/professor/ensino/buscar.xhtml";
		}

		return ret;
	}

	public String buscarProfessorPorCoordenacao(String coordenacao) {

		if (this.professores == null) {
			this.professores = new ArrayList<Professor>();
		} else {
			this.professores.clear();
		}
		if (this.professorLogado.getAutorizacao() == AUTORIZACAO.DIVEN
				|| this.professorLogado.getAutorizacao() == AUTORIZACAO.DEN
				|| this.professorLogado.getAutorizacao() == AUTORIZACAO.SUPER
				|| this.professorLogado.getAutorizacao().getNome().equals(coordenacao)) {
			this.professores = professorRepositorio.findByCoordenacao(coordenacao);
		}

		return "/restrito/professor/ensino/buscar.xhtml";
	}

	public String buscarProfessores() {

		if (this.professores == null) {
			this.professores = new ArrayList<Professor>();
		} else {
			this.professores.clear();
		}

		if (this.professorLogado.getAutorizacao() == AUTORIZACAO.DIVEN
				|| this.professorLogado.getAutorizacao() == AUTORIZACAO.DEN
				|| this.professorLogado.getAutorizacao() == AUTORIZACAO.SUPER) {
			this.professores = professorRepositorio.findAll();
			Professor pRemove = null;
			for (Professor prof : this.professores) {
				if (prof.getAutorizacao() == AUTORIZACAO.SUPER) {
					pRemove = prof;
				}
			}
			this.professores.remove(pRemove);
		} else {
			this.professores = professorRepositorio.findByCoordenacao(this.professorLogado.getCoordenacao());
		}

		return "/restrito/professor/ensino/buscar.xhtml";
	}

	public String realizarLogin(String siape, String senha) {
		String ret = "";
		Professor prof = null;

		prof = this.professorRepositorio.findBySiape(siape);

		if (prof != null) {
			if (prof.getSenha().equals(senha)) {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorLogado", prof);
				this.professorLogado = prof;
				ret = "/index.xhtml";
			} else {
				prof = null;
			}
		}

		return ret;
	}

	public String realizarLogout() {

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		this.professorLogado = null;
		this.professorDetalhado = null;
		this.professores.clear();
		this.professores = null;

		return "/login.xhtml";
	}

	public String mostrarProfessor(String siape) {
		String ret = "";

		if (siape != null) {
			this.professorDetalhado = this.professorRepositorio.findBySiape(siape);
			ret = "/restrito/professor/ensino/mostrar.xhtml";
		}

		return ret;
	}

	public String alterarAutorizacao(Integer autorizacao) {
		String ret = "";

		if (autorizacao != null) {
			this.professorDetalhado.setAutorizacao(AUTORIZACAO.getAutorizacao(autorizacao));
			professorRepositorio.saveAndFlush(this.professorDetalhado);
			ret = "/restrito/professor/ensino/buscar.xhtml";
		}

		return ret;
	}

	// Getters from bean
	public List<Professor> getProfessores() {
		return professores;
	}

	public Professor getProfessorLogado() {
		this.professorLogado = (Professor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("professorLogado");
		return professorLogado;
	}

	public Professor getProfessorDetalhado() {
		return professorDetalhado;
	}
}
