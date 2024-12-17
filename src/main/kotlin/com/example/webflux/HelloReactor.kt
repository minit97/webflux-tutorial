package com.example.webflux

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

// 클래스 최상단에 정의한다.
private val log = KotlinLogging.logger {}


fun example01() {
    Mono.just("Hello Reactor")
        .subscribe { println(it) }
}

fun reactor01() {
    val sequence = Flux.just("Hello", "Reactor")
    sequence.map { it.lowercase(Locale.getDefault()) }
        .subscribe { println(it) }
}