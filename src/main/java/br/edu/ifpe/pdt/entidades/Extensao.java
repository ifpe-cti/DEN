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
@Table( name = "extensao" )
public class Extensao {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="atividade", nullable=false, length=500)
	private String atividade;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ptd_id", updatable=false)
	private PTD ptd;

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

	public PTD getPTD() {
		return ptd;
	}

	public void setPTD(PTD ptd) {
		this.ptd = ptd;
	}
	
}
