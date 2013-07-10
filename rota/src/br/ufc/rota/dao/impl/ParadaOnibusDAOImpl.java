package br.ufc.rota.dao.impl;

import java.util.List;

import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.ParadaOnibusDAO;

@Component
public class ParadaOnibusDAOImpl extends AbstractDAOImpl implements ParadaOnibusDAO {

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
	
}
