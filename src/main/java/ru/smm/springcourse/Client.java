package ru.smm.springcourse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {

    public static void main(String[] args) {
        final String sensorName = "Sensor for client";

//        registerSensor(sensorName);

        Random random = new Random();

        double minTemperature = -100.0;
        double maxTemperature = 100.0;

        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
            sendMeasurement(minTemperature + (random.nextDouble() * (maxTemperature - minTemperature)),
                    random.nextBoolean(), sensorName);

        }
    }

    // Метод регистрации сенсора в БД
    public static void registerSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJSONData(url, jsonData);
    }

    private static void sendMeasurement(double temperature, boolean raining, String sensorName) {
        final String url = "http://localhost:8080/measurement/add";

        Map<String, Object> jsonData = new HashMap<>();

        jsonData.put("temperature", temperature);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));

        makePostRequestWithJSONData(url, jsonData);

    }

    // Метод для POST запроса к серверу
    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Измерение успешно отправлено на сервер!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            e.getMessage();
        }
    }
}