package com.thehecklers.fsrkotlincoffeeservice

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CoffeeRepository : ReactiveCrudRepository<Coffee, String> {
}