package com.example.webflux.example09_testing

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink


private val log = KotlinLogging.logger {}

fun generateNumberByErrorStrategy(): Flux<Int?> {
    return Flux
        .create({ emitter: FluxSink<Int?> ->
            for (i in 1..100) {
                emitter.next(i)
            }
            emitter.complete()
        }, FluxSink.OverflowStrategy.ERROR)
}

fun generateNumberByDropStrategy(): Flux<Int?> {
    return Flux
        .create({ emitter: FluxSink<Int?> ->
            for (i in 1..100) {
                emitter.next(i)
            }
            emitter.complete()
        }, FluxSink.OverflowStrategy.DROP)
}