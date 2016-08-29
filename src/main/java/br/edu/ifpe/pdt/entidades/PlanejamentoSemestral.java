package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


@Entity
@Table(name="planejamentoSemestral")
public class PlanejamentoSemestral implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public static enum STATUS_PS{
		AGUARDO,
		APROVADO,
		CORRECAO;
		
		public int getOrdinal() {
			return this.ordinal();
		}
		
		public String getNome() {
			return this.name();
		}
		
		public static STATUS_PS getStatus(Integer auto) {
			STATUS_PS a = null;
			
			switch (auto) {
			case 0:
				a = AGUARDO;
				break;
			case 1:
				a = APROVADO;
				break;
			case 2:
				a = CORRECAO;
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
	
	@Column
	private String competencias;
	
	@Column
	private STATUS_PS status;

	@OneToMany(mappedBy="planejamentoSemestral", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	@OrderBy ("numero DESC")
	private List<Semana> semanas;
	
	@OneToMany(mappedBy="planejamentoSemestral", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<Avaliacao> avaliacoes;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getCompetencias() {
		return competencias;
	}

	public void setCompetencias(String competencias) {
		this.competencias = competencias;
	}

	public List<Semana> getSemanas() {
		return semanas;
	}

	public void setSemanas(List<Semana> semanas) {
		this.semanas = semanas;
	}

	public List<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public STATUS_PS getStatus() {
		return status;
	}

	public void setStatus(STATUS_PS status) {
		this.status = status;
	}
}
