package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.repositorios.PTDRepository;

@ManagedBean(name="PTDControlador")
@SessionScoped
public class PTDControlador implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private PTDRepository ptdRepository;
	
	private String siape;
	
	private String coordenacao;
	
	private List<PTD> ptds;
	
	private List<Professor> professores;
	
	private PTD selectedPtd;
	
	private Professor selectedProfessor;
	
	private boolean searched;
	
	public PTDControlador() {
		this.ptdRepository = PTDRepository.getInstance();
		this.ptds = new ArrayList<PTD>();
		this.professores = new ArrayList<Professor>();
		searched = false;
	}
	
	public String adicionaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.CRIADO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		this.ptdRepository.savePTD(ptd);
		
		return "cadastroptd.xhtml";
	}
	
	public String atualizaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.AGUARDO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		this.ptdRepository.updatePTD(ptd);
		
		return "buscarptd.xhtml";
	}
	
	public String buscarPTDPorSIAPE() {		
		this.ptds = this.ptdRepository.findByProfessorSiape(siape);
		searched = true;
		return "buscarptd.xhtml"; 
	}
	
	public String buscarPTDEnsinoPorSIAPE() {		
		this.ptds = this.ptdRepository.findByProfessorSiape(siape);
		this.professores.clear();
		return "buscarptdensino.xhtml"; 
	}
	
	public String buscarPTDEnsinoPorCoordenacao() {		
		this.professores = this.ptdRepository.findByProfessorCoordenacao(coordenacao);
		this.ptds.clear();
		return "buscarptdensino.xhtml"; 
	}
	
	public String mostrarPTDByProfessor() {		
		if (selectedProfessor != null) {
			this.ptds = this.ptdRepository.findByProfessorSiape(selectedProfessor.getSiape());
			this.professores.clear();
		}
		return "buscarptdensino.xhtml"; 
	}

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
}
