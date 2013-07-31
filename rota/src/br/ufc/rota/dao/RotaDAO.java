package br.ufc.rota.dao;

import java.util.List;

public interface RotaDAO {

	/**
	 * Busca todas as rotas de onibus
	 * @return
	 */
	List<Object[]> buscarTodasRotasOnibus();
	
	/**
	 * Busca o nome de todas as rotas de onibus 
	 * @return
	 */
	List<String> buscarNomeTodasRotas();
	
	/**
	 * Busca o objeto rota pelo codigo
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	List<Object[]> buscarRotaPeloCodigo(String codigo) throws Exception;
	
	/**
	 * Busca a LineString da rota pelo codigo
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	String buscarLineStringRotaPeloCodigo(String codigo) throws Exception;

	/**
	 * Busca a LineString da rota pelo codigo considerando o sentido
	 * @param codigoRota
	 * @param sentido
	 * @return
	 * @throws Exception
	 */
	String buscarLineStringRotaPeloCodigoESentido(String codigoRota, boolean sentido) throws Exception;

	/**
	 * Busca as rotas de onibus que passam naquela parada
	 * @param idA
	 * @return
	 */
	List<String> buscarRotasDaParada(Long idA);

	/**
	 * Busca os id's das paradas em comum as linhas que passam na origem e as linhas que passam no destino
	 * @param linhaA
	 * @param linhaB
	 * @return
	 */
	List<Integer> buscarParadasEmComum(String linhaA, String linhaB);

	/**
	 * Calcula a distancia da LineString entre os pontos origem e destino
	 * @param latA
	 * @param lngA
	 * @param latB
	 * @param lngB
	 * @param linha
	 * @return
	 * @throws Exception
	 */
	Double calcularCusto(Double latA, Double lngA, Double latB, Double lngB,
			String linha) throws Exception;

	/**
	 * Calcula a distancia da LineString entre os pontos origem e destino considerando o sentido
	 * @param latA
	 * @param lngA
	 * @param latB
	 * @param lngB
	 * @param linha
	 * @param sentido
	 * @return
	 * @throws Exception
	 */
	Double calcularCustoConsiderandoSentido(Double latA, Double lngA,
			Double latB, Double lngB, String linha, Boolean sentido) throws Exception;
	
	/**
	 * Busca um objeto rota/sentidos de uma parada
	 * @param idA
	 * @return
	 */
	List<Object[]> buscarRotasESentidosDaParada(Long idA);

	/**
	 * Busca a LineString entre os pontos origem e destino considerando o sentido
	 * @param latA
	 * @param lngA
	 * @param latB
	 * @param lngB
	 * @param linha
	 * @param sentido
	 * @return
	 * @throws Exception
	 */
	String selecionarIntervaloEntreParadasConsiderandoSentido(Double latA,
			Double lngA, Double latB, Double lngB, String linha, Boolean sentido)
			throws Exception;

	/**
	 * Busca a LineString entre os pontos origem e destino
	 * @param latA
	 * @param lngA
	 * @param latB
	 * @param lngB
	 * @param linha
	 * @return
	 * @throws Exception
	 */
	String selecionarIntervaloEntreParadas(Double latA, Double lngA,
			Double latB, Double lngB, String linha) throws Exception;

}
