package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
@ManagedBean(name="PTDControlador", eager=true)
@SessionScoped
public class PTDControlador implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private String siape;
	
	private String coordenacao;
	
	private List<PTD> ptds = new ArrayList<PTD>();
	
	private List<Professor> professores = new ArrayList<Professor>();
	
	private PTD selectedPtd;
	
	private Professor selectedProfessor;
	
	private boolean searched = false;
	
	@Autowired
	private PTDRepositorio ptdRepositorio;
	
	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
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
	
	public String adicionaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.CRIADO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		this.selectedPtd = ptdRepositorio.saveAndFlush(ptd);
		this.selectedProfessor = this.selectedPtd.getProfessor();
		this.selectedProfessor.createSets();
		
		searched = false;		
		return "/ptd/cadastro.xhtml";
	}
	
	public String atualizaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.AGUARDO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptd.setProfessor(this.selectedPtd.getProfessor());
		this.selectedPtd = ptdRepositorio.saveAndFlush(ptd);		
		this.selectedProfessor = this.selectedPtd.getProfessor();
		
		searched = false;	
		return "/ptd/editar.xhtml";
	}
	
	public String fecharPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.FECHADO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptd.setProfessor(this.selectedPtd.getProfessor());
		this.selectedPtd = ptdRepositorio.saveAndFlush(ptd);		
		this.selectedProfessor = this.selectedPtd.getProfessor();
		
		searched = false;	
		return "/ptd/editar.xhtml";
	}
	
	public String reabrirPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.AGUARDO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptd.setProfessor(this.selectedPtd.getProfessor());
		this.selectedPtd = ptdRepositorio.saveAndFlush(ptd);		
		this.selectedProfessor = this.selectedPtd.getProfessor();
		
		searched = false;	
		return "/ptd/editar.xhtml";
	}
	
	public String atualizaPTDEnsino() {		
		this.selectedPtd.setLastUpdate(Date.valueOf(LocalDate.now()));
		this.selectedPtd = ptdRepositorio.saveAndFlush(this.selectedPtd);		
		this.selectedProfessor = this.selectedPtd.getProfessor();
		
		searched = false;	
		return "/ptd/mostrarEnsino.xhtml";
	}
	
	public String editarPTD() {		
		String ret  = "";
		if (this.selectedPtd != null) {
			this.selectedPtd = ptdRepositorio.findOne(this.selectedPtd.getCodigo());
			ret = "/ptd/editar.xhtml";
		}		
		
		searched = false;	
		return ret;
	}
	
	public String mostrarPTD() {		
		String ret  = "";
		if (this.selectedPtd != null) {
			this.selectedPtd = ptdRepositorio.findOne(this.selectedPtd.getCodigo());
			ret = "/ptd/mostrarEnsino.xhtml";
		}		
		
		searched = false;	
		return ret;
	}
	
	public String cadastrarNovoPTD() {		
		this.selectedPtd = new PTD();
		this.selectedProfessor.clear();
		this.selectedPtd.setProfessor(this.selectedProfessor);
		
		searched = false;	
		return "/ptd/cadastro.xhtml";
	}
	
	public String cadastrarNovoPTDFromSelected() {		
		PTD ptd = this.selectedPtd.clone();
		this.selectedPtd = ptd;
		searched = false;	
		return "/ptd/cadastro.xhtml";
	}
	
	public String buscarPTDPorSIAPE() {	
		this.ptds = ptdRepositorio.findByProfessorSiape(siape);
		if (ptds.size() > 0) {
			this.selectedProfessor = ptds.get(0).getProfessor();
		}
		
		
		searched = true;
		return "/ptd/buscar.xhtml"; 
	}
	
	public String buscarPTDEnsinoPorSIAPE() {		
		this.ptds = ptdRepositorio.findByProfessorSiape(siape);
		if (ptds.size() > 0) {
			this.selectedProfessor = ptds.get(0).getProfessor();
		}
		
		this.professores.clear();		
		searched = false;
		return "/ptd/buscarensino.xhtml"; 
	}
	
	public String buscarPTDEnsinoPorCoordenacao() {		
		this.professores = professorRepositorio.findByCoordenacao(coordenacao);
		
		this.ptds.clear();		
		this.selectedProfessor = null;
		searched = false;
		return "/ptd/buscarensino.xhtml"; 
	}
	
	public String mostrarPTDByProfessor() {		
		if (selectedProfessor != null) {
			this.ptds = ptdRepositorio.findByProfessorSiape(selectedProfessor.getSiape());
			this.professores.clear();
		}
		
		searched = false;
		return "/ptd/buscarensino.xhtml"; 
	}
	
// Métodos de apoio a tela editar.
	public String addDisciplina() {
		this.selectedPtd.getProfessor().getDisciplinas().add(this.disciplina);
		this.disciplina.setProfessor(this.selectedPtd.getProfessor());
		return ""; 
	}
	
	public String removeDisciplina(Disciplina disciplina) {
		this.selectedPtd.getProfessor().getDisciplinas().remove(disciplina);
		return ""; 
	}
	
	public String addAAE() {
		this.selectedPtd.getProfessor().getAaes().add(this.aae);
		this.aae.setProfessor(this.selectedPtd.getProfessor());
		return ""; 
	}
	
	public String removeAAE(AAE aae) {
		this.selectedPtd.getProfessor().getAaes().remove(aae);
		return ""; 
	}
	
	public String addPesquisa() {
		this.selectedPtd.getProfessor().getPesquisas().add(this.pesquisa);
		this.pesquisa.setProfessor(this.selectedPtd.getProfessor());
		return ""; 
	}
	
	public String removePesquisa(Pesquisa pesquisa) {
		this.selectedPtd.getProfessor().getPesquisas().remove(pesquisa);
		return ""; 
	}
	
	public String addExtensao() {
		this.selectedPtd.getProfessor().getExtensoes().add(this.extensao);
		this.extensao.setProfessor(this.selectedPtd.getProfessor());
		return ""; 
	}
	
	public String removeExtensao(Extensao extensao) {
		this.selectedPtd.getProfessor().getExtensoes().remove(extensao);
		return ""; 
	}
	
	public String addAAP() {
		this.selectedPtd.getProfessor().getAaps().add(this.aap);
		this.aap.setProfessor(this.selectedPtd.getProfessor());
		return ""; 
	}
	
	public String removeAAP(AAP aap) {
		this.selectedPtd.getProfessor().getAaps().remove(aap);
		return ""; 
	}
// Getters and Setters from bean
	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

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

	public boolean isSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public String getCoordenacao() {
		return coordenacao;
	}

	public void setCoordenacao(String coordenacao) {
		this.coordenacao = coordenacao;
	}

	public List<Professor> getProfessores() {
		return professores;
	}

	public void setProfessores(List<Professor> professores) {
		this.professores = professores;
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
