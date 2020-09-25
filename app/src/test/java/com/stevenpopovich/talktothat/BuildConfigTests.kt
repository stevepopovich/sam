package com.stevenpopovich.talktothat

import org.junit.Assert
import org.junit.Test

class BuildConfigTests {
    @Test
    fun testBuildConfig() {
        Assert.assertEquals("com.stevenpopovich.talktothat", BuildConfig.APPLICATION_ID)
        Assert.assertEquals(1, BuildConfig.VERSION_CODE)
        Assert.assertEquals("1.0", BuildConfig.VERSION_NAME)
        Assert.assertEquals(BuildConfig.DEBUG, BuildConfig.DEBUG)
        Assert.assertEquals(BuildConfig.BUILD_TYPE, BuildConfig.BUILD_TYPE)
    }
}
