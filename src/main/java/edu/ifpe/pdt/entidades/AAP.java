package edu.ifpe.pdt.entidades;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 * Atividades Administrativas e Pedagógicas - AAP
 */
@ManagedBean(name = "aap")
@RequestScoped
@Entity
@Table( name = "aap" )
public class AAP {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="atividade", nullable=false, length=200)
	private String atividade;
	
	@Column(name="portaria", nullable=false, length=50)
	private String portaria;
	
	@ManyToOne
	@JoinColumn(name="professor_id", updatable=false)
	private Professor professor;
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getAtividade() {
		return atividade;
	}
	
	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}
	
	public String getPortaria() {
		return portaria;
	}
	
	public void setPortaria(String portaria) {
		this.portaria = portaria;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
}
