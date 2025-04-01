package com.example.webflux.example06_schedulers

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers


private val log = KotlinLogging.logger {}

/**
 * Schedulers.immediate()을 적용하기 전.
 * 2개의 parallel 쓰레드가 할당된다.
 */
fun schedulersImmediateExample01() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .publishOn(Schedulers.parallel())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .publishOn(Schedulers.parallel())
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(200L)
}

fun schedulersImmediateExample02() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .publishOn(Schedulers.parallel())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .publishOn(Schedulers.immediate())
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(200L)
}