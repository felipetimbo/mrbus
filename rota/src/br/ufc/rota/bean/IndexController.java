package br.ufc.rota.bean;

import java.util.List;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.ufc.rota.dao.ParadaOnibusDAO;

@Resource
public class IndexController {

	private Result result;

	private ParadaOnibusDAO paradaOnibusDAO;
	
	public IndexController(Result result, ParadaOnibusDAO paradaOnibusDAO) {
		this.result = result;
		this.paradaOnibusDAO = paradaOnibusDAO;
	}

	@Path("/")
	public void index() {
		List<Object> objetos = paradaOnibusDAO.buscarTodasParadasOnibus();
		System.out.println(objetos.get(0).toString());
		result.include("variable", "VRaptor!");
	}

}
