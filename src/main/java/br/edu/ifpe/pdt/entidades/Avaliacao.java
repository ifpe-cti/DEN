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
import javax.persistence.Table;

@Entity
@Table(name="avaliacao")
public class Avaliacao implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="atividade", nullable=false)
	private String atividade;
	
	@Column(name="unidade", nullable=false)
	private Integer unidade;
	
	@Column(name="dataProva", nullable=false)
	private Date dataProva;
	
	@Column(name="dataRecuperacao", nullable=false)
	private Date dataRecuperacao;
	
	@ManyToOne (cascade=CascadeType.MERGE)
	@JoinColumn(name="planejamentoSemestral_id", updatable=false)
	private PlanejamentoSemestral planejamentoSemestral;

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

	public Integer getUnidade() {
		return unidade;
	}

	public void setUnidade(Integer unidade) {
		this.unidade = unidade;
	}

	public Date getDataProva() {
		return dataProva;
	}

	public void setDataProva(Date dataProva) {
		this.dataProva = dataProva;
	}

	public Date getDataRecuperacao() {
		return dataRecuperacao;
	}

	public void setDataRecuperacao(Date dataRecuperacao) {
		this.dataRecuperacao = dataRecuperacao;
	}

	public PlanejamentoSemestral getPlanejamentoSemestral() {
		return planejamentoSemestral;
	}

	public void setPlanejamentoSemestral(PlanejamentoSemestral planejamentoSemestral) {
		this.planejamentoSemestral = planejamentoSemestral;
	}
	
	@Override
	public int hashCode() {
		return this.codigo;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if (obj != null && ((Avaliacao)obj).getDataProva() != null) {
			ret = ((Avaliacao)obj).getDataProva().equals(this.getDataProva());
			if (ret && ((Avaliacao)obj).getAtividade() != null) {
				ret = ((Avaliacao)obj).getAtividade().equals(this.getAtividade());
			}
		}
		
		return ret;
	}
}
