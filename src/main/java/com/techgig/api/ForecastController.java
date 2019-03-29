package com.techgig.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techgig.service.WeatherService;
import com.techgig.service.dto.CityForeCastDTO;

@RestController
@RequestMapping(value = "/api/")
public class ForecastController {
	private WeatherService weatherService;

	@Autowired
	public ForecastController(WeatherService weatherService) {
		super();
		this.weatherService = weatherService;
	}

	@GetMapping(value = "/")
	public String iPage() {
		return "Welcome";
	}

	@GetMapping(value = "check-forecast-for-cities/{cities}")
	public List<CityForeCastDTO> checkForeCastForCities(@PathVariable List<String> cities) {
		return weatherService.findByCities(cities);
	}

	@GetMapping(value = "check-forecast-for-cities/{cities}/{fromtime}/{totime}")
	public List<CityForeCastDTO> checkForeCastForCities(@RequestBody List<String> cities,
			@PathVariable("fromtime") Long fromtime, @PathVariable("totime") Long totime) {
		return weatherService.findByCities(cities);
	}
}