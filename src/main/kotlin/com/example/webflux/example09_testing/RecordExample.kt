package com.example.webflux.example09_testing

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux


private val log = KotlinLogging.logger {}

fun getCountry(source: Flux<String>): Flux<String> {
    return source
        .map { country -> country.substring(0, 1).uppercase() + country.substring(1) }
}