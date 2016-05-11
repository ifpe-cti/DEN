package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedHashSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.AAE;
import br.edu.ifpe.pdt.entidades.AAP;
import br.edu.ifpe.pdt.entidades.Disciplina;
import br.edu.ifpe.pdt.entidades.Extensao;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.PTD.STATUS;
import br.edu.ifpe.pdt.entidades.Pesquisa;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.repositorios.PTDRepositorio;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;
import br.edu.ifpe.pdt.util.PTDEmail;

@Component
@ManagedBean(name = "PTDControlador", eager = true)
@SessionScoped
public class PTDControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private PTDRepositorio ptdRepositorio;

	private Disciplina disciplina;
	private AAE aae;
	private Pesquisa pesquisa;
	private Extensao extensao;
	private AAP aap;

	public PTDControlador() {
		this.disciplina = new Disciplina();
		this.aae = new AAE();
		this.pesquisa = new Pesquisa();
		this.extensao = new Extensao();
		this.aap = new AAP();
	}

	public String cadastrarNovoPTD() {
		this.setSelectedPtd(new PTD());
		return "/restrito/ptd/cadastro.xhtml?faces-redirect=true";
	}

	public String cadastrarNovoPTDFromSelected(Integer ptdId) {
		String ret = "";
		if (ptdId != null) {
			PTD ptd = ptdRepositorio.findOne(ptdId);

			this.setSelectedPtd(ptd.clone());
			ret = "/restrito/ptd/cadastro.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String editarPTD(Integer ptdId) {
		String ret = "/restrito/ptd/editar.xhtml?faces-redirect=true";
		if (ptdId != null) {
			this.setSelectedPtd(ptdRepositorio.findOne(ptdId));
		} else {
			ret = "";
		}

		return ret;
	}

	public String atualizaPTD() {
		PTD ptd = this.getSelectedPtd();
		ptd.setStatus(PTD.STATUS.AGUARDO);
		ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));
		ptd = ptdRepositorio.saveAndFlush(ptd);
		this.setSelectedPtd(ptd);
		this.updateProfessorLogado(ptd.getProfessor());

		return "/restrito/index.xhtml?faces-redirect=true";
	}

	public String criarPTD(String siape, Integer ano, Integer semestre) {
		PTD ptd = this.getSelectedPtd();
		ptd.setStatus(PTD.STATUS.AGUARDO);
		ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));
		ptd.setAno(ano);
		ptd.setSemestre(semestre);

		Professor prof = this.professorRepositorio.findBySiape(siape);
		prof.getPtds().add(ptd);
		ptd.setProfessor(prof);
		prof = professorRepositorio.saveAndFlush(prof);

		this.updateProfessorLogado(prof);
		this.setSelectedPtd(ptd);

		return "/restrito/index.xhtml?faces-redirect=true";
	}

	public String fecharPTD() {
		/*
		 * this.selectedPtd.setStatus(PTD.STATUS.FECHADO);
		 * this.selectedPtd.setLastUpdate(Date.valueOf(LocalDate.now()));
		 * 
		 * ptdRepositorio.saveAndFlush(this.selectedPtd);
		 * 
		 * FacesContext.getCurrentInstance().getExternalContext().getSessionMap(
		 * ).put("professorLogado", this.selectedPtd.getProfessor());
		 */

		return "/restrito/index.xhtml?faces-redirect=true";
	}

	public String reabrirPTD() {
		PTD ptd = this.getSelectedPtd();
		ptd.setStatus(PTD.STATUS.AGUARDO);
		ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));

		this.setSelectedPtd(ptdRepositorio.saveAndFlush(ptd));

		this.updateProfessorLogado(ptd.getProfessor());

		return "/restrito/index.xhtml?faces-redirect=true";
	}

	public String detalharPTD(Integer ptdId) {
		String ret = "";

		if (ptdId != null) {
			this.setSelectedPtd(ptdRepositorio.findOne(ptdId));
			ret = "/restrito/ptd/mostrar.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String atualizaPTDEnsino(Integer status) {
		String ret = "/restrito/ptd/mostrar.xhtml";

		PTD ptd = this.getSelectedPtd();
		ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));
		STATUS s = STATUS.getStatus(status);
		ptd.setStatus(s);
		ptd = ptdRepositorio.saveAndFlush(ptd);
		this.setSelectedPtd(ptd);

		if (s == STATUS.CORRECAO) {
			ret = "/restrito/ptd/enviarCorrecao.xhtml?faces-redirect=true";
		}

		return ret;
	}

	public String enviarEmailCorrecao(String mensagem) {
		String ret = "/restrito/ptd/mostrar.xhtml?faces-redirect=true";
		PTDEmail mail = new PTDEmail();
		PTD ptd = this.getSelectedPtd();
		mail.postMail(ptd.getProfessor().getEmail(), AppContext.getEmailSubject(), mensagem, AppContext.getEmailAuth());

		return ret;
	}

	public String editarRelatorioSemestral(Integer ptdId) {
		String ret = "";
		if (ptdId != null) {
			PTD ptd = ptdRepositorio.findOne(ptdId);
			if (ptd.getStatus() == STATUS.HOMOLOGADO) {
				this.setSelectedPtd(ptd);
				ret = "/restrito/ptd/editarRelatorioSemestral.xhtml?faces-redirect=true";
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Status Inválido", "O PTD precisa estar homologado!"));
			}
		}

		return ret;
	}

	public String entregarRelatorioSemestral(String resultado) {
		String ret = "/restrito/index.xhtml?faces-redirect=true";

		PTD ptd = this.getSelectedPtd();
		ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));
		ptd.setStatus(STATUS.FECHADO);
		ptd.setResultado(resultado);
		ptd = ptdRepositorio.saveAndFlush(ptd);
		this.setSelectedPtd(ptd);
		this.updateProfessorLogado(ptd.getProfessor());

		return ret;
	}

	public String visualizarRelatorioSemestral() {
		String ret = "/restrito/professor/ensino/relatorioSemestral.xhtml?faces-redirect=true";
		PTD ptd = this.getSelectedPtd();
		this.setSelectedPtd(ptd);

		return ret;
	}

	public String corrigirRelatorioSemestral(String correcoes) {
		String ret = "";
		if (correcoes != null && correcoes.length() > 0) {
			ret = "/restrito/professor/ensino/mostrar.xhtml?faces-redirect=true";
			PTD ptd = this.getSelectedPtd();
			ptd.setStatus(STATUS.HOMOLOGADO);
			ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));
			ptd = ptdRepositorio.saveAndFlush(ptd);
			this.setSelectedPtd(ptd);
			this.updateProfessorDetalhado(ptd.getProfessor());
			PTDEmail email = new PTDEmail();
			email.postMail(ptd.getProfessor().getEmail(), AppContext.getEmailSubject(), correcoes,
					AppContext.getEmailAuth());
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correção não pode estar em branco!", ""));
		}

		return ret;
	}

	public String homologarRelatorioSemestral() {
		String ret = "";
		ret = "/restrito/professor/ensino/mostrar.xhtml?faces-redirect=true";
		PTD ptd = this.getSelectedPtd();
		ptd.setStatus(STATUS.RELATORIO_HOMOLOGADO);
		ptd.setLastUpdate(Date.valueOf(LocalDate.now().toString()));
		ptd = ptdRepositorio.saveAndFlush(ptd);
		this.setSelectedPtd(ptd);
		this.updateProfessorDetalhado(ptd.getProfessor());
		
		return ret;
	}

	// Métodos de apoio a tela editar.
	public String addDisciplina() {
		PTD ptd = this.getSelectedPtd();
		if (ptd.getDisciplinas() == null) {
			ptd.setDisciplinas(new LinkedHashSet<Disciplina>());
		}
		Disciplina d = new Disciplina();
		d.setNome(this.disciplina.getNome());
		d.setCurso(this.disciplina.getCurso());
		d.setCargaHoraria(this.disciplina.getCargaHoraria());
		d.setTurma(this.disciplina.getTurma());
		d.setPtd(ptd);
		ptd.getDisciplinas().add(d);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String removeDisciplina(Disciplina disciplina) {
		PTD ptd = this.getSelectedPtd();
		ptd.getDisciplinas().remove(disciplina);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String addAAE() {
		PTD ptd = this.getSelectedPtd();
		if (ptd.getAaes() == null) {
			ptd.setAaes(new LinkedHashSet<AAE>());
		}
		AAE aae = new AAE();
		aae.setAtividade(this.aae.getAtividade());
		aae.setCargaHoraria(this.aae.getCargaHoraria());
		aae.setPTD(ptd);
		ptd.getAaes().add(aae);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String removeAAE(AAE aae) {
		PTD ptd = this.getSelectedPtd();
		ptd.getAaes().remove(aae);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String addPesquisa() {
		PTD ptd = this.getSelectedPtd();
		if (ptd.getPesquisas() == null) {
			ptd.setPesquisas(new LinkedHashSet<Pesquisa>());
		}
		Pesquisa p = new Pesquisa();
		p.setAtividade(this.pesquisa.getAtividade());
		p.setPTD(ptd);
		ptd.getPesquisas().add(p);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String removePesquisa(Pesquisa pesquisa) {
		PTD ptd = this.getSelectedPtd();
		ptd.getPesquisas().remove(pesquisa);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String addExtensao() {
		PTD ptd = this.getSelectedPtd();
		if (ptd.getExtensoes() == null) {
			ptd.setExtensoes(new LinkedHashSet<Extensao>());
		}
		Extensao e = new Extensao();
		e.setAtividade(this.extensao.getAtividade());
		e.setPTD(ptd);
		ptd.getExtensoes().add(e);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String removeExtensao(Extensao extensao) {
		PTD ptd = this.getSelectedPtd();
		ptd.getExtensoes().remove(extensao);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String addAAP() {
		PTD ptd = this.getSelectedPtd();
		if (ptd.getAaps() == null) {
			ptd.setAaps(new LinkedHashSet<AAP>());
		}
		AAP a = new AAP();
		a.setAtividade(this.aap.getAtividade());
		a.setPortaria(this.aap.getPortaria());
		aap.setPTD(ptd);
		ptd.getAaps().add(this.aap);
		this.setSelectedPtd(ptd);
		return "";
	}

	public String removeAAP(AAP aap) {
		PTD ptd = this.getSelectedPtd();
		ptd.getAaps().remove(aap);
		this.setSelectedPtd(ptd);
		return "";
	}

	// Getters and Setters from bean
	public PTD getSelectedPtd() {
		return (PTD) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedPtd");
	}

	public void setSelectedPtd(PTD selectedPtd) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPtd", selectedPtd);
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public AAE getAae() {
		return aae;
	}

	public void setAae(AAE aae) {
		this.aae = aae;
	}

	public Pesquisa getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(Pesquisa pesquisa) {
		this.pesquisa = pesquisa;
	}

	public Extensao getExtensao() {
		return extensao;
	}

	public void setExtensao(Extensao extensao) {
		this.extensao = extensao;
	}

	public AAP getAap() {
		return aap;
	}

	public void setAap(AAP aap) {
		this.aap = aap;
	}

	private void updateProfessorLogado(Professor prof) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorLogado", prof);
	}

	private void updateProfessorDetalhado(Professor prof) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorDetalhado", prof);
	}
}