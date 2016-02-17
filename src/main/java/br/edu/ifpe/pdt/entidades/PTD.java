package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/*
 * Plano de Trabalho Docente
 */
@ManagedBean(name = "ptd")
@RequestScoped
@Entity
@Table( name = "ptd" )
public class PTD implements Serializable {

	public static enum STATUS{
		CRIADO,
		AGUARDO,
		HOMOLOGADO,
		APROVADO,
		CORRECAO,
		FECHADO
	};
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="status")
	private byte status;
	
	@Column(name="last_update")
	private Date lastUpdate;
	
	@ManagedProperty(value="#{professor}")
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="professor_id", updatable=false)
	private Professor professor;
	

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}	
}
