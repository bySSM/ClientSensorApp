package ru.smm.springcourse;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;
import ru.smm.springcourse.dto.MeasurementDTO;
import ru.smm.springcourse.dto.MeasurementResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawChart {
    public static void main(String[] args) {
        List<Double> temperatures = getTemperaturesFromServer();
        drawChart(temperatures);
    }

    private static List<Double> getTemperaturesFromServer() {
        final String url = "http://localhost:8080/measurement/";
        final RestTemplate restTemplate = new RestTemplate();

        MeasurementResponse jsonResponse = restTemplate.getForObject(url, MeasurementResponse.class);

        if (jsonResponse == null || jsonResponse.getMeasurements() == null)
            return Collections.emptyList();

        return jsonResponse.getMeasurements().stream().map(MeasurementDTO::getTemperature)
                .collect(Collectors.toList());

    }

    private static void drawChart(List<Double> temperatures) {
        double[] xData = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures","X", "Y", "temperature",
                xData, yData);

        new SwingWrapper(chart).displayChart();


    }

}
