package com.example.webflux.example05_sinks

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Sinks
import java.util.stream.IntStream


private val log = KotlinLogging.logger {}

/**
 * create() Operator를 사용하는 예제
 *  - 일반적으로 Publisher의 데이터 생성을 단일 쓰레드에서 진행한다. 멀티 쓰레드에서도 가능
 *  - 데이터 emit은 create Operator 내부에서 가능.
 *  - Backpressure 적용 가능
 */
fun programmaticCreateExample01() {
    val tasks = 6
    Flux.create { sink: FluxSink<String?> ->
            IntStream.range(1, tasks)
                    .forEach { n: Int -> sink.next(doTask(n)) }
        }
//        .subscribeOn(Schedulers.boundedElastic())
//        .doOnNext(n -> log.info("# create(): {}", n))
//        .publishOn(Schedulers.parallel())
//        .map(result -> result + " success!")
//        .doOnNext(n -> log.info("# map(): {}", n))
//        .publishOn(Schedulers.parallel())
        .subscribe { log.info { "# onNext: $it" } }

    Thread.sleep(500L)
}

/**
 * Sinks를 사용하는 예제
 *  - Publisher의 데이터 생성을 멀티 쓰레드에서 진행해도 Thread safe 하다.
 */
fun ProgrammaticSinksExample01() {
    val tasks = 6

    val unicastSink: Sinks.Many<String> = Sinks.many().unicast().onBackpressureBuffer()
    val fluxView: Flux<String> = unicastSink.asFlux()

    IntStream.range(1, tasks).forEach { n ->
            try {
                Thread {
                    unicastSink.emitNext(doTask(n), Sinks.EmitFailureHandler.FAIL_FAST)
                    log.info { "# emitted: $n" }
                }.start()

                Thread.sleep(100L)
            } catch (e: InterruptedException) {
                log.warn { e.message }
            }
        }

    fluxView
//        .publishOn(Schedulers.parallel())
//        .map(result -> result + " success!")
//        .doOnNext(n -> log.info("# map(): {}", n))
//        .publishOn(Schedulers.parallel())
        .subscribe { data -> log.info { "# onNext: $data" } }

    Thread.sleep(200L)
}


private fun doTask(taskNumber: Int): String {
    // now tasking.
    // complete to task.
    return "task $taskNumber result"
}