package com.thehecklers.fsrkotlincoffeeservice

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant

@Service
class CoffeeService(private val repo: CoffeeRepository) {
    fun getAllCoffees() = repo.findAll()

    fun getCoffeeById(id: String) = repo.findById(id)

    fun getOrdersForCoffeeById(coffeeId: String) = Flux.interval(Duration.ofSeconds(1))
            .onBackpressureDrop()
            .map { CoffeeOrder(coffeeId, Instant.now()) }
}