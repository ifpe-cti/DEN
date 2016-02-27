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

import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.repositorios.PTDRepositoryInterface;

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
	private PTDRepositoryInterface ptdRepositoryInterface;
	
	public PTDControlador() {		
	}
	
	public String adicionaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.CRIADO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptdRepositoryInterface.saveAndFlush(ptd);				
		
		return "/ptd/cadastro.xhtml";
	}
	
	public String atualizaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.AGUARDO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptdRepositoryInterface.save(ptd);
		return "/ptd/buscar.xhtml";
	}
	
	public String buscarPTDPorSIAPE() {		
		this.ptds = ptdRepositoryInterface.findByProfessorSiape(siape);
		searched = true;
		return "/ptd/buscar.xhtml"; 
	}
	
	public String buscarPTDEnsinoPorSIAPE() {		
		this.ptds = ptdRepositoryInterface.findByProfessorSiape(siape);
		this.professores.clear();
		return "/ptd/buscarensino.xhtml"; 
	}
	
	public String buscarPTDEnsinoPorCoordenacao() {		
		this.professores = ptdRepositoryInterface.findByProfessorCoordenacao(coordenacao);
		this.ptds.clear();
		return "/ptd/buscarensino.xhtml"; 
	}
	
	public String mostrarPTDByProfessor() {		
		if (selectedProfessor != null) {
			this.ptds = ptdRepositoryInterface.findByProfessorSiape(selectedProfessor.getSiape());
			this.professores.clear();
		}
		return "/ptd/buscarensino.xhtml"; 
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
