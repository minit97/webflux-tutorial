package com.example.webflux.example

import com.jayway.jsonpath.JsonPath
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

/**
 * Mono 기본 개념 에제01
 * - 1개의 데이터를 생성해서 emit 한다.
 */
fun mono01() {
    Mono.just("Hello Reactor!!")
        .subscribe { log.info { "# emitted data: $it" } }
}

/** Mono 기본 개념 예제02
 * - 원본 데이터의 emit 없이 onComplete signal만 emit 한다.
 */
fun mono02() {
    Mono.empty<Any>()
        .subscribe(
            { data -> log.info { "# emitted data: $data" } },       // 상위 upstream 으로부터 emit 된 데이터 전달 받는 부분
            { /* error 처리 로직 */ },                                // upstream 에서 error 발생 시 에러를 전달 받는 부분
            { log.info { "# emitted onComplete signal" } }          // upstream 으로부터 onComplete signal 을 전달받는 부분
        )
}

/** Mono 활용 예제
 * - worldtimeapi.org Open API를 이용해서 서울의 현재 시간을 조회한다.
 */
fun mono03() {
    val worldTimeUri = UriComponentsBuilder.newInstance().scheme("http")
        .host("worldtimeapi.org")
        .port(80)
        .path("/api/timezone/Asia/Seoul")
        .build()
        .encode()
        .toUri()

    val restTemplate = RestTemplate()
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON)

    Mono.just(
        restTemplate.exchange(worldTimeUri, HttpMethod.GET, HttpEntity<String>(headers), String::class.java)
    ).map { response ->
        val jsonContext = JsonPath.parse(response.body ?: "{}")
        jsonContext.read("$.datetime", String::class.java)
    }.subscribe(
        { data -> log.info { "# emmited data: $data" } },
        { error -> log.error { error } },
        { log.info { "# emitted onComplete signal" } },
    )
}