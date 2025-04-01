package com.example.webflux.example08_debugging

import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Flux


private val log = KotlinLogging.logger {}

/**
 * checkpoint() Operator 를 이용한 예제
 * - 에러가 예상되는 assembly 지점에 checkpoint()를 사용해서 에러 발생 지점을 확인할 수 있다.
 * - checkpoint()는 에러 발생 시, traceback 이 추가된다.
 */
fun checkpointExample01() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .checkpoint()
        .map { num: Int -> num + 2 }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * checkpoint() Operator 를 사용한 예제
 * - 발생한 에러는 Operator 체인에 전파가 되기때문에 에러가 전파된 지점의 checkpoint()에서 확인할 수 있다.
 * - traceback 은 실제 에러가 발생한 assembly 지점 또는 에러가 전파된 assembly 지점의 traceback 즉,
 *   실제 checkpoint()를 추가한 지점의 traceback 이 추가된다.
 */
fun checkpointExample02() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .map { num: Int -> num + 2 }
        .checkpoint()
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * checkpoint() Operator 를 2개 사용한 예제
 * - 발생한 에러는 Operator 체인에 전파가 되기때문에 각각의 checkpoint()에서 확인할 수 있다.
 */
fun checkpointExample03() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .checkpoint()
        .map { num: Int -> num + 2 }
        .checkpoint()
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * checkpoint(description) Operator 를 이용한 예제
 * - description 을 추가해서 에러가 발생한 지점을 구분할 수 있다.
 * - description 을 지정할 경우에 에러가 발생한 assembly 지점의 traceback 을 추가하지 않는다.
 */
fun checkpointExample04() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .checkpoint("CheckpointExample02.zipWith.checkpoint")
        .map { num: Int -> num + 2 }
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}


/**
 * checkpoint(description) Operator 를 2개 사용한 예제
 * - 식별자를 추가해서 에러가 발생한 지점을 구분할 수 있다.
 * - 식별자를 지정할 경우에 에러가 발생한 assembly 지점의 traceback 을 추가하지 않는다.
 */
fun checkpointExample05() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .checkpoint("CheckpointExample02.zipWith.checkpoint")
        .map { num: Int -> num + 2 }
        .checkpoint("CheckpointExample02.map.checkpoint")
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * checkpoint(description, true/false) Operator 를 이용한 예제
 * - description 도 사용하고, 에러가 발생한 assembly 지점 또는 에러가 전파된 assembly 지점의 traceback 도 추가한다.
 */
fun checkpointExample06() {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0)) { x: Int, y: Int -> x / y }
        .checkpoint("CheckpointExample02.zipWith.checkpoint", true)
        .map { num: Int -> num + 2 }
        .checkpoint("CheckpointExample02.map.checkpoint", true)
        .subscribe{
            log.info { "# onNext(): $it" }
            log.error { "error happened: $it" }
        }
}

/**
 * 분리된 method 에서 checkpoint() Operator 를 이용한 예제
 */
fun checkpointExample07() {
    val source = Flux.just(2, 4, 6, 8)
    val other = Flux.just(1, 2, 3, 0)

    val multiplySource = divide(source, other).checkpoint()
    val plusSource = plus(multiplySource).checkpoint()

    plusSource.subscribe{
        log.info { "# onNext(): $it" }
        log.error { "error happened: $it" }
    }
}

private fun divide(source: Flux<Int>, other: Flux<Int>): Flux<Int> {
    return source.zipWith(other) { x: Int, y: Int -> x / y }
}

private fun plus(source: Flux<Int>): Flux<Int> {
    return source.map { num: Int -> num + 2 }
}