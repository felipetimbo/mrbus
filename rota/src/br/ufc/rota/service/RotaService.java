package br.ufc.rota.service;

import java.util.List;

public interface RotaService {

	List<String> buscarNomeTodasRotas();

	List<Object[]> buscarRotaPeloCodigo(String codigoRota) throws Exception;

	String spliLineStringPorParada() throws Exception;

	String buscarLineStringRotaPeloCodigo(String codigoRota) throws Exception;
	
	String buscarLineStringRotaPeloCodigoESentido(String codigoRota, boolean sentido) throws Exception;

}
