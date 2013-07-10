package br.ufc.rota.bean;

import static br.com.caelum.vraptor.view.Results.json;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.ufc.rota.dao.ParadaOnibusDAO;
import br.ufc.rota.entity.ParadaOnibus;

@Resource
public class RotaController {

	private Result result;

	private ParadaOnibusDAO paradaOnibusDAO;
	
	private List<Object[]> paradasListObj;
	private List<ParadaOnibus> paradasList;
	
	public RotaController(Result result, ParadaOnibusDAO paradaOnibusDAO) {
		this.result = result;
		this.paradaOnibusDAO = paradaOnibusDAO;
	}

	@Path("/rota")
	public void index() {
		
	}
	
	public void buscarTodasParadas(){
		paradasListObj = paradaOnibusDAO.buscarTodasParadasOnibus();
		paradasList = new ArrayList<ParadaOnibus>();
		
		for(Object[] o : paradasListObj){
			Long id = ((Integer) o[0]).longValue();
			String localizacao = (String) o[1];
			String pertoDe = (String) o[2];
			String linhasParada = (String) o[3];
			String[] qtdLinhasStr = linhasParada.split("\\;");
			Integer qtdLinhas = qtdLinhasStr.length;
			ParadaOnibus parada = new ParadaOnibus(id, localizacao, pertoDe, qtdLinhas.toString(), linhasParada);
			paradasList.add(parada);
		}
		
		result.use(json()).from(paradasList, "paradas"). serialize();
	}
	
	public void buscarParadasAdjacentes(double latitude, double longitude){
		paradasListObj = paradaOnibusDAO.buscarParadasAdjacentes(latitude, longitude);
		paradasList = new ArrayList<ParadaOnibus>();
		
		for(Object[] o : paradasListObj){
			Long id = ((Integer) o[0]).longValue();
			String localizacao = (String) o[1];
			String pertoDe = (String) o[2];
			String linhasParada = (String) o[3];
			String[] qtdLinhasStr = linhasParada.split("\\;");
			Integer qtdLinhas = qtdLinhasStr.length;
			ParadaOnibus parada = new ParadaOnibus(id, localizacao, pertoDe, qtdLinhas.toString(), linhasParada);
			paradasList.add(parada);
		}
		
		result.use(json()).from(paradasList, "paradas"). serialize();
	}
	
	public static void main(String[] args) {
		String linhasParada = "06";
		String[] qtdLinhasStr = linhasParada.split("\\;");
		System.out.println(qtdLinhasStr.length);
	}

}
