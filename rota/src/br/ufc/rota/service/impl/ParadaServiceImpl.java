package br.ufc.rota.service.impl;

import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.ParadaDAO;
import br.ufc.rota.service.ParadaService;

@Component
public class ParadaServiceImpl implements ParadaService{

	private ParadaDAO paradaDAO;
	
	public ParadaServiceImpl(ParadaDAO paradaDAO) {
		this.paradaDAO = paradaDAO;
	}

	@Override
	public List<Object[]> buscarTodasParadasOnibus() {
		return paradaDAO.buscarTodasParadasOnibus();
	}

	@Override
	public List<Object[]> buscarParadasAdjacentes(double latitude,	double longitude) {
		return paradaDAO.buscarParadasAdjacentes(latitude, longitude);
	}

}
