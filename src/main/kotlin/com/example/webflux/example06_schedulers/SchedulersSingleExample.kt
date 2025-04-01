package com.example.webflux.example06_schedulers

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers


private val log = KotlinLogging.logger {}

/**
 * Schedulers.single()을 적용 할 경우,
 * Schedulers.single()에서 할당된 쓰레드를 재사용 한다.
 */
fun schedulersSingleExample01() {
    doTask1("task1")
        .subscribe{ log.info { "# onNext() : $it" } }

    doTask1("task2")
        .subscribe{ log.info { "# onNext() : $it" } }

    Thread.sleep(200L);
}

private fun doTask1(taskName: String): Flux<Int> {
    return Flux.fromArray(arrayOf(1, 3, 5, 7))
        .publishOn(Schedulers.single())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() $taskName filter: $it" } }
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() $taskName map: $it" } }
}

/**
 * Schedulers.single()을 적용 후,
 * 첫번째 Schedulers.single()에서 할당 된 쓰레드를 재사용 한다.
 */
fun schedulersSingleExample02() {
    doTask2("task1")
        .subscribe{ log.info { "# onNext() : $it" } }

    doTask2("task2")
        .subscribe{ log.info { "# onNext() : $it" } }

    Thread.sleep(200L);
}

private fun doTask2(taskName: String): Flux<Int> {
    return Flux.fromArray(arrayOf(1, 3, 5, 7))
        .doOnNext { log.info { "# doOnNext() $taskName fromArray: $it" } }
        .publishOn(Schedulers.newSingle("new-single", true))
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() $taskName filter: $it" } }
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() $taskName map: $it" } }
}