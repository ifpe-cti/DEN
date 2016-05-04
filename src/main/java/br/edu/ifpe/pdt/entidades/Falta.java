package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.sql.Date;

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
@Table( name = "falta" )
public class Falta implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;

	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="professor_id", updatable=false)
	private Professor professor;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="disciplina_id")
	private Disciplina disciplina;
	
	@Column(name="data")
	private Date data;
	
	@Column(name="numeroFaltas")
	private Integer numeroFaltas;
	
	@Column(name="observacao")
	private String observacao;
	
	@Column(name="turma")
	private String turma;
	
	@Column(name="reposicao")
	private Date reposicao;
	
	@Column(name="numeroAulasRepostas")
	private Integer numeroAulasRepostas;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getNumeroFaltas() {
		return numeroFaltas;
	}

	public void setNumeroFaltas(Integer numeroFaltas) {
		this.numeroFaltas = numeroFaltas;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Date getReposicao() {
		return reposicao;
	}

	public void setReposicao(Date reposicao) {
		this.reposicao = reposicao;
	}

	public Integer getNumeroAulasRepostas() {
		return numeroAulasRepostas;
	}

	public void setNumeroAulasRepostas(Integer numeroAulasRepostas) {
		this.numeroAulasRepostas = numeroAulasRepostas;
	}	
}
