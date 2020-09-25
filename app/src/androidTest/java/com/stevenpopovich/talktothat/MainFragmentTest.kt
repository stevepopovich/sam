package com.stevenpopovich.talktothat

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    @Test
    fun testFragmentStartUp() {
        val fragment = launchFragmentInContainer<MainFragment>()

        fragment.onFragment {
//            assertEquals(
//                PackageManager.PERMISSION_GRANTED,
//                it.requireContext().checkCallingPermission("com.android.example.USB_PERMISSION")
//            )
//
//            assertEquals(
//                PackageManager.PERMISSION_GRANTED,
//                it.requireContext().checkCallingPermission("com.android.example.USB_PERMISSION")
//            )
        }
    }
}
