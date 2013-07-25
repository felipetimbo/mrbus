package br.ufc.rota.entity;

public class Rota {

	private String codigo;
	private String nome;
	private String rota;
	private String terminais;

	public Rota(){ }

	public Rota(String codigo, String nome, String rota, String terminais) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.rota = rota;
		this.terminais = terminais;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRota() {
		return rota;
	}

	public void setRota(String rota) {
		this.rota = rota;
	}

	public String getTerminais() {
		return terminais;
	}

	public void setTerminais(String terminais) {
		this.terminais = terminais;
	}
	
	
}
