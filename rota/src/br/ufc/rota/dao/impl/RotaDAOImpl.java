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
	@SuppressWarnings("unchecked")
	public List<Object[]> buscarRotasESentidosDaParada(Long idParada) {
		Query query = em.createNativeQuery("select pr.cod_route, r.sense_way " +
				"from point_routes pr, routes r " +
				"where " +
				"pr.cod_route = r.cod_route and " +
				"pr.point_id = " + idParada);
		
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> buscarRotasDaParada(Long idParada) {
		Query query = em.createNativeQuery("select pr.cod_route " +
				"from point_routes pr " +
				"where pr.point_id = "+ idParada);
		
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> buscarParadasEmComum(String linhaA, String linhaB) {
		Query query = em.createNativeQuery("select r1.point_id " +
				"from point_routes r1, point_routes r2 " +
				"where r1.cod_route = '"+linhaA+"' and " +
				"r2.cod_route = '"+linhaB+"' and " +
				"r1.point_id = r2.point_id");
		
		return query.getResultList();
	}

	@Override
	public Double calcularCusto(Double latA, Double lngA, Double latB, Double lngB, String linha) throws Exception{
		String sql = "select ST_Length(Geography(ST_Transform( ST_Line_Substring (" +
				"from_linha.linha,  " +
				" ST_Line_Locate_Point ( " +
				"from_linha.linha, " +
				" ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latA +","+ lngA +") ,4326))), " +
				"ST_Line_Locate_Point(" +
				"from_linha.linha, " +
				"ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latB +","+ lngB +") , 4326)))), 4326))) as custo " +
				"from (SELECT ST_LineMerge(" +
				"ST_Collect( " +
				"from_ida.geometry, " +
				"from_volta.geometry)) as linha " +
				"from ( select CAST(r.path AS geometry) as geometry from routes r " +
				"where r.cod_route like '" + linha + "' and sense_way is true) as from_ida, " +
				"( select CAST(r.path AS geometry) as geometry from routes r " +
				"where r.cod_route like '" + linha + "' and sense_way is false) as from_volta ) as from_linha";
		Query query = em.createNativeQuery(sql);
		
		return (Double) query.getSingleResult();
	}


	@Override
	public Double calcularCustoConsiderandoSentido(Double latA, Double lngA,
			Double latB, Double lngB, String linha, Boolean sentido) throws Exception{
		String sql = "select ST_Length(Geography(ST_Transform( ST_Line_Substring (" +
				"from_linha.linha,  " +
				" ST_Line_Locate_Point ( " +
				"from_linha.linha, " +
				" ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latA +","+ lngA +") ,4326))), " +
				"ST_Line_Locate_Point(" +
				"from_linha.linha, " +
				"ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latB +","+ lngB +") , 4326)))), 4326))) as custo " +
				"from (select CAST(r.path AS geometry) as linha from routes r "+
				"where r.cod_route like "+ "'" + linha + "'" + " and r.sense_way is " + sentido + ") as from_linha";
		Query query = em.createNativeQuery(sql);
		
		return (Double) query.getSingleResult();
	}
	
	@Override
	public String selecionarIntervaloEntreParadas(Double latA, Double lngA, Double latB, Double lngB, String linha) throws Exception{
		String sql = "select st_asgeojson(ST_Transform( ST_Line_Substring (" +
				"from_linha.linha,  " +
				" ST_Line_Locate_Point ( " +
				"from_linha.linha, " +
				" ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latA +","+ lngA +") ,4326))), " +
				"ST_Line_Locate_Point(" +
				"from_linha.linha, " +
				"ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latB +","+ lngB +") , 4326)))), 4326)) as custo " +
				"from (SELECT ST_LineMerge(" +
				"ST_Collect( " +
				"from_ida.geometry, " +
				"from_volta.geometry)) as linha " +
				"from ( select CAST(r.path AS geometry) as geometry from routes r " +
				"where r.cod_route like '" + linha + "' and sense_way is true) as from_ida, " +
				"( select CAST(r.path AS geometry) as geometry from routes r " +
				"where r.cod_route like '" + linha + "' and sense_way is false) as from_volta ) as from_linha";
		Query query = em.createNativeQuery(sql);
		
		return (String) query.getSingleResult();
	}


	@Override
	public String selecionarIntervaloEntreParadasConsiderandoSentido(Double latA, Double lngA,
			Double latB, Double lngB, String linha, Boolean sentido) throws Exception{
		String sql = "select st_asgeojson(ST_Transform( ST_Line_Substring (" +
				"from_linha.linha,  " +
				" ST_Line_Locate_Point ( " +
				"from_linha.linha, " +
				" ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latA +","+ lngA +") ,4326))), " +
				"ST_Line_Locate_Point(" +
				"from_linha.linha, " +
				"ST_ClosestPoint(from_linha.linha, ST_SetSRID(ST_MakePoint("+ latB +","+ lngB +") , 4326)))), 4326)) as custo " +
				"from (select CAST(r.path AS geometry) as linha from routes r "+
				"where r.cod_route like "+ "'" + linha + "'" + " and r.sense_way is " + sentido + ") as from_linha";
		Query query = em.createNativeQuery(sql);
		
		return (String) query.getSingleResult();
	}


}
