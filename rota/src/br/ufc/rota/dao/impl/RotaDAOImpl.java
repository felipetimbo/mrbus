package br.ufc.rota.dao.impl;

import java.util.List;

import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.RotaDAO;

@Component
public class RotaDAOImpl  extends AbstractDAOImpl implements RotaDAO{

	@SuppressWarnings("unchecked")
	public List<Object[]> buscarTodasRotasOnibus(){
		Query query = em.createNativeQuery("select r.id, r.name_route, st_asgeojson(r.path), r.sense_way, r.station from routes r");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarNomeTodasRotas() {
		Query query = em.createNativeQuery("select distinct r.name_route from routes r order by r.name_route");
		
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> buscarRotaPeloCodigo(String codigo) throws Exception {
		Query query = em.createNativeQuery("select r.cod_route, r.name_route, r.station, r.sense_way from routes r " +
				"where r.cod_route like "+ "'" + codigo + "'");
		
		return query.getResultList();
	}
	
	@Override
	public String buscarLineStringRotaPeloCodigo(String codigo) throws Exception {
		Query query = em.createNativeQuery("select st_asgeojson(" +
				"ST_LineMerge(" +
				"ST_Collect(" +
				"from_ida.geometry,from_volta.geometry)))" +
				"from ( select CAST(r.path AS geometry) as geometry from routes r where r.cod_route like " + "'" + codigo + "'" + " and sense_way is true) as from_ida, " +
						"( select CAST(r.path AS geometry) as geometry from routes r where r.cod_route like " + "'" + codigo + "'" + " and sense_way is false) as from_volta");
		
		return (String) query.getSingleResult();
	}

	@Override
	public String buscarLineStringRotaPeloCodigoESentido(String codigo, boolean sentido) throws Exception {
		Query query = em.createNativeQuery("select st_asgeojson(r.path) from routes r "+
				"where r.cod_route like "+ "'" + codigo + "'" + " and r.sense_way is " + sentido);
		
		return (String) query.getSingleResult();
	}
	
	@Override
	public String spliLineStringPorParada() throws Exception {
		Query query = em.createNativeQuery("select st_asgeojson(ST_Line_Substring ( from_linha.linha, ST_Line_Locate_Point " +
				"( from_linha.linha, ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint(-3.739305,-38.572397) ,4326)))," +
				" ST_Line_Locate_Point( from_linha.linha, ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint(-3.742517,-38.574977) ,4326))))) " +
				"from (SELECT ST_LineMerge ( ST_Collect( from_ida.geometry, from_volta.geometry )) as linha from ( select CAST(r.path AS geometry) as geometry " +
				"from routes r where r.cod_route like '020' and sense_way is true) as from_ida, ( select CAST(r.path AS geometry) as geometry from routes r " +
				"where r.cod_route like '020' and sense_way is false) as from_volta ) as from_linha");
		
		return (String) query.getSingleResult();
	}
	
}
