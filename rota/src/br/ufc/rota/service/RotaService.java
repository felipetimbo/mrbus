package br.ufc.rota.service;

import java.util.List;

import br.ufc.rota.entity.vo.MelhorRotaVO;

public interface RotaService {

	/**
	 * Busca o nome de todas as rotas de onibus na base de dados
	 * @return Lista de String com o nome das rotas
	 */
	List<String> buscarNomeTodasRotas();

	/**
	 * Busca uma rota de onibus pelo codigo da rota
	 * @param codigoRota
	 * @return retorna uma lista de objetos rota, no sentido ida e volta.
	 * 			O objeto rota e composto por:
	 * 			[0]: codigo da rota
	 * 			[1]: nome da rota
	 * 			[2]: terminais de onibus que a rota trafega
	 * 			[3]: sentido da rota
	 * @throws Exception
	 */
	List<Object[]> buscarRotaPeloCodigo(String codigoRota) throws Exception;

	/**
	 * Busca a LineString da rota para ser exibida no mapa 
	 * @param codigoRota
	 * @return
	 * @throws Exception
	 */
	String buscarLineStringRotaPeloCodigo(String codigoRota) throws Exception;
	
	/**
	 * Busca a LineString da rota para ser exibida no mapa considerando o sentido
	 * @param codigoRota
	 * @param sentido
	 * @return
	 * @throws Exception
	 */
	String buscarLineStringRotaPeloCodigoESentido(String codigoRota, boolean sentido) throws Exception;

	/**
	 * Buscar a melhor rota de onibus entre dois pontos
	 * @param idA
	 * @param latA
	 * @param lngA
	 * @param idB
	 * @param latB
	 * @param lngB
	 * @return
	 * @throws Exception
	 */
	List<MelhorRotaVO> calcularRota(Long idA, Double latA, Double lngA, Long idB,
			Double latB, Double lngB) throws Exception;
	
}
