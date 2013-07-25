package br.ufc.rota.dao;

import java.util.List;

public interface ParadaDAO{

	List<Object[]> buscarTodasParadasOnibus();
	
	List<Object[]> buscarParadasAdjacentes(double lat, double lng);
}
