package br.ufc.rota.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.atrion.astar.AStar;
import org.atrion.astar.Path;
import org.atrion.graph.Edge;
import org.atrion.graph.Graph;
import org.atrion.graph.Node;
import org.atrion.graph.io.GraphReader;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.ParadaDAO;
import br.ufc.rota.dao.RotaDAO;
import br.ufc.rota.entity.Parada;
import br.ufc.rota.entity.Rota;
import br.ufc.rota.entity.graph.Aresta;
import br.ufc.rota.entity.graph.Noh;
import br.ufc.rota.entity.vo.MelhorRotaVO;
import br.ufc.rota.entity.vo.RotaSentidoVO;
import br.ufc.rota.service.RotaService;

@Component
public class RotaServiceImpl implements RotaService{

	private RotaDAO rotaDAO;
	private ParadaDAO paradaDAO;
	private PrintWriter pwArestas;
	private PrintWriter pwNohs;
	
	private List<Noh> nohList;
	private List<Aresta> arestaList;
	private Long nNohs = 1L;
	private Long nArestas = 1L;
	
	Locale bLocale = new Locale.Builder().setLanguage("en").setRegion("US").build();
	NumberFormat nf = NumberFormat.getNumberInstance(bLocale);
	DecimalFormat df = (DecimalFormat)nf;
	
	
	public RotaServiceImpl(RotaDAO rotaDAO, ParadaDAO paradaDAO) {
		this.rotaDAO = rotaDAO;
		this.paradaDAO = paradaDAO;
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
	public String buscarLineStringRotaPeloCodigo(String codigoRota) throws Exception {
		return rotaDAO.buscarLineStringRotaPeloCodigo(codigoRota);
	}
	
	@Override
	public String buscarLineStringRotaPeloCodigoESentido(String codigoRota,	boolean sentido) throws Exception {
		return rotaDAO.buscarLineStringRotaPeloCodigoESentido(codigoRota, sentido);
	}

	@Override
	public List<MelhorRotaVO> calcularRota(Long idA, Double latA, Double lngA, 
			Long idB, Double latB,	Double lngB) throws Exception {
		
		df.applyPattern("#.#");
		
		List<MelhorRotaVO> melhoresRotasList = new ArrayList<MelhorRotaVO>();
		
		pwNohs = new PrintWriter (new FileOutputStream("nohs" + ".txt",false),false);  
		pwArestas = new PrintWriter (new FileOutputStream("arestas" + ".txt",false),false);  
		
		nNohs = 1L; nArestas = 1L;
		nohList = new ArrayList<Noh>();
		arestaList = new ArrayList<Aresta>();
		
		Noh nohA = new Noh(idA, nNohs++, latA, lngA);
		nohList.add(nohA);
		
		Noh nohB = new Noh(idB, nNohs++, latB, lngB);
		nohList.add(nohB);
		
		List<Object[]> rotasESentidosAObj = rotaDAO.buscarRotasESentidosDaParada(idA);
		List<Object[]> rotasESentidosBObj = rotaDAO.buscarRotasESentidosDaParada(idB);
		
		List<RotaSentidoVO> linhasQuePassamEmA = new ArrayList<RotaSentidoVO>();
		List<RotaSentidoVO> linhasQuePassamEmB = new ArrayList<RotaSentidoVO>();
		
		obterLinhasQuePassamNoPonto(rotasESentidosAObj, rotasESentidosBObj, linhasQuePassamEmA,
				linhasQuePassamEmB);
		
		List<RotaSentidoVO> linhasQuePassamEmAeB = new ArrayList<RotaSentidoVO>();
		
		for(RotaSentidoVO linhaA : linhasQuePassamEmA){
			for(RotaSentidoVO linhaB : linhasQuePassamEmB){
				if(linhaA.getCodigoRota().equals(linhaB.getCodigoRota())){
					linhasQuePassamEmAeB.add(linhaA);
				}
			}
		}
		
		if(linhasQuePassamEmAeB.size() > 0){
			
			for(RotaSentidoVO linhaQuePassaEmAeBStr : linhasQuePassamEmAeB){
				
				calculoCusto(idA, latA, lngA, idB, latB, lngB, linhaQuePassaEmAeBStr.getCodigoRota(), linhaQuePassaEmAeBStr.getSentidos(), true);
				
				RotaSentidoVO linhaAQueSeraRemovida = new RotaSentidoVO();
				RotaSentidoVO linhaBQueSeraRemovida = new RotaSentidoVO();
				
				for(RotaSentidoVO linhaA : linhasQuePassamEmA){
					if(linhaA.getCodigoRota().equals(linhaQuePassaEmAeBStr.getCodigoRota())){
						linhaAQueSeraRemovida = linhaA;
						break;
					}
				}
				
				for(RotaSentidoVO linhaB : linhasQuePassamEmB){
					if(linhaB.getCodigoRota().equals(linhaQuePassaEmAeBStr.getCodigoRota())){
						linhaBQueSeraRemovida = linhaB;
						break;
					}
				}
				
				if(linhaAQueSeraRemovida.getCodigoRota() != null && !linhaAQueSeraRemovida.getCodigoRota().isEmpty()){
					linhasQuePassamEmA.remove(linhaAQueSeraRemovida);
				}
				
				if(linhaBQueSeraRemovida.getCodigoRota() != null && !linhaBQueSeraRemovida.getCodigoRota().isEmpty()){
					linhasQuePassamEmB.remove(linhaBQueSeraRemovida);
				}
			}
		}
		
		for(RotaSentidoVO linhaA : linhasQuePassamEmA){
			for(RotaSentidoVO linhaB : linhasQuePassamEmB){
				List<Integer> paradasEmComumRetornadas = rotaDAO.buscarParadasEmComum(linhaA.getCodigoRota(), linhaB.getCodigoRota());
				
				//se entre a linha A e B existe parada em comum, eu adiciono na lista de paradas em comum 
				if(paradasEmComumRetornadas != null && paradasEmComumRetornadas.size() > 0){
					for(Integer paradaEmComum : paradasEmComumRetornadas){
						
						Object[] latlng = paradaDAO.buscarLatLngParada(paradaEmComum);
						Double latParadaEmComum = (Double) latlng[0];
						Double lngParadaEmComum = (Double) latlng[1];
						
						calculoCusto(idA, latA, lngA, paradaEmComum.longValue(), latParadaEmComum, lngParadaEmComum, 
								linhaA.getCodigoRota(), linhaA.getSentidos(), false);
						calculoCusto(paradaEmComum.longValue(), latParadaEmComum, lngParadaEmComum, idB, latB, lngB, 
								linhaB.getCodigoRota(), linhaB.getSentidos(), true);
						
					}
				}
			}
		}
		
		for(Noh noh : nohList){
			pwNohs.print(noh.getCodigoGrafo() + "," + noh.getLatitude() + "," + noh.getLongitude() + "\r\n");
		}
		
		for(Aresta aresta : arestaList){
			pwArestas.print(aresta.getPontoA() + "," + aresta.getPontoB() + "," + aresta.getCusto() + "\r\n");
		}
		
		pwArestas.close();
		pwNohs.close();
		
		List<Noh> paradasMelhorRota = new ArrayList<Noh>();
		
		List<Node> nohsMelhorRota = encontrarMelhorRota();
		for(Node nohAstar : nohsMelhorRota){
			Long nohAstarLong = Long.valueOf (nohAstar.getId());
			for(Noh noh : nohList){
				if(noh.getCodigoGrafo().equals(nohAstarLong)){
					paradasMelhorRota.add(noh);
				}
			}
		}
		
		for(int i=0; i < paradasMelhorRota.size() - 1 ; i++){
			Long pontoAGrafo = paradasMelhorRota.get(i).getCodigoGrafo();
			Long pontoBGrafo = paradasMelhorRota.get(i+1).getCodigoGrafo();
			
			for(Aresta aresta : arestaList){
				if(aresta.getPontoA().equals(Long.valueOf(pontoAGrafo)) && aresta.getPontoB().equals(Long.valueOf(pontoBGrafo))){
					
					String melhorRotaStr = "";
					
					List<Object[]> paradaAListObj = paradaDAO.buscarParadaPeloId(paradasMelhorRota.get(i).getCodigoParada());
					List<Object[]> paradaBListObj = paradaDAO.buscarParadaPeloId(paradasMelhorRota.get(i+1).getCodigoParada());
					
					Parada paradaA = new Parada();
					Parada paradaB = new Parada();
					
					for(Object[] o : paradaAListObj){
						Long id = ((Integer) o[0]).longValue();
						String localizacao = (String) o[1];
						String pertoDe = (String) o[2];
						String linhasParada = (String) o[3];
						String[] qtdLinhasStr = linhasParada.split("\\;");
						Integer qtdLinhas = qtdLinhasStr.length;
						paradaA = new Parada(id, localizacao, pertoDe, qtdLinhas.toString(), linhasParada);
					}
					
					for(Object[] o : paradaBListObj){
						Long id = ((Integer) o[0]).longValue();
						String localizacao = (String) o[1];
						String pertoDe = (String) o[2];
						String linhasParada = (String) o[3];
						String[] qtdLinhasStr = linhasParada.split("\\;");
						Integer qtdLinhas = qtdLinhasStr.length;
						paradaB = new Parada(id, localizacao, pertoDe, qtdLinhas.toString(), linhasParada);
					}
					
					Object[] latlngA = paradaDAO.buscarLatLngParada(paradaA.getId().intValue());
					Object[] latlngB = paradaDAO.buscarLatLngParada(paradaB.getId().intValue());
					
					Double latAMelhorRota = (Double) latlngA[0];
					Double lngAMelhorRota = (Double) latlngA[1];
					Double latBMelhorRota = (Double) latlngB[0];
					Double lngBMelhorRota = (Double) latlngB[1];
					
					List<Object[]> rotasListObjList = buscarRotaPeloCodigo(aresta.getRotaDeAparaB());
					Rota rotaSelecionada = new Rota();
					
					String codigo = (String) rotasListObjList.get(0)[0];
					String nome = (String)  rotasListObjList.get(0)[1];
					String terminais = (String)  rotasListObjList.get(0)[2];
					String rotaStr = "";
					/**
					 *  Se ha mais de uma rota, entao 
					 *  a rota selecionada sera a rota completa, em ambos os sentidos
					 *  c.c. rota selecionada sera a rota cujo sentido existe
					 */
					if(rotasListObjList.size() > 1){
						try{
							rotaStr = rotaDAO.selecionarIntervaloEntreParadas(latAMelhorRota, 
									lngAMelhorRota, latBMelhorRota, lngBMelhorRota, aresta.getRotaDeAparaB());
						}catch(Exception e){
							try{
								rotaStr = rotaDAO.selecionarIntervaloEntreParadas(latBMelhorRota, 
										lngBMelhorRota, latAMelhorRota, lngAMelhorRota, aresta.getRotaDeAparaB());
							}catch(Exception ex){
								ex.printStackTrace(); 
							}
						}
						
						rotaSelecionada = new Rota(codigo, nome, rotaStr, terminais, aresta.getCusto());
						
					}else{
						boolean sentido =  (boolean) rotasListObjList.get(0)[3];
						 
						try {
							rotaStr = rotaDAO.selecionarIntervaloEntreParadasConsiderandoSentido(latAMelhorRota, lngAMelhorRota, 
									latBMelhorRota, lngBMelhorRota, aresta.getRotaDeAparaB(), sentido);
						} catch (Exception e) {
							try {
								rotaStr = rotaDAO.selecionarIntervaloEntreParadasConsiderandoSentido(latBMelhorRota, lngBMelhorRota, 
										latAMelhorRota, lngAMelhorRota, aresta.getRotaDeAparaB(), sentido);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
						
						rotaSelecionada = new Rota(codigo, nome, rotaStr, terminais, aresta.getCusto());
					}
					
					melhorRotaStr = "Da parada próxima a(o) " + paradaA.getPertoDe() + " utilizar a rota "
							+ rotaSelecionada.getNome() + " até a parada próxima a(o) " + paradaB.getPertoDe() + ".\n" +
									"Tempo no tráfego atual: " +rotaSelecionada.getCusto() + " minutos\n\n";
					
					MelhorRotaVO melhorRota = new MelhorRotaVO(rotaSelecionada, paradaA, paradaB, melhorRotaStr);
					melhoresRotasList.add(melhorRota);
					
					break;
				}
			}
			
			
		}
		
		return melhoresRotasList;
	}

	private void obterLinhasQuePassamNoPonto(List<Object[]> rotasESentidosAObj,
			List<Object[]> rotasESentidosBObj,
			List<RotaSentidoVO> linhasQuePassamEmA,
			List<RotaSentidoVO> linhasQuePassamEmB) {
		for(Object[] rotaESentidoAObj : rotasESentidosAObj){
			boolean rotaExiste = false;
			
			String cdRota = (String) rotaESentidoAObj[0];
			Boolean sentido = (Boolean) rotaESentidoAObj[1];
			if(!linhasQuePassamEmA.isEmpty()){
				for(RotaSentidoVO rotaESentido : linhasQuePassamEmA){
					//se o codigo da rota ja existe, adiciona o sentido
					if(rotaESentido.getCodigoRota().equals(cdRota)){
						rotaESentido.getSentidos().add(sentido);
						rotaExiste = true;
						break;
					}
				}
				if(!rotaExiste){
					ArrayList<Boolean> sentidos = new ArrayList<Boolean>();
					sentidos.add(sentido);
					RotaSentidoVO rotaSentido = new RotaSentidoVO(cdRota, sentidos);
					linhasQuePassamEmA.add(rotaSentido);
				}
			}else{
				ArrayList<Boolean> sentidos = new ArrayList<Boolean>();
				sentidos.add(sentido);
				RotaSentidoVO rotaSentido = new RotaSentidoVO(cdRota, sentidos);
				linhasQuePassamEmA.add(rotaSentido);
			}
		}
		
		for(Object[] rotaESentidoBObj : rotasESentidosBObj){
			boolean rotaExiste = false;
			
			String cdRota = (String) rotaESentidoBObj[0];
			Boolean sentido = (Boolean) rotaESentidoBObj[1];
			if(!linhasQuePassamEmB.isEmpty()){
				for(RotaSentidoVO rotaESentido : linhasQuePassamEmB){
					//se o codigo da rota ja existe, adiciona o sentido
					if(rotaESentido.getCodigoRota().equals(cdRota)){
						rotaESentido.getSentidos().add(sentido);
						rotaExiste = true;
						break;
					}
				}
				if(!rotaExiste){
					ArrayList<Boolean> sentidos = new ArrayList<Boolean>();
					sentidos.add(sentido);
					RotaSentidoVO rotaSentido = new RotaSentidoVO(cdRota, sentidos);
					linhasQuePassamEmB.add(rotaSentido);
				}
			}else{
				ArrayList<Boolean> sentidos = new ArrayList<Boolean>();
				sentidos.add(sentido);
				RotaSentidoVO rotaSentido = new RotaSentidoVO(cdRota, sentidos);
				linhasQuePassamEmB.add(rotaSentido);
			}
		}
	}

	public void calculoCusto(Long idA, Double latA, Double lngA, Long idB, Double latB, Double lngB, String linha, List<Boolean> sentidos, boolean rotaDireta){
		
		Double distancia = 0.0;
		
		//pois eu nao tenho a informacao que rota passa naquele ponto em que sentido
		if(sentidos.size() > 1){
			try{
				distancia = rotaDAO.calcularCusto(latA, lngA, latB, lngB, linha);
			}catch(Exception e){
				try{
					distancia = rotaDAO.calcularCusto(latB, lngB, latA, lngA, linha);
				}catch(Exception ex){
					ex.printStackTrace(); 
				}
			}
		}else{
			try {
				distancia = rotaDAO.calcularCustoConsiderandoSentido(latA, lngA, latB, lngB, linha, sentidos.get(0));
			} catch (Exception e) {
				try {
					distancia = rotaDAO.calcularCustoConsiderandoSentido(latB, lngB, latA, lngA, linha, sentidos.get(0));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		boolean existeNohA = false;
		boolean existeNohB = false;
		boolean existeAresta = false;
		
		Noh nohA = new Noh();
		Noh nohB = new Noh();
		
		for(Noh noh : nohList){
			if(noh.getCodigoParada().equals(idA)){
				existeNohA = true;
				nohA = noh;
			}
			if(noh.getCodigoParada().equals(idB)){
				existeNohB = true;
				nohB = noh;
			} 
		}
		
		if(!existeNohA){
			nohA = new Noh(idA, nNohs++, latA, lngA);
			nohList.add(nohA);
		}
		
		if(!existeNohB){
			nohB = new Noh(idB, nNohs++, latB, lngB);
			nohList.add(nohB);
		}
		
		for(Aresta aresta : arestaList){
			if(aresta.getPontoA().equals(nohA.getCodigoGrafo()) && aresta.getPontoB().equals(nohB.getCodigoGrafo())){
				existeAresta = true;
			}
		}
		
		if(!existeAresta){
			Double tempo = (distancia/1000) / getVelocidadeMediaOnibus(); // t = s/v
			Double custo = tempo * 60; //conversao do tempo em horas para minutos
			
			if(!rotaDireta){
				custo += 15.0;
			}
			
			Aresta aresta = new Aresta(nArestas++, nohA.getCodigoGrafo(), nohB.getCodigoGrafo(), Double.parseDouble(df.format(custo)), linha);
			arestaList.add(aresta);
		}
			
	}
	
	/**
	 * Le o arquivo das arestas e roda o A* para encontrar a melhor rota
	 * @return
	 * @throws IOException
	 */
	private List<Node> encontrarMelhorRota() throws IOException {
		String nodeFile = "nohs.txt";
        String edgeFile = "arestas.txt";

        Graph graph = GraphReader.readFromCSV(nodeFile,edgeFile);

        Node start = graph.getNode(1);
        Node destination = graph.getNode(2);

        AStar astar = new AStar();
        astar.distance(graph, start, destination);

        Path path = astar.getPath();
        
        for(Edge aresta : path.getEdges()){
        	System.out.println("de: " + aresta.getSource() + " para: " + aresta.getTarget());
        }
        
        return path.getReversedPath().getNodes();
	}
	
	/**
	 * Calcula a velocidade do onibus de acordo com a hora do dia
	 * @return velocidade do onibus 8 ou 22
	 */
	public Double getVelocidadeMediaOnibus(){
		
		Double velocidade;
		
		GregorianCalendar calendar = new GregorianCalendar();
		
		int hora = calendar.get(Calendar.HOUR_OF_DAY);  
		
		System.out.println(hora);
		
		if ((hora >= 8 && hora <= 10) || (hora >= 11 && hora <= 12) || (hora >= 17 && hora <= 19)){
			velocidade = 8.00;
		}
		else
			velocidade = 22.00;
		
		return velocidade; 
		
	}
	
}
	

