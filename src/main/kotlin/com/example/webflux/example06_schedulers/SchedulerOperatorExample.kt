package com.example.webflux.example06_schedulers

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers




private val log = KotlinLogging.logger {}

/**
 * Sequence의 Operator 체인에서 최초의 쓰레드는 subscribe()가
 * 호출되는 scope에 있는 쓰레드이다.
 */
fun schedulerOperatorExample01() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .filter { it > 3 }
        .map { it * 10 }
        .subscribe{ log.info { "# onNext(): $it" } }
}

/**
 * Operator 체인에서 publishOn( )이 호출되면 publishOn( ) 호출 이후의 Operator 체인은
 * 다음 publisherOn( )을 만나기전까지 publishOn( )에서 지정한 Thread에서 실행이 된다.
 */
fun schedulerOperatorExample02() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .doOnNext { log.info { "# doOnNext() fromArray: $it" } }
        .publishOn(Schedulers.parallel())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(500L)
}

/**
 * Operator 체인에서 publishOn( )이 호출되면 publishOn( ) 호출 이후의 Operator 체인은
 * *** 다음 publisherOn( )을 만나기전까지 *** publishOn( )에서 지정한 Thread에서 실행이 된다.
 */
fun schedulerOperatorExample03() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .doOnNext { log.info { "# doOnNext() fromArray: $it" } }
        .publishOn(Schedulers.parallel())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .publishOn(Schedulers.parallel())
        .map { data: Int -> data * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(500L)
}

/**
 * subscribeOn()은 구독 직후에 실행 될 쓰레드를 지정한다.
 * 즉, 원본 Publisher의 실행 쓰레드를 subscribeOn()에서 지정한 쓰레드로 바꾼다.
 */
fun schedulerOperatorExample04() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext { log.info { "# doOnNext() fromArray: $it" } }
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(500L)
}

/**
 * subscribeOn( )과 publishOn( )이 같이 있다면, publishOn( )을 만나기 전 까지의 Upstream Operator 체인은
 * subscribeOn( )에서 지정한 쓰레드에서 실행되고, publishOn( )을 만날때마다
 * publishOn( ) 아래의 Operator 체인 downstream은 publishOn( )에서 지정한 쓰레드에서 실행된다.
 */
fun schedulerOperatorExample05() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .subscribeOn(Schedulers.boundedElastic())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .publishOn(Schedulers.parallel())
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(500L)
}

/**
 * subscribeOn( )과 publishOn( )이 같이 있다면, publishOn( )을 만나기 전 까지의 Upstream Operator 체인은
 * subscribeOn( )에서 지정한 쓰레드에서 실행되고, publishOn( )을 만날때마다
 * publishOn( ) 아래의 Operator 체인 downstream은 publishOn( )에서 지정한 쓰레드에서 실행된다.
 */
fun schedulerOperatorExample06() {
    Flux.fromArray(arrayOf(1, 3, 5, 7))
        .doOnNext { log.info { "# doOnNext() fromArray: $it" } }
        .publishOn(Schedulers.parallel())
        .filter { it > 3 }
        .doOnNext { log.info { "# doOnNext() filter: $it" } }
        .subscribeOn(Schedulers.boundedElastic())
        .map { it * 10 }
        .doOnNext { log.info { "# doOnNext() map: $it" } }
        .subscribe{ log.info { "# onNext(): $it" } }

    Thread.sleep(500L)
}

