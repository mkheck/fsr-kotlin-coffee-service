package com.thehecklers.fsrkotlincoffeeservice

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import javax.annotation.PostConstruct

@Component
class DataLoader(private val repo: CoffeeRepository) {
    @PostConstruct
    private fun load() = repo.deleteAll().thenMany(
            Flux.just("Kaldi's Coffee", "Philz Coffee", "Blue Bottle Coffee")
                    .map { Coffee(name = it) }
                    .flatMap { repo.save(it) })
            .thenMany(repo.findAll())
            .subscribe { println(it) }
}