package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.Avaliacao;
import br.edu.ifpe.pdt.entidades.Disciplina;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.PlanejamentoSemestral;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.PlanejamentoSemestral.STATUS_PS;
import br.edu.ifpe.pdt.entidades.Semana;
import br.edu.ifpe.pdt.repositorios.DisciplinaRepositorio;
import br.edu.ifpe.pdt.repositorios.PTDRepositorio;
import br.edu.ifpe.pdt.repositorios.PlanejamentoSemestralRepositorio;
import br.edu.ifpe.pdt.util.PTDEmail;

@Component
@ManagedBean(name = "planejamentoSemestralControlador", eager = true)
@SessionScoped
public class PlanejamentoSemestralControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DisciplinaRepositorio disciplinaRepositorio;
	
	@Autowired
	private PlanejamentoSemestralRepositorio planejamentoSemestralRepositorio;

	@Autowired
	private PTDRepositorio ptdRepositorio;

	private Semana semana;
	private Avaliacao avaliacao;

	public PlanejamentoSemestralControlador() {
		this.semana = new Semana();
		this.avaliacao = new Avaliacao();
	}

	public String criarPlanejamento(Integer disciplinaId, Integer ensino) {

		String ret = "";

		Disciplina disciplina = this.getDisciplinaFromSelectedPTD(disciplinaId);

		if (disciplina != null) {
			if (ensino == 0) {
				this.setDisciplina(disciplina);
				ret = "/restrito/planejamento/cadastro.xhtml?faces-redirect=true";
			} else {
				this.setDisciplina(disciplina);
				ret = "/restrito/planejamento/ensino/cadastro.xhtml?faces-redirect=true";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione uma disciplina!", ""));
		}

		return ret;
	}

	private Disciplina getDisciplinaFromSelectedPTD(Integer disciplinaId) {
		Disciplina d = null;

		if (this.getSelectedPtd() != null) {
			for (Disciplina disciplina : this.getSelectedPtd().getDisciplinas()) {
				if (disciplina.getCodigo().equals(disciplinaId)) {
					d = disciplina;
					break;
				}
			}
		}

		return d;
	}

	public String registrarPlanejamento() {
		return savePlanejamento();
	}

	public String atualizaPlanejamento() {

		return savePlanejamento();
	}

	public void listarDisciplinas(Integer ano, Integer semestre) {

		Professor prof = (Professor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("professorLogado");

		PTD ptd = ptdRepositorio.findByAnoAndSemestreAndProfessorSiape(ano, semestre, prof.getSiape());

		this.setSelectedPtdImport(ptd);

	}

	public String importarDisciplina(Integer disciplinaId) {
		String ret = "";
		Disciplina d = disciplinaRepositorio.findByCodigo(disciplinaId);
		if (d.getPlanejamentoSemestral() != null) {

			Disciplina disciplina = this.getDisciplina();
			if (disciplina.getPlanejamentoSemestral() == null) {
				disciplina.setPlanejamentoSemestral(new PlanejamentoSemestral());
				disciplina.getPlanejamentoSemestral().setAvaliacoes(new ArrayList<Avaliacao>());
				disciplina.getPlanejamentoSemestral().setSemanas(new ArrayList<Semana>());
			}
			disciplina.getPlanejamentoSemestral().setCompetencias(d.getPlanejamentoSemestral().getCompetencias());

			for (Avaliacao a : d.getPlanejamentoSemestral().getAvaliacoes()) {
				if (!(disciplina.getPlanejamentoSemestral().getAvaliacoes().contains(a))) {
					Avaliacao nova = new Avaliacao();
					nova.setAtividade(a.getAtividade());
					nova.setUnidade(a.getUnidade());
					nova.setDataProva(a.getDataProva());
					nova.setDataRecuperacao(a.getDataRecuperacao());
					nova.setPlanejamentoSemestral(disciplina.getPlanejamentoSemestral());
					disciplina.getPlanejamentoSemestral().getAvaliacoes().add(nova);
				}
			}

			for (Semana s : d.getPlanejamentoSemestral().getSemanas()) {
				if (!(disciplina.getPlanejamentoSemestral().getSemanas().contains(s))) {
					Semana nova = new Semana();
					nova.setConteudo(s.getConteudo());
					nova.setEstrategia(s.getEstrategia());
					nova.setNumAulas(s.getNumAulas());
					nova.setNumero(s.getNumero());
					nova.setPlanejamentoSemestral(disciplina.getPlanejamentoSemestral());
					disciplina.getPlanejamentoSemestral().getSemanas().add(nova);
				}
			}
			this.setDisciplina(disciplina);
			ret = "/restrito/planejamento/cadastro.xhtml?faces-redirect=true";
		}
		this.setSelectedPtdImport(null);
		return ret;
	}

	public String visualizarPlanejamento(Integer disciplinaId) {

		String ret = "";
		if (disciplinaId != null) {
			Disciplina disciplina = this.getDisciplinaFromSelectedPTD(disciplinaId);
			this.setPlanejamentoSemestral(disciplina.getPlanejamentoSemestral());
			ret = "/restrito/planejamento/ensino/formVisualizar.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String aprovarPlanejamento(Integer planejamentoId) {

		String ret = "";
		if (planejamentoId != null) {
			PlanejamentoSemestral plano = planejamentoSemestralRepositorio.findByCodigo(planejamentoId);
			if (plano != null) {
				plano.setStatus(STATUS_PS.APROVADO);
				planejamentoSemestralRepositorio.saveAndFlush(plano);
				this.setPlanejamentoSemestral(plano);
				ret = "/restrito/planejamento/ensino/formVisualizar.xhtml?faces-redirect=true";
			}
		}

		return ret;
	}

	public String abrirCorrecaoPlanejamento(Integer planejamentoId) {

		String ret = "";
		if (planejamentoId != null) {
			PlanejamentoSemestral plano = planejamentoSemestralRepositorio.findByCodigo(planejamentoId);
			this.setPlanejamentoSemestral(plano);
			ret = "/restrito/planejamento/ensino/correcao.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String enviarCorrecaoPlanejamento(Integer planejamentoId, String msg) {

		String ret = "";
		if (planejamentoId != null) {
			Disciplina disciplina = disciplinaRepositorio.findByPlanejamentoSemestralCodigo(planejamentoId);
			disciplina.getPlanejamentoSemestral().setStatus(STATUS_PS.CORRECAO);
			disciplinaRepositorio.saveAndFlush(disciplina);
			this.setPlanejamentoSemestral(disciplina.getPlanejamentoSemestral());
			PTDEmail mail = new PTDEmail();
			String subject = AppContext.getEmailPlanejamentoSubject();
			subject = subject.replaceFirst("%p", disciplina.getNome());
			mail.postMail(disciplina.getPtd().getProfessor().getEmail(), 
					subject , msg, AppContext.getEmailAuth());
			ret = "/restrito/planejamento/ensino/formVisualizar.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public void onRowSelect(SelectEvent event) {
		PTD ptd = ((PTD) event.getObject());
		this.setSelectedPtd(ptd);
	}

	public void onRowEdit(RowEditEvent event) {
		Semana semana = ((Semana) event.getObject());

		for (Semana s : this.getDisciplina().getPlanejamentoSemestral().getSemanas()) {
			if (semana.getNumero().equals(s.getNumero())) {
				s.setConteudo(semana.getConteudo());
				s.setEstrategia(semana.getEstrategia());
				s.setNumAulas(semana.getNumAulas());
				break;
			}
		}
	}

	public PTD getSelectedPtd() {
		return (PTD) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedPtdPlan");
	}

	public void setSelectedPtd(PTD selectedPtd) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPtdPlan", selectedPtd);
	}

	private void setDisciplina(Disciplina disciplina) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedDisciplina", disciplina);
	}

	public Disciplina getDisciplina() {
		return (Disciplina) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("selectedDisciplina");
	}

	public PTD getSelectedPtdImport() {
		return (PTD) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("selectedPtdPlanImport");
	}

	public void setSelectedPtdImport(PTD selectedPtd) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPtdPlanImport",
				selectedPtd);
	}

	public PTD getPlanejamentoSemestral() {
		return (PTD) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("planejamentoSemestral");
	}

	private void setPlanejamentoSemestral(PlanejamentoSemestral planejamentoSemestral) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("planejamentoSemestral",
				planejamentoSemestral);
	}

	// Métodos de apoio a tela editar.
	public String addSemana() {

		Disciplina d = this.getDisciplina();

		if (d.getPlanejamentoSemestral() == null) {
			d.setPlanejamentoSemestral(new PlanejamentoSemestral());
		}

		Semana s = new Semana();
		s.setNumero(this.semana.getNumero());
		s.setNumAulas(this.semana.getNumAulas());
		s.setEstrategia(this.semana.getEstrategia());
		s.setConteudo(this.semana.getConteudo());
		s.setPlanejamentoSemestral(d.getPlanejamentoSemestral());
		PlanejamentoSemestral plan = d.getPlanejamentoSemestral();
		if (plan.getSemanas() == null) {
			plan.setSemanas(new ArrayList<Semana>());
		}

		boolean exists = false;
		for (Semana sem : plan.getSemanas()) {
			if (sem.getNumero().equals(this.semana.getNumero())) {
				exists = true;
				break;
			}
		}

		if (!exists) {
			plan.getSemanas().add(s);
			d.setPlanejamentoSemestral(plan);
			this.setDisciplina(d);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Semana já cadastrada!", ""));
		}

		return "";
	}

	public String removeSemana(Semana semana) {
		Disciplina d = this.getDisciplina();
		d.getPlanejamentoSemestral().getSemanas().remove(semana);
		this.setDisciplina(d);
		return "";
	}

	public String addAvaliacao(String dataProva, String dataRecuperacao) {

		Disciplina d = this.getDisciplina();

		if (d.getPlanejamentoSemestral() == null) {
			d.setPlanejamentoSemestral(new PlanejamentoSemestral());
		}

		if (((dataProva != null) && !dataProva.equals(""))
				&& ((dataRecuperacao != null) && !dataRecuperacao.equals(""))) {

			Avaliacao a = new Avaliacao();
			a.setAtividade(this.avaliacao.getAtividade());
			a.setDataProva(Date.valueOf(dataProva));
			a.setDataRecuperacao(Date.valueOf(dataRecuperacao));
			a.setUnidade(this.avaliacao.getUnidade());

			a.setPlanejamentoSemestral(d.getPlanejamentoSemestral());
			PlanejamentoSemestral plan = d.getPlanejamentoSemestral();
			if (plan.getAvaliacoes() == null) {
				plan.setAvaliacoes(new ArrayList<Avaliacao>());
			}
			plan.getAvaliacoes().add(a);
			d.setPlanejamentoSemestral(plan);
			this.setDisciplina(d);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Especificar Datas!", ""));
		}

		return "";
	}

	public String removeAvaliacao(Avaliacao avaliacao) {
		Disciplina d = this.getDisciplina();
		d.getPlanejamentoSemestral().getAvaliacoes().remove(avaliacao);
		this.setDisciplina(d);
		return "";
	}

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	private String savePlanejamento() {
		String ret = "";
		Disciplina d = this.getDisciplina();
		if (d.getPlanejamentoSemestral() != null) {
			d.getPlanejamentoSemestral().setStatus(STATUS_PS.AGUARDO);
			d = disciplinaRepositorio.saveAndFlush(d);
			this.setDisciplina(null);
			this.setSelectedPtd(null);
			ret = "/restrito/planejamento/ptd/listar.xhtml?faces-redirect=true";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Cadastrar!", ""));
		}

		return ret;
	}
}
