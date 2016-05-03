package br.edu.ifpe.pdt.entidades;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "disciplina" )
public class Disciplina {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="nome", nullable=false, length=100)
	private String nome;
	
	@Column(name="curso", nullable=false, length=10)
	private String curso;
	
	@Column(name="carga_horaria")
	private Integer cargaHoraria;
	
	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="ptd_id", updatable=false)
	private PTD ptd;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public PTD getPTD() {
		return ptd;
	}

	public void setPTD(PTD ptd) {
		this.ptd = ptd;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
}
