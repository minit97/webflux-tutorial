package com.example.webflux.example08_debugging

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import java.util.*
import java.util.function.Function


private val log = KotlinLogging.logger {}

/**
 * Non-Debug mode
 */
fun debugModeExample01() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * onOperatorDebug() Hook 메서드를 이용한 Debug mode
 * - 애플리케이션 전체에서 global 하게 동작한다.
 */
fun debugModeExample02() {
    Hooks.onOperatorDebug();

    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * Non-Debug mode
 */
private val fruits = mapOf(
    "banana" to "바나나",
    "apple" to "사과",
    "pear" to "배",
    "grape" to "포도"
)
fun debugModeExample03() {
    Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
        .map { it.lowercase() }
        .map { it.dropLast(1) }
        .mapNotNull { fruits[it] }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * onOperatorDebug() Hook 메서드를 이용한 Debug mode 예제
 */
fun debugModeExample04() {
    Hooks.onOperatorDebug()

    Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
        .map { it.lowercase() }
        .map { it.substring(0, it.length - 1) }
        .mapNotNull { fruits[it] }
        .map { "맛있는 $it" }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}
