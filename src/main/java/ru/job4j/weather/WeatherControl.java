package ru.job4j.weather;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

@RestController
public class WeatherControl {
    private final WeatherService service;

    public WeatherControl(WeatherService service) {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> findAll() {
        Flux<Weather> data = service.findAll();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping(value = "/cityGreatThen/{temperature}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> findCitiesTemperatureGreatThen(@PathVariable Integer temperature) {
        Flux<Weather> data = service.findCitiesTemperatureGreatThen(temperature);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/hottest")
    public Mono<Weather> findHottestCity() {
        return service.findHottestCity();
    }
}
