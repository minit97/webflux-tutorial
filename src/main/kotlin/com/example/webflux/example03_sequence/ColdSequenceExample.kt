package com.example.webflux.example03_sequence

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux

private val log = KotlinLogging.logger {}

fun coldSequenceExample() {
    val coldFlux: Flux<String> = Flux.fromIterable(listOf("RED", "YELLOW", "PINK"))
        .map(String::lowercase)

    coldFlux.subscribe { country -> log.info { "# Subscriber1: $country" } }
    log.info { "==================" }
    coldFlux.subscribe { country -> log.info { "# Subscriber2: $country" } }
}