package com.example.webflux.example03_sequence

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.stream.Stream

private val log = KotlinLogging.logger {}

fun hotSequenceExample() {
    val concertFlux: Flux<String> = Flux.fromStream(Stream.of("Singer A", "Singer B", "Singer C", "Singer D", "Singer E"))
        .delayElements(Duration.ofSeconds(1)).share()
    // share() 원본 Flux를 여러 Subscriber가 공유한다. -> cold sequnnce를 hot sequence로 변경

    concertFlux.subscribe { singer -> log.info { "# Subscriber1 is watching $singer's song." } }
    Thread.sleep(2500)
    concertFlux.subscribe { singer -> log.info { "# Subscriber2 is watching $singer's song." } }
    Thread.sleep(3000)
}