package com.example.webflux.example07_context

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import reactor.util.function.Tuple2


private val log = KotlinLogging.logger {}

/**
 * Context 활용 예제
 *  - 직교성을 가지는 정보를 표현할 때 주로 사용된다.
 */
const val HEADER_NAME_AUTH_TOKEN = "authToken"
fun contextRealExample01() {
    val mono: Mono<String> = postBook(
                Mono.just(Book("abcd-1111-3533-2809", "Reactor's Bible", "Kevin"))
            )
            .contextWrite(Context.of(HEADER_NAME_AUTH_TOKEN, "eyJhbGciOiJIUzUxMiJ9.eyJzdWI"))

    mono.subscribe { log.info { "# onNext(): $it" } }
}

private fun postBook(book: Mono<Book>): Mono<String> {
    return Mono.zip(
            book,
            Mono.deferContextual { ctx: ContextView -> Mono.just(ctx.get<Any>(HEADER_NAME_AUTH_TOKEN))}
        )
        .flatMap { tuple -> Mono.just(tuple) }  // 외부 API 서버로 HTTP POST request를 전송한다고 가정
        .flatMap { tuple ->
            val response = "POST the book(${tuple.t1.bookName},${tuple.t1.author}) with token: ${tuple.t2}"
            Mono.just(response)     // HTTP response를 수신했다고 가정
        }
}