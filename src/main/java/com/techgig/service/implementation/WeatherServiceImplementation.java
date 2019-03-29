package com.techgig.service.implementation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techgig.model.CityForeCast;
import com.techgig.model.Log;
import com.techgig.model.Weather;
import com.techgig.model.WeatherDateWise;
import com.techgig.repo.LogRepo;
import com.techgig.service.WeatherService;
import com.techgig.service.dto.CityForeCastDTO;

@Service
@PropertySource(value = "weather.properties")
public class WeatherServiceImplementation implements WeatherService {
	private Environment env;
	private RestTemplate restTemplate;
	private HttpServletRequest request;
	private LogRepo logRepo;

	@Autowired
	public WeatherServiceImplementation(Environment env, RestTemplate restTemplate, HttpServletRequest request,
			LogRepo logRepo) {
		super();
		this.env = env;
		this.restTemplate = restTemplate;
		this.request = request;
		this.logRepo = logRepo;
	}

	@Override
	public List<CityForeCastDTO> findByCities(List<String> cities) {
		LocalDate tmw = getTmwDate();
		return forecast(cities, LocalDateTime.of(tmw, LocalTime.of(0, 0, 0)),
				LocalDateTime.of(tmw, LocalTime.of(23, 59, 59)));
	}

	@Override
	public List<CityForeCastDTO> findByCities(List<String> cities, Long from, Long to) {
		LocalDate tmw = getTmwDate();
		LocalDateTime fromdate = new Date(from).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime toDate = new Date(to).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		if (fromdate.toLocalDate().isAfter(tmw) || toDate.toLocalDate().isAfter(tmw))
			return new ArrayList<CityForeCastDTO>();
		return forecast(cities, fromdate, toDate);
	}

	public List<CityForeCastDTO> forecast(List<String> cities, LocalDateTime fromdate, LocalDateTime todate) {
		List<CityForeCastDTO> cityForecast = new ArrayList<CityForeCastDTO>();
		SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LocalDate tmw = getTmwDate();

		cities.forEach(city -> {
			String response = restTemplate.getForObject(
					env.getProperty("api.url") + "?q=" + city + "&appid=" + env.getProperty("api.id"), String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			CityForeCast forecast;
			try {
				forecast = objectMapper.readValue(response, CityForeCast.class);
				List<WeatherDateWise> list = new ArrayList<>();
				WeatherDateWise previousValue = new WeatherDateWise();
				double max_val = 0.00;
				LocalDate date = null;
				LocalTime time = null;
				for (WeatherDateWise action : forecast.getList()) {
					try {
						date = fromFormat.parse(action.getDt_txt()).toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();
						time = fromFormat.parse(action.getDt_txt()).toInstant().atZone(ZoneId.systemDefault())
								.toLocalTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (tmw.equals(date)
							&& (time.isAfter(fromdate.toLocalTime()) || time.equals(fromdate.toLocalTime()))
							&& (time.isBefore(todate.toLocalTime()) || time.equals(todate.toLocalTime()))) {
						for (Weather main1 : action.getMain()) {
							if (max_val < main1.getTemp_max()) {
								previousValue = action;
							}
						}
					}
				}
				list.add(previousValue);
				forecast.setList(list);
				CityForeCastDTO foreCastDTO = new CityForeCastDTO();
				foreCastDTO.setCity(forecast.getCity());
				foreCastDTO.setDate(tmw.toString());
				if (previousValue.getMain() != null && previousValue.getMain().get(0) != null)
					foreCastDTO.setMaximumTemperature(previousValue.getMain().get(0).getTemp_max());
				cityForecast.add(foreCastDTO);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		log(cityForecast);
		return cityForecast;
	}

	public LocalDate getTmwDate() {
		return LocalDate.now().plusDays(1);
	}

	public void log(List<CityForeCastDTO> foreCastDTOs) {
		Log log = new Log();
		log.setRequest(request.getRequestURL().toString());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		try {
			log.setResponse(objectMapper.writeValueAsString(foreCastDTOs));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logRepo.save(log);
	}

}
