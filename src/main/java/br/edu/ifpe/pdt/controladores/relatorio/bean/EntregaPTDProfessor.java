package br.edu.ifpe.pdt.controladores.relatorio.bean;

public class EntregaPTDProfessor {

	private String siape;
	private String nome;
	private String situacao;

	public EntregaPTDProfessor(String siape, String nome, String situacao) {
		super();
		this.siape = siape;
		this.nome = nome;
		this.situacao = situacao;
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
