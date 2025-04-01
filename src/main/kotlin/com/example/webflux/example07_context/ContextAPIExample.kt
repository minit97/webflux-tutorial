package com.example.webflux.example07_context

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.context.Context
import reactor.util.context.ContextView


private val log = KotlinLogging.logger {}

/**
 * Context API 중에서 write API 예제 코드
 * - Context.of(...) 사용
 */
fun contextAPIExample01() {
    val key1 = "id"
    val key2 = "name"
    val mono: Mono<String> = Mono.deferContextual { ctx: ContextView ->
            Mono.just<String>("ID: ${ctx.get<Any>(key1)}, Name: ${ctx.get<Any>(key2)}")
        }
        .publishOn(Schedulers.parallel())
        .contextWrite(Context.of(key1, "itVillage", key2, "Kevin"))

    mono.subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}

/**
 * Context API 예제 코드
 *  - pullAll(ContextView) API 사용
 */
fun contextAPIExample02() {
    val key1 = "id"
    val key2 = "name"
    val key3 = "country"

    Mono.deferContextual { ctx: ContextView ->
        Mono.just<String>("ID: ${ctx.get<Any>(key1)}, Name: ${ctx.get<Any>(key2)}, Country: ${ctx.get<Any>(key3)}")
    }
    .publishOn(Schedulers.parallel())
    .contextWrite { context: Context -> context.putAll(Context.of(key2, "Kevin", key3, "Korea").readOnly()) }
    .contextWrite { context: Context -> context.put(key1, "itVillage") }
    .subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}

/**
 * ContextView API 예제 코드
 */
fun contextAPIExample03() {
    val key1 = "id"
    val key2 = "name"

    Mono.deferContextual { ctx: ContextView ->
        Mono.just<String>("ID: ${ctx.get<Any>(key1)}, Name: ${ctx.get<Any>(key2)}, Job: ${ctx.getOrDefault<String>("job", "Software Engineer")}")
    }
    .publishOn(Schedulers.parallel())
    .contextWrite(Context.of(key1, "itVillage", key2, "Kevin"))
    .subscribe { log.info { "# onNext(): $it" } }

    Thread.sleep(100L)
}