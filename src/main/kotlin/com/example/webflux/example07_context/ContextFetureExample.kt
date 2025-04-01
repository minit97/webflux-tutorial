package com.example.webflux.example07_context

import io.github.oshai.kotlinlogging.KotlinLogging
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.context.Context
import reactor.util.context.ContextView


private val log = KotlinLogging.logger {}

/**
 * Context의 특징
 *  - Context는 각각의 구독을 통해 Reactor Sequence에 연결 되며 체인의 각 연산자가 연결된 Context에 접근할 수 있어야 한다.
 */
fun contextFetureExample01() {
    val key1 = "id"

    val mono: Mono<String> = Mono.deferContextual { ctx: ContextView ->
            Mono.just("ID: ${ctx.get<Any>(key1)}")
        }
        .publishOn(Schedulers.parallel())


    mono.contextWrite { context: Context -> context.put(key1, "itVillage") }
        .subscribe { log.info { "# onNext() subscriber 1: $it" } }

    mono.contextWrite { context: Context -> context.put(key1, "itWorld") }
        .subscribe { log.info { "# onNext() subscriber 2: $it" } }

    Thread.sleep(100L)
}

/**
 * Context의 특징
 *  - Context는 체인의 맨 아래에서부터 위로 전파된다.
 *      - 따라서 Operator 체인에서 Context read 읽는 동작이 Context write 동작 밑에 있을 경우에는 write된 값을 read할 수 없다.
 */
fun contextFetureExample02() {
    val key1 = "id"
    val key2 = "name"

    Mono.deferContextual { ctx: ContextView ->
            Mono.just<Any>(ctx.get<Any>(key1))
        }
        .publishOn(Schedulers.parallel())
        .contextWrite { context: Context -> context.put(key2, "Kevin") }
        .transformDeferredContextual { mono: Mono<Any>, ctx: ContextView ->
            mono.map { data: Any -> "$data, ${ctx.getOrDefault<String>(key2,"Tom")}" }
        }
        .contextWrite { context: Context -> context.put(key1, "itVillage") }
        .subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}

/**
 * Context의 특징
 *  - 동일한 키에 대해서 write 할 경우, 해당 키에 대한 값을 덮어 쓴다.
 */
fun contextFetureExample03() {
    val key1 = "id"

    Mono.deferContextual { ctx: ContextView ->
        Mono.just<String>("ID: ${ctx.get<Any>(key1)}")
    }
    .publishOn(Schedulers.parallel())
    .contextWrite { context: Context -> context.put(key1, "itWorld") }
    .contextWrite { context: Context -> context.put(key1, "itVillage") }
    .subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}

/**
 * Context의 특징
 *  - inner Sequence 내부에서는 외부 Context에 저장된 데이터를 읽을 수 있다.
 *  - inner Sequence 내부에서 Context에 저장된 데이터는 inner Sequence 외부에서 읽을 수 없다.
 */
fun contextFetureExample04() {
    val key1 = "id"
    Mono.just("Kevin")
        .transformDeferredContextual { stringMono: Mono<String>?, contextView: ContextView ->
            contextView.get<Publisher<Any>>("job")
        }
        .flatMap { name: Any ->
            Mono.deferContextual { ctx: ContextView ->
                Mono.just<String>("ctx.get<Any>(key1), ${name}")
                    .transformDeferredContextual { mono: Mono<String>, innerCtx: ContextView ->
                        mono.map { data: String -> "$data, ${innerCtx.get<Any>("job")}" }
                    }
                    .contextWrite { context: Context -> context.put("job", "Software Engineer") }
            }
        }
        .publishOn(Schedulers.parallel())
        .contextWrite { context: Context -> context.put(key1, "itVillage") }
        .subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}