package com.thehecklers.fsrkotlincoffeeservice

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// MAH: comment out to use idiomatic Kotlin functional routing as defined in RouteConfig (either/or)
//@RestController
//@RequestMapping("/coffees")
class CoffeeController(private val service: CoffeeService) {
    @GetMapping
    fun all() = service.getAllCoffees()

    @GetMapping("/{id}")
    fun byId(@PathVariable id: String) = service.getCoffeeById(id)

    @GetMapping("/{id}/orders", produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
    fun orders(@PathVariable id: String) = service.getOrdersForCoffeeById(id)
}