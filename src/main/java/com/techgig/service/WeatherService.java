package com.techgig.service;

import java.util.List;

import com.techgig.service.dto.CityForeCastDTO;

public interface WeatherService {
	List<CityForeCastDTO> findByCities(List<String> cities);

	List<CityForeCastDTO> findByCities(List<String> cities, Long fromDate, Long toDate);
}
