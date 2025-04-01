package com.example.webflux.example05_sinks

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST


private val log = KotlinLogging.logger {}

/**
 * Sinks.One 예제
 *  - 한 건의 데이터만 emit 하는 예제
 */
fun sinkOneExample01() {

    // emit 된 데이터 중에서 단 하나의 데이터만 Subscriber에게 전달한다. 나머지 데이터는 Drop 됨.
    val sinkOne: Sinks.One<String> = Sinks.one()
    val mono: Mono<String> = sinkOne.asMono()

    sinkOne.emitValue("Hello Reactor", FAIL_FAST)

    mono.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }
    mono.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }
}

/**
 * Sinks.One 예제
 *  - 두 건의 데이터만 emit 하는 예제
 */
fun sinkOneExample02() {
    // emit 된 데이터 중에서 단 하나의 데이터만 Subscriber에게 전달한다. 나머지 데이터는 Drop 됨.
    val sinkOne: Sinks.One<String> = Sinks.one()
    val mono: Mono<String> = sinkOne.asMono()

    sinkOne.emitValue("Hello Reactor", FAIL_FAST)

    // Sink.One 은 단 한개의 데이터를 emit 할 수 있기때문에 두번째 emit한 데이터는 drop 된다.
    sinkOne.emitValue("Hi Reactor", FAIL_FAST);

    mono.subscribe { data -> log.info { "# onNext(): Subscriber1 : $data" } }
    mono.subscribe { data -> log.info { "# onNext(): Subscriber2 : $data" } }
}