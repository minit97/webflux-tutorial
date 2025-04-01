package com.example.webflux.example07_context

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.context.Context
import reactor.util.context.ContextView


private val log = KotlinLogging.logger {}

/**
 * Context 개념 설명 예제 코드
 *  - contextWrite()으로 Context에 값을 쓸 수 있고, ContextView.get()을 통해서 Context에 저장된 값을 read 할 수 있다.
 *  - ContextView는 deferContextual() 또는 transformDeferredContextual()을 통해 제공된다.
 */
fun contextIntroduceExample01() {
    val key = "message"
    val mono: Mono<String> = Mono.deferContextual { ctx: ContextView ->
            Mono.just<String>("Hello ${ctx.get<Any>(key)}")
                .doOnNext { log.info { "# doOnNext(): $it" } }
        }
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.parallel())
        .transformDeferredContextual { mono2: Mono<String>, ctx: ContextView ->
            mono2.map { data: String -> "$data ${ctx.get<Any>(key)}" }
        }
        .contextWrite { context: Context -> context.put(key, "Reactor") }


    mono.subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}