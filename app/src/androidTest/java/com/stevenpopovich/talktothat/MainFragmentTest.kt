package com.stevenpopovich.talktothat

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @Test
    fun testPermissions() {
        val thing = launchFragmentInContainer<MainFragment>()

        thing.onFragment {
//            verify { SpeechRecognizer.createSpeechRecognizer(this.)}

//            verify(exactly = 1) {
//                ActivityCompat.requestPermissions(
//                    it.requireActivity(),
//                    listOf(Manifest.permission.RECORD_AUDIO, "com.android.example.USB_PERMISSION").toTypedArray(),
//                    200
//                )
//            }
        }
    }
}
