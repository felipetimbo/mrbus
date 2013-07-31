package br.ufc.rota.bean;

import static br.com.caelum.vraptor.view.Results.json;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.ufc.rota.entity.Parada;
import br.ufc.rota.entity.Rota;
import br.ufc.rota.entity.vo.MelhorRotaVO;
import br.ufc.rota.service.ParadaService;
import br.ufc.rota.service.RotaService;

@Resource
public class RotaController {

	private Result result;

	private ParadaService paradaService;
	private RotaService rotaService;
	
	private List<Parada> paradasList;
	private List<Rota> rotasList;
	
	private List<String> nomesTodasRotas;
	
	public RotaController(Result result, ParadaService paradaService, RotaService rotaService) {
		this.result = result;
		this.paradaService = paradaService;
		this.rotaService = rotaService;
	}

	@Path("/rota")
	public void index() {
		
	}
	
	/**
	 * Busca o nome de todas as rotas para preencher o combo de rotas
	 */
	public void buscarNomeTodasRotas(){
		nomesTodasRotas = rotaService.buscarNomeTodasRotas();
		
		result.use(json()).from(nomesTodasRotas, "nomesTodasRotas").serialize();
	}
	
	/**
	 * Busca a rota selecionada pelo usuario para ser desenhada no mapa
	 * @param nomeDaRota
	 * @throws Exception
	 */
	public void exibirRota(String nomeDaRota) throws Exception{
		Rota rotaSelecionada = new Rota();
		String codigoRota = nomeDaRota.split(" ")[0];
			
		List<Object[]> rotasListObjList = rotaService.buscarRotaPeloCodigo(codigoRota);
			
		String codigo = (String) rotasListObjList.get(0)[0];
		String nome = (String)  rotasListObjList.get(0)[1];
		String terminais = (String)  rotasListObjList.get(0)[2];
		
		/**
		 *  Se ha mais de uma rota, entao 
		 *  a rota selecionada sera a rota completa, em ambos os sentidos
		 *  c.c. rota selecionada sera a rota cujo sentido existe
		 */
		if(rotasListObjList.size() > 1){
			String rotaStr = rotaService.buscarLineStringRotaPeloCodigo(codigoRota);
			rotaSelecionada = new Rota(codigo, nome, rotaStr, terminais, 0.0);
		}else{
			boolean sentido =  (boolean) rotasListObjList.get(0)[3];
			String rotaStr = rotaService.buscarLineStringRotaPeloCodigoESentido(codigoRota, sentido);
			rotaSelecionada = new Rota(codigo, nome, rotaStr, terminais, 0.0);
		}
		
		result.use(json()).from(rotaSelecionada, "rotaSelecionada").serialize();
		
	}
	
	/**
	 * Busca todas as paradas de onibus contidas no banco
	 */
	public void buscarTodasParadas(){
		List<Object[]> paradasListObj = paradaService.buscarTodasParadasOnibus();
		paradasList = new ArrayList<Parada>();
		
		for(Object[] o : paradasListObj){
			Long id = ((Integer) o[0]).longValue();
			String localizacao = (String) o[1];
			String pertoDe = (String) o[2];
			String linhasParada = (String) o[3];
			String[] qtdLinhasStr = linhasParada.split("\\;");
			Integer qtdLinhas = qtdLinhasStr.length;
			Parada parada = new Parada(id, localizacao, pertoDe, qtdLinhas.toString(), linhasParada);
			paradasList.add(parada);
		}
		
		result.use(json()).from(paradasList, "paradas").serialize();
	}
	
	/**
	 * Faz a busca pelas paradas adjacentes ao ponto clicado no mapa
	 * @param latitude
	 * @param longitude
	 */
	public void buscarParadasAdjacentes(double latitude, double longitude){
		List<Object[]> paradasListObj = paradaService.buscarParadasAdjacentes(latitude, longitude);
		paradasList = new ArrayList<Parada>();
		
		for(Object[] o : paradasListObj){
			Long id = ((Integer) o[0]).longValue();
			String localizacao = (String) o[1];
			String pertoDe = (String) o[2];
			String linhasParada = (String) o[3];
			String[] qtdLinhasStr = linhasParada.split("\\;");
			Integer qtdLinhas = qtdLinhasStr.length;
			Parada parada = new Parada(id, localizacao, pertoDe, qtdLinhas.toString(), linhasParada);
			paradasList.add(parada);
		}
		
		result.use(json()).from(paradasList, "paradas").serialize();
	}

	/**
	 * Busca a melhor rota de Onibus a partir das 2 paradas escolhidas pelo usuario.
	 * @param idA
	 * @param latA
	 * @param lngA
	 * @param idB
	 * @param latB
	 * @param lngB
	 */
	public void buscarMelhorRotaOnibus(String idA, Double latA, Double lngA, String idB, Double latB, Double lngB){
		List<MelhorRotaVO> melhoresRotas = new ArrayList<MelhorRotaVO>();

		try {
			melhoresRotas = rotaService.calcularRota(Long.parseLong(idA), latA, lngA, Long.parseLong(idB), latB, lngB);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			result.nothing();
		} catch (Exception e) {
			e.printStackTrace();
			result.nothing();
		}
		
		result.use(json()).from(melhoresRotas, "melhoresRotas").include("rota").include("origem").include("destino").serialize();
	}
	
}
