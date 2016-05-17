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
import javax.persistence.Table;

@Entity
@Table (name="semana")
public class Semana implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="numero", nullable=false)
	private Integer numero;
	
	@Column(name="numAulas", nullable=false)
	private Integer numAulas;
	
	@Column(name="conteudo", nullable=false)
	private String conteudo;
	
	@Column(name="estrategia", nullable=false)
	private String estrategia;
	
	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="planejamentoSemestral_id", updatable=false)
	private PlanejamentoSemestral planejamentoSemestral;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getNumAulas() {
		return numAulas;
	}

	public void setNumAulas(Integer numAulas) {
		this.numAulas = numAulas;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public PlanejamentoSemestral getPlanejamentoSemestral() {
		return planejamentoSemestral;
	}

	public void setPlanejamentoSemestral(PlanejamentoSemestral planejamentoSemestral) {
		this.planejamentoSemestral = planejamentoSemestral;
	}
}
