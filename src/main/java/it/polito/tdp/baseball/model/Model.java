package it.polito.tdp.baseball.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.baseball.db.BaseballDAO;
import it.polito.tdp.baseball.db.DBConnect;

public class Model {
	
	private BaseballDAO dao;
	private SimpleGraph<People, DefaultEdge> grafo;
	private Integer nVertici;
	private Integer nArchi;
	private Integer gradoMassimo;
	private Integer nComponentiConnesse;
	private List<People> verticiGradoMassimo;
	private List<People> dreamTeam;
	private Double salarioMax;
	private Map<People, Double> registroSalary;
	
	public Model() {
		this.dao = new BaseballDAO();
	}
	
	public List<Integer> getAllAnni(){
		return this.dao.getAllAnni();
	}
	
	public void creaGrafo(Integer anno, Integer salario) {
		
		this.grafo = new SimpleGraph<People, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getAllVertexes(anno, salario));
		for (People p1 : this.grafo.vertexSet()) {
			for (People p2 : this.grafo.vertexSet()) {
				if (p1.getPlayerID().compareTo(p2.getPlayerID()) < 0) {
					if (this.dao.isConnected(anno, p1, p2)) {
						this.grafo.addEdge(p1, p2);
					}
				}
			}
		}
		this.nVertici = this.grafo.vertexSet().size();
		this.nArchi = this.grafo.edgeSet().size();
		this.nComponentiConnesse = new ConnectivityInspector<>(this.grafo).connectedSets().size();
		
		this.registroSalary = new HashMap<People, Double>();
		for (People p : this.grafo.vertexSet()) {
			this.registroSalary.put(p, this.dao.salaryPlayerYear(anno, p));
		}
	}

	public Integer getnVertici() {
		return nVertici;
	}

	public Integer getnArchi() {
		return nArchi;
	}

	public BaseballDAO getDao() {
		return dao;
	}

	public SimpleGraph<People, DefaultEdge> getGrafo() {
		return grafo;
	}

	public Integer getGradoMassimo() {
		return gradoMassimo;
	}

	public List<People> getVerticiGradoMassimo() {
		return verticiGradoMassimo;
	}
	
	
	public void gradoMassimo() {
		this.gradoMassimo = 0;
		this.verticiGradoMassimo = new ArrayList<>();
		
		for (People p : this.grafo.vertexSet()) {
			Integer grado = this.grafo.degreeOf(p);
			if (grado > this.gradoMassimo) {
				this.gradoMassimo = grado;
			}
		}
		
		for (People p : this.grafo.vertexSet()) {
			Integer grado = this.grafo.degreeOf(p);
			if (grado == this.gradoMassimo) {
				this.verticiGradoMassimo.add(p);
			}
		}
	}
	
	public void inizializza() {
		this.dreamTeam = new ArrayList<People>();
		this.salarioMax = (double) 0;
		
		List<People> parziale = new ArrayList<People>();
		double salario = 0;
		for (People p : this.grafo.vertexSet()) {
			parziale.add(p);
			salario += this.registroSalary.get(p);
			cerca(parziale, salario);
		}
	}
	
	public void cerca(List<People> parziale, Double salario) {
		
		if (parziale.size() == this.nComponentiConnesse) {
			
			System.out.println(parziale);
			
			if (salario > this.salarioMax) {
				this.salarioMax = salario;
				this.dreamTeam = new ArrayList<People>(parziale);
			}
		}
		else {
			for (People p : this.grafo.vertexSet()) {
				
				if (p.getPlayerID().compareTo(parziale.get(parziale.size()-1).getPlayerID()) < 0) {
					if (this.isAggiungibile(parziale, p)) {
						parziale.add(p);
						salario += this.registroSalary.get(p);
						cerca(parziale, salario);
						parziale.remove(parziale.size()-1);
					}
				}	
			}
		}
	}
	
	public boolean isAggiungibile(List<People> parziale, People player1) {
		for (People player2 : parziale) {
			if (this.grafo.containsEdge(player1, player2)) {
				return false;
			}
		}
		return true;
	}
}