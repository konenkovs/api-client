package com.goeuro.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {
	@JsonProperty("_id")
	private int id;
	
	private String name;
	private String type;
	
	@JsonProperty("geo_position")
	private GeoPosition geoPosition;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public GeoPosition getGeoPosition() {
		return geoPosition;
	}
	public void setGeoPosition(GeoPosition geoPosition) {
		this.geoPosition = geoPosition;
	}
	
	@Override
	public String toString() {
		return id + "," + name + "," + type + "," + geoPosition.getLatitude() + "," + geoPosition.getLongitude();
	}
}
