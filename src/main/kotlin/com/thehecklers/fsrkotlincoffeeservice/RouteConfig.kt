package com.thehecklers.fsrkotlincoffeeservice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router

@Configuration
class RouteConfig(private val service: CoffeeService) {
    @Bean
    fun routerFunction() = router {
        listOf(GET("/coffees", ::all),
                GET("/coffees/{id}", ::byId),
                GET("/coffees/{id}/orders", ::orders))
    }

    private fun all(req: ServerRequest) = ServerResponse.ok()
            .body<Coffee>(service.getAllCoffees())

    private fun byId(req: ServerRequest) = ServerResponse.ok()
            .body<Coffee>(service.getCoffeeById(req.pathVariable("id")))

    private fun orders(req: ServerRequest) = ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body<CoffeeOrder>(service.getOrdersForCoffeeById(req.pathVariable("id")))
}