package br.ufc.rota.dao;

import java.util.List;

public interface ParadaOnibusDAO{

	List<Object[]> buscarTodasParadasOnibus();
	
	List<Object[]> buscarParadasAdjacentes(double lat, double lng);
}
