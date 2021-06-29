package com.dh.sp.reactive.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReactiveControllerTest {

  public static final String URL = "/reactive";
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void getFlux(){
    //When
    final Flux<Integer> actual = webTestClient.get()
        .uri(URL +"/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Integer.class)
        .getResponseBody();

    //Then
    StepVerifier.create(actual)
        .expectSubscription()
        .expectNext(1,2,3)
        .verifyComplete();
  }

  @Test
  public void getFlux_withEntityExchangeResult(){
    //Given
    final List<Integer> expected = List.of(1,2,3);
    //When
    final EntityExchangeResult<List<Integer>> actual = webTestClient.get()
        .uri(URL +"/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Integer.class)
        .returnResult();

    //Then
    assertEquals(expected, actual.getResponseBody());
  }

  @Test
  public void getFlux_withConsumeResult(){
    //Given
    final List<Integer> expected = List.of(1,2,3);
    //When/Then
    webTestClient.get()
        .uri(URL +"/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Integer.class)
        .consumeWith(response ->{
          assertEquals(expected, response.getResponseBody());
        });
  }

  @Test
  public void getFluxStream(){
    //When
    final Flux<Long> actual = webTestClient.get()
        .uri(URL +"/flux/stream")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Long.class)
        .getResponseBody();

    //Then
    StepVerifier.create(actual)
        .expectSubscription()
        .expectNext(0L,1L,2L,3L)
        .thenCancel()
        .verify();
  }

  @Test
  public void getMono(){
    //Given
    final Integer expected = 1;
    //When
    final EntityExchangeResult<Integer> actual = webTestClient.get()
        .uri(URL +"/mono")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Integer.class)
        .returnResult();

    //Then
    assertEquals(expected, actual.getResponseBody());
  }
}
