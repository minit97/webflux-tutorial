package com.example.webflux.example08_debugging

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.scheduler.Schedulers
import java.util.*
import java.util.function.Function


private val log = KotlinLogging.logger {}

/**
 * log() operator를 사용한 예제
 */
private val fruits = mapOf(
    "banana" to "바나나",
    "apple" to "사과",
    "pear" to "배",
    "grape" to "포도"
)
fun logOperatorExample01() {
    Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
        .log()
        .map { it.lowercase() }
        .map { it.dropLast(1) }
        .mapNotNull { fruits[it] }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * log() operator와 Debug mode 를 같이 사용한 예제
 * - log()는 에러 발생 시, stacktrace와 함께 traceback도 같이 출력한다.
 */
fun logOperatorExample02() {
    Hooks.onOperatorDebug()

    Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
        .log()
        .map { it.lowercase() }
        .log()
        .map { it.dropLast(1) }
        .log()
        .mapNotNull { fruits[it] }
        .log()
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * log() operator Custom Category 를 사용하는 예제
 */
fun logOperatorExample03() {
    Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
        .subscribeOn(Schedulers.boundedElastic())
        .log("Fruit.Source")
        .publishOn(Schedulers.parallel())
        .map { it.lowercase() }
        .log("Fruit.Lower")
        .map { it.dropLast(1) }
        .log("Fruit.Substring")
        .mapNotNull { fruits[it] }
        .log("Fruit.Name")
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }

    Thread.sleep(100L)
}