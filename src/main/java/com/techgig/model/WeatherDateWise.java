package com.techgig.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDateWise {
	private String dt_txt;
	private List<Weather> main;

	public String getDt_txt() {
		return dt_txt;
	}

	public void setDt_txt(String dt_txt) {
		this.dt_txt = dt_txt;
	}

	public List<Weather> getMain() {
		return main;
	}

	public void setMain(List<Weather> main) {
		this.main = main;
	}

	@Override
	public String toString() {
		return "WeatherDateWise [dt_txt=" + dt_txt + ", main=" + main + "]";
	}

}
