package com.example.webflux.example04_backpressure

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.BufferOverflowStrategy
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration


private val log = KotlinLogging.logger {}

/** 1. Error 전략
 * Unbounded request 일 경우, Downstream 에 Backpressure Error 전략을 적용하는 예제
 * - Downstream 으로 전달할 데이터가 버퍼에 가득 찰 경우, Exception을 발생시키는 전략
 */
fun backpressureStrategyExample01() {
    Flux.interval(Duration.ofSeconds(1))
        .onBackpressureError()
        .doOnNext { log.info { "# doOnNext: $it" } }
        .publishOn(Schedulers.parallel())           // thread를 추가적으로 할당
        .subscribe (
            { data ->
                Thread.sleep(5L)
                log.info { "# onNext: $data" }
            },
            { error -> log.info { "# onError: $error" } }
        )

    Thread.sleep(2000L)
}

/** 2. Drop 전략 - 즉시 삭제
 * Unbounded request 일 경우, Downstream 에 Backpressure Drop 전략을 사용하는 예제
 * - Downstream 으로 전달할 데이터가 버퍼에 가득 찰 경우, 버퍼 밖에서 대기하는 먼저 emit 된 데이터를 Drop 시키는 전략
 */
fun backpressureStrategyExample02() {
    Flux.interval(Duration.ofSeconds(1))
        .onBackpressureDrop { log.info { "# dropped: $it" } }
        .publishOn(Schedulers.parallel())
        .subscribe(
            { data ->
                Thread.sleep(5L)
                log.info { "# onNext: $data" }
            },
            { error -> log.info { "# onError: $error" } }
        )

    Thread.sleep(2000L)
}

/** 3. Latest 전략 - 다음 데이터가 들어왔을 때, 이전의 데이터를 삭제
 * Unbounded request 일 경우, Downstream 에 Backpressure Latest 전략을 적용하는 예제
 * - Downstream 으로 전달할 데이터가 버퍼에 가득 찰 겨우,
 *   버퍼 밖에서 폐기되지 않고 대기하는 가장 나중에(최근에) emit 된 데이터부터 버퍼에 채우는 전략
 */
fun backpressureStrategyExample03() {
    Flux.interval(Duration.ofMillis(1L))
        .onBackpressureLatest()
        .publishOn(Schedulers.parallel())
        .subscribe(
            { data ->
                Thread.sleep(5L)
                log.info { "# onNext: $data" }
            },
            { error -> log.info { "# onError: $error" } }
        )

    Thread.sleep(2000L)
}

/** 4. Buffer Drop Latest
 * Unbounded request 일 경우, Downstream 에 Backpressure Buffer DROP_LATEST 전략을 적용하는 예제
 *  - Downstream 으로 전달 할 데이터가 버퍼에 가득 찰 경우,
 *    버퍼 안에 있는 데이터 중에서 가장 최근에(나중에) 버퍼로 들어온 데이터부터 Drop 시키는 전략
 */
fun backpressureStrategyExample04() {
    Flux.interval(Duration.ofMillis(300L))
        .doOnNext { log.info { "# emitted by original Flux: $it" } }
        .onBackpressureBuffer(2,
            { dropped -> log.info { "# Overflow & dropped: $dropped" } },
            BufferOverflowStrategy.DROP_LATEST
        )
        .doOnNext { data -> log.info { "# emitted by Buffer: $data" } }
        .publishOn(Schedulers.parallel(), false, 1)
        .subscribe (
            { data: Long? ->
                Thread.sleep(5L)
                log.info { "# onNext: $data" }
            },
            { error -> log.info { "# onError: $error" } }
        )

    Thread.sleep(3000L)
}

/** 5. Buffer Drop Oldest
 * Unbounded request 일 경우, Downstream 에 Backpressure Buffer DROP_OLDEST 전략을 적용하는 예제
 *  - Downstream 으로 전달 할 데이터가 버퍼에 가득 찰 경우,
 *    버퍼 안에 있는 데이터 중에서 가장 먼저 버퍼로 들어온 오래된 데이터부터 Drop 시키는 전략
 */
fun backpressureStrategyExample05() {
    Flux
        .interval(Duration.ofMillis(300L))
        .doOnNext { log.info { "# emitted by original Flux: $it" } }
        .onBackpressureBuffer(2,
            { dropped -> log.info { "# Overflow & dropped: $dropped" } },
            BufferOverflowStrategy.DROP_OLDEST
        )
        .doOnNext { data -> log.info { "# emitted by Buffer: $data" } }
        .publishOn(Schedulers.parallel(), false, 1)
        .subscribe (
            { data: Long? ->
                Thread.sleep(1000L)
                log.info { "# onNext: $data" }
            },
            { error -> log.info { "# onError: $error" } }
        )

    Thread.sleep(3000L)
}