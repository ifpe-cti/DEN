package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "disciplina" )
public class Disciplina implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="nome", nullable=false, length=200)
	private String nome;
	
	@Column(name="curso", nullable=false, length=10)
	private String curso;

	@Column(name="turma", nullable=false, length=100)
	private String turma;
	
	@Column(name="carga_horaria")
	private Integer cargaHoraria;
	
	@ManyToOne (cascade=CascadeType.MERGE)
	@JoinColumn(name="ptd_id", updatable=false)
	private PTD ptd;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="planejamento_semestral_id")
	private PlanejamentoSemestral planejamentoSemestral;

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
	
	public PTD getPtd() {
		return ptd;
	}

	public void setPtd(PTD ptd) {
		this.ptd = ptd;
	}

	@Override
	public String toString() {
		return this.nome + " - " + this.turma;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public PlanejamentoSemestral getPlanejamentoSemestral() {
		return planejamentoSemestral;
	}

	public void setPlanejamentoSemestral(PlanejamentoSemestral planejamentoSemestral) {
		this.planejamentoSemestral = planejamentoSemestral;
	}
}
