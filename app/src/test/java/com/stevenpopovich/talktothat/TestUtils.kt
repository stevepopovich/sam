package com.stevenpopovich.talktothat

import io.mockk.MockKVerificationScope
import io.mockk.mockk
import io.mockk.verify

fun verifyExactlyOne(verifyBlock: MockKVerificationScope.() -> Unit) =
    verify(exactly = 1, verifyBlock = verifyBlock)

inline fun <reified T : Any> relaxedMock(): T = mockk(relaxed = true)
