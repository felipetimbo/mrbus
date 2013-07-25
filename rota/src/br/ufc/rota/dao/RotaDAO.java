package br.ufc.rota.dao;

import java.util.List;

public interface RotaDAO {

	List<Object[]> buscarTodasRotasOnibus();
	
	List<String> buscarNomeTodasRotas();
	
	List<Object[]> buscarRotaPeloCodigo(String codigo) throws Exception;
	
	String buscarLineStringRotaPeloCodigo(String codigo) throws Exception;

	String buscarLineStringRotaPeloCodigoESentido(String codigoRota, boolean sentido) throws Exception;

	String spliLineStringPorParada() throws Exception;
	
}
