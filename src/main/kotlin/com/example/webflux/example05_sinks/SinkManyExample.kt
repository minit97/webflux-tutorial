package com.example.webflux.example05_sinks

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST


private val log = KotlinLogging.logger {}

/**
 * Sinks.Many 예제
 *  - unicast()를 사용해서 단 하나의 Subscriber에게만 데이터를 emit하는 예제
 */
fun sinkManyExample01() {
    // 단 하나의 Subscriber에게만 데이터를 emit할 수 있다.
    val unicastSink: Sinks.Many<Int> = Sinks.many().unicast().onBackpressureBuffer()
    val fluxView: Flux<Int> = unicastSink.asFlux()

    unicastSink.emitNext(1, FAIL_FAST)
    unicastSink.emitNext(2, FAIL_FAST)


    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }

    unicastSink.emitNext(3, FAIL_FAST)

    // TODO 주석 전, 후 비교해서 보여 줄 것.
    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }
}

/**
 * Sinks.Many 예제
 *  - multicast()를 사용해서 하나 이상의 Subscriber에게 데이터를 emit하는 예제
 */
fun sinkManyExample02() {

    // 하나 이상의 Subscriber에게 데이터를 emit할 수 있다.
    val multicastSink: Sinks.Many<Int> = Sinks.many().multicast().onBackpressureBuffer()
    val fluxView: Flux<Int> = multicastSink.asFlux()

    multicastSink.emitNext(1, FAIL_FAST)
    multicastSink.emitNext(2, FAIL_FAST)


    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }
    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }

    multicastSink.emitNext(3, FAIL_FAST)
}

/**
 * Sinks.Many 예제
 *  - replay()를 사용하여 이미 emit된 데이터 중에서 특정 개수의 최신 데이터만 전달하는 예제
 */
fun sinkManyExample03() {
    // 구독 이후, emit 된 데이터 중에서 최신 데이터 2개만 replay 한다.
    val replaySink: Sinks.Many<Int> = Sinks.many().replay().limit(2)
    val fluxView: Flux<Int> = replaySink.asFlux()

    replaySink.emitNext(1, FAIL_FAST)
    replaySink.emitNext(2, FAIL_FAST)
    replaySink.emitNext(3, FAIL_FAST)

    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }
    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }
}

/**
 * Sinks.Many 예제
 *  - replay()를 사용하여 이미 emit된 데이터 중에서 특정 개수의 최신 데이터만 전달하는 예제
 */
fun sinkManyExample04() {
    // 구독 이후, emit된 데이터 중에서 최신 데이터 2개만 replay 한다.
    val replaySink: Sinks.Many<Int> = Sinks.many().replay().limit(2)
    val fluxView: Flux<Int> = replaySink.asFlux()

    replaySink.emitNext(1, FAIL_FAST)
    replaySink.emitNext(2, FAIL_FAST)
    replaySink.emitNext(3, FAIL_FAST)

    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }

    replaySink.emitNext(4, FAIL_FAST)

    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }
}

fun sinkManyExample05() {
    // 구독 시점과 상관없이 emit된 모든 데이터를 replay 한다.
    val replaySink: Sinks.Many<Int> = Sinks.many().replay().all()
    val fluxView: Flux<Int> = replaySink.asFlux()

    replaySink.emitNext(1, FAIL_FAST)
    replaySink.emitNext(2, FAIL_FAST)
    replaySink.emitNext(3, FAIL_FAST)


    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }
    fluxView.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }
}