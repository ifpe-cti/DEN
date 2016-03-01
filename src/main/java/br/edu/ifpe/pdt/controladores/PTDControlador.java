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
	
	public PTDControlador() {		
	}
	
	public String adicionaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.CRIADO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptdRepositorio.saveAndFlush(ptd);
		this.selectedProfessor = ptd.getProfessor();
		
		
		searched = false;		
		return "/ptd/cadastro.xhtml";
	}
	
	public String atualizaPTD(PTD ptd) {		
		ptd.setStatus((byte) PTD.STATUS.AGUARDO.ordinal());
		ptd.setLastUpdate(Date.valueOf(LocalDate.now()));
		ptdRepositorio.save(ptd);
		this.selectedProfessor = ptd.getProfessor();
		
		
		searched = false;	
		return "/ptd/buscar.xhtml";
	}
	
	public String editarPTD() {		
		String ret  = "";
		if (this.selectedPtd != null) {
			ret = "/ptd/editar.xhtml";
		}		
		
		searched = false;	
		return ret;
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
