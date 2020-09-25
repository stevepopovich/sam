package com.stevenpopovich.talktothat

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechRecognizerErrorTests {
    @Test
    fun testNoMatchError() {
        assertEquals(7.toRecognizerError, "ERROR_NO_MATCH")
    }
    @Test
    fun testRecognizerBusyError() {
        assertEquals(8.toRecognizerError, "ERROR_RECOGNIZER_BUSY")
    }

    @Test
    fun testUnknownError() { // its got a got spread
        assertEquals(0.toRecognizerError, "UNKNOWN ERROR")
        assertEquals(1.toRecognizerError, "UNKNOWN ERROR")
        assertEquals(Int.MIN_VALUE.toRecognizerError, "UNKNOWN ERROR")
        assertEquals(Int.MAX_VALUE.toRecognizerError, "UNKNOWN ERROR")
        assertEquals(10.toRecognizerError, "UNKNOWN ERROR")
        assertEquals((-1).toRecognizerError, "UNKNOWN ERROR")
    }
}
