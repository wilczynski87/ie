package dlarodziny.wolontariusze.ie.service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dlarodziny.wolontariusze.ie.model.Volunteer;
import dlarodziny.wolontariusze.ie.model.Volunteerdetails;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class SaveByRestService {
    @Value("${api:localhost}")
    private String myApi;

    @Value(value = "${my.port:327}")
    private String myUrl;

    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(conn -> 
            conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
    
    private WebClient webClient() {
        // myApi = myApi == null || myApi.isBlank() ? "localhost" : myApi;
        // myUrl = myUrl == null || myUrl.isBlank() ? "8081" : myUrl;
        myApi = myApi == null || myApi.isBlank() ? "wolontariusz.byst.re" : myApi;
        myUrl = myUrl == null || myUrl.isBlank() ? "327" : myUrl;
        return WebClient.builder()
        .baseUrl(String.format("https://%s:%s", myApi, myUrl))
        // .baseUrl("https://wolontariusz.byst.re")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .clientConnector(new ReactorClientHttpConnector(this.httpClient))
        .build();
    }

    public Flux<Object> saveNewVolunteers(List<Volunteer> volunteers) {
        return webClient().post()
            .uri("/saveVolunteers")
            .bodyValue(volunteers)
            .exchangeToFlux(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToFlux(Volunteer.class);
                }
                else {
                    return response.createError().flux();
                }
            });
    }

    public Flux<Object> saveNewVolunteerDetails(List<Volunteerdetails> volunteerDetails) {
        return webClient().post()
            .uri("/saveVolunteerDetails")
            .bodyValue(volunteerDetails)
            .exchangeToFlux(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToFlux(Volunteerdetails.class);
                }
                else {
                    return response.createError().flux();
                }
            });
    }
}