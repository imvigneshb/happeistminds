package com.techgig.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityForeCast {
	private City city;
	private List<WeatherDateWise> list;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<WeatherDateWise> getList() {
		return list;
	}

	public void setList(List<WeatherDateWise> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "CityForeCast [city=" + city + ", list=" + list + "]";
	}

}
