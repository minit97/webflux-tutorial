package com.example.webflux.example02_mono_flux

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


private val log = KotlinLogging.logger {}

/** Flux 기본 예제
 */
fun flux01() {
    Flux.just(6,9,13)
        .map { num -> num % 2 }
        .subscribe { reminder -> log.info { "# reminder: $reminder" } }
}

/** Flux 에서의 Operator 체인 사용 예제
 */
fun flux02() {
    Flux.fromArray(arrayOf(3, 6, 7, 9))
        .filter { num -> num > 6 }
        .map{ num -> num * 2 }
        .subscribe{ mutiply -> log.info { "# mutiply: $mutiply" } }
}

/** 2개의 Mono를 연결해서 Flux로 변환하는 예제
 */
fun flux03() {
    val flux: Flux<Any> = Mono.justOrEmpty<Any>(null)
        .concatWith(Mono.justOrEmpty("Jobs"))
    flux.subscribe { log.info{ "# result: $it" } }
}

/** 여러개의 Flux를 연결해서 하나의 Flux로 결합하는 예제
 */
fun flux04() {
    Flux.concat(
        Flux.just("Venus"),
        Flux.just("Earth"),
        Flux.just("Mars"),
    ).collectList()
        .subscribe { planetList -> log.info { "# Solar System: $planetList" } }
}