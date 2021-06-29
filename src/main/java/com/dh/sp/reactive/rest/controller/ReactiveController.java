package com.dh.sp.reactive.rest.controller;

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive")
public class ReactiveController {

  @GetMapping("/flux")
  public Flux<Integer> getFlux(){
    return Flux.just(1,2,3).log();
  }

  @GetMapping(value = "/flux/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Long> getFluxStream(){
    return Flux.interval(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping("/mono")
  public Mono<Integer> getMono(){
    return Mono.just(1).log();
  }
}
