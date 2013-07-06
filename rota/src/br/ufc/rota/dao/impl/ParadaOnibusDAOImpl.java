package br.ufc.rota.dao.impl;

import java.util.List;

import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.ParadaOnibusDAO;

@Component
public class ParadaOnibusDAOImpl extends AbstractDAOImpl implements ParadaOnibusDAO {

	@SuppressWarnings("unchecked")
	public List<Object> buscarTodasParadasOnibus(){
		Query query = em.createQuery("select p.id, p.cod_point, astext(p.coord_desc), p.next_to, p.route_point from ParadaOnibus p");
		
		return query.getResultList();
	}
	
}
