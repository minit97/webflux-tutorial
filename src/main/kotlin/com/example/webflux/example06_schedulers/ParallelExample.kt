package com.example.webflux.example06_schedulers

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers






private val log = KotlinLogging.logger {}


/**
 * parallel()만 사용할 경우에는 병렬로 작업을 수행하지 않는다.
 */
fun parallelExample01() {
    Flux.fromArray(arrayOf(1, 3, 5, 7, 9, 11, 13, 15))
        .parallel()
        .subscribe{ log.info { "# onNext(): $it" } }
}

/**
 * - parallel()만 사용할 경우에는 병렬로 작업을 수행하지 않는다.
 * - **** runOn()을 사용해서 Scheduler를 할당해주어야 병렬로 작업을 수행한다. ****
 */
fun parallelExample02() {
    Flux.fromArray(arrayOf(1, 3, 5, 7, 9, 11, 13, 15))
        .parallel()
        .runOn(Schedulers.parallel())
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}

/**
 * - parallel()만 사용할 경우에는 병렬로 작업을 수행하지 않는다.
 * - runOn()을 사용해서 Scheduler를 할당해주어야 병렬로 작업을 수행한다.
 * - **** CPU 코어 갯수내에서 worker thread를 할당한다. ****
 */
fun parallelExample03() {
    Flux.fromArray(arrayOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19))
        .parallel()
        .runOn(Schedulers.parallel())
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}

/**
 * - parallel()만 사용할 경우에는 병렬로 작업을 수행하지 않는다.
 * - runOn()을 사용해서 Scheduler를 할당해주어야 병렬로 작업을 수행한다.
 * - **** CPU 코어 갯수에 의존하지 않고, worker thread를 강제 할당한다. ****
 */
fun parallelExample04() {
    Flux.fromArray(arrayOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19))
        .parallel(4)
        .runOn(Schedulers.parallel())
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}