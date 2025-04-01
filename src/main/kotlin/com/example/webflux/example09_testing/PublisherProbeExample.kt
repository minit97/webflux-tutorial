package com.example.webflux.example09_testing

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono



private val log = KotlinLogging.logger {}

fun processWith(main: Mono<String>, standby: Mono<String>?): Mono<String> {
    return main
        .flatMap { massage: String -> Mono.just(massage) }
        .switchIfEmpty(standby!!)
}

fun useMainPower(): Mono<String> {
    return Mono.empty()
}

fun useStandbyPower(): Mono<*> {
    return Mono.just("# use Standby Power")
}