package br.com.rota.service.impl;

import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.RotaDAO;
import br.ufc.rota.service.RotaService;

@Component
public class RotaServiceImpl implements RotaService{

	private RotaDAO rotaDAO;

	public RotaServiceImpl(RotaDAO rotaDAO) {
		this.rotaDAO = rotaDAO;
	}

	@Override
	public List<String> buscarNomeTodasRotas() {
		return rotaDAO.buscarNomeTodasRotas();
	}

	@Override
	public List<Object[]> buscarRotaPeloCodigo(String codigoRota) throws Exception {
		return rotaDAO.buscarRotaPeloCodigo(codigoRota);
	}

	@Override
	public String spliLineStringPorParada() throws Exception {
		return rotaDAO.spliLineStringPorParada();
	}

	@Override
	public String buscarLineStringRotaPeloCodigo(String codigoRota) throws Exception {
		return rotaDAO.buscarLineStringRotaPeloCodigo(codigoRota);
	}
	
	@Override
	public String buscarLineStringRotaPeloCodigoESentido(String codigoRota,	boolean sentido) throws Exception {
		return rotaDAO.buscarLineStringRotaPeloCodigoESentido(codigoRota, sentido);
	}


}
