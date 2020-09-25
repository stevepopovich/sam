package com.stevenpopovich.talktothat

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilTests {
    @Test
    fun testVerboseLog() {
        "thing".verboseLog()
    }

    @Test
    fun testBuildConfig() {
        assertEquals("com.stevenpopovich.talktothat", BuildConfig.APPLICATION_ID)
        assertEquals("debug", BuildConfig.BUILD_TYPE)
        assertEquals(true, BuildConfig.DEBUG)
        assertEquals(1, BuildConfig.VERSION_CODE)
        assertEquals("1.0", BuildConfig.VERSION_NAME)
    }
}
