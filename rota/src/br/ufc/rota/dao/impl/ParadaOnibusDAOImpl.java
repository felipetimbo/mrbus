package br.ufc.rota.dao.impl;

import java.util.List;

import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.ParadaDAO;

@Component
public class ParadaOnibusDAOImpl extends AbstractDAOImpl implements ParadaDAO {

	@SuppressWarnings("unchecked")
	public List<Object[]> buscarTodasParadasOnibus(){
		Query query = em.createNativeQuery("select p.id, st_asgeojson(p.coord_desc), p.next_to " +
				"from point_stops p where p.id > 1 and p.id < 500");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> buscarParadasAdjacentes(double lat, double lng) {
		Query query = em.createNativeQuery("select p.id, st_asgeojson(p.coord_desc), p.next_to, p.route_point " +
				"from point_stops p where " +
				"(st_distance(ST_SetSRID(ST_MakePoint(" + lat +", " + lng + ") ,4326), " +
				"CAST(p.coord_desc AS geometry)) < 0.003)");
		
		return query.getResultList();
	}

	@Override
	public Object[] buscarLatLngParada(Integer parada) {
		Query query = em.createNativeQuery("SELECT ST_X(CAST(p.coord_desc AS geometry)), ST_Y(CAST(p.coord_desc AS geometry))" +
									" FROM point_stops p WHERE p.id = " + parada);
		
		return (Object[]) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> buscarParadaPeloId(Long idParada) {
		Query query = em.createNativeQuery("select p.id, st_asgeojson(p.coord_desc), p.next_to, p.route_point " +
				"from point_stops p where p.id = " + idParada);
		
		return query.getResultList();
	}
	
}
