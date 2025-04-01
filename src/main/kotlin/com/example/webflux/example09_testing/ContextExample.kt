package com.example.webflux.example09_testing

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono
import reactor.util.context.ContextView


private val log = KotlinLogging.logger {}

fun helloMessage(source: Mono<String>, key: String?): Mono<String> {
    return source
        .zipWith(
            Mono.deferContextual { ctx: ContextView -> Mono.just(ctx.get<Any>(key!!)) }
        )
        .map { tuple -> "${tuple.t1}, ${tuple.t2}" }
}