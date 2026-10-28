package com.carlaun12.navoltanoiscompra

import kotlin.test.Test
import kotlin.test.assertEquals

class CounterTest {
    @Test
    fun countsSuccessiveClicks() {
        var count = 0

        repeat(3) {
            count = incrementCounter(count)
        }

        assertEquals(3, count)
    }
}
