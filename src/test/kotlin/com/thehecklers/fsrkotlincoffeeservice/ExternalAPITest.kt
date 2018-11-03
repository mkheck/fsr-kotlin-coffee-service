package com.thehecklers.fsrkotlincoffeeservice

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
// MAH: comment appropriate line to use idiomatic Kotlin functional routing or MVC style routing
//@WebFluxTest(*arrayOf(CoffeeService::class, CoffeeController::class))
@WebFluxTest(*arrayOf(CoffeeService::class, RouteConfig::class))
class ExternalAPITest {
    @Autowired
    lateinit var client: WebTestClient

    @MockBean
    lateinit var repo: CoffeeRepository

    private val coffee1 = Coffee("000-TEST-111", "Tester's Choice")
    private val coffee2 = Coffee("000-TEST-222", "Failgers")

    @Before
    fun setUp() {
        Mockito.`when`(repo.findAll()).thenReturn(Flux.just(coffee1, coffee2))
        Mockito.`when`(repo.findById(coffee1.id!!)).thenReturn(Mono.just(coffee1))
        Mockito.`when`(repo.findById(coffee2.id!!)).thenReturn(Mono.just(coffee2))
    }

    @Test
    fun `Get all coffees via WebTestClient`() {
        StepVerifier.create(client.get()
                .uri("/coffees")
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult<Coffee>()
                .responseBody)
                .expectNext(coffee1)
                .expectNext(coffee2)
                .verifyComplete()
    }

    @Test
    fun `Get a particular coffee via WebTestClient`() {
        StepVerifier.create(client.get()
                .uri("/coffees/{id}", coffee2.id)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult<Coffee>()
                .responseBody)
                .expectNext(coffee2)
                .verifyComplete()
    }

    @Test
    fun `Get some orders for a particular coffee`() {
        StepVerifier.create(client.get()
                .uri("/coffees/{id}/orders", coffee1.id)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
                .returnResult<CoffeeOrder>()
                .responseBody
                .take(2))
                .expectNextCount(2)
                .verifyComplete()
    }
}