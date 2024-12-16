package com.example.webflux

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


fun example01() {
    Mono.just("Hello Reactor")
        .subscribe { println(it) }
}

fun main() {
    val sequence = Flux.just("Hello", "Reactor")
    sequence.map { it.lowercase(Locale.getDefault()) }
        .subscribe { println(it) }
}