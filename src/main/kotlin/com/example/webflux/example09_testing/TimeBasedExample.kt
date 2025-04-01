package com.example.webflux.example09_testing

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2
import reactor.util.function.Tuples


private val log = KotlinLogging.logger {}

fun getCOVID19Count(source: Flux<Long?>): Flux<Tuple2<String, Int>> {
    return source.flatMap {
            Flux.just(
                Tuples.of("서울", 1000),
                Tuples.of("경기도", 500),
                Tuples.of("강원도", 300),
                Tuples.of("충청도", 60),
                Tuples.of("경상도", 100),
                Tuples.of("전라도", 80),
                Tuples.of("인천", 200),
                Tuples.of("대전", 50),
                Tuples.of("대구", 60),
                Tuples.of("부산", 30),
                Tuples.of("제주도", 5)
            )
        }
}

fun getVoteCount(source: Flux<Long?>): Flux<Tuple2<String, Int>> {
    return source
        .zipWith(Flux.just(
                Tuples.of("중구", 15400),
                Tuples.of("서초구", 20020),
                Tuples.of("강서구", 32040),
                Tuples.of("강동구", 14506),
                Tuples.of("서대문구", 35650)
        ))
        .map { it.t2 }
}