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
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;

@Component
@ManagedBean(name="professorControlador", eager=true)
@SessionScoped
public class ProfessorControlador implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private List<Professor> professores = new ArrayList<Professor>();
	
	private Professor professorLogado;
	
	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
	public ProfessorControlador() {	
	}
	
	public String redirectCadastro() {		
		return "/professor/cadastro.xhtml";
	}
	
	public String adicionaProfessor(String siape, String senha, String coordenacao,
			String nome, String email) {
		Professor p = new Professor();
		p.setSiape(siape);
		p.setSenha(senha);
		p.setCoordenacao(coordenacao);
		p.setNome(nome);
		p.setEmail(email);
		p.setGestao(false);
		this.professorLogado = professorRepositorio.saveAndFlush(p);		
		
		return "/login.xhtml";
	}
	
	public String buscarProfessorEnsinoPorSIAPE(String siape) {		
		this.professorLogado = professorRepositorio.findBySiape(siape);
		if (this.professorLogado == null) {
			this.professorLogado = new Professor();
		}
		
		return "/restrito/professor/ensino/buscar.xhtml"; 
	}
	
	public String realizarLogin(String siape, String senha) {
		String ret = "";
		Professor prof = null;
		
		prof = this.professorRepositorio.findBySiape(siape);
		
		if (prof != null) {
			if (prof.getSenha().equals(senha)) {
				FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("professorLogado", prof);
				this.professorLogado = prof;
				ret = "index.xhtml";
			} else {
				prof = null;	
			}
		} 

		return ret;
	}
	
	public String realizarLogout() {
		
		FacesContext.getCurrentInstance().
		getExternalContext().invalidateSession();
		this.professorLogado = null;
		
		return "login.xhtml";
	}
// Getters from bean	
	public List<Professor> getProfessores() {
		return professores;
	}

	public Professor getProfessorLogado() {
		this.professorLogado = (Professor) FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("professorLogado");
		return professorLogado;
	}
}
