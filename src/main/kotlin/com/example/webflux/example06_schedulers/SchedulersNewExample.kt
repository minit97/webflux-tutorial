package com.example.webflux.example06_schedulers

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


private val log = KotlinLogging.logger {}

/**
 * Schedulers.newBoundedElastic()을 적용
 */
fun schedulersNewBoundedElasticExample01() {
    val scheduler = Schedulers.newBoundedElastic(2, 2, "I/O-Thread")
    val mono: Mono<Int> = Mono.just(1)
                                .subscribeOn(scheduler)

    log.info{ "# Start" }

    mono.subscribe { data ->
        log.info { "# onNext() subscribe 1 doing: $data" }
        Thread.sleep(3000L)
        log.info { "# onNext() subscribe 1 done: $data" }
    }

    mono.subscribe { data ->
        log.info { "# onNext() subscribe 2 doing: $data" }
        Thread.sleep(3000L)
        log.info { "# onNext() subscribe 2 done: $data" }
    }

    mono.subscribe { data ->
        log.info { "# onNext() subscribe 3 doing: $data" }
    }

    mono.subscribe { data ->
        log.info { "# onNext() subscribe 4 doing: $data" }
    }

    mono.subscribe { data ->
        log.info { "# onNext() subscribe 5 doing: $data" }
    }

    mono.subscribe { data ->
        log.info { "# onNext() subscribe 6 doing: $data" }
    }
}

/**
 * Schedulers.newParallel()을 적용
 */
fun schedulersNewParallelExample01() {
    val flux: Mono<Int> = Mono.just(1)
                    .publishOn(Schedulers.newParallel("Parallel Thread", 4, true))


    flux.subscribe { data ->
        Thread.sleep(5000L)
        log.info { "# onNext() subscribe 1: $data" }
    }

    flux.subscribe { data: Int? ->
        Thread.sleep(4000L)
        log.info { "# onNext() subscribe 2: $data" }
    }

    flux.subscribe { data: Int? ->
        Thread.sleep(3000L)
        log.info { "# onNext() subscribe 3: $data" }
    }

    flux.subscribe { data: Int? ->
        Thread.sleep(2000L)
        log.info { "# onNext() subscribe 4: $data" }
    }

    Thread.sleep(6000L)
}