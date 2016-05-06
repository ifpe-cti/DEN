package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.sql.Date;

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

	public static enum FALTA_STATUS{
		CRIADA,
		ATA_ENTREGUE,
		REPOSTA,
		NAO_REPOSTA;
		
		public int getOrdinal() {
			return this.ordinal();
		}
		
		public String getNome() {
			return this.name();
		}
		
		public static FALTA_STATUS getFaltaStatus(Integer auto) {
			FALTA_STATUS a = null;
			
			switch (auto) {
			case 0:
				a = CRIADA;
				break;
			case 1:
				a = ATA_ENTREGUE;
				break;
			case 2:
				a = REPOSTA;
				break;
			case 3:
				a = NAO_REPOSTA;
				break;			
			default:
				break;
			}
			
			return a;
		}
	};
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;

	@ManyToOne 
	@JoinColumn(name="professor_id", updatable=false)
	private Professor professor;
	
	@OneToOne 
	@JoinColumn(name="disciplina_id")
	private Disciplina disciplina;
	
	@Column(name="data")
	private Date data;
	
	@Column(name="numeroFaltas")
	private Integer numeroFaltas;
	
	@Column(name="observacao")
	private String observacao;

	@Column(name="reposicao")
	private Date reposicao;
	
	@Column(name="numeroAulasRepostas")
	private Integer numeroAulasRepostas;
	
	@Column(name="faltaStatus")
	private FALTA_STATUS faltaStatus;

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

	public FALTA_STATUS getFaltaStatus() {
		return faltaStatus;
	}

	public void setFaltaStatus(FALTA_STATUS faltaStatus) {
		this.faltaStatus = faltaStatus;
	}		
}
