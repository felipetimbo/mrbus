package br.ufc.rota.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "point_stops")
public class ParadaOnibus {

	@Id
	private Long id;
	
	private String cod_point;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	private Point coord_desc;
	
	private String next_to;
	
	private String route_point;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCod_point() {
		return cod_point;
	}

	public void setCod_point(String cod_point) {
		this.cod_point = cod_point;
	}
	
	public Point getCoord_desc() {
		return coord_desc;
	}

	public void setCoord_desc(Point coord_desc) {
		this.coord_desc = coord_desc;
	}

	public String getNext_to() {
		return next_to;
	}

	public void setNext_to(String next_to) {
		this.next_to = next_to;
	}

	public String getRoute_point() {
		return route_point;
	}

	public void setRoute_point(String route_point) {
		this.route_point = route_point;
	}

}
