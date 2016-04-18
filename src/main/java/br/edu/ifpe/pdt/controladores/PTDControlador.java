package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.entidades.AAE;
import br.edu.ifpe.pdt.entidades.AAP;
import br.edu.ifpe.pdt.entidades.Disciplina;
import br.edu.ifpe.pdt.entidades.Extensao;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Pesquisa;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.repositorios.PTDRepositorio;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;

@Component
@ManagedBean(name = "PTDControlador", eager = true)
@SessionScoped
public class PTDControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<PTD> ptds = new ArrayList<PTD>();

	private Professor selectedProfessor;

	private PTD selectedPtd;

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
		this.selectedPtd = new PTD();
		return "/restrito/ptd/cadastro.xhtml";
	}

	public String cadastrarNovoPTDFromSelected(Integer ptdId) {
		String ret = "";
		if (ptdId != null) {
			PTD ptd= ptdRepositorio.findOne(ptdId);

			this.selectedPtd = ptd.clone();
			ret = "/restrito/ptd/cadastro.xhtml";
		}

		return ret;
	}

	public String editarPTD(Integer ptdId) {
		String ret = "/restrito/ptd/editar.xhtml";
		if (ptdId != null) {
			this.selectedPtd = ptdRepositorio.findOne(ptdId);
		} else {
			ret = "";
		}

		return ret;
	}

	public String atualizaPTD() {
		this.selectedPtd.setStatus(PTD.STATUS.AGUARDO);
		this.selectedPtd.setLastUpdate(Date.valueOf(LocalDate.now()));

		ptdRepositorio.saveAndFlush(this.selectedPtd);

		return "/index.xhtml";
	}

	public String criarPTD(String siape) {
		this.selectedPtd.setStatus(PTD.STATUS.AGUARDO);
		this.selectedPtd.setLastUpdate(Date.valueOf(LocalDate.now()));

		Professor prof = this.professorRepositorio.findBySiape(siape);
		prof.getPtds().add(this.selectedPtd);
		this.selectedPtd.setProfessor(prof);
		professorRepositorio.saveAndFlush(prof);

		FacesContext.getCurrentInstance().getExternalContext().
						getSessionMap().put("professorLogado", prof);

		return "/index.xhtml";
	}

	public String fecharPTD() {
		this.selectedPtd.setStatus(PTD.STATUS.FECHADO);
		this.selectedPtd.setLastUpdate(Date.valueOf(LocalDate.now()));

		ptdRepositorio.saveAndFlush(this.selectedPtd);

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorLogado",
				this.selectedPtd.getProfessor());

		return "/index.xhtml";
	}

	public String reabrirPTD() {
		this.selectedPtd.setStatus(PTD.STATUS.AGUARDO);
		this.selectedPtd.setLastUpdate(Date.valueOf(LocalDate.now()));

		ptdRepositorio.saveAndFlush(this.selectedPtd);

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("professorLogado",
				this.selectedPtd.getProfessor());

		return "/index.xhtml";
	}

	// Antigos

	public String atualizaPTDEnsino(PTD ptd) {
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));

		// buscar e atualizar PTD

		professorRepositorio.saveAndFlush(this.selectedProfessor);

		return "/restrito/ptd/mostrarEnsino.xhtml";
	}

	public String mostrarPTD() {
		String ret = "";
		if (this.selectedPtd != null) {
			// buscar PTD
			// this.selectedPtd =
			// ptdRepositorio.findOne(this.selectedPtd.getCodigo());
			ret = "/restrito/ptd/mostrarEnsino.xhtml";
		}

		return ret;
	}

	public String mostrarPTDByProfessor() {
		if (selectedProfessor != null) {
			this.ptds = this.selectedProfessor.getPtds();
		}

		return "/restrito/ptd/buscarensino.xhtml";
	}

	// Métodos de apoio a tela editar.
	public String addDisciplina() {
		if (this.selectedPtd.getDisciplinas() == null) {
			this.selectedPtd.setDisciplinas(new LinkedHashSet<Disciplina>());
		}
		Disciplina d = new Disciplina();
		d.setNome(this.disciplina.getNome());
		d.setCurso(this.disciplina.getCurso());
		d.setCargaHoraria(this.disciplina.getCargaHoraria());
		d.setPTD(this.selectedPtd);
		this.selectedPtd.getDisciplinas().add(d);
		return "";
	}

	public String removeDisciplina(Disciplina disciplina) {
		this.selectedPtd.getDisciplinas().remove(disciplina);
		return "";
	}

	public String addAAE() {
		if (this.selectedPtd.getAaes() == null) {
			this.selectedPtd.setAaes(new LinkedHashSet<AAE>());
		}
		AAE aae = new AAE();
		aae.setAtividade(this.aae.getAtividade());
		aae.setCargaHoraria(this.aae.getCargaHoraria());
		aae.setPTD(this.selectedPtd);
		this.selectedPtd.getAaes().add(aae);
		return "";
	}

	public String removeAAE(AAE aae) {
		this.selectedPtd.getAaes().remove(aae);
		return "";
	}

	public String addPesquisa() {
		if (this.selectedPtd.getPesquisas() == null) {
			this.selectedPtd.setPesquisas(new LinkedHashSet<Pesquisa>());
		}
		Pesquisa p = new Pesquisa();
		p.setAtividade(this.pesquisa.getAtividade());
		p.setPTD(this.selectedPtd);
		this.selectedPtd.getPesquisas().add(p);
		return "";
	}

	public String removePesquisa(Pesquisa pesquisa) {
		this.selectedPtd.getPesquisas().remove(pesquisa);
		return "";
	}

	public String addExtensao() {
		if (this.selectedPtd.getExtensoes() == null) {
			this.selectedPtd.setExtensoes(new LinkedHashSet<Extensao>());
		}
		Extensao e = new Extensao();
		e.setAtividade(this.extensao.getAtividade());
		e.setPTD(this.selectedPtd);
		this.selectedPtd.getExtensoes().add(e);
		return "";
	}

	public String removeExtensao(Extensao extensao) {
		this.selectedPtd.getExtensoes().remove(extensao);
		return "";
	}

	public String addAAP() {
		if (this.selectedPtd.getAaps() == null) {
			this.selectedPtd.setAaps(new LinkedHashSet<AAP>());
		}
		AAP a = new AAP();
		a.setAtividade(this.aap.getAtividade());
		a.setPortaria(this.aap.getPortaria());
		aap.setPTD(this.selectedPtd);
		this.selectedPtd.getAaps().add(this.aap);
		return "";
	}

	public String removeAAP(AAP aap) {
		this.selectedPtd.getAaps().remove(aap);
		return "";
	}

	// Getters and Setters from bean
	public List<PTD> getPtds() {
		return ptds;
	}

	public void setPtds(List<PTD> ptds) {
		this.ptds = ptds;
	}

	public PTD getSelectedPtd() {
		return selectedPtd;
	}

	public void setSelectedPtd(PTD selectedPtd) {
		this.selectedPtd = selectedPtd;
	}

	public Professor getSelectedProfessor() {
		return selectedProfessor;
	}

	public void setSelectedProfessor(Professor selectedProfessor) {
		this.selectedProfessor = selectedProfessor;
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
}