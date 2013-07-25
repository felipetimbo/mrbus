package br.ufc.rota.service;

import java.util.List;

public interface ParadaService {

	List<Object[]> buscarTodasParadasOnibus();

	List<Object[]> buscarParadasAdjacentes(double latitude, double longitude);

}
