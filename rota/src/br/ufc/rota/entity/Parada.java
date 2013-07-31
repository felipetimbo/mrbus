package br.ufc.rota.entity;

public class Parada {

	private Long id;
	private String localizacao;
	private String pertoDe;
	private String qtdLinhas;
	private String linhasParada;

	public Parada(){}
	
	public Parada(Long id, String localizacao, String pertoDe,
			String qtdLinhas, String linhasParada) {
		super();
		this.id = id;
		this.localizacao = localizacao;
		this.pertoDe = pertoDe;
		this.qtdLinhas = qtdLinhas;
		this.linhasParada = linhasParada;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getPertoDe() {
		return pertoDe;
	}

	public void setPertoDe(String pertoDe) {
		this.pertoDe = pertoDe;
	}

	public String getQtdLinhas() {
		return qtdLinhas;
	}

	public void setQtdLinhas(String qtdLinhas) {
		this.qtdLinhas = qtdLinhas;
	}

	public String getLinhasParada() {
		return linhasParada;
	}

	public void setLinhasParada(String linhasParada) {
		this.linhasParada = linhasParada;
	}
	
}
