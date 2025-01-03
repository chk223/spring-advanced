package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.BaseException;
import org.example.expert.domain.common.exception.util.ErrorMessage;
import org.example.expert.domain.common.exception.util.ExceptionGenerator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherClient {

    private final RestTemplate restTemplate;

    public WeatherClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getTodayWeather() {
        ResponseEntity<WeatherDto[]> responseEntity =
                restTemplate.getForEntity(buildWeatherApiUri(), WeatherDto[].class);

        WeatherDto[] weatherArray = responseEntity.getBody();
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new ApiException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: ", (HttpStatus) responseEntity.getStatusCode());
        } else {
            if (weatherArray == null || weatherArray.length == 0) {
                throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.WEATHER_DATA_NOT_FOUND, ApiException.class);
            }
        }

        String today = getCurrentDate();

        for (WeatherDto weatherDto : weatherArray) {
            if (today.equals(weatherDto.getDate())) {
                return weatherDto.getWeather();
            }
        }

        throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.INVALID_TODAY_WEATHER, ApiException.class);
    }

    private URI buildWeatherApiUri() {
        return UriComponentsBuilder
                .fromUriString("https://f-api.github.io")
                .path("/f-api/weather.json")
                .encode()
                .build()
                .toUri();
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return LocalDate.now().format(formatter);
    }
}
