package com.example.webflux.example04_backpressure

import io.github.oshai.kotlinlogging.KotlinLogging
import org.reactivestreams.Subscription
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux

private val log = KotlinLogging.logger {}

/** Subscriber 가 처리 가능한 만큼의 request 개수를 조절하는 Backpressure 예제
 */
fun backpressureExample01() {
    Flux.range(1, 5)
        .doOnNext { log.info { "# doOnNext(): $it" } }          // emit 한 데이터 로그
        .doOnRequest { log.info { "# doOnRequest(): $it" } }   // subscriber 쪽에서 요청하는 데이터의 갯수 출력
        .subscribe(object : BaseSubscriber<Int>() {
            override fun hookOnSubscribe(subscription: Subscription) {
                request(1)
            }

            override fun hookOnNext(value: Int) {
                Thread.sleep(2000L)
                log.info { "# onNext(): $value" }
                request(1)
            }
        })
}

/** Subscriber가 처리 가능한 만큼의 request 갯수를 조절하는 Backpressure 예제
 */
fun backpressureExample02() {
    var count: Int = 0

    Flux.range(1, 5)
        .doOnNext { log.info { "# doOnNext(): $it" } }
        .doOnRequest { log.info { "# doOnRequest(): $it" } }
        .subscribe(object : BaseSubscriber<Int>() {
            override fun hookOnSubscribe(subscription: Subscription) {
                request(2)
            }

            override fun hookOnNext(value: Int) {
                count++
                log.info { "# onNext(): $value" }
                if (count == 2){
                    Thread.sleep(2000L)
                    request(2)
                    count = 0;
                }
            }
        })
}

