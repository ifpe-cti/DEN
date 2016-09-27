package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.controladores.util.MD5Hash;
import br.edu.ifpe.pdt.entidades.Disciplina;
import br.edu.ifpe.pdt.entidades.Falta;
import br.edu.ifpe.pdt.entidades.Falta.FALTA_STATUS;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.Professor.AUTORIZACAO;
import br.edu.ifpe.pdt.repositorios.FaltaRepositorio;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;
import br.edu.ifpe.pdt.util.PTDEmail;

@Component
@ManagedBean(name = "professorControlador", eager = true)
@SessionScoped
public class ProfessorControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private FaltaRepositorio faltaRepositorio;

	public ProfessorControlador() {
	}

	public String redirectCadastro() {
		return "/professor/cadastro.xhtml";
	}

	public String mostrarIndex() {

		Professor prof = professorRepositorio.findBySiape(this.getProfessorLogado().getSiape());
		this.setProfessorLogado(prof);
		return "/restrito/index.xhtml";
	}

	public String adicionaProfessor(String siape, String senha, String coordenacao, String nome, String email) {
		Professor p = new Professor();
		p.setSiape(siape);
		senha = MD5Hash.md5(senha);
		p.setSenha(senha);
		p.setCoordenacao(coordenacao);
		p.setNome(nome);
		p.setEmail(email);
		p.setAutorizacao(AUTORIZACAO.PROFESSOR);
		this.setProfessorLogado(professorRepositorio.saveAndFlush(p));

		return "/login.xhtml";
	}

	public String editarProfessor(String siape, String coordenacao, String nome, String email) {
		Professor p = this.getProfessorLogado();
		p.setSiape(siape);
		p.setCoordenacao(coordenacao);
		p.setNome(nome);
		p.setEmail(email);
		this.setProfessorLogado(professorRepositorio.saveAndFlush(p));

		return "/restrito/index.xhtml";
	}

	public String enviarSenha(String siape) {

		Professor prof = professorRepositorio.findBySiape(siape);

		PTDEmail email = new PTDEmail();

		String hash = MD5Hash.md5(prof.getSiape());

		String message = "http://sisdiven.garanhuns.ifpe.edu.br/DEN/mudarSenha.xhtml?siape=" + prof.getSiape() + "&hash=" + hash;

		email.postMail(prof.getEmail(), "Mudar Senha", message, AppContext.getEmailAuth());
		return "/login.xhtml";
	}

	public String enviarNovaSenha(String novaSenha) throws Exception {
		String ret = "";
		
		String siape = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("siape");
		String hash = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("hash");
		
		
		if (!(MD5Hash.md5(siape).equals(hash))) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hash Não confere!", ""));
		} else {
			Professor prof = professorRepositorio.findBySiape(siape);

			if (prof != null) {
				novaSenha = MD5Hash.md5(novaSenha);
				prof.setSenha(novaSenha);
				this.setProfessorLogado(professorRepositorio.saveAndFlush(prof));
				ret = "/login.xhtml";
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Siape Inválido!", ""));

			}
		}
		return ret;
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

		return "";
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

		return "";
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
			senha = MD5Hash.md5(senha);
			if (prof.getSenha().equals(senha)) {
				this.setProfessorLogado(prof);
				ret = "/restrito/index.xhtml?faces-redirect=true";
			} else {
				prof = null;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha no Login!", "SIAPE ou Senha Inválidos!"));
			}
		} else {
			prof = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha no Login!", "SIAPE Inválido!"));
		}

		return ret;
	}

	public String realizarLogout() {

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/login.xhtml?faces-redirect=true";
	}

	public String mostrarProfessor(String siape) {
		String ret = "";

		if (siape != null) {
			this.setProfessorDetalhado(this.professorRepositorio.findBySiape(siape));
			ret = "/restrito/professor/ensino/mostrar.xhtml?faces-redirect=true";
		}

		return ret;
	}
	
	public String mostrarProfessorPlanejamento(String siape) {
		String ret = "";

		if (siape != null) {
			this.setProfessorPlanejamento(this.professorRepositorio.findBySiape(siape));
			ret = "/restrito/planejamento/ensino/mostrar.xhtml?faces-redirect=true";
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
			ret = "/restrito/professor/ensino/buscar.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String mostrarFaltas() {
		Professor prof = this.getProfessorLogado();
		this.setFaltas(prof.getFaltas());

		return "/restrito/falta/professor/listar.xhtml?faces-redirect=true";
	}

	public String cadastrarFalta() {
		return "/restrito/falta/professor/buscar.xhtml?faces-redirect=true";
	}

	public String adicionarFalta(String siape, Integer ano, Integer semestre) {
		String ret = "";

		if (siape != null && ano != null && semestre != null) {

			this.setProfessorDetalhado(this.professorRepositorio.findBySiape(siape));

			for (PTD ptd : this.getProfessorDetalhado().getPtds()) {
				if (ptd.getAno().equals(ano) && ptd.getSemestre().equals(semestre)) {
					this.setPtdFalta(ptd);
					ret = "/restrito/falta/registrar.xhtml?faces-redirect=true";
					break;
				}
			}
		}

		return ret;
	}

	public String salvarFalta(String numFaltas, Integer disciplina, String data, String obs) {
		Falta f = new Falta();
		String ret = "";

		if (numFaltas != null && disciplina != null && data != null && data.length() > 0) {

			Falta existe = this.faltaRepositorio.findByDisciplinaCodigoAndData(disciplina, Date.valueOf(data));

			if (existe != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta Já cadastrada!", ""));
				return "";
			}

			f.setData(Date.valueOf(data));
			f.setNumeroFaltas(Integer.parseInt(numFaltas));
			f.setObservacao(obs);
			Professor prof = getProfessorDetalhado();
			f.setProfessor(prof);
			f.setFaltaStatus(FALTA_STATUS.CRIADA);

			for (Disciplina disc : this.getPtdFalta().getDisciplinas()) {
				if (disc.getCodigo().equals(disciplina)) {
					f.setDisciplina(disc);
					break;
				}
			}

			prof.getFaltas().add(f);
			professorRepositorio.saveAndFlush(prof);
			PTDEmail email = new PTDEmail();
			String message = AppContext.getEmailCturMessage();

			message = message.replaceFirst("%p", prof.getNome());
			message = message.replaceFirst("%d", f.getData().toString());
			message = message.replaceFirst("%s", f.getDisciplina().getNome());
			message = message.replaceFirst("%t", f.getDisciplina().getTurma());
			email.postMail(prof.getEmail(), AppContext.getEmailCturSubject(), message, AppContext.getEmailAuth());
			ret = "/restrito/falta/buscar.xhtml?faces-redirect=true";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preencher campos obrigatórios!", ""));
		}

		return ret;
	}

	public String listarFaltas(String siape) {
		String ret = "";
		if (siape != null) {
			Professor prof = this.professorRepositorio.findBySiape(siape);
			this.setProfessorDetalhado(prof);
			this.setFaltas(prof.getFaltas());
			ret = "/restrito/falta/listar.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String atualizarFalta(Integer numFaltas, String data, String reposicao, Integer numeroAulasRepostas,
			String observacao, Integer statusFalta) {

		Falta f = this.getFalta();
		if (numFaltas != null && numFaltas > 0) {
			f.setNumeroFaltas(numFaltas);
		}

		if (data != null && data.length() > 0) {
			f.setData(Date.valueOf(data));
		}

		if (reposicao != null && reposicao.length() > 0) {
			f.setReposicao(Date.valueOf(reposicao));
		}

		if (numeroAulasRepostas != null && numeroAulasRepostas > 0) {
			f.setNumeroAulasRepostas(numeroAulasRepostas);
		}

		if (observacao != null && observacao.length() > 0) {
			f.setObservacao(observacao);
		}

		if (statusFalta != null) {
			f.setFaltaStatus(FALTA_STATUS.getFaltaStatus(statusFalta));
		}

		this.faltaRepositorio.save(f);

		List<Falta> faltas = this.getFaltas();

		Falta rem = null;
		for (Falta falta : faltas) {
			if (falta.getCodigo().equals(f.getCodigo())) {
				rem = falta;
				break;
			}
		}

		faltas.remove(rem);
		faltas.add(f);
		this.setFaltas(faltas);

		return "/restrito/falta/listar.xhtml?faces-redirect=true";
	}

	public String removerFalta() {

		Falta f = this.getFalta();

		Professor prof = professorRepositorio.findBySiape(f.getProfessor().getSiape());

		Falta rem = null;
		for (Falta toRemove : prof.getFaltas()) {
			if (toRemove.getCodigo().equals(f.getCodigo())) {
				rem = toRemove;
			}
		}
		prof.getFaltas().remove(rem);

		prof = professorRepositorio.save(prof);

		this.setFaltas(prof.getFaltas());

		return "/restrito/falta/listar.xhtml?faces-redirect=true";
	}

	public String datalharFalta(Integer codigo) {
		String ret = "";

		if (codigo != null) {
			List<Falta> faltas = this.getFaltas();

			for (Falta f : faltas) {
				if (f.getCodigo().equals(codigo)) {
					this.setFalta(f);
					ret = "/restrito/falta/detalhar.xhtml?faces-redirect=true";
					break;
				}
			}
		}

		return ret;
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

	private void setFaltas(List<Falta> faltas) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("faltas", faltas);
	}

	@SuppressWarnings("unchecked")
	public List<Falta> getFaltas() {
		return (List<Falta>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("faltas");
	}

	private void setFalta(Falta f) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("faltaDetalhada", f);
	}

	public Falta getFalta() {
		return (Falta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("faltaDetalhada");
	}
	
	public Professor getProfessorPlanejamento() {
		return (Professor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("professorPlanejamento");
	}

	private void setProfessorPlanejamento(Professor prof) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorPlanejamento", prof);
	}
}
