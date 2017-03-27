package br.edu.ifpe.pdt.controladores.relatorio.bean;

public class EntregaPTDProfessor {

	private Integer codigo;
	private String siape;
	private String nome;
	private String situacao;

	public EntregaPTDProfessor(Integer codigo, String siape, String nome, String situacao) {
		super();
		this.codigo = codigo;
		this.siape = siape;
		this.nome = nome;
		this.situacao = situacao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}
